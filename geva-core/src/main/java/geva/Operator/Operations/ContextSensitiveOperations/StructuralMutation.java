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

package geva.Operator.Operations.ContextSensitiveOperations;

/*
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//*/
import geva.Individuals.GEChromosome;
import geva.Individuals.GEIndividual;
import geva.Individuals.Individual;
import geva.Mapper.ContextualDerivationTree;
import geva.Mapper.DerivationTree;
import geva.Operator.Operations.*;
import geva.Util.GenotypeHelper;
import geva.Util.Random.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This will find if the chromosome contains a expandable structural node
 * and will mutate it depending on the mutation probability
 */
public class StructuralMutation extends MutationOperation
{

    private CreationOperation creationOperation;
    
    public StructuralMutation(RandomNumberGenerator rng, Properties p)
    {   super(rng, p);
    }

    public StructuralMutation(double prob, RandomNumberGenerator rng)
    {   super(prob, rng);
    }

    public void setCreationOperation(CreationOperation creationOperation)
    {   this.creationOperation = creationOperation;
    }
    
    @Override
    public void doOperation(Individual operand)
    {
        ContextualDerivationTree tree = (ContextualDerivationTree) GenotypeHelper.buildDerivationTree(operand);
        GEChromosome chromosome = (GEChromosome)operand.getGenotype().get(0);
               
        if(tree!=null) // this is to check that the individual is not invalid
        {
            // This vector contains the index values for all the structural codons
            ArrayList<Integer> structCodonList = new ArrayList(tree.getStructCodonList());
            //iterate through the structural codons and mutate depending on probability
            for(int i =0;i< structCodonList.size();i++)
            {               
                int codonIndex  =structCodonList.get(i);
                if(this.rng.nextBoolean(this.probability)) 
                {  
//                    if(tree!=null) // this is to check that the individual is not invalid
//                    {
//                    System.out.println("The old tree was:");
//                    System.out.println(tree.toString());
//                    System.out.println("The OLD codon list is:"+structCodonList.toString());
//                    System.out.println("The codon to mutate is:"+codonIndex);
//                    }

                    chromosome.set(codonIndex, Math.abs(rng.nextInt()));                   
                    tree = (ContextualDerivationTree) GenotypeHelper.buildDerivationTree(operand);
//                    if(tree!=null)
//                    {
//                    structCodonList = new ArrayList(tree.getStructCodonList());
//                    System.out.println("The NEW codon list is:"+structCodonList.toString());
//                    System.out.println("The new tree is:");
//                    System.out.println(tree.toString());
//                    }
                }
                                         
            }
            ((GEIndividual)operand).invalidate();
            tree = null;  
        }
    }
    
    @Override
    public void doOperation(List<Individual> operands)
    {   for(Individual operand : operands)
            doOperation(operand);
    }
}
