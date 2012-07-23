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

import geva.Util.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author patrickmiddleburgh
 */





public class enlarged extends JFrame implements ActionListener{
//public class guiComp extends JFrame {

    public enlarged(String grammar, int depth, float angle){

        alphaPhenos = grammar;
        alphaDepth = depth;
        alphaAngle = angle;
        initComponents(alphaPhenos, alphaDepth, alphaAngle);
        this.pack();
        this.setVisible(true);
        

    }
                         
    private void initComponents(String gras, int deps, float angs) {
        
        /*/ 
        *
        *componant instansiantion
        *
        //*/
        
        header = new JPanel();
        body = new JPanel();
        footer = new JPanel();
        body.setLayout(new BorderLayout());
        
        comp1 = new largeLSystem(gras, deps, angs);
        
        jButton_save = new JButton();
        jButton_save.setText("Save");
        jButton_save.addActionListener(this);
        
        jButton_saveps = new JButton();
        jButton_saveps.setText("Save PostScript");
        jButton_saveps.addActionListener(this);
        
        jButton_done = new JButton();
        jButton_done.setText("Close");
        jButton_done.addActionListener(this);
        
        jLabel_tital = new javax.swing.JLabel();
        jLabel_tital.setFont(new java.awt.Font("Lucida Grande", 1, 36));
        jLabel_tital.setText("GELsys");
        
        
        /*/
         *
         *setting applet size and layout
         *
        //*/
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
       // size = new Dimension(1000,700);
        //this.setSize(size);
        
//        header.setBounds(0,0,500,50);
  //      body.setBounds(0,50,500,500);    
    //    footer.setBounds(0,550,500,50);
        
        /*/
         *
         *adding conponants
         *
        //*/
        
        add(header, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        
        header.add(jLabel_tital);
        
        body.add(comp1);
        
        
        footer.add(jButton_saveps);
        footer.add(jButton_save);
        footer.add(jButton_done);
        
        
        
        
    }// </editor-fold>                        

                    

    public void actionPerformed(ActionEvent actionEvent) {
        
        if (actionEvent.getSource() == jButton_save){
            if(guard>0){
                JOptionPane.showMessageDialog(null, "This L-System is Already Saved.");
                //System.out.println("no save");
            }else{
                //writing the parent selections to a file.
                String fileName = "savedLSystems.txt";
                //String fileName = "../geva.Parameter/output.txt";
                try{
                    FileWriter fw = new FileWriter(fileName, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(alphaDepth+" "+alphaAngle+" "+alphaPhenos);
                    bw.write("\n");
                    bw.close();
                    guard++;
                    JOptionPane.showMessageDialog(null, "L-System Saved to \"savedLSystems.txt\" in Project Folder.");

                }
                catch (Exception ex) {
                    System.out.println("not writing to the file! :(");
                }
            }
            
        }
        else
        if(actionEvent.getSource() == jButton_saveps)
            savePostScript();
        else
        if (actionEvent.getSource() == jButton_done) {
            this.setVisible(false);
            this.dispose(); 
        }
       
        
        
    }
    
    public String savePostScript()
    {   PSLSystem2 lSystem = new PSLSystem2(alphaPhenos, alphaDepth, alphaAngle);
        JFileChooser chooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter("PostScript", "ps");
        String filename;
        chooser.setFileFilter(filter);
        if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        {   filename = chooser.getSelectedFile().toString();
            // TODO - file already exists, request if ok to overwrite. Currently
            //  this just overwrites
            if(filename.toLowerCase().endsWith(".ps") == false)
                filename += ".ps";
            lSystem.save(filename);
        }
        return null;
        
    }

    
    // Variables declaration - do not modify
    //GUI componants
    private JPanel header;
    private JPanel body;
    private JPanel footer;
    private JLabel jLabel_tital;
    private JButton jButton_save;
    private JButton jButton_saveps;
    private JButton jButton_done;
    private JPanel comp1;
    
    Dimension size;

    int guard = 0;
    String alphaPhenos;
    int alphaDepth;
    float alphaAngle ;
    
    String betaPhenos;
    int betaDepth;
    float betaAngle;
            
    // End of variables declaration                   
    
}