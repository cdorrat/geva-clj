/*
Grammatical Evolution in Java
Release: GEVA-v1.2.zip
Copyright (C) 2008 Michael O'Neill, Erik Hemberg, Anthony Brabazon, Conor Gilligan 
Contributors Patrick Middleburgh, Eliott Bartley, Jonathan Hugosson, Jeff Wrigh

Separate licences for asm, bsf, antlr, groovy, jscheme, commons-logging, jsci is included in the lib folder. 
Separate licence for rieps is included in src/com folder.

This licence refers to GEVA-v1.2.

This software is distributed under the terms of the GNU General Public License.


This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
/>.
*/

/*
 * NimbleTree.java
 *
 * Created on 16 October 2006, 16:45
 *
 * Tree structure
 */

package geva.Util.Structures;

import java.util.Stack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * Lightweight tree n-arity structure
 * @author EHemberg
 */
public class NimbleTree<E> {

    private TreeNode<E> root,currentNode;
    private Stack<TreeNode<E>> freeNodes;// nodes that have been taken off the tree and recycled 
    private int nodeCount; //Tracks number of nodes in the tree
    private int depth; // maximum depth of tree
    private int currentLevel; // 
    private int maxStackSize;

    /** Creates a new instance of NimbleTree */
    public NimbleTree() {
        this.root = newNode();
        this.currentNode = this.root;
        this.nodeCount = 1;
	this.root.setID(this.nodeCount);
        this.depth = 0;
        this.currentLevel = 0;
        //Important to make a new stack
        this.freeNodes = new Stack<TreeNode<E>>();
        this.maxStackSize = 10;
    }

    /** Copy constructor
     *  @param n copied tree
     */
    public NimbleTree(NimbleTree<E> n) {
        this.root = n.root;
        this.currentNode = n.currentNode;
        this.nodeCount = n.nodeCount;
        this.freeNodes = new Stack<TreeNode<E>>();
        this.depth = n.depth;
        this.currentLevel = n.currentLevel;
    }

    protected TreeNode<E> newNode()
    {   return new TreeNode<E>();
    }

    /**
     * A static constructor (is this the right term?) where a lisp-style s-expression
     * is passed in as a string. This can't be a true constructor because the result
     * is a tree over String, not a generic tree.
     */
    public static NimbleTree<String> makeTreeOverStringFromSExpression(String input) {
	NimbleTree<String> tree = new NimbleTree<String>();
        Stack<TreeNode<String>> previousParents = new Stack<TreeNode<String>>();

	// Make sure the string is tokenizable
	// FIXME allow [] and maybe other delimiters?
	input = input.replace("(", " ( ");
        input = input.replace(")", " ) ");

	StringTokenizer st = new StringTokenizer(input);

	boolean firstTimeThrough = true;
	while (st.hasMoreTokens()){
	    String currTok = st.nextToken().trim();

	    if (currTok.equals("")) {
		// Tokenizer gave us an empty token, do nothing.

	    } else if (currTok.equals("(")) {
		// Get the *next* token and make a new subtree on it.
		currTok = st.nextToken().trim();

		if (firstTimeThrough == true) {
		    // The tree is of the form "(x)"
		    // This is the root node: just set its data
		    firstTimeThrough = false;
		    tree.getRoot().setData(currTok);

		} else {
		    // This is the root of a new subtree. Save parent,
		    // then set this node to be the new parent.
		    tree.addChild(currTok);
		    tree.getCurrentNode().getEnd().setID(tree.getNodeCount());
		    previousParents.push(tree.getCurrentNode());
		    tree.setCurrentNode(tree.getCurrentNode().getEnd());
		}
		
	    } else if (currTok.equals(")")) {
		// Finished adding children to current parent. Go back
		// to previous parent (if there was none, it's because
		// current parent is root, so we're finished anyway).
		if (!previousParents.empty()) {
		    tree.setCurrentNode(previousParents.pop());
		}

	    } else {
		if (firstTimeThrough == true) {
		    // The tree is of the form "x".
		    // This is to be the root node: just set its data. 
		    firstTimeThrough = false;
		    tree.getRoot().setData(currTok);
		} else {
		    // Add a child node to current parent.
		    tree.addChild(currTok);
		    tree.getCurrentNode().getEnd().setID(tree.getNodeCount());
		}
	    }
	}

	return tree;
    }

    
    /**
     * Set max stack size
     * @param i max stack size
     */
    public void setMaxStackSize(int i) {
        this.maxStackSize = i;
    }

    /**
     * Get max stack size
     * @return max stack size
     */
    public int getMaxStackSize() {
        return this.maxStackSize;
    }

    /**
     * Create nodes and push to the stack
     */
    public void populateStack() {
        TreeNode<E> tn;
        for(int i = 0; i < maxStackSize; i++) {
            tn = newNode();
            this.freeNodes.push(tn);
        }
    }

    /**
     * Get root of tree
     * @return tree root
     */
    public TreeNode<E> getRoot() {
        return this.root;
    }

    /**
     * Set tree root
     * @param tn root of tree
     */
    public void setRoot(TreeNode<E> tn){
        this.root = tn;
    }

