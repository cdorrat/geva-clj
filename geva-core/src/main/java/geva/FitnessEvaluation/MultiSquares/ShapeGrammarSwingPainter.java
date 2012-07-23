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
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import java.util.StringTokenizer;
import java.util.ArrayList;


/**
 * ShapeGrammarSwingPainter
 * This class takes a phenotype string and creates an image object that can be
 * - compared to a user-made image.
 *
 * @class ShapeGrammarSwingPainter 
 * @author John Mark Swafford
 * @author jmmcd
 */
public class ShapeGrammarSwingPainter{

    private ShapeGrammarSwingPainterState startState;
    private String input;
    
    private double rot;         // angle of rotation
    private int x, y;           // shape x and y coordinates
    private int startX, startY; // shape initial x and y coordinates
    private int w, h;           // shape width and height
    private double sclX, sclY;  // shape scaling factors
    private double shrX, shrY;  // shape shearing factors
    private int trltX, trltY;   // shate translating factors
    private Color color = Color.white;

    private int imW, imH;       // image width and height

    private BufferedImage image;
    private Graphics g;
    private Graphics2D g2;

    private Shape oval;
    private Shape rect;
    private Shape tri;
    private int[] xPts = new int[3];
    private int[] yPts = new int[3];
    private int numPts = 3;

    // Contructor for the target image passed in the properties file
    public ShapeGrammarSwingPainter(File targetFile){
        try {         
	    image = ImageIO.read(targetFile);
        }
        catch (IOException ie) {
	    System.out.println("Error:"+ie.getMessage());
	}     
    }

