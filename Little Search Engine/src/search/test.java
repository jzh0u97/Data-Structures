package search;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class test{
	public static void main(String[] args) throws IOException{
		/*String word = "I."; String noise = "noisewords.txt";
		String proc = word.toLowerCase();
		String temp = "";
		for (int i = 0; i < word.length(); i++){ 
			if (Character.isLetter(proc.charAt(i)))
				temp = temp+ proc.charAt(i);
			else{
				for (int j = i; j < word.length(); j++){
					if (proc.charAt(j) == '.' || proc.charAt(j) == ',' || proc.charAt(j) == '?'
							|| proc.charAt(j) == ':' || proc.charAt(j) == ';' ||proc.charAt(j) == '!'){
						continue;
					}
					else{
						temp = null;
						break;
					}
				}
				i = word.length(); // so it ends the outer loop
			}		
		}
		System.out.println(temp);
		*/
		
		
		String docs = "docs.txt"; String noise = "noisewords.txt";
		LittleSearchEngine first = new LittleSearchEngine();
		first.makeIndex(docs, noise);
		
//		PrintWriter writer = new PrintWriter("foundKeys.txt", "UTF-8");
//				
//		for (String str : first.keywordsIndex.keySet()){
//			writer.print(str + ": ");
//			writer.print(first.keywordsIndex.get(str));
//		}
//		
//		writer.close();
		System.out.println(first.top5search("first", "upon"));
		
		
		/*Scanner sc = new Scanner(new File("foundKeys"));
		String str;
		while(sc.hasNext()){
			str = sc.next();
			if (!str.equals("null"))
				System.out.println(str);
		}
		sc.close();
		*/	
	}
}