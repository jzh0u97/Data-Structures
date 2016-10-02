package search;

import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences are
 * associated with keywords in an index hash table.
 * 
 * @author Sesh Venugopal
 * 
 */
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;
	
	/**
	 * The frequency (number of times) the keyword occurs in the above document.
	 */
	int frequency;
	
	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 * 
	 * @param doc Document name
	 * @param freq Frequency
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.put(word,word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			mergeKeyWords(kws);
		}
		
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
	throws FileNotFoundException {
		Scanner sc = new Scanner(new File(docFile));
		String key;
		HashMap<String, Occurrence> tempHash = new HashMap<String,Occurrence>(1000,2.0f);
		Occurrence temp;
		while (sc.hasNext()){
			key = sc.next(); //System.out.println(key);
			key = getKeyWord(key); //System.out.println(key);
			
			if (key == null)
				continue;
			/*else if(noiseWords.get(key)!=null){
				//System.out.println("*");
				continue;
			}
			*/
			else{
				temp = tempHash.get(key);
				if (temp == null){
					Occurrence hashable = new Occurrence(docFile, 1);
					tempHash.put(key, hashable);
				}
				else
					temp.frequency = temp.frequency + 1;
			}
			
		}
		sc.close();
		return tempHash;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeyWords(HashMap<String,Occurrence> kws) {
		for (String key: kws.keySet()) {
			Occurrence toAdd = kws.get(key);
			ArrayList<Occurrence> list = keywordsIndex.get(key);// could return null if the key word is not there
			if (list == null){
				list = new ArrayList<Occurrence>();
				list.add(toAdd);
				keywordsIndex.put(key, list);
			}
			
			else
				list.add(toAdd);
			
			insertLastOccurrence(list); //at this point list should not == null
			//System.out.println(keywordsIndex.get(key));
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyWord(String word) {
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
		if (temp == null || temp.equals(""))
			return null;
		if (noiseWords.get(temp) != null)
			return null;
		return temp;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		if (occs.size() == 1)
			return null;
		ArrayList<Integer> ints = new ArrayList<Integer>();
		
		int low = 0, hi = occs.size() -2, mid = 0,index = 0;
		Occurrence toAdd = occs.remove(occs.size()-1);
		int toAddFreq = toAdd.frequency;
		
		//binary search
		while (low <= hi){
			mid = (low+hi)/2;
			ints.add(mid);
			if (occs.get(mid).frequency == toAddFreq){
				index = mid;
				break;
			}
			if (toAddFreq > occs.get(mid).frequency){
				hi = mid -1; // go left
				index = mid;
			}
			else{
				low = mid + 1; //go right
				index = low;
			}
		}
		if (index <0)
			index = 0;
		occs.add(index, toAdd);
		//System.out.println(ints);
		return ints;
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of NAMES of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matching documents,
	 *         the result is null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		ArrayList<String> top5 = new ArrayList<String>();
		ArrayList<Occurrence> L1 = keywordsIndex.get(kw1); //System.out.println("\n"+L1);
		ArrayList<Occurrence> L2 = keywordsIndex.get(kw2); //System.out.println(L2);
		int f1 = 0, f2 = 0;
		
		if (L2 == null){
			L2 = new ArrayList<Occurrence>();
			f2 = 1;
		}
		
		if (L1 == null)
		{
			L1 = new ArrayList<Occurrence>();
			f1 = 1;
		}
		
		while(top5.size() < 5 ){
			if(f1 >= L1.size() && f2 >= L2.size())
				break;
			
			if (f1 < L1.size() && f2 >= L2.size()){
				if (!isDuplicate(top5,L1.get(f1).document)){
					top5.add(L1.get(f1).document);
				}
				f1++;
			}
			
			else if (f1 >= L1.size() && f2 < L2.size()){
				if (!isDuplicate(top5,L2.get(f2).document)){
					top5.add(L2.get(f2).document);
				}
				f2++;
			}
			else if (L1.get(f1).frequency >= L2.get(f2).frequency){
				if (!isDuplicate(top5,L1.get(f1).document)){
					top5.add(L1.get(f1).document);
				}
				f1++;
			}
			else{
				if (!isDuplicate(top5,L2.get(f2).document)){
					top5.add(L2.get(f2).document);
				}
				f2++;
			}
		}
		if (top5.isEmpty())
			return null; 
		
		return top5;
		
	}
	
	private boolean isDuplicate(ArrayList<String> arr, String doc){ //returns true if there is a duplicate
		if (arr == null)
			return false;
		
		for (String str : arr){
			if (str.equals(doc))
				return true;
		}
		
		return false;
	}
}
