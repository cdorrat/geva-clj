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
 * LSystem.java
 *
 * Created on October 18, 2007, 4:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geva.Fractal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;
import java.util.Stack;
import javax.swing.JPanel;
//import Cursor;

/** 2-D L-System Applet
 * LSystem.java
 * @author J Scott Cameron
 * Edit author Patrick Middleburgh
 *
 */
public class LSystem extends JPanel {
    
    
    
    
    Dimension size;
    Image buffer;
    
    int xMin, xMax, yMin, yMax;
    int width,height;
    
    int n = 5;
    float angle = 25.7f;
    double fitness;
    
    String init = "F", grammar = "F=F[+F]F[-F]F+F-F", finalPath;
    

    //defult constructor
    public LSystem(String Gra, int dep, float ang, int width, double fitness){
        grammar = Gra;
        n = dep;
        angle = ang;
        this.fitness = fitness;
        init(width);
        
    }

    
/** Initializes the components of the applet
 */
    public void init(int width) {
        setLayout(null);
        
        
        size = new Dimension(width, width);
        this.setPreferredSize(size);
        //buffer = createImage(20, 80);
        //bufferGraphics = buffer.getGraphics();
        
        this.width=height=width;
        xMin=yMin=-10;
        xMax=yMax=10;
        
    }
    
/** sets the scale so that the applet shows the whole Lsystem graphic
 * @param finalPath the final string that the cursor will move through
 * @param angle the angle the cursor turns for "+" and "-" symbols
 */
    public void getScale(String finalPath, double angle)
    {
        double xMax = 0, xMin =0, yMin=0, yMax=0;
        Cursor cursor = new Cursor();
        Stack<Cursor> cursorStack = new Stack<Cursor>();
        
        int c;
        StringReader pathReader = new StringReader(finalPath);
        try {
            /* moves through the L-System the same as if it were 
             * being drawn to determine what the bounds of the
             * L-System will be */
            for(int i=0; i<finalPath.length(); i++)
            {
                c = pathReader.read();
                switch(c)
                {
                    case('F'):
                    case('f'): /*move cursor forward one unit*/
                        
                        cursor.x += Math.cos(cursor.angle); 
                        if(cursor.x > xMax) xMax = cursor.x;
                        else if(cursor.x < xMin) xMin = cursor.x;
                        
                        cursor.y += Math.sin(cursor.angle);
                        if(cursor.y > yMax) yMax = cursor.y;
                        else if(cursor.y < yMin) yMin = cursor.y;
                        
                        break;
                    case('+'): /*rotate cursor*/
                        
                        cursor.angle+=angle;
                        
                        while(cursor.angle >= (2*Math.PI))//greater than 360 degrees
                            cursor.angle-=(2*Math.PI);
                        break;
                    case('-'): /*rotate cursor*/
                        
                        cursor.angle-=angle;
                        
                        while(cursor.angle < 0)//not negitive degrees
                            cursor.angle+=(2*Math.PI);
                        break;
                    case('['): /* push down on the stack */
                        cursorStack.push(new Cursor(cursor));
                        break;
                    case(']'): /*  pop off the stack */
                        
                        cursor = cursorStack.pop();
                        break;
                        
                        default:
                            break;
                }
            }
        }
        catch(IOException e)
        {}
        
        double size = (xMax-xMin);
        if(size < (yMax-yMin) )
            size = (yMax-yMin);
        
        /* calculate the proper bounds */
        this.xMax=(int)( ((xMax+xMin)/2) + (size/2))+1;
        this.xMin=(int)( ((xMax+xMin)/2) - (size/2))-1;
        this.yMax=(int)( ((yMax+yMin)/2) + (size/2))+1;
        this.yMin=(int)( ((yMax+yMin)/2) - (size/2))-1;
        
        
        
    }
    
/** maps an LSystem x value to a screen x-coordinate
 * @param x The x value to be mapped
 * @return the appropriate x-coordinate for the screen
 */
    public int getXCoord(double x)
    {
        float p = ((float)width)/((float)(xMax-xMin));
        return (int) ((x-xMin)*p);
        
    }
    
/** maps an LSystem y value to a screen y-coordinate
 *
 * @return the appropriate x-coordinate for the screen
 * @param y The y value to be mapped
 */
    public int getYCoord(double y)
    {
        float p = ((float)width)/((float)(yMax-yMin));
        return (int) ((y-yMin)*p);
        
    }
    
/** draws the expanded LSystem string to the screen
 * @param finalPath expanded LSYstem string
 * @param angle angle the cursor will rotate for "+,=" symbols
 * @param graphics Graphics object to draw to.
 */
    public void drawPath(String finalPath, double angle, Graphics graphics)
    {

        Cursor cursor = new Cursor();
        Cursor newCursor = new Cursor();
        
        /* create a stack to keep track of cursors */
        Stack<Cursor> cursorStack = new Stack<Cursor>();
        
        int c;
        StringReader pathReader = new StringReader(finalPath);
        
        try{
            /* checks each character */
            for(int i=0; i<finalPath.length(); i++)
            {
                
                c = pathReader.read();
                
                switch(c)
                {
                    
                    case('F'):/* draw a line one unit long */
                        
                        /* move the newCursor to where the
                         * cursor will be after the move forward */
                        newCursor.x = cursor.x +Math.cos(cursor.angle);
                        newCursor.y = cursor.y +Math.sin(cursor.angle);
                        newCursor.angle = cursor.angle;
                        
                        /* draw a line from the old cursor to the new 
                         * cursor */
                        graphics.drawLine(getXCoord(cursor.x),
                                          getYCoord(cursor.y),
                                          getXCoord(newCursor.x),
                                          getYCoord(newCursor.y));
                        
                        
                        /* move the cursor to its new position */
                        cursor.x = newCursor.x;
                        cursor.y = newCursor.y;
                        
                        break;
                    case('f'): /* moves cursor without drawing*/
                        cursor.x += Math.cos(cursor.angle);                       
                        cursor.y += Math.sin(cursor.angle);
                        
                        break;
                    case('+'): /* rotates cursor */
                        
                        cursor.angle+=angle;
                        
                        while(cursor.angle >= (2*Math.PI))
                            cursor.angle-=(2*Math.PI);
                        break;
                    case('-'): /* rotates cursor */
                        
                        cursor.angle-=angle;
                        
                        while(cursor.angle < 0)
                            cursor.angle+=(2*Math.PI);
                        break;
                    case('['): /* pushes cursor state down on the stack*/
                        cursorStack.push(new Cursor(cursor));
                        break;
                    case(']'): /* pops the stack and 
                                * sets cursor to that value*/
                        
                        cursor = cursorStack.pop();
                        break;
                        
                        default:
                            break;
                }
            }
        }
        catch(IOException e)
        {}
        
        
        
    }
    
/** expands an Lsystem grammar
 * @param init The initial string for the LSystem
 * @param grammar The string containing the rules for the LSystem of format:
 * <char>=<string>\n
 * @param depth number of times the LSystem should recurse
 * @return the expanded string
 */
    public String expandPath(String init, String grammar, int depth)
    
