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
 * RoyalTree.java
 *
 * Created on 26 March 2009
 *
 * Based on the description in "Program Distribution Estimation with
 * Grammar Models", Yin Shan, 2005.
 *
 * The Royal Tree Problem (Punch, Zongker, and Goodman 1996) was
 * designed as a difficult problem for GP. In this experiment, we use
 * the level-E Royal Tree Problem. This problem has one terminal x and
 * a series of nonterminals A, B, C, D, E etc. with increasing arity.
 * For example, these five nonterminals have arity 1, 2, 3, 4 and 5
 * respectively. The perfect solution for this problem is a complete
 * full tree, with nonterminals of the biggest arity at the root and
 * the nonterminals of second biggest arity at the next level, etc.
 * The fitness reflects the resemblance to this perfect solution, and
 * was designed to encourage searching for the solution in a bottom up
 * manner. The fitness of the perfect solution of the level 5 Royal
 * Tree Problem is 122,880. More specifically, for any depth, we
 * define a "perfect" tree as shown in Figure 6.5 adopted from (Punch,
 * Zongker, and Goodman 1996). A level-A tree is an A with a single x
 * child. A level-B tree is a B with two level-A trees as children. A
 * level-C tree is a C with three level-B trees as children, and so
 * on. A level-E tree has depth 5 and 326 nodes, while a level-F tree
 * has depth 6 and 1927 nodes. The raw fitness of the tree (or any
 * sub-tree) is the score of its root. Each node calculates its score
 * by summing the weighted scores of its direct children. If the child
 * is a perfect tree of the appropriate level (for instance, a
 * complete level-C tree beneath a D node), then the score of that
 * sub-tree, times a FullBonus weight, is added to the score of the
 * root. If the child has the correct root but is not a perfect tree,
 * then the weight is multiplied by a PartialBonus. If the child's
 * root is incorrect, then the weight is multiplied by Penalty. After
 * scoring the root, if the function is itself the root of a perfect
 * tree, the final sum is multiplied by CompleteBonus. Typical values
 * used are: FullBonus = 2, PartialBonus = 1, Penalty = 1/3, and
 * CompleteBonus = 2.
 *
 * The score base case is a level-A tree, which has a score of 4 (the
 * A-x connection is worth 1, times the FullBonus, times the
 * CompleteBonus). The reasoning behind this "stairstep" approach is
 * to give a big jump in evaluation credit to each proper combination,
 * so that the problem can be solved by progressively discovering
 * sub-solutions and combining them. The FullBonus is provided to give
 * a large credit to those trees that find the correct, complete royal
 * tree child. The PartialBonus is used to give credit for finding the
 * proper, direct child for a node, even if that direct child is not
 * the root of a royal tree. This pressure is not as great as the
 * FullBonus, but it is an effective incentive, since the score is
 * determined recursively down the tree, and thus each node receives
 * some credit when if finds its proper, direct children. If a node
 * does not have the correct, direct children, it is penalised by
 * Penalty, making the FullBonus and PartialBonus even more effective.
 * Finally, if the resulting tree is itself complete, then a very
 * large credit is given. The reasoning behind the increase in arity
 * required at each increased level of the royal tree is to introduce
 * tunable difficulty.
 *
 */

/*
 * My assumption is that the aim is to make the largest possible
 * complete tree. In other words, (B (A x) (A x)) is always better
 * than (A x). There is no sense of a "target of depth 1". The best
 * possible fitness is determined by the number of non-terminals
 * available in the grammar, not by any "target depth" -- jmmcd.
 */

package geva.FitnessEvaluation.RoyalTree;

import geva.Exceptions.BadParameterException;
import geva.FitnessEvaluation.FitnessFunction;
import geva.Individuals.Individual;
import geva.Individuals.Phenotype;
import geva.Util.Constants;
import geva.Util.Structures.NimbleTree;
import geva.Util.Structures.TreeNode;

import java.util.Properties;

import java.util.ArrayList;

/**
 * Evaluates a royal tree.
 * @author jmmcd
 */
public class RoyalTree implements FitnessFunction {
    
    private ArrayList<String> alphabet;
    private double completeBonus = 2.0, 
	partialBonus = 1.0, 
	fullBonus = 2.0, 
	penalty = 1.0 / 3;

    /** Creates a new instance of RoyalTree */
    public RoyalTree() {
	alphabet = getAlphabet();
    }

    // I don't know who should be most embarrassed by this method -- me or Java.
    private ArrayList<String> getAlphabet() {
	ArrayList<String> alphabet = new ArrayList<String>();
	alphabet.add("x");
	alphabet.add("A");
	alphabet.add("B");
	alphabet.add("C");
	alphabet.add("D");
	alphabet.add("E");
	alphabet.add("F");
	alphabet.add("G");
	alphabet.add("H");
	return alphabet;
    }
	
