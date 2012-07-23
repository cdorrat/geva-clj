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

public class GenPSFile {
    
    
    public static void main(String[] args){
        try {
            if(args.length <= 0){
                System.out.println("Test the Rieps java postscript library.\nUSAGE : java -cp rieps.jar com.rie.rieps.simpletest.GenPSFile output.ps [AWTPSFontMapping.properties]");
                System.exit(0);
            }
            
            java.net.URL url = (GenPSFile.class).getResource("duke.jpg");
            BufferedImage bufferedImage = ImageIO.read(url);
            SmallGrayImage bwi = new SmallGrayImage(SmallGrayImage.CONVERT_MODE_AVERAGE);
            bwi.load(bufferedImage);
            
            SmallGrayImage bwi2 = new SmallGrayImage(SmallGrayImage.CONVERT_MODE_CIE_601);
            bwi2.load(bufferedImage);
            
            SmallGrayImage bwi3 = new SmallGrayImage(SmallGrayImage.CONVERT_MODE_CIE_709);
            bwi3.load(bufferedImage);
            
            
            OutputStream os = new FileOutputStream(args[0]);
            //OutputStream os = System.out;
            Job job = Job.createPSTopLeftOriginJob(os, 595, 842);
            
            if(args.length > 1){
                String mapfile = null;
                mapfile = args[1];
                File f = new File(mapfile);
                if(f.exists() && f.isFile()){
                    job.addAwtPSFontMappingPropertiesFile(f);
                }
            }
            
            job.setAvailableDeviceFont(new String[]{"Times-Roman", "Helvetica", "Courier", "Times-Italic", "Times-Bold", "Helvetica-Bold"});
            job.askForUnmappedAWTFont(true);
            job.echoFontMapping(true);
            job.open();
            
            Document doc = job.createDocument(10);
            doc.open();
            
            for(int i =0; i<10; i++){
                Page p = doc.createPage();
                p.setColor(new Gray(0.7f));
                p.setLineFormat(new LineFormat(5, new int[]{10}, i, new Gray(0f)));
                //p.setPSFont(new PSFont("Times-Roman", 20, 10*i));
                p.setAWTFont(new Font("DIALOG", Font.PLAIN, 7+i));
                p.drawRect(100, 200+10*i, 300, 50, i*5, i*3, true, true);
                p.drawRect(10, 300+10*i, 100, 70, i*7, 0, true, false);
                
                p.drawCircle(400, 500, 75, true, true);
                
                p.setColor(new Gray(1 - i/10f));
                p.setLineFormat(new LineFormat(2, 2*i+1, new Gray(i/10f)));
                p.drawEllipse(250, 600, 30*(i+1), 100, i*20, true, true);
                
                p.drawImage(50,100, bwi, 100, 100);
                p.drawImage(150,100, bwi2, 100, 100);
                p.drawImage(350,100, bwi3, (i+1)*30, 100);
                p.setColor(new Gray(0f));
                p.showText(50, 400, 100, 50, "Bonjour le monde (!!! : "+i, new TextParameter(TextParameter.HZ_RIGHT, TextParameter.VERT_MIDDLE, Gray.BLACK, i*15, i%2 == 0));
                
                //  Frame test
                LineFormat lf = new LineFormat(1, new int[]{5}, 0, new Gray(0f));
                String txt = "this is a very long text to test the frame metode of RIEPS. A frame can display text into a box with vertical and horizontal justification.";
                Frame frm = new Frame((float)200, (float)500, (float)100, (float)70 + (i*5), (float)i, (float)i, (float)i, (float)i, lf, (LineFormat)null, lf, (LineFormat)null, true, new Gray(0.8f), i*20f, txt, p.getCurrentFont(), new TextParameter(TextParameter.HZ_CENTER, TextParameter.VERT_MIDDLE, Gray.BLACK, i*20, i%2==0));
                p.drawFrame(frm);
                frm = new Frame((float)200, (float)300, (float)100, (float)70 + (i*5), (float)i, (float)i, (float)i, (float)i, lf, (LineFormat)lf, lf, (LineFormat)lf, true, new Gray(0.8f), i*20f*(-1), txt, p.getCurrentFont(), new TextParameter(TextParameter.HZ_CENTER, TextParameter.VERT_MIDDLE, Gray.BLACK, i*20*(-1), i%2==0));
                p.drawFrame(frm);
                // -----------
                p.setLineFormat(new LineFormat(2, new int[]{5, 5, 2, 5}, 0, new Gray(0f)));
                p.drawLine(200, 500, 100, 0);
                p.drawLine(200, 500, 0, 100);
                
                doc.showPage(p);
            }
            doc.close();
            job.close();
            
        } catch (Exception ex) {
            System.err.println("Erreur lors du test : "+ex);
            ex.printStackTrace();
        }
    }
    
}