    // Contructor for the target image
    public ShapeGrammarSwingPainter(int imgW, int imgH,
				    int initX, int initY,
				    int initW, int initH){

	imW = imgW;
	imH = imgH;	

	image = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_RGB);      
	g = image.getGraphics();
	g2 = (Graphics2D)g;	
    }
    
    // Constructor for generating images
    public ShapeGrammarSwingPainter(String inString,
				    int imgW, int imgH,
				    int initX, int initY,
				    int initW, int initH){

	input = inString;
	
	imW = imgW;
	imH = imgH;
	
	x = initX;
	y = initY;
	startX = initX;
	startY = initY;
	w = initW;
	h = initH;
	
	// Finish the state
	startState = new ShapeGrammarSwingPainterState(rot, x, y, startX, startY,
						       w, h, sclX, sclY, shrX, shrY,
						       trltX, trltY, color);
	
	image = new BufferedImage(imW, imH, BufferedImage.TYPE_INT_RGB);
	g = image.getGraphics();
	g2 = (Graphics2D)g;
	
	g2.setColor(Color.white);

	generate();
    }
    /**
     * getImage
     * Getter for the BufferedImage
     * 
     * @author John Mark Swafford
     * @return image
     */
    public BufferedImage getImage(){
	return image;
    }

    protected static int charCount(String s, char c) {
	int count = 0;
	for (int i = 0; i < s.length(); i++) {
	    if (s.charAt(i) == c) {
		count++;
	    }
	}
	return count;
    }

    // TODO

    /**
     * generate This method just calls into the recursive generate
     * function, passing in a start state and the input string.
     * 
     * @author John Mark Swafford
     * @author jmmcd
     */
    public void generate(){

	if (charCount(input, '(') != charCount(input, ')') 
	    || charCount(input, '[') != charCount(input, ']') 
	    || (input.indexOf("sqr") == -1 && input.indexOf("tri") == -1 && input.indexOf("crcl") == -1)) {
	    return;
	}

	//input = "lft_( 40 ) sqr";
	// Regex to remove white spaces from between parens and underscores
	input = input.replaceAll("\\(\\s", "");
	input = input.replaceAll("\\s\\)", "");
	input = input.replaceAll(" _ ", "_");
	input = input.replaceAll(" _", "_");
	//System.out.println("Chopped Phen.: " + input);
	generateRecursive(input, startState);
    }

    /** 
     * Utility function for string processing. This is ugly!
     * 
     */
    private String getNewStringAfterCurrent(String stringAfterCurrent, String currTok) {
	stringAfterCurrent = stringAfterCurrent.trim();
	stringAfterCurrent = stringAfterCurrent.substring(currTok.length(), stringAfterCurrent.length());
	stringAfterCurrent = stringAfterCurrent.trim();
	return stringAfterCurrent;
    }

    /**
     * generateRecursive: This method iterates over the tokens of the
     * input string and does some manipulation to the input state and
     * then optionally calls itself (generally with a shortened input
     * string argument). There is no longer any need for a stack of
     * states since the function call stack contains one stack
     * argument per call.
     *
     * @param inputString String containing operators and primitives
     * @state current state of drawing.
     */

    private void generateRecursive(String inputString, ShapeGrammarSwingPainterState state) {
	
	//System.out.println("Phen: "+inputString);

	// Declare and initialize those variables!
	StringTokenizer st = new StringTokenizer(inputString);
	AffineTransform tx = new AffineTransform();
       	String currTok = "";
	String us = "_";
	int transVal = 0;
	int lastUS;
	int firstUS;
	String stringAfterCurrent = inputString;

	// Read in all the tokens
	while (st.hasMoreTokens()){
	    currTok = st.nextToken();
	    currTok = currTok.trim();
	    stringAfterCurrent = getNewStringAfterCurrent(stringAfterCurrent, currTok);

	    lastUS = currTok.lastIndexOf(us);
	    firstUS = currTok.indexOf(us);
	    //System.out.println("Underscore at: " + lastUS);
	    if(lastUS == -1){

		// if sqr, write:
		if(currTok.equals("sqr")){
		    //System.out.print("Drawing Square ");
		    
		    rect = new Rectangle2D.Double((double)(imW/2 + state.trltX), 
						  (double)(imH/2 + state.trltY), 
						  (double)state.w, 
						  (double)state.h);

		    tx.setToRotation((Math.toRadians(state.rot)), 
				     (double)(imW/2 + state.trltX + state.w/2),
				     (double)(imH/2 + state.trltY + state.h/2));
		    
		    rect = tx.createTransformedShape(rect);
		    g2.fill(rect);
		}
		
		// if crcl, write:
		else if(currTok.equals("crcl")){
		    //System.out.println("Drawing Circle");

		    oval = new Ellipse2D.Double((double)(imW/2 + state.trltX), 
						(double)(imH/2 + state.trltY), 
						(double)state.w, 
						(double)state.h);

		    tx.setToRotation((Math.toRadians(state.rot)), 
				     (double)(imW/2 + state.trltX + state.w/2),
				     (double)(imH/2 + state.trltY + state.h/2));

		    oval = tx.createTransformedShape(oval);
		    g2.fill(oval);
		}
		
		// if tri, write:
		else if(currTok.equals("tri")){
		    //System.out.println("Drawing Triangle");

		    xPts[0] = state.x;
		    xPts[1] = state.x + state.w;
		    xPts[2] = state.x;

		    yPts[0] = state.y;
		    yPts[1] = state.y;
		    yPts[2] = state.y + state.h;

		    tri = new Polygon(xPts, yPts, numPts);

		    tx.setToRotation((Math.toRadians(state.rot)), 
				     (double)(imW/2 + state.trltX + state.w/2),
				     (double)(imH/2 + state.trltY + state.h/2));
		    
		    tri = tx.createTransformedShape(tri);
		    g2.fill(tri);
		}
		
		else if(currTok.equals("[")){
		    // Make a recursive call: take more tokens up to
		    // the matching "]", join them, and pass that as
		    // the string. When we return our tokenizer will
		    // continue from after the "]". Also copy state so
		    // that when we return our current state will
		    // still exist
		    ShapeGrammarSwingPainterState newState = 
			new ShapeGrammarSwingPainterState(state);
		    int rBraceCount = 1;
		    ArrayList<String> newTokens = new ArrayList<String>();
		    while (true) {
			currTok = st.nextToken().trim();
			stringAfterCurrent = getNewStringAfterCurrent(stringAfterCurrent, currTok);
			if (currTok.equals("[")) {
			    rBraceCount += 1;
			} else if (currTok.equals("]")) {
			    rBraceCount -= 1;
			}
			if (rBraceCount == 0) {
			    break;
			}
			newTokens.add(currTok);
		    }
		    String newString = "";
		    // Would a string builder be better for these
		    // (usually small) numbers of tokens?
		    for (String tok: newTokens) {
			newString = newString + tok + " ";
		    }
		    generateRecursive(newString, newState);
		}
		else if(currTok.equals("]")) {
		    // We never reach here, since the "[" clause
		    // chomps up to and including the corresponding
		    // "]".
		    return;
		} 
		else {
		    System.out.println("Unknown token `" + currTok + "'");
		}
			   
	    }
	    
	    else if(lastUS >= 0){
		//System.out.println("Found transformation");
		//System.out.println(currTok.substring(lastUS+1));
		
		// if up, dwn, lft, rght, move:
	        if(currTok.substring(0,lastUS).equals("LftRght")){
		    transVal = Integer.parseInt(currTok.substring(lastUS+1));
		    //System.out.println("Translating: " + transVal);
		    state.trltX += transVal;
		}
		else if(currTok.substring(0,lastUS).equals("UpDwn")){
		    transVal = Integer.parseInt(currTok.substring(lastUS+1));
		    //System.out.println("Translating: " + transVal);
		    state.trltY += transVal;
		}
		
		// scale the objects up and down
		else if(currTok.substring(0,lastUS).equals("gro")){
		    transVal = Integer.parseInt(currTok.substring(lastUS+1));
		    //System.out.println("Scaling: " + transVal);
		    state.w *= transVal;
		    state.h *= transVal;
		}
		else if(currTok.substring(0,lastUS).equals("shrnk")){
		    transVal = Integer.parseInt(currTok.substring(lastUS+1));
		    //System.out.println("Scaling: " + transVal);
		    state.w /= transVal;
		    state.h /= transVal;
		}

		else if(currTok.substring(0,lastUS).equals("rot")){
		    transVal = Integer.parseInt(currTok.substring(lastUS+1));
		    //System.out.println("Rotating: " + transVal);
		    state.rot += transVal;
		}

		else if(currTok.substring(0,lastUS).equals("symX")){
		    // System.out.println("Symmetry: imW = " + imW);
		    // copy state and alter the copy, so that the
		    // remaining string will be drawn twice, from two
		    // different starting positions.
		    transVal = Integer.parseInt(currTok.substring(lastUS+1));
		    ShapeGrammarSwingPainterState newState = 
			new ShapeGrammarSwingPainterState(state);
		    newState.trltX += transVal;
		    generateRecursive(stringAfterCurrent, newState);
		}

		else if(currTok.substring(0,lastUS).equals("symY")){
		    // System.out.println("Symmetry: imW = " + imW);
		    // copy state and alter the copy, so that the
		    // remaining string will be drawn twice, from two
		    // different starting positions.
		    transVal = Integer.parseInt(currTok.substring(lastUS+1));
		    ShapeGrammarSwingPainterState newState = 
			new ShapeGrammarSwingPainterState(state);
		    newState.trltY += transVal;
		    generateRecursive(stringAfterCurrent, newState);
		}

		else if(currTok.substring(0,firstUS).equals("color")){
		    int red;
		    int green;
		    int blue;
		    int alpha;
		    String[] vals = new String[5];
		    
		    vals = currTok.split(us);

		    red = Integer.parseInt(vals[1]);
		    green = Integer.parseInt(vals[2]);
		    blue = Integer.parseInt(vals[3]);
		    alpha = Integer.parseInt(vals[4]);

		    Color newColor = new Color(red, green, blue, alpha);
		    state.color = newColor;
		    g2.setColor(newColor);
		}
		
		else if(currTok.substring(0,firstUS).equals("poly")){

		    int numPnts = Integer.parseInt(currTok.substring(lastUS+1));
		    int[] xPoints = new int[numPnts];
		    int[] yPoints = new int[numPnts];
		    int xCnt = 0;
		    int yCnt = 0;
		    String[] strPoints = new String[(numPnts*2)+2];		    
		    strPoints = currTok.split(us);
		    
		    // Split the string between the x and y points
		    for(int i = 1; i < strPoints.length-1; i++){
			if(i % 2 == 0){
			    yPoints[yCnt] = Integer.parseInt(strPoints[i]);
			    yCnt++;
			}
			else if(i % 2 != 0){
			    xPoints[xCnt] = Integer.parseInt(strPoints[i]);
			    xCnt++; 
			}
		    }

		    // Create and draw the new polygon

		    g2.fill(new Polygon(xPoints, yPoints, numPnts));
		}

		else {
		    System.out.println("Unknown token `" + currTok + "'");
		}
	    }
	}
	return;
    }
}
