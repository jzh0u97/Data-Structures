package apps;

import structures.*;
import java.util.ArrayList;

public class MST {
	
	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {

		PartialTreeList L = new PartialTreeList();
		
		for (int v =0; v <graph.vertices.length; v++){
			Vertex vert1 = graph.vertices[v];
			PartialTree T = new PartialTree(vert1);
			MinHeap<PartialTree.Arc> P = T.getArcs();
			
			Vertex vert2;
			int weight;
			Vertex.Neighbor curr = vert1.neighbors;
			
			while(curr != null){
				vert2 = curr.vertex;
				weight = curr.weight;
				PartialTree.Arc A = new PartialTree.Arc(vert1, vert2, weight);
				P.insert(A);
				
				curr = curr.next;
			}
//			
//			System.out.println(T.getArcs());
			L.append(T);
		}
		
		return L;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {

		ArrayList<PartialTree.Arc> out = new ArrayList<PartialTree.Arc>();
		
		while (ptlist.size()!=1){
			//Step 3
			PartialTree PTX = ptlist.remove(); 
			MinHeap<PartialTree.Arc> PQX = PTX.getArcs();
			
			//Step 4
			PartialTree.Arc alpha = PQX.deleteMin();
			
			//Step 5
			Vertex curr = PTX.getRoot(); Vertex start = PTX.getRoot(); //System.out.println(alpha);
			
			do{ //will the min heap ever be empty?
				//System.out.println(curr);
				if (curr == alpha.v2){
					curr = start;
					alpha = PQX.deleteMin(); //System.out.println(alpha); //System.out.println(PQX);
				} 
				else
					curr = curr.parent;
				
			}while (curr != curr.parent);//(curr != curr.parent); (curr != start);
			//System.out.println(alpha);
			//Step 6
			out.add(alpha);
			
			//Step 7
			PartialTree PTY = ptlist.removeTreeContaining(alpha.v2);
			MinHeap<PartialTree.Arc> PQY = PTY.getArcs();
			
			//Step 8- SOLVED... MAY OR MAYNOT HAVE DELETED PREVIOUS PARENTS
			//PTY.getRoot().parent = PTX.getRoot().parent; //do we keep the parent as a cll
			Vertex t = PTX.getRoot();
			while (t!=t.parent){
				t = t.parent;
			}
			t.parent = PTY.getRoot(); 
			PQX.merge(PQY);
			ptlist.append(PTX);
			
			//System.out.println(PTX);
			//System.out.println(PTX.getRoot().parent);

		}
//		int size = ptlist.size();
//		System.out.println();
//		for (int i = 0; i < size; i++){
//			System.out.println(ptlist.remove());
//		}
		return out;
		
	}
}
