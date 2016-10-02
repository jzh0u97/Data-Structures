package solitaire;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

public class testSOl {
	public static void main (String args[]){

		Solitaire1 s = new Solitaire1();
		s.makeDeck();	
		s.printCLL(s.deckRear);
		
	}
}
