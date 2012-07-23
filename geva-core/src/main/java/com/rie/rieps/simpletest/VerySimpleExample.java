/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Rieps - Free Java  postscript API
 * Copyright (C) 2007 TIIE http://www.tiie.fr
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 *
 * TIIE/RIE Technologies
 * Z.A de la Duquerie
 * 37390 Chanceaux sur Choisille FRANCE
 * http://www.tiie.fr
 *
 * main developper : Yves Piel ( yvespielusenet AT free.fr )
 */

package com.rie.rieps.simpletest;

import com.rie.rieps.engine.Document;
import com.rie.rieps.engine.Frame;
import com.rie.rieps.engine.Job;
import com.rie.rieps.engine.LineFormat;
import com.rie.rieps.engine.fonts.PSFont;
import com.rie.rieps.engine.Page;
import com.rie.rieps.engine.TextParameter;
import com.rie.rieps.engine.color.Gray;
import com.rie.rieps.engine.image.SmallGrayImage;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;

/** This is a minimal example of how to use RiePS.
 */
public class VerySimpleExample {
    
    public static void main(String args[]){
        
        try {
            if(args.length <= 0){
                System.out.println("Test the Rieps java postscript library.\nUSAGE : java -cp rieps.jar com.rie.rieps.simpletest.VerySimpleExample output.ps");
                System.exit(0);
            }
            
            // Create a  gray postscript picture from a color jpg
            java.net.URL url = (GenPSFile.class).getResource("duke.jpg");
            BufferedImage bufferedImage = ImageIO.read(url);
            SmallGrayImage bwi = new SmallGrayImage(SmallGrayImage.CONVERT_MODE_AVERAGE);
            bwi.load(bufferedImage);
            
            
            // Select the output file
            OutputStream os = new FileOutputStream(args[0]);
            
            
            // create a new job which use A4 paper
            // The origine is the top/left corner of the paper
            Job job = Job.createPSTopLeftOriginJob(os, 595, 842);
            
            // Set usable postscript fonts of the device
            job.setAvailableDeviceFont(new String[]{"Times-Roman", "Helvetica", "Courier", "Times-Italic", "Times-Bold", "Helvetica-Bold"});
            
            // Show the dialog box the link unkwown awt font to device postscript font
            job.askForUnmappedAWTFont(true);
            
            // output the awt/postscript font links.
            // The output is well formatted to be automatically loaded by the RiePS library
            // for future jobs
            job.echoFontMapping(true);
            
            // Open the job
            job.open();
            
            
            // Create a 2 pages document into the job
            Document doc = job.createDocument(2);
            doc.open();
            
            // create a page
            Page p = doc.createPage();
            
            // Get the font for the frame. If this awt is unknown, the dialog box ask you which postscript font to use.
            PSFont fnt = job.getPSFontFromAWT(new Font("DIALOG", Font.PLAIN, 12)); 
            
            String txt = "Here is a very long text into a frame. The text is vertically and horizontally centered. Broder of a frame can be configured for each side. You can choose the background color too and the text color. Moreover, you can set text padding and clip the text into the frame if it's too height.";
            LineFormat lf0 = new LineFormat(4, new Gray(0.5f));
            LineFormat lf1 = new LineFormat(1, new int[]{3,5}, 0, Gray.BLACK);
            Frame frm = new Frame(100, 100, 100, 200, 15, 15, 15, 15, lf0, lf0, lf1, lf1, true, new Gray(0.8f), 30, txt, fnt, new TextParameter(TextParameter.HZ_CENTER, TextParameter.VERT_MIDDLE, Gray.BLACK, 30, false));
            p.drawFrame(frm);
            frm = new Frame(300, 200, 100, 200, 15, 15, 15, 15, lf0, lf1, lf0, lf1, true, new Gray(0.8f), -30, txt, fnt, new TextParameter(TextParameter.HZ_CENTER, TextParameter.VERT_MIDDLE, Gray.BLACK, -30, true));
            p.drawFrame(frm);
            
            // draw an ellipse
            p.setColor(new Gray(0.95f));
            p.setLineFormat(lf1);
            p.drawEllipse(150, 550, 100, 150, 50, true, true);
            
            // draw a rectangle with runded corners
            p.setColor(new Gray(0.95f));
            p.setLineFormat(lf0);
            p.drawRect(350, 550, 200, 150, 50, 25, true, true);
            
            // insert the page into the document
            doc.showPage(p);
            
            // insert the same page with a picture added
            p.drawImage(500, 200, bwi, 100, 100);
            doc.showPage(p);
            
            // close the document and the job
            doc.close();
            job.close();
        } catch(Exception e){
            System.err.println("Error in the example : "+e.getMessage());
            e.printStackTrace(System.err);
        }
    }
    
}
