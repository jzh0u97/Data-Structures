package structures;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

import structures.Tree1;

public class testTree {
	
	public static TagNode addTag(TagNode root, String word, String tag) { //the word is all lowercase and tag is without its brackets
		for (TagNode curr = root; curr != null; curr = curr.sibling){
			String tmp = curr.tag.toLowerCase();
			String substr = ""; String beforeTag = "";String afterTag = "";
			int index, outerbnd;
			
			if (curr.firstChild != null){
				if (tmp.contains(word)){
					
					index = tmp.indexOf(word);
					outerbnd = tmp.charAt(index + word.length());
					
					if (((index -1) >= 0) || (tmp.charAt(index-1) == ' ')) { // checks if the conditions are met to tag this word
						if (outerbnd >= tmp.length() || (tmp.charAt(outerbnd)==' ')) // will outerbnd ever be more than 1 more than tmp.length()
							substr = tmp.substring(index, outerbnd);
						if (tmp.charAt(outerbnd) == '.' || tmp.charAt(outerbnd) == ',' || tmp.charAt(outerbnd) == '?' || 
								tmp.charAt(outerbnd) == '!' || tmp.charAt(outerbnd) == ':' || tmp.charAt(outerbnd) == ';' ){
							if ((outerbnd +1)== tmp.length() || tmp.charAt(outerbnd +1)==' '){
								substr = tmp.substring(index, outerbnd+1);
								outerbnd++;
								}
							}	
						}
					System.out.println("substring target "+substr);
					if (substr != null){
						
						beforeTag = curr.tag.substring(0, index); System.out.println("broken up before "+beforeTag);
						afterTag = curr.tag.substring(outerbnd); System.out.println("broken up after "+afterTag);
						
						TagNode after = new TagNode (afterTag,null,null);
						TagNode tagger = new TagNode (tag, null,null);
						TagNode toTag = new TagNode (substr, null,null);
						
						//changing things around
						if (beforeTag.length() == 0){ // if the word is at the beginning of string
							curr.tag = tag;
							curr.firstChild = toTag;
							after.sibling = curr.sibling;
							curr.sibling = after;
						}
						
						else if (afterTag.length() == 0){	// if its the last word in the string
							curr.tag = beforeTag;
							tagger.firstChild = toTag;
							curr.sibling = tagger;
							curr = curr.sibling;
						}
						else {	// normal case
							curr.tag = beforeTag; // changes the content to hold the string before the target word
							tagger.firstChild = toTag; // connects a tag to the the target word
							after.sibling = curr.sibling; // so we don't look current's sibling pointer
							curr.sibling = tagger; // inserts tagger as curr's new sibling
							tagger.sibling = after; // the rest of string if any
							curr = curr.sibling; // skips over the newly inserted tag to the rest of the rest of the string			
						}
					}
				}
			}
			
			else
				root = addTag(root.sibling, word, tag);
		}
					
		return root;		
		
	}
	
	public static void main (String[] args) throws IOException{
		String htmlFile = "sample.html"; 
		Tree tree = new Tree(new Scanner(new File(htmlFile)));
		tree.build();System.out.println();
		String test = "Sub ";
		System.out.println(test.indexOf("b"));
		int index = test.indexOf("b");
		System.out.println(test.charAt(index-1) == ' ');
		System.out.println((index -1) >= -1);
		
		//tree.getHTML();
		
		
		
		
	/*Stack<TagNode> temp = new Stack<TagNode>();
	
	temp.push(new TagNode("hello word",null,null));
	temp.push(new TagNode("3",null,null));
	temp.push(new TagNode("2",null,null));
	temp.push(new TagNode("1",null,null));
	
	TagNode first = temp.peek();
	TagNode root, toAdd;
	root = temp.pop();
	toAdd = temp.pop();
	root.sibling = toAdd;
	temp.push(toAdd);
	
	root = temp.pop();
	toAdd = temp.pop();
	root.sibling = toAdd;
	temp.push(toAdd);
	
	System.out.println(first.sibling.sibling);
	*/
}}