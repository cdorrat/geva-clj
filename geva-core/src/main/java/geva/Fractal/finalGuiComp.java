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
 * guiComp.java
 *
 * Created on January 28, 2008, 11:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geva.Fractal;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author patrickmiddleburgh
 */

public class finalGuiComp extends JFrame implements ActionListener{
//public class guiComp extends JFrame {

    public finalGuiComp(String[] arrayPheno, String[] arrayDepth, String[] arrayAngle ,int valids){
        
        
        if(valids>0){
            for(int i = 0; i < valids; i++){
                alphaPhenos[i]=arrayPheno[i];
                alphaDepth[i]=Integer.parseInt(arrayDepth[i]);
                alphaAngle[i]=Float.parseFloat(arrayAngle[i]);
                if(alphaPhenos[i]==null){
                    alphaPhenos[i]="F =  F + [ [ F + F ] - F - F ] - F";
                    System.out.println("got a problem!");
                }
                
            }
            init(alphaPhenos, alphaDepth, alphaAngle);
            this.pack();
            this.setVisible(true);
        }else{
            for (int i = 0; i < alphaPhenos.length; i++) {
                alphaPhenos[i]="F =  F + [ [ F + F ] - F - F ] - F";
            }
            init(alphaPhenos, alphaDepth, alphaAngle);
            this.pack();
            this.setVisible(true);
        }

    }

    
    /** Initializes the applet prototype */
    public void init(String [] grammars, int[] depths, float[] angles) {
        for (int i = 0; i < angles.length; i++) {
            betaPhenos[i]= grammars[i];
            betaDepth[i]= depths[i];
            betaAngle[i]= angles[i];
           
        }
        
        try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    //System.out.println("grammar1: "+Grammar1);
                    initComponents(betaPhenos, betaDepth, betaAngle);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
    }
    
                             
    private void initComponents(String[] gras, int[] deps, float[] angs) {
        
        
        header = new JPanel();
        body = new JPanel();
        footer = new JPanel();
        
        comp1 = new finalProtoComp(gras[0], deps[0], angs[0]);
        comp2 = new finalProtoComp(gras[1], deps[1], angs[1]);
        comp3 = new finalProtoComp(gras[2], deps[2], angs[2]);
        comp4 = new finalProtoComp(gras[3], deps[3], angs[3]);
        comp5 = new finalProtoComp(gras[4], deps[4], angs[4]);
        comp6 = new finalProtoComp(gras[5], deps[5], angs[5]);
        comp7 = new finalProtoComp(gras[6], deps[6], angs[6]);
        comp8 = new finalProtoComp(gras[7], deps[7], angs[7]);
        comp9 = new finalProtoComp(gras[8], deps[8], angs[8]);
        comp10 = new finalProtoComp(gras[9], deps[9], angs[9]);

        
        jButton_next = new JButton();
        jButton_next.setText("Next");
        jButton_next.addActionListener(this);

        jButton_done = new JButton();
        jButton_done.setText("Done");
        jButton_done.addActionListener(this);
        
        jLabel_tital = new javax.swing.JLabel();
        jLabel_tital.setFont(new java.awt.Font("Lucida Grande", 1, 36));
        jLabel_tital.setText("GELsys");
        
        jLabel_text = new javax.swing.JLabel();
        //jLabel_text.setFont(new java.awt.Font("Lucida Grande", 0, 14));
        jLabel_text.setText("The Grammatically Evolved L-System Parents Chosen by You :) ");
        
        
        /*/
         *
         *setting applet size and layout
         *
        //*/
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        size = new Dimension(1000,700);
        this.setSize(size);
        
        setLayout(null);
        
        header.setBounds(0,0,1000,60);
        body.setBounds(0,60,1000,580);    
        footer.setBounds(0,635,1000,700);
        
        GridLayout bodyLayout = new GridLayout(2,10);
        body.setLayout(bodyLayout);
        //footer.setLayout(footerLayout);
        
        /*/
         *
         *adding conponants
         *
        //*/
        
        add(header);
        add(body);
        add(footer);
        
        header.add(jLabel_tital);
        
        body.add(comp1);
        body.add(comp2);
        body.add(comp3);
        body.add(comp4);
        body.add(comp5);
        body.add(comp6);
        body.add(comp7);
        body.add(comp8);
        body.add(comp9);
        body.add(comp10);
        
        //not working dont know why???
        footer.add(jLabel_text);
        footer.add(jButton_next);
        footer.add(jButton_done);
        
        
        
        
    }// </editor-fold>                        

                                

    public void actionPerformed(ActionEvent actionEvent) {
       

        if (actionEvent.getSource() == jButton_next){
            //System.out.println("jButton_next is working");
            icontinue=true;
        }
        if (actionEvent.getSource() == jButton_done) {
            //System.out.println("jButton_done is working");
            System.exit(0);
        }
        
    }
    
    public static boolean shouldContinue(){
        return icontinue;
    } 
    
    public static void setIContinue(){
        icontinue = false;
    }
    
    // Variables declaration - do not modify
    //GUI componants
    private JPanel header;
    private JPanel body;
    private JPanel footer;
    private JLabel jLabel_tital;
    private JLabel jLabel_text;
    private JButton jButton_next;
    private JButton jButton_done;
    private finalProtoComp comp1;
    private finalProtoComp comp2;
    private finalProtoComp comp3;
    private finalProtoComp comp4;
    private finalProtoComp comp5;
    private finalProtoComp comp6;
    private finalProtoComp comp7;
    private finalProtoComp comp8;
    private finalProtoComp comp9;
    private finalProtoComp comp10;
    Dimension size;
    
    int guard = 0;
    String[] alphaPhenos = new String[10];
    int[] alphaDepth = new int[10];
    float[] alphaAngle = new float [10];
    
    String [] betaPhenos = new String [10];
    int[] betaDepth = new int[10];
    float[] betaAngle = new float [10];
    
    final String Grammar1 = "F=F[+F]F[-F]F+F-F";
    private static boolean icontinue =false;
   
    
    // End of variables declaration                   
    
}