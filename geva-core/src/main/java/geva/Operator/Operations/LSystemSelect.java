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

package geva.Operator.Operations;

import geva.Fractal.guiComp;
import geva.Individuals.Individual;
import geva.Util.Constants;
import geva.Util.Random.RandomNumberGenerator;
import geva.Util.Random.Stochastic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author erikhemberg
 * @author Patrick Middleburgh
 * @author eliott bartley
 */
public class LSystemSelect extends SelectionOperation implements Stochastic
{

	private static Log logger = LogFactory.getLog(LSystemSelect.class);
	
    protected RandomNumberGenerator rng;
    List<Individual> parents = new LinkedList<Individual>();
    int generations = 0;
    int generation = 0;

    public LSystemSelect()
    {
    }

    public LSystemSelect(int size)
    {   super(size);
    }

    @Override
    public void setProperties(Properties p)
    {   super.setProperties(p);
        String value = p.getProperty(Constants.GENERATION);
        try { generations = Integer.parseInt(value); }
        catch(NumberFormatException e) { generations = 0; }
	// Only generational and no elites for LSystem select
	// Will currently only work if selection is created before replacement
	p.setProperty(Constants.REPLACEMENT_TYPE, Constants.GENERATIONAL);
	p.setProperty(Constants.ELITE_SIZE,"0");
    }

    @Override
    public void doOperation(Individual operand)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doOperation(List<Individual> operands)
    {

        List<Individual> valids = new LinkedList<Individual>();
        String[] arrayPheno = new String[operands.size()];
        String[] arrayDepth = new String[operands.size()];
        String[] arrayAngle = new String[operands.size()];
        double[] arrayFitns = new double[operands.size()];
        int[]    fitness = new int[operands.size()];
        guiComp gui;
        int count = 0;
        String outPop = "";

        // Make a list of valid operands only
        for(Individual operand : operands) {
            if(operand.isValid() == true) {
                valids.add(operand);
	    }
	}

        // Exit if there are no valids
        if(valids.size() == 0) {
            return;
	}
        
        // Bring the list up to the required size by duplicating valids
        while(valids.size() < operands.size()) {
            valids.add(valids.get(rng.nextInt(valids.size())));
	}

        //reset iterator to run through updated list of individuals to create 
        //map of specific phenotype strings. 
        for(Individual ind : valids)
        {   arrayDepth[count] = ind
                .getPhenotype()
                .getString()
                .substring(0,1);
            arrayAngle[count] = ind
                .getPhenotype()
                .getString()
                .substring(1,6);
            arrayPheno[count] = ind
                .getPhenotype()
                .getString()
                .substring(6);
            arrayFitns[count] = ind.getFitness().getDouble();
            if(arrayPheno[count].length() > 200
            && Integer.parseInt(arrayDepth[count]) > 3)
                arrayDepth[count] = "3";
            count++;
        }
            
        //create instance of the gui and pass phenotype string map
        gui = new guiComp(arrayPheno,  arrayDepth, arrayAngle, arrayFitns, count, ++generation, generations);

        while(gui.stillRunning() == true)
        {   try { synchronized(gui) { gui.wait(1000); } }
            catch(InterruptedException e) { }
        }

        //getting the fitness' once generate is pressed
        fitness = gui.getGELSYSFitness();
        gui.setVisible(false);
        gui.dispose();
        gui = null;

        if(fitness == null) {
            logger.warn("not working!");
	} else {
            //assigning the relevant fitnes' to their genotype in operands list
            int current = 0;
            String tempOut[] = new String[valids.size()];
            for(Individual ind : valids) {
                //System.out.println("fitness: "+fitness[current]);
                if(fitness[current]==guiComp.LF_FIT){
                    ind.getFitness().setDouble(ind.getFitness().getDouble() * 0.99);
                    super.selectedPopulation.add(ind.clone());
                    //System.err.println(ind.getFitness().getDouble());
                    parents.add(ind);
                    tempOut[current] = ind.getPhenotype().toString();
                    outPop = outPop+"\n"+ind.getPhenotype().toString();
                }
                else
                if(fitness[current]==guiComp.LF_UNFIT){
                    Iterator<Individual> itor;
                    for(itor = operands.iterator(); itor.hasNext() == true; )
                    {   Individual i = itor.next();
                        if(i == ind)
                            itor.remove();
                    }
                }
                current++;
            }
        }
        
        while(super.selectedPopulation.size() < 2) {
            super.selectedPopulation.add(valids.get(rng.nextInt(valids.size())).clone());
	}
	List<Individual> fillPop = new ArrayList<Individual>(super.selectedPopulation.getAll());
	//Make sure the selected population is the required size for replacemnt
	while(super.selectedPopulation.size() < super.size) {
	    super.selectedPopulation.add(fillPop.get(rng.nextInt(fillPop.size())).clone());
	}

        //writing the parent selections to a file.
	//        String fileName = "output.txt";
	//        try{
	//            FileWriter fw = new FileWriter(fileName, true);
	//            BufferedWriter bw = new BufferedWriter(fw);
	//            bw.write(outPop);
	//            bw.close();
	//        } catch (Exception ex) {
	//            System.out.println("not writing to the "+fileName+" "+ex);
	//        }
    }

    public void setRNG(RandomNumberGenerator m) {
            this.rng =m;
    }

    public RandomNumberGenerator getRNG() {
        return this.rng;
    }

}