    public void setProperties(Properties p) { }

    /**
     * The phenotype will be a tree in lisp-syntax. Its nonterminals
     * will be A, B, C, etc (of increasing arity), and its only
     * terminal is x. eg: ( C ( A x ) ( A x ) ( B ( A x ) x ) ). 
     * @param phenotype The candidate phenotype
     * @return double fitness
     */
    public double evaluateString(Phenotype p) {
	// The Royal Tree is really a maximisation problem, so we
	// invert the fitness so that GEVA can minimise. No danger of
	// dividing by zero here, because the minimal tree ("x") has
	// fitness 1.0.
	return 1.0 / evaluate(p.getString());
    }

    /**
     * Given a string, calculate its fitness. It's useful to have a method
     * which takes a String (rather than a Phenotype) for testing porpoises.
     * @param s string describing the candidate tree.
     * @return fitness (in classical royal-tree maximising format)
     */
    public double evaluate(String s) {
	NimbleTree<String> tree = NimbleTree.makeTreeOverStringFromSExpression(s);
	// System.out.println(s);
	// System.out.println(tree);
	return fitness(tree.getRoot());
    }

    /**
     * This is the recursive method which does the calculations.
     *
     * @param node The root of the (sub-)tree to be evaluated
     * @return fitness of the (sub-)tree
     */
    private double fitness(TreeNode<String> node) {
	if (node.getData().equals(alphabet.get(0))) {
	    return 1.0;
	}
	double retval = 0.0;
	boolean nodeIsPerfect = true;
	for (TreeNode<String> child: node) {
	    if (isPerfect(node.getData(), child)) {
		retval += fullBonus * fitness(child);
	    } else if (isSuccessor(node.getData(), child.getData())) {
		retval += partialBonus * fitness(child);
		nodeIsPerfect = false;
	    } else {
		retval += penalty * fitness(child);
		nodeIsPerfect = false;
	    }
	}
	// Only if every child is a perfect subtree of the appropriate
	// type does this node get completeBonus.
	if (nodeIsPerfect) {
	    retval *= completeBonus;
	}
	return retval;
    }

    /**
     * @param p supposed "parent"
     * @param q supposed "child"
     * @return whether q is the correct "successor", eg p = B and q = A
     */
    private boolean isSuccessor(String p, String q) {
	int pIdx = alphabet.indexOf(p);
	int qIdx = alphabet.indexOf(q);
	boolean retval = (pIdx >= 0 && qIdx >= 0 && (pIdx == qIdx + 1));
	return retval;
    }

    /**
     * Calculate whether the tree rooted at child is a perfect subtree
     * of the appropriate type given the current node.
     * @param current "parent"
     * @param child root of the sub-tree to be tested.
     * @return whether it is a perfect subtree of the right type.
     */
    private boolean isPerfect(String current, TreeNode<String> child) {
	if (!isSuccessor(current, child.getData())) {
	    return false;
	}
	for (TreeNode<String> node: child) {
	    if (!isPerfect(child.getData(), node)) {
		return false;
	    }
	}
	return true;
    }

    public void getFitness(Individual i) {
        i.getFitness().setDouble(this.evaluateString(i.getPhenotype()));
    }
    
    public boolean canCache() {
	return true;
    }

    // A useful testing method. FIXME move this into a class under Test/
    public static void main(String [] args) {
	RoyalTree rt = new RoyalTree();
	double d = rt.evaluate(args[0]);
	System.out.println("fitness:" + d);
    }

    // Some test cases (note that we invert these fitness values for GEVA's sake):
    
    // examples from the Shan paper:
    // "( C ( B ( A x ) ( A x ) ) ( B ( A x ) ( A x ) ) ( B ( A x ) ( A x ) ) )" -> 384.0
    // "( C ( B ( A x ) ( A x ) ) ( B ( A x ) ( A x ) ) ( B x x ) )" -> 128.66
    // "( C ( B ( A x ) ( A x ) ) x x )" -> 64.66

    // I'm not 100% sure these are right:

    // full tree from D:
    // $ java geva.FitnessEvaluation/RoyalTree/RoyalTree "(D (C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) ) (C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) )(C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) )(C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) ) )" -> fitness:6144.0

    // full tree from E:
    // $ java geva.FitnessEvaluation/RoyalTree/RoyalTree "(E (D (C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) ) (C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) )(C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) )(C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) ) ) (D (C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) ) (C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) )(C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) )(C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) ) ) (D (C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) ) (C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) )(C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) )(C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) ) ) (D (C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) ) (C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) )(C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) )(C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) ) ) (D (C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) ) (C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) )(C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) )(C (B (A x) (A x) ) (B (A x) (A x) ) (B (A x) (A x) ) ) ) )" -> fitness:122880.0

}
