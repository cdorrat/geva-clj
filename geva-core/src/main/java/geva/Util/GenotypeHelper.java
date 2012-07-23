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

package geva.Util;

import geva.Individuals.GEChromosome;
import geva.Individuals.Individual;
import geva.Mapper.DerivationNode;
import geva.Mapper.DerivationTree;
import geva.Mapper.GEGrammar;
import geva.Mapper.Symbol;
import geva.Mapper.TreeMill;
import geva.Util.Structures.TreeNode;

import java.util.Stack;

/**
 * A collection of useful functions for manipulating genotype and such
 * @author eliott bartley
 */
public class GenotypeHelper
{

    private GenotypeHelper() { }
    
    /**
     * Build a derivation tree rooted at the specified node for a given
     *  individual. If node is null, the tree's root is used
     * @return null if the tree is invalid
     */
    public static DerivationTree buildDerivationTree
    (   Individual individual,
        DerivationNode node
    ){  assert individual.getGenotype().get(0) instanceof GEChromosome
             : individual.getGenotype().get(0).getClass().getName();
        assert individual.getMapper() instanceof GEGrammar
             : individual.getMapper().getClass().getName();

        // Build the derivation tree
        GEChromosome chromosome = (GEChromosome)individual.getGenotype().get(0);
        GEGrammar grammar = (GEGrammar)individual.getMapper();
        if(node != null)
            grammar.setStartSymbol(node.getData());
        grammar.getPhenotype().clear();
        DerivationTree tree = TreeMill.getDerivationTree(grammar);
        if(tree.buildDerivationTree() == true)
            return tree;
        
        // Don't return invalids
        return null;

    }

    /**
     * buildAdjDerivationTree
     * Build a derivation tree that is compatible with adjunction mutation for
     * - a given individual.
     */

    /**
     * Build a derivation tree for a given individual
     * @return null if the tree is invalid
     */
    public static DerivationTree buildDerivationTree
    (Individual individual)
    {
        return buildDerivationTree(individual, null);
    }
    
    /**
     * Given a derivation tree and a codon index, return the tree node that
     *  makes use of the codon at that index. e.g., if codonIndex was 0, the
     *  root branch decides which production is picked based on the value of the
     *  codon at index 0, so the root is returned in this case
     */
    public static DerivationNode findNodeFromCodonIndex
    (   DerivationTree tree,
        int codonIndex
    ){  return findNodeFromCodonIndex(tree, codonIndex, -1);
    }
    public static DerivationNode findNodeFromCodonIndex
    (   DerivationTree tree,
        int codonIndex,
        int usedGenes
    ){  Stack<DerivationNode> nodeStack = new Stack<DerivationNode>();
        nodeStack.push((DerivationNode)tree.getRoot());
        while(nodeStack.empty() == false)
        {   DerivationNode nodes = nodeStack.pop();
            if(nodes.getCodonIndex() == codonIndex)
                return nodes;
            for(TreeNode<Symbol> node : nodes)
                nodeStack.push((DerivationNode)node);
        }
        throw new AssertionError("Indexed node not found");
    }

    /**
     * Calculate how may codons are used to build a specified branch of a tree.
     *  This works by searching the tree itself, and counting up which branches
     *  have a codon related to them. When a branch uses a codon, it gets the
     *  index of that codon attached to it by giving it a 'getCodonIndex' value
     *  other that -1
     */
    public static int calcNodeLength(DerivationNode nodes)
    {   Stack<DerivationNode> nodeStack = new Stack<DerivationNode>();
        nodeStack.push(nodes);
        int size = 0;
        while(nodeStack.empty() == false)
        {   nodes = nodeStack.pop();
            if(nodes.getCodonIndex() != -1)
                size++;
            for(TreeNode<Symbol> node : nodes)
                nodeStack.push((DerivationNode)node);
        }
        // This method must only ever be called on branches (not leaves), so
        //  must always return a size > 0
        assert size > 0 : size;
        return size;
    }