    {
        /* if the grammar isn't to be expanded, simply return the
         * initial string */
        if(depth <= 0)
        {
            return init;
        }
        
        StringWriter sw = new StringWriter();
        
        Properties props = new Properties();
        try{
            /* Big-time cheat that uses the Java property loader to
             * parse the rules */
            props.load(new ByteArrayInputStream(grammar.getBytes()));
            StringReader initReader = new StringReader(init);
            
            int c;
            
            /* replaces all characters that have associated rules
             * with the appropriate string */
            for(int i=0;i<init.length();i++)
            {
                c = initReader.read();
                
                StringWriter sw2 = new StringWriter();
                sw2.write(c);
                
                sw.write(props.getProperty(sw2.toString(), sw2.toString()));
            }
        }
        catch (IOException e)
        {}
        /* if the grammar needs to be expanded again, do so*/
        if((depth-1)>0)
        {
            return expandPath(sw.toString(),grammar, depth-1);
        }
        else
            return sw.toString();
        
    }
    

/** repaints the main window
 * @param g Graphics object to be repainted
 */
    @Override
    public void paint(Graphics g) {
        
        //bufferGraphics .setColor(Color.green);

        g.setColor(Color.red);
        g.fillRect(2, height - (int)(height * fitness) + 2, 5, (int)(height * fitness) - 4);

        g.setColor(Color.gray);
        g.drawRoundRect(0, 0, width - 1, height - 1, 5, 5);

        g.setColor(Color.green);
        /*Get the expanded path from the initText and rules Fields*/
        String expandedPath = expandPath(init,grammar,n);
//        System.out.println(grammar + " : " + n + " : " + expandedPath);
        
        /*Find/set range for drawing area and draw L-System*/
        try{
            getScale(expandedPath,(Double.valueOf(angle)).doubleValue()/(180/Math.PI));
            drawPath(expandedPath,(Double.valueOf(angle)).doubleValue()/(180/Math.PI),g);
        }
        catch(NumberFormatException e)
        {}
        
        
        
//        g.drawImage(buffer,5,5,this);
        
    }
    
    @Override
    public void repaint(){
        super.repaint();
       // System.out.println("Repainting: " + this.hashCode());
    }
    
    @Override
    public void update(Graphics g)
    {
        paint(g);
    }
    
    /*/
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.add(new LSystem());
        window.setVisible(true);
        //window.show();
    }
    //*/
    
}