    /**
     * Get current node
     * @return current node
     */
    public TreeNode<E> getCurrentNode(){
        return this.currentNode;
    }

    /**
     * Set current node
     * @param tn node to be current
     */
    public void setCurrentNode(TreeNode<E> tn){
        this.currentNode = tn;
    }

    /**
     * Get node count
     * @return number of nodes in tree
     */
    public int getNodeCount(){
        return this.nodeCount;
    }

    /**
     * Set node count
     * @param i number to set
     */
    public void setNodeCount(int i){
        this.nodeCount = i;
    }

    /**
     * Set depth of tree
     * @param i depth
     */
    public void setDepth(int i) {
        this.depth = i;
    }

    /**
     * Get maximum depth of tree
     * @return tree max depth
     */
    public int getDepth() {
        return this.depth;
    }

    /**
     * Get current level
     * @return current level
     */
    public int getCurrentLevel() {
        return this.currentLevel;
    }

    /**
     * Set current level
     * @param i level to set
     */
    public void setCurrentLevel(int i) {
        this.currentLevel = i;
    }

    /**
     * Find all the paths from root to leaves. Used by NGramEDAReproductionOperation.
     * @return list of list of TreeNode, each list starting at the root and ending at a leaf. 
     */
    public ArrayList<ArrayList<TreeNode<E>>> getRootToLeafPaths() {
	ArrayList<TreeNode<E>> leafNodes = new ArrayList<TreeNode<E>>();
	// traverse the tree and find the leaf nodes
	for (TreeNode<E> node: depthFirstTraversal(getRoot())) {
	    if (node.size()== 0) {
		leafNodes.add(node);
	    }
	}
	ArrayList<ArrayList<TreeNode<E>>> paths = new ArrayList<ArrayList<TreeNode<E>>>();
	for (TreeNode<E> leaf: leafNodes) {
	    ArrayList<TreeNode<E>> path = new ArrayList<TreeNode<E>>();
	    TreeNode<E> current = leaf;
	    while (current != getRoot()) {
		path.add(current);
		current = current.getParent();
	    }
	    // add the root
	    path.add(current);
	    Collections.reverse(path);
	    paths.add(path);
	}
	return paths;
    }

    /**
     * Get all the chains of ancestors of length n in this tree. Used
     * as sources for NGram model.
     */
    public ArrayList<ArrayList<TreeNode<E>>> getAncestorChains(int n) {
	ArrayList<ArrayList<TreeNode<E>>> retval = new ArrayList<ArrayList<TreeNode<E>>>();
	for (TreeNode<E> node: depthFirstTraversal(getRoot())) {
	    ArrayList<TreeNode<E>> chain = getAncestorChain(node, n);
	    if (chain.size() == n) {
		Collections.reverse(chain);
		retval.add(chain);
	    }
	}
	return retval;
    }

    /**
     * Starting at a given node, get a chain of ancestors of length n or less.
     */
    public ArrayList<TreeNode<E>> getAncestorChain(TreeNode<E> node, int n) {
	ArrayList<TreeNode<E>> retval = new ArrayList<TreeNode<E>>();
	TreeNode<E> current = node;
	while (retval.size() < n && current != null && current.getData() != null) {
	    retval.add(current);
	    current = current.getParent();
	}
	return retval;
    }

    /**
     * Do a depth-first traversal of the tree starting at a given node.
     * @return a list of TreeNodes in depth-first order.
     */
    public ArrayList<TreeNode<E>> depthFirstTraversal(TreeNode<E> root) {
	ArrayList<TreeNode<E>> retval = new ArrayList<TreeNode<E>>();
	retval.add(root);
	for (TreeNode<E> child: root) {
	    retval.addAll(depthFirstTraversal(child));
	}
	return retval;
    }
	    
    /**
     * Add a child to the current node. Take a node from the free nodes.
     * INFINITE LOOP POSSIBILITY??!!
     * @param data data contained in the child
     */
    public void addChild( E data) {
        if(!this.freeNodes.empty()) {
            TreeNode<E> n = this.freeNodes.pop();
            n.setData(data);
            n.setParent(this.currentNode);
            n.clear();
            this.setDepth(this.depth + 1);
            this.currentNode.add(n);
            this.nodeCount++;
	    n.setID(this.nodeCount);
        } else {
            populateStack();
            addChild(data);//Infinite loop possibility
        }
    }

    @Override
    public String toString() {
	String s = "";
        s += root.toString();
        return s;
    }

    /**
     * Get some code for drawing this tree using Graphviz.
     */
    public String getGraphvizCode() {
	String retval = "graph dotFile {\n";

	// Write out the nodes.
	for (TreeNode<E> n: depthFirstTraversal(root)) {
	    if (n != null && n.getData() != null) {
		retval += "id" + n.getID() + " [label=\"" + n.getData() + "\"];\n";
	    }
	}
	// Write out the incoming edge for every node that has a parent.
	for (TreeNode<E> n: depthFirstTraversal(root)) {
	    if (n != null && n.getParent() != null && n.getParent().getData() != null) {
		retval += "id" + n.getParent().getID() + " -- id" + n.getID() + ";\n";
	    }
	}
	retval += "}";
	return retval;
    }
}
