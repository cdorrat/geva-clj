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

package geva.FitnessEvaluation.MultiSquares;


import geva.FitnessEvaluation.InterpretedJScheme;
import geva.Individuals.Phenotype;

import java.util.Properties;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 * BooleanGrammarSwingPainter. This class takes a phenotype string and
 * creates an image object that can be compared to a user-made image.
 * The string is a Sims-style expression, looping over x and y and
 * setting each pixel either on or off.
 *
 * Extending InterpretedJScheme is somewhat unorthodox because this
 * class will not be used to calculate fitness itself (it will be used
 * as a helper by PictureCopy), therefore the fitness-function
 * interface inherited from InterpretedFitnessEvaluation via
 * InterpretedJScheme is redundant. We provide dummy overrides.
 *
 * @class BooleanGrammarSwingPainter
 * @author jmmcd
 */
public class BooleanGrammarSwingPainter extends InterpretedJScheme {

    private BufferedImage image;
    private Graphics g;
    private Graphics2D g2;

    public BooleanGrammarSwingPainter(String input, 
				      int width, 
				      int height) {
	
	image = new BufferedImage(width, 
				  height, 
				  BufferedImage.TYPE_INT_RGB);
	// System.out.println("in BooleanGrammarSwingPainter, input = " + input);
	g = image.getGraphics();
	g2 = (Graphics2D)g;	
	g2.setColor(Color.white);

	RGB.init(400, 400);

	String strImport = "(import \"geva.FitnessEvaluation.MultiSquares.RGB\")";
	String strSetWidth = "(set! width " + width + ")";
	String strSetHeight = "(set! height " + height + ")";
	String strRGB = "(define rgbArray (RGB. " + width + " " + height + "))";
	String prog = "(begin " + strImport + "\n" + strSetWidth + "\n" + strSetHeight + "\n" + strRGB + "\n" + input + ")";

	String output = js.eval(prog).toString();
	// System.out.println("in BooleanGrammarSwingPainter, output = " + output);

	image.setRGB(0, 0, width, height, RGB.getAllData(), 0, width);
    }

    /**
     * getImage
     * Getter for the BufferedImage
     *
     * @return image
     */
    public BufferedImage getImage(){
	return image;
    }

    /**
     * Dummy override.
     * @param p input
     * @return fitness of input
     */
    public double runFile(Phenotype p) {
	return 0.0;
    }

    /** 
     * Dummy override
     * @param p input
     */
    public void setProperties(Properties p) {
    }

}
