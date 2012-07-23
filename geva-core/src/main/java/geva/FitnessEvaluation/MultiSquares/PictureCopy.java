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
import geva.Individuals.FitnessPackage.BasicFitness;
import geva.Util.Constants;

import java.awt.*;
import java.awt.image.*;

import java.util.Properties;
import java.io.*;
import java.lang.IllegalArgumentException;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * PictureCopy
 * This is the fitness evaluation class for the
 * Picture Copying problem. The user will generate an image
 * and GE will try and copy that image. 
 * 
 * @class PictureCopy
 * @author John Mark Swafford
 */
public class PictureCopy implements FitnessFunction{

    protected int imageW = 400;
    protected int imageH = 400;

    private int initX = imageW / 2;
    private int initY = imageH / 2;
    private int shapeW = 25;
    private int shapeH = 25;
    private double rot = 0;
    private double sclX = 0;
    private double sclY = 0;
    private double shrX = 0;
    private double shrY = 0;
    private int trltX = 0;
    private int trltY = 0;
    private static String targetImage;
    private static String targetPhenotype;
    File targetFile = null;
    private boolean useShapeGrammar = true;
    private boolean drawPhenotype = false;

    private JFrame frame = null;
    public Painting picture = null;

    private double best = Double.MAX_VALUE;

    private BufferedImage original = 
	new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
    protected BufferedImage generated = 
	new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
    private BufferedImage bestImage = 
	new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
    
    /** 
     * Creates a new instance of PictureCopy
     */
    public PictureCopy() {}

    public PictureCopy(Properties p) {
       super();
       this.setProperties(p);
    }

     /**
      *  This checks if there are any target images or phenotypes
      *  to use
      * @param p
      */
    public void setProperties(Properties p){
        
	try{
	    if(p.getProperty(Constants.TARGET_IMAGE) != null 
	       && !p.getProperty(Constants.TARGET_IMAGE).equals("")){
		
		targetImage =new String(p.getProperty(Constants.TARGET_IMAGE));
		
		targetFile = new File(targetImage);
		if(!targetFile.exists())
            targetFile = null;
		    throw new BadParameterException(Constants.TARGET_IMAGE);
	    }  

	    if(p.getProperty(Constants.TARGET_PHENOTYPE) != null 
	       && !p.getProperty(Constants.TARGET_PHENOTYPE).equals("")){	       		
		targetPhenotype = p.getProperty(Constants.TARGET_PHENOTYPE);
	    }
        drawPhenotype = Boolean.parseBoolean(p.getProperty(Constants.DRAW_PHENOTYPE));
        
	    // By default, use shape grammar. If user asks for
	    // boolean_grammar, use that.
	    String grammarType = p.getProperty("image_grammar_type");
	    if (grammarType != null) {
		if (grammarType.equals("boolean_grammar")) {
		    useShapeGrammar = false;
		}
	    }
	}
	catch (BadParameterException e) {
	    System.out.println("Error: File does not exist");
        throw new IllegalArgumentException(Constants.TARGET_IMAGE);
	}	
	init();
    }
    
    public boolean canCache(){
	return true;
    }

    public void getFitness(Individual i) {
	double result;
	String p = i.getPhenotype().getString();
	if (ShapeGrammarSwingPainter.charCount(p, '(') != ShapeGrammarSwingPainter.charCount(p, ')') 
	    || ShapeGrammarSwingPainter.charCount(p, '[') != ShapeGrammarSwingPainter.charCount(p, ']') 
	    || (p.indexOf("sqr") == -1 && p.indexOf("tri") == -1 && p.indexOf("crcl") == -1)) {
	    i.getFitness().setDouble(BasicFitness.DEFAULT_FITNESS);
	    return;
	}

    picture.paint(i.getPhenotype().getString());
	result = picture.evaluate();
	picture.invalidate();
	i.getFitness().setDouble(result);       
    }

