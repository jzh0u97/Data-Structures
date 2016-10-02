package apps;

import java.io.IOException;
import java.util.Scanner;

import structures.Node;


public class Radixsort1 {


	Node<String> masterListRear;

	Node<String>[] buckets;
	

	int radix=10;
	

	public Radixsort1() {
		masterListRear = null;
		buckets = null;
	}

	public Node<String> sort(Scanner sc) 
	throws IOException {
		// first line is radix
		if (!sc.hasNext()) { // empty file, nothing to sort
			return null;
		}
		
		// read radix from file, and set up buckets for linked lists
		radix = sc.nextInt();
		buckets = (Node<String>[])new Node[radix];
		
		// create master list from input
		createMasterListFromInput(sc);
		
		// find the string with the maximum length
		int maxDigits = getMaxDigits();
		
		for (int i=0; i < maxDigits; i++) {
			scatter(i);
			gather();
		}
		
		return masterListRear;
	}

	public void createMasterListFromInput(Scanner sc) 
	throws IOException {
		if (!sc.hasNext())
			return;
		
		String data = sc.next();
		
		masterListRear = new Node<String>(data,null);
    	masterListRear.next = masterListRear;
		
		Node<String> entry = null;
		while(sc.hasNext()) {
	        data = sc.next();
	        
	        entry = new Node<String>(data, null);
	        entry.next = masterListRear.next;
	        masterListRear.next = entry;
	        masterListRear = entry;
	      }
		sc.close();

	}

	public int getMaxDigits() {
		int maxDigits = masterListRear.data.length();
		Node<String> ptr = masterListRear.next;
		while (ptr != masterListRear) {
			int length = ptr.data.length();
			if (length > maxDigits) {
				maxDigits = length;
			}
			ptr = ptr.next;
		}
		return maxDigits;
	}
	

	public void scatter(int pass) {
		if (masterListRear == null)
			return; 

		for (int i = 0; i <radix; i++){
			buckets[i]=null;
		}
		
		int pos = 0, val = 0;
		char c = 0; boolean go = true;
		Node<String> current = masterListRear, entry;

		
		do{	//Scattering
			entry = current.next;
			pos = entry.data.length() - pass - 1;
			
			if (pos<0) // if it doesn't have a number at that place, it actually has a 0 and no char
				val = 0;
			else{ // else get that char at that pos and the val of the char
				c = entry.data.charAt(pos);
				val = Character.digit(c, radix);
			}
			
			if (current == current.next)
				go = false;
			
			// isolating the entry and also incremented to next current
			entry = current.next;		
			current.next = current.next.next;
			entry.next = null;
			
			// scattering values; buckets[val] holds the rear
			if (buckets[val] == null){
				buckets[val] = entry;
				buckets[val].next = buckets[val];
			
			}
			else {
				entry.next = buckets[val].next;
				buckets[val].next = entry;
				buckets[val] = buckets[val].next;
			}				
		}while (go);
	}


	public void gather() {
		masterListRear = null;
		Node<String> head;

		// take every element of bucket
		for (int j = 0; j < radix; j++){
			if (buckets[j] != null){

				if (masterListRear == null)
					masterListRear = buckets[j];
				
				else{
					head = buckets[j].next;
					buckets[j].next = masterListRear.next;
					masterListRear.next = head;
					masterListRear = buckets[j];
				}
			}
		}
	}	
	
	public static<T> void printCLL(Node<T> rear) {
		if (rear == null) {
			return;
		}
		Node<T> ptr = rear;
		do {
			ptr = ptr.next;
			System.out.println(ptr.data);
		} while (ptr != rear);
		System.out.println();
	}
	
}

