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

import geva.Individuals.GEChromosome;
import geva.Individuals.GEIndividual;
import geva.Individuals.Individual;
import geva.Mapper.ContextualDerivationTree;
import geva.Operator.Operations.*;
import geva.Util.GenotypeHelper;
import geva.Util.Random.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This will find if the chromosome contains a expandable leaf node
 * and will mutate it depending on the mutation probability
 */
public class NodalMutation extends MutationOperation
{

    private CreationOperation creationOperation;
    
    public NodalMutation(RandomNumberGenerator rng, Properties p)
    {   super(rng, p);
    }

    public NodalMutation(double prob, RandomNumberGenerator rng)
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
        // This vector contains the index values for all the leaf node codons
        ArrayList<Integer> nodeCodonList = new ArrayList(tree.getNodeCodonList());          
        //iterate through the leaf Node codons and mutate depending on probability
        for(int i =0;i< nodeCodonList.size();i++)
            {
            int codonIndex  = nodeCodonList.get(i);
            if(this.rng.nextBoolean(this.probability)) 
                {
                 chromosome.set(codonIndex, Math.abs(rng.nextInt()));
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
