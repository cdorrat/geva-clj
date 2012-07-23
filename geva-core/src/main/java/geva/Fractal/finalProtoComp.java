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
 * panelComp.java
 *
 * Created on January 25, 2008, 2:24 PM
 */

package geva.Fractal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author  patrickmiddleburgh
 */
public class finalProtoComp extends JPanel implements ActionListener{
    
    /** Creates new form protoComp */
    public finalProtoComp(String grammar, int depth, float angle) {
        //System.out.println("finalProtoComp: "+depth+angle+grammar);
        grammar_new = grammar;
        depth_new = depth;
        angle_new = angle;
        //System.out.println("PROTOCOMP; grammar: "+grammar+", depth: "+depth+", angle: "+angle);
        initComponents();
        
         
    }
    
    
                        
    private void initComponents() {
        
        jPanel1 = new LSystem(grammar_new, depth_new, angle_new, 460, 0);
        jButton_enlarge = new JButton();
        jButton_enlarge.setText("Enlarge");
        jButton_enlarge.addActionListener(this);

        

        jPanel1.setBackground(new Color(255, 255, 255));

        
        size = new Dimension(200,260);
        this.setSize(size);
        
        setLayout(null);
        
        jPanel1.setBounds(10,10,170,170);
        jButton_enlarge.setBounds(50,210,90,20);
        
        add(jPanel1);
        add(jButton_enlarge);
        
        
    }// </editor-fold> 

    public void actionPerformed(ActionEvent actionEvent) {
        //System.out.println("the enlarge button works!");
        Dimension size = new Dimension(500,625);
        enlarged bigger = new enlarged(grammar_new, depth_new, angle_new);
        bigger.setSize(size);
        
        //keep running untill generate button on gui is pressed.
        //while(enlarged.shouldContinue()==false){
          //  System.out.print("");
        //}
        //bigger.setVisible(false);
        //bigger = null;
    }

    // Variables declaration - do not modify                     
    private JPanel jPanel1;
    private JButton jButton_enlarge;
    Dimension size;
    BufferedImage test;
    
    String grammar_new;
    int depth_new;
    float angle_new;
    // End of variables declaration                   
    
}

