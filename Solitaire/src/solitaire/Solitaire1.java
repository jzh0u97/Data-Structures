package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

public class Solitaire1 {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	public void printCLL(CardNode rear){
		if (rear == null){
			System.out.println("Nothing here");
		}
		CardNode current= rear.next;
		do{
			System.out.print(current.cardValue+" ");
			current = current.next;
		}while(current != rear.next);
		
		System.out.println();
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		//System.out.println("Initial deck: ");printCLL(deckRear);
		CardNode current = deckRear.next;
		while(current.cardValue != 27){
			current = current.next;
		}
		current.cardValue = current.next.cardValue;
		current.next.cardValue = 27;
		//System.out.println("After Step 1: ");printCLL(deckRear);
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
		CardNode current = deckRear.next; 
		while(current.cardValue != 28){
			current = current.next;
		}
		int first = current.next.cardValue;
		int second = current.next.next.cardValue;
		current.cardValue = first;
		current.next.cardValue = second;
		current.next.next.cardValue = 28;
		//System.out.println("After Step 2: ");printCLL(deckRear);
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		if ((deckRear.cardValue == 27 || deckRear.cardValue == 28) && (deckRear.next.cardValue == 27 || deckRear.next.cardValue == 28))
			return;
		
		CardNode current = deckRear;
		CardNode firstJprev= null, secondJprev=null;
		do{
			if (current.next.cardValue == 27 || current.next.cardValue == 28){
				if (firstJprev == null)
					firstJprev = current;
				else
					secondJprev = current;
			}
			
			current = current.next;	
			
		}while(current != deckRear);
		
		//System.out.println(firstJprev.cardValue +" "+ secondJprev.cardValue);
		if (firstJprev.next == deckRear.next){
			deckRear = secondJprev;
			deckRear = deckRear.next;
		}
		else if (secondJprev.next == deckRear){
			deckRear = firstJprev;
		}
		else{
			CardNode joker2 = secondJprev.next, joker1 = firstJprev.next;
			firstJprev.next = joker2.next;
			joker2.next = deckRear.next;
			deckRear.next = joker1;
			deckRear = firstJprev;
			
		}
		//System.out.println("After triple cut: "); printCLL(deckRear);
	
	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {		
		int proc = deckRear.cardValue;
		if (proc == 28)
			proc = 27;

		if (proc == 27)
			return;
		
		int count = 0;
		CardNode last = deckRear, beforeRear = deckRear;
		while (count<proc){
			count++;
			last = last.next;
		}
		while (beforeRear.next != deckRear){
			beforeRear = beforeRear.next;
		}
		
		beforeRear.next = deckRear.next;
		deckRear.next = last.next;
		last.next = deckRear;
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		int key = 0;
		int count= 0;
		while(true){
			printList(deckRear);// print
			jokerA(); System.out.println("After jokerA: "); printCLL(deckRear);
			jokerB(); System.out.println("After jokerB: "); printCLL(deckRear);
			tripleCut(); System.out.println("After triple cut: "); printCLL(deckRear);
			countCut(); System.out.println("After count cut: "); printCLL(deckRear); System.out.println();
			
			count = deckRear.next.cardValue;
			if (deckRear.next.cardValue == 28)
				count = 27;
			
			CardNode current = deckRear;
			for(int i = 0; i<count; i++){
				current =current.next;
			}
			if (current.next.cardValue != 27 && current.next.cardValue != 28){
				key = current.next.cardValue;
				return key;
			}
		}
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		String d = "", p = "";
		message = message.toUpperCase();
		for (int i = 0; i <message.length(); i++){
			char ch = message.charAt(i);
			if (Character.isLetter(ch)){
				//p = p +ch;
				int c = ch-'A'+1;
				c = c+ getKey();
				if (c>26)
					c = c - 26;
				
				ch = (char)(c-1+'A'); 
				d = d +ch;
			}
		}
		
		//System.out.println(p+"\n"+d);
	    return d;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		String d = "";
		for (int i = 0; i <message.length(); i++){
			char ch = message.charAt(i);
			if (Character.isLetter(ch)){ System.out.println(ch);
				
				int c = ch-'A'+1;
				int key = getKey();
				if (c<=key)
					c = c-key+26;
				else
					c = c -key;
				ch = (char)(c-1+'A'); 
				d = d +ch;
			}
		}
		
	    return d;
	}
}
