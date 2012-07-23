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

import geva.Exceptions.BadParameterException;
import geva.FitnessEvaluation.FitnessFunction;
import geva.Individuals.Individual;
import geva.Util.Constants;

import java.awt.*;
import java.awt.image.*;
import java.util.Properties;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author eliott bartley
 * @author John Mark Swafford
 * @author Jonathan Byrne
 */
public class PolygonArtFitness implements FitnessFunction{

    protected int imageW = 400;
    protected int imageH = 400;

    private int initX = imageW / 2;
    private int initY = imageH / 2;
    private static String targetImage = new String();
    private static String targetPhenotype = new String();

    File targetFile = null;

    private JFrame frame = null;
    private Painting picture = null;
    private double best = Double.MAX_VALUE;

    private BufferedImage original = 
	new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
    protected BufferedImage genImage = 
	new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
    private BufferedImage bestImage = 
	new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);

    // Empty contructor
    public PolygonArtFitness(){}

    // Properties contructor
    public PolygonArtFitness(Properties p){
       super();
       this.setProperties(p);
    }

    // Set properties
    public void setProperties(Properties p){
	try{
	    if(p.getProperty(Constants.TARGET_IMAGE) != null 
	       && !p.getProperty(Constants.TARGET_IMAGE).equals("")){
		
		this.targetImage = p.getProperty(Constants.TARGET_IMAGE);
		
		targetFile = new File(targetImage);
		if(!targetFile.exists())
		    throw new BadParameterException(Constants.TARGET_IMAGE);
	    }  

	    if(p.getProperty(Constants.TARGET_PHENOTYPE) != null 
	       && !p.getProperty(Constants.TARGET_PHENOTYPE).equals("")){	       		
		this.targetPhenotype = p.getProperty(Constants.TARGET_PHENOTYPE);
	    }  	    
	}
	catch (BadParameterException e) {
	    System.out.println("Error: File does not exist");
	}
	init();
    }

    public void getFitness(Individual i){
	double result;
        init();
        
        //System.out.println(i.getPhenotype().getString());
        picture.paint(i.getPhenotype().getString());
        result = picture.evaluate();
        if(result < best){
	    best = result;
            picture.mirror();
        }
        picture.invalidate();
        i.getFitness().setDouble(result);
    }

    public boolean canCache(){
	return true;
    }

    // Set up the frame and put the picture in it    
    private void init(){

        if(picture != null)
            return;
        
        picture = new Painting();
        picture.setSize(imageW*2, imageH);

	frame = new JFrame("Matching the Image");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(imageW*2, imageH);
        frame.setVisible(true);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(picture);
    }
    
    // Inner class for getting images and painting them
    class Painting extends JPanel{

	int[] orRGB;

	// Inner class contructor
	public Painting(){
	    super();
	    
            // Target image
            ShapeGrammarSwingPainter sgspT;
            if(targetFile!=null && !(targetFile.equals(""))){
                sgspT = new ShapeGrammarSwingPainter(targetFile);
            }
	    
            else if(targetPhenotype!=null&& !(targetPhenotype.equals(""))){
                sgspT = new ShapeGrammarSwingPainter(targetPhenotype, imageW, imageH,
						     initX, initY, 
						     10, 10);
            }
            else{
	        sgspT = new ShapeGrammarSwingPainter(imageW, imageH,
						     initX, initY, 
						     40, 40);
            }
	    
	    original = sgspT.getImage();
	    orRGB = getRGB(original);
	}

        @Override
        protected void paintComponent(Graphics graphics){
	    super.paintComponent(graphics);
            render(graphics);
        }
     
        public void paint(String ps){

	    ShapeGrammarSwingPainter sgspG = 
		new ShapeGrammarSwingPainter(ps, imageW, imageH,
					     initX, initY, 
					     10, 10);
	    genImage = sgspG.getImage();

            render(this.getGraphics());            
        }

	// Show the best image
        public void mirror(){
	    bestImage.getGraphics().drawImage(genImage, 0, 0, null);
        }

	// Render the images
        private void render(Graphics graphics){   

            graphics.drawImage(genImage,
			       0, 0, 
			       frame.getWidth() / 2, frame.getHeight(),
			       0, 0, 
			       genImage.getWidth(), genImage.getHeight(),
			       null);
            graphics.drawImage(bestImage,
			       frame.getWidth() / 2, 0, 
			       frame.getWidth(), frame.getHeight(),
			       0, 0, 
			       bestImage.getWidth(), bestImage.getHeight(),
			       null);
        }

	// Get the fitness of the generated image
        public double evaluate(){
	    double result = 0;
	    int[] genRGB = getRGB(genImage);
	    double dist;

	    for(int i = 0; i < orRGB.length; i++){
		dist = Math.abs(orRGB[i] - genRGB[i]);
		result += dist;
	    }

            return result;
        }

	public int[] getRGB(BufferedImage img){
	    int w = img.getWidth(null);
	    int h = img.getHeight(null);
	    int[] rgb = new int[w * h];
	    int[] RGB = img.getRGB(0, 0, w, h, rgb, 0, w);
	    return RGB;
	}
    }
}
