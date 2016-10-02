package apps;

import java.io.IOException;
import java.util.ArrayList;

import structures.Graph;
import structures.Vertex;

public class driver {
	public static void main(String[] args) throws IOException{
		Graph g = new Graph("eulergraph.txt");
		PartialTreeList w = MST.initialize(g);

//		PartialTree ex = w.remove();
//		System.out.println(w.removeTreeContaining(ex.getArcs().getMin().v2));
		
//		int size = w.size();
//		System.out.println();
//		for (int i = 0; i < size; i++){
//			System.out.println(w.remove());
//		}
//		System.out.println(w.size());
		
		
		ArrayList<PartialTree.Arc> out = MST.execute(w); int weight = 0; int count = 0;
		System.out.println(out);
		for (PartialTree.Arc n: out){
			weight = n.weight +weight;
			count++;
		}
		Vertex p = w.remove().getRoot();
		while (p!=p.parent){
			System.out.print(p+"   ");
			p = p.parent;
		}
		System.out.println(p);
		
		System.out.println(weight);
		System.out.println(count);
	}
}