    public void init(){

	if(frame != null){
	    return;
	}
	frame = new JFrame("Matching the Image");
	picture = new Painting();
	picture.setSize(imageW*2, imageH);
       
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(imageW*2, imageH);
	frame.setVisible(drawPhenotype);
	frame.getContentPane().setLayout(new BorderLayout());
	frame.getContentPane().add(picture);
    }

    public void init(String name){

	if(frame != null){
	    return;
	}

	frame = new JFrame(name);
	picture = new Painting();
	picture.setSize(imageW*2, imageH);
       
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(imageW*2, imageH);
	frame.setVisible(drawPhenotype);
	frame.getContentPane().setLayout(new BorderLayout());
	frame.getContentPane().add(picture);
    }
    
    
    public class Painting extends JPanel{

        int[] oRGB;
        
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
	    oRGB = getRGB(original);
	}
        
        @Override
        public void paintComponent(Graphics graphics){   
	    super.paintComponent(graphics);
           render(graphics);
        }
        
        public void paint(String ps){

	    if (useShapeGrammar == true) {
		// Generated Image
		ShapeGrammarSwingPainter sgspG = 
		    new ShapeGrammarSwingPainter(ps, imageW, imageH,
						 initX, initY, 
						 10, 10);
		generated = sgspG.getImage();
	    } else {
		// Generated Image using Sims-style expressions.
		BooleanGrammarSwingPainter bgsp = 
		    new BooleanGrammarSwingPainter(ps, imageW, imageH);
		generated = bgsp.getImage();
        }
	    render(this.getGraphics());
        }

	public void render(Graphics graph){
    if(drawPhenotype)
       {
	    Graphics2D g2 = (Graphics2D)graph;

	    g2.drawImage(original, 
			    0, 0, 
			    frame.getWidth()/2, frame.getHeight(), 
			    0, 0, 
			    original.getWidth(), original.getHeight(),
			    null);

	    g2.drawImage(generated, 
	    		    frame.getWidth()/2, 0, 
			    frame.getWidth(), frame.getHeight(),
			    0, 0,
	    		    generated.getWidth(), generated.getHeight(), 
			    null);

	    g2.setColor(Color.white);
	    g2.drawLine(frame.getWidth()/2, 0,
			frame.getWidth()/2, frame.getHeight());
	   }
    }

        public void mirror(){
	    bestImage.getGraphics().drawImage(generated, 
					      frame.getWidth()/2, 0, 
					      null);
        }

	public double evaluate(){
        
	    double tmpFit = 0;
	    int[] gRGB = getRGB(generated);
            
	    for(int i = 0; i < oRGB.length; i++){
		if(oRGB[i] != gRGB[i]){
		    tmpFit++;
		}
	    }

	    return tmpFit;
	}

	public int[] getRGB(BufferedImage img){
	    int w = img.getWidth(null);
	    int h = img.getHeight(null);
	    int[] rgb = new int[w * h];
	    int[] RGB = img.getRGB(0, 0, w, h, rgb, 0, w);
	    return RGB;
	}
    
    }
    public static void main(String [] args) {

    String input = "[ gro_( 2 ) tri ] ";
    if(args.length>0){
        input = args[0];
    }
    String target = "shrnk_( 2 ) crcl [ UpDwn_( -10 ) UpDwn_( -35 ) symY_( 75 ) gro_( 2 ) tri ] LftRght_( 15 ) rot_( 45 ) sqr";
    
    
    targetPhenotype = target;
    double result;
    
    if (ShapeGrammarSwingPainter.charCount(input, '(') != ShapeGrammarSwingPainter.charCount(input, ')') 
	|| ShapeGrammarSwingPainter.charCount(input, '[') != ShapeGrammarSwingPainter.charCount(input, ']') 
	|| (input.indexOf("sqr") == -1 && input.indexOf("tri") == -1 && input.indexOf("crcl") == -1)) {
	result = BasicFitness.DEFAULT_FITNESS;
	
    } else {
	
	PictureCopy pc = new PictureCopy();
	pc.init();
        
	pc.picture.paint(input);
	result = pc.picture.evaluate();
    }
    
    System.out.println(result);
    }
    
}
