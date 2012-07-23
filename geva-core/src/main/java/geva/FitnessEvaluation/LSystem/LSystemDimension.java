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

package geva.FitnessEvaluation.LSystem;

import geva.FitnessEvaluation.FitnessFunction;
import geva.Fractal.LSystem2FDBoxCounting;
import geva.Individuals.Individual;
import geva.Util.Constants;
import geva.Util.Statistics.IndividualCatcher;

import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A fitness which tends towards a specified fractal dimension 
 * @author eliott bartley
 */
public class LSystemDimension implements FitnessFunction
{

	private static Log logger = LogFactory.getLog(LSystemDimension.class);
    private double targetFractalDim;
    private int boxCountDivision;
    
    public void getFitness(Individual i)
    {

        String grammar = i.getPhenotype().getString().substring(6);
        String depth = i.getPhenotype().getString().substring(0, 1);
        String angle = i.getPhenotype().getString().substring(1, 6);
        double fractalDim = new LSystem2FDBoxCounting
        (   null,
            grammar,
            Integer.parseInt(depth),
            Float.parseFloat(angle),
            boxCountDivision
        ).calcFractalDimension();

        i.getFitness().setDouble
        (   Math.abs(targetFractalDim - fractalDim)
        );
        
    }

    public boolean canCache()
    {   return true;
    }

    public void setProperties(Properties p)
    {
        
        try
        {
            targetFractalDim = Double.parseDouble
            (   p.getProperty
                (   Constants.TARGET_FRACTAL_DIMENSION,
                    "1.666"
                )
            );
            logger.info(String.format(   "%s=%.3f%n",
                Constants.TARGET_FRACTAL_DIMENSION,
                targetFractalDim
            ));
        }
        catch(NumberFormatException e)
        {   targetFractalDim = 1.666;
            logger.info(String.format("%s [%s] using default: %.3f%n",
                Constants.TARGET_FRACTAL_DIMENSION,
                e,
                targetFractalDim
            ));
        }

        try
        {
            boxCountDivision = Integer.parseInt
            (   p.getProperty
                (   Constants.BOX_COUNT_DIVISION,
                    "512"
                )
            );
            logger.info(String.format("%s=%d%n",
                Constants.BOX_COUNT_DIVISION,
                boxCountDivision
            ));
        }
        catch(NumberFormatException e)
        {   boxCountDivision = 512;
            logger.warn(String.format("%s [%s] using default: %d%n",
                Constants.BOX_COUNT_DIVISION,
                e,
                boxCountDivision
            ));
        }

    }

}