    /**
     * Given two individuals, whoses chromosomes are split into three codon
     *  groups running from [0..point1), [point1..point1+length), and
     *  [point1+length..usedGenes), named head, body, and tail respectively,
     *  return a chromosome with a head and tail made up of individual-one's
     *  chromosome, and individual-two's body, i.e., head(i1)+body(i2)+tail(i1).
     *  The returned chromosome will be unwrapped and therefor may be larger
     *  than either or both the two individuals.
     * @param i1 The individual whose genotype will be the head and tail of the
     *  new chromosome
     * @param point1 The index of the body (excluded from new chromosome)
     * @param length1 The length of the body (excluded from new chromosome)
     * @param i2 The individual whose genotype will be the body of the new
     *  chromosome
     * @param point2 The index of the body (included from new chromosome)
     * @param length2 The length of the body (included from new chromosome)
     * @return A new chromosome made up of i1's head, i2's body, and i1's tail
     */
    public static GEChromosome makeNewChromosome
    (   Individual i1,
        int point1,
        int length1,
        Individual i2,
        int point2,
        int length2
    ){  return makeNewChromosome(i1, point1, length1, i2, point2, length2, null);
    }
    public static GEChromosome makeNewChromosome
    (   Individual i1,
        int point1,
        int length1,
        Individual i2,
        int point2,
        int length2,
        DerivationTree tree
    ){  GEChromosome c1 = (GEChromosome)i1.getGenotype().get(0);
        GEChromosome c2 = (GEChromosome)i2.getGenotype().get(0);
        // Create a chromosome big enough to take the head (point1),
        //  body (length2), and tail (c1.getUsedGenes() - (point1 + length1))
        int total = c1.getUsedGenes() - length1 + length2;
        GEChromosome c = new GEChromosome(total);
	c.setMaxChromosomeLength(c1.getMaxChromosomeLength());

        // Copy the start of c1, up to the removed body.
        //  Note. this unwraps the chromosome from c1
        for(int i = 0; i < point1; i++)
            c.add(c1.get(i % c1.size()));
        
        // Copy the body of c2.
        //  Note. this unwraps the chromosome from c2
        for(int i = point2; i < point2 + length2; i++)
            c.add(c2.get(i % c2.size()));

        // Copy the end of c1, from the removed body
        //  Note. this unwraps the chromosome from c1
        for(int i = point1 + length1; i < c1.getUsedGenes(); i++)
            c.add(c1.get(i % c1.size()));

        // Update the number of used genes to match the copied protion
        c.setUsedGenes(total);

        /*
        Individual i = i1.clone();
        i.getGenotype().set(0, c);
        DerivationTree validTree = GenotypeHelper.buildDerivationTree(i);
        if(validTree != null)
            if(((GEChromosome)i.getGenotype().get(0)).getUsedGenes() != total)
            {   System.err.println("*** Mismatch ***" + ((GEChromosome)i.getGenotype().get(0)).getUsedGenes() + ", " + total + " :: " + c1.getUsedGenes() + " - " + length1 + " + " + length2);
                System.err.println(validTree);
                System.err.println("-------------");
                if(tree != null)
                    System.err.println(tree);
                System.err.println("-------------");
                System.err.println(c1);
                System.err.println(c);
                System.err.println(c2);
                System.err.println(point1 + ", " + point2 + ", " + length1 + ", " + length2);
                System.err.println("-------------");
                System.exit(0);
            }
        */

        assert c.size() == c.allocationSize() && c.size() == total
             : c.size() + "==" + c.allocationSize() + "==" + total;
        
//        System.out.format("c1: %d; c2: %d; c: %d, %d;%n", c1.allocationSize(), c2.allocationSize(), c.allocationSize(), c.size());
        
        return c;
        
    }
    
    /**
     * Given two individuals, whose chromosomes are split into three codon
     *  groups running from [0..point1), [point1..point1+length), and
     *  [point1+length..usedGenes), named head, body, and tail respectively,
     *  return a chromosome with a head and tail made up of individual-one's
     *  chromosome, and all of individual-two, i.e., head(i1)+all(i2)+tail(i1).
     *  The returned chromosome will be unwrapped and therefor may be larger
     *  than either or both the two individuals.
     * @param i1 The individual whose genotype will be the head and tail of the
     *  new chromosome
     * @param point1 The index of the body (excluded from new chromosome)
     * @param length1 The length of the body (excluded from new chromosome)
     * @param i2 The individual whose genotype will be the body of the new
     *  chromosome
     * @return A new chromosome made up of i1's head, all of i2, and i1's tail
     */
    public static GEChromosome makeNewChromosome
    (   Individual i1,
        int point,
        int length,
        Individual i2,
        DerivationTree tree
    ){  GEChromosome c2 = (GEChromosome)i2.getGenotype().get(0);
        return makeNewChromosome(i1, point, length, i2, 0, c2.getUsedGenes(), tree);
    }
    
}
