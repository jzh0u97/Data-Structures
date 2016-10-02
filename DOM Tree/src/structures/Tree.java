package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file. The root of the 
	 * tree is stored in the root field.
	 */
	public void build() {
		if (!sc.hasNext())
			return;
		
		String data = "";
		String processed = "";
		Stack<TagNode> temp = new Stack<TagNode>();
		Stack<TagNode> sibList = new Stack<TagNode>();
		TagNode first, second,current;
		
		do{
			data = sc.nextLine(); //System.out.println(data);
			for (int i = 0; i < data.length(); i++){
				if (data.charAt(i) != '<' && data.charAt(i) != '>' && data.charAt(i) != '/')
					processed = processed + data.charAt(i);
				} //System.out.println(processed);
			
			if (data.length() == 1 || data.charAt(1) != '/'){
				temp.push(new TagNode(processed, null, null)); //System.out.println(temp.peek().tag);
				}
			
			else{
				while (!temp.peek().tag.equals(processed)){// System.out.println(temp.peek());
					sibList.push(temp.pop());
				}
				
				first = sibList.pop(); current = first; //System.out.println(first.tag);
				while (!sibList.isEmpty()){
					second = sibList.pop(); //System.out.println(second.tag);
					current.sibling = second;
					current = current.sibling;
				}
					
				current = temp.pop(); //System.out.println(current.tag); //System.out.println(tempRoot.sibling.tag);
				current.firstChild = first;
				temp.push(current);
			}
			
			processed = "";
			
		}while (sc.hasNextLine());
		
		root = temp.pop();
		sc.close();
	}
	
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) { // write checks for tags
		String processed = "";
		for (int i = 0; i < newTag.length(); i++){
			if (newTag.charAt(i) != '<' && newTag.charAt(i) != '>' && newTag.charAt(i) != '/')
				processed = processed + newTag.charAt(i);
			} //System.out.println(processed);
		newTag = processed;
		
		processed = "";
		for (int i = 0; i < oldTag.length(); i++){
			if (oldTag.charAt(i) != '<' && oldTag.charAt(i) != '>' && oldTag.charAt(i) != '/')
				processed = processed + oldTag.charAt(i);
			} //System.out.println(processed);
		oldTag = processed;
		
		replaceTag(root, oldTag, newTag);
	}
	
	private void replaceTag(TagNode root, String oldTag, String newTag){ //may not traverse through the entire tree
		for (TagNode curr = root; curr != null; curr = curr.sibling){
			if (curr.tag.equals(oldTag) && curr.firstChild != null){
				curr.tag = newTag;
				if (curr.firstChild != null)
					replaceTag(curr.firstChild, oldTag, newTag);
			}
			else
				if (curr.firstChild != null)
					replaceTag(curr.firstChild, oldTag, newTag);
		}
	}

	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) { // check base cases for this one too
		TagNode targetRow = findTable(root); //System.out.println(targetRow);//find where the table is
		targetRow = targetRow.firstChild; //System.out.println(targetRow);
		for (int i = 0; i < row-1; i++){ // get the correct row
			targetRow = targetRow.sibling;
		}
		TagNode temp, toAdd; //System.out.println(targetRow.firstChild);
		for (TagNode curr = targetRow.firstChild; curr != null; curr = curr.sibling){ //targetRow.first accesses td 
			temp = curr.firstChild;
			toAdd = new TagNode("b",null,null);
			toAdd.firstChild = temp;
			curr.firstChild = toAdd;
		}
	}
	
	private TagNode findTable(TagNode root){
		TagNode found = null;
		for (TagNode curr = root; curr != null; curr = curr.sibling){ //System.out.println(curr.tag);
			if (curr.tag.equals("table"))
				return curr;
			else
				found = findTable(curr.firstChild);
		}
		return found;
	}
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		String proc = "";
		for (int i = 0; i < tag.length(); i++){ 
			if (tag.charAt(i) != '<' && tag.charAt(i) != '>' && tag.charAt(i) != '/')
				proc = proc + tag.charAt(i);
			} //System.out.println(proc);
		
		if (proc.equals("p") || proc.equals("em") || proc.equals("b"))
			removeFormatTag(root, proc);
		
		if (proc.equals("ol") || proc.equals("ul"))
			removeListTag(root, proc);
		
	}
	
	private void removeFormatTag (TagNode root, String tag){ //System.out.println(root.tag);
		for (TagNode curr = root; curr != null; curr = curr.sibling){//System.out.println(curr.tag); System.out.println(curr.sibling);
			if (curr.tag.equals(tag)){
				curr.tag = curr.firstChild.tag;
				curr.firstChild = curr.firstChild.firstChild;
			}
			else
				if (curr.firstChild != null)
					removeFormatTag(curr.firstChild,tag);
		}
	}
	
	private void removeListTag (TagNode root, String tag){
		for (TagNode curr = root; curr != null; curr = curr.sibling){//System.out.println(curr); System.out.println(curr.sibling);
		//System.out.println(curr.firstChild);
			if (curr.tag.equals(tag)){
				//System.out.println(curr.tag);
				
				if (curr.firstChild != null)
					removeListTag(root.firstChild, tag);
				
				curr.tag = "li";
				TagNode temp = curr.sibling;
				
				curr.sibling = curr.firstChild.sibling;
				curr.firstChild = curr.firstChild.firstChild;
				
				for (TagNode n = curr; n!= null; n = n.sibling){
					if (n.tag.equals("li")){ //System.out.println("current tag: "+n);
						n.tag = "p"; //System.out.println("after changes: "+n);
					}
					if (n.sibling == null){
						n.sibling = temp;
						break;
					}
				}
			}
			
			else{
				if (curr.firstChild != null)
					removeListTag(curr.firstChild, tag);
			}
		}
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		//preprocessing of word and tag
		String processed = "";
		for (int i = 0; i < tag.length(); i++){
			if (tag.charAt(i) != '<' && tag.charAt(i) != '>' && tag.charAt(i) != '/')
				processed = processed + tag.charAt(i);
			} //System.out.println(processed);
		tag = processed;
		addTag(root, word.toLowerCase(),tag.toLowerCase());
	}
	
	private void addTag(TagNode root, String word, String tag){
		String tmp = ""; //System.out.println(root);
		for (TagNode curr = root; curr != null; curr = curr.sibling){//System.out.println(curr);
			tmp = curr.tag.toLowerCase();
			String substr = null; String beforeTag = "";String afterTag = "";
			int index, outerbnd, in, out;
			
			//System.out.println("curr's first child "+curr.firstChild); 
			//System.out.println(getHTML());//System.out.println(curr.firstChild != null);
			if (curr.firstChild == null && (tmp.contains(word))){
				index = tmp.indexOf(word);
				outerbnd = index + word.length();
				in = index; out = outerbnd;
				
			while(tmp.contains(word)){
				if (((index -1) >= 0 && tmp.charAt(index-1) == ' ') || (index -1) == -1) { // checks if the conditions are met to tag this word
					if (outerbnd >= tmp.length() || (tmp.charAt(outerbnd)==' ')){ // will outerbnd ever be more than 1 more than tmp.length()
						substr = curr.tag.substring(in, out);
						break;
					}
					else if (tmp.charAt(outerbnd) == '.' || tmp.charAt(outerbnd) == ',' || tmp.charAt(outerbnd) == '?' || 
							tmp.charAt(outerbnd) == '!' || tmp.charAt(outerbnd) == ':' || tmp.charAt(outerbnd) == ';' ){
						if ((outerbnd +1)== tmp.length() || tmp.charAt(outerbnd +1)==' '){
							substr = curr.tag.substring(in, out+1);
							out++;
							break;
							}
						else{
							tmp = tmp.substring(outerbnd);
							in = outerbnd; 
							index = tmp.indexOf(word);
							outerbnd = index + word.length();
							in = in + index;
							out = in + word.length();
							}
						}	
					else{
						tmp = tmp.substring(outerbnd);
						in = outerbnd; 
						index = tmp.indexOf(word);
						outerbnd = index + word.length();
						in = in + index;
						out = in + word.length();
						}
					}
				else{
					tmp = tmp.substring(outerbnd);
					in = outerbnd; 
					index = tmp.indexOf(word);
					outerbnd = index + word.length();
					in = in + index;
					out = in + word.length();
				
				}
				
			}
				//System.out.println("substring target "+substr);
				if (substr != null){
					
					beforeTag = curr.tag.substring(0, in); //System.out.println("broken up before "+beforeTag);
					afterTag = curr.tag.substring(out); //System.out.println("broken up after "+afterTag);
					
					TagNode after = new TagNode (afterTag,null,curr.sibling); // this breaks in an ordered list
					TagNode tagger = new TagNode (tag, null,null);
					TagNode toTag = new TagNode (substr, null,null); //System.out.println(after.sibling);
					
					//changing things around
					if (beforeTag.length() == 0 && afterTag.length() == 0){ //if the entire phrase
						curr.tag = tag;
						curr.firstChild = toTag;
					}
					
					else if (beforeTag.length() == 0){ // if the word is at the beginning of string
						curr.tag = tag;
						curr.firstChild = toTag;
						//after.sibling = curr.sibling;
						curr.sibling = after;
					}
					
					else if (afterTag.length() == 0){	// if its the last word in the string
						//System.out.println(curr.sibling.firstChild.firstChild);
						curr.tag = beforeTag;
						tagger.firstChild = toTag;
						tagger.sibling = curr.sibling; 
						curr.sibling = tagger;//System.out.println(tagger.sibling);
						curr = curr.sibling;//System.out.println(curr);
					}
					else {	// normal case
						curr.tag = beforeTag; // changes the content to hold the string before the target word
						tagger.firstChild = toTag; // connects a tag to the the target word
						//after.sibling = curr.sibling; // so we don't lose current's siblings
						curr.sibling = tagger; // inserts tagger as curr's new sibling
						tagger.sibling = after; // the rest of string if any
						curr = curr.sibling; // skips over the newly inserted tag to the rest of the rest of the string			
					}
					//addTag(curr.sibling,word,tag);
				}
			}
			else
				addTag(curr.firstChild, word, tag);
		}
		
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
}
