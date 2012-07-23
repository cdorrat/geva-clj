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

/*
 * PSFontFinder.java
 *
 * Created on 1 mars 2007, 16:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine.fonts;

import com.rie.rieps.exception.RiepsException;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Yves Piel
 */
public class PSFontFinder {
    private final static String dot = "-";
    
    /** A Map with awtfontname.style[.size] as keys, and PSFont as values.
     */
    private Map awtps = new HashMap();
    
    private boolean echoMapping = false;
    private boolean askForUnkownAWTFont = false;
    private String[] availableDevicePSFont;
    
    /** Creates a new instance of PSFontFinder */
    public PSFontFinder() {
    }
    
    public void setAvailableDeviceFont(String[] deviceFnt){
        this.availableDevicePSFont = new String[deviceFnt.length];
        System.arraycopy(deviceFnt, 0, this.availableDevicePSFont, 0, deviceFnt.length);
    }
    
    /** Load a properties file which contains awt description = psfont description
     */
    public void addMappingFile(File f) throws IOException{
        Properties prp = new Properties();
        InputStream is = new FileInputStream(f);
        prp.load(is);
        
        Set es = prp.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()){
            Map.Entry e = (Map.Entry)it.next();
            try{
                PSFont psfnt = new PSFont((String) e.getValue());
                this.addAwtPS((String)e.getKey(), psfnt);
            } catch(RiepsException ex){
                System.err.println("Can't create a PSFont from this description : '"+e.getValue()+"'");
                ex.printStackTrace(System.err);
            }
        }
        
        is.close();
    }
    
    private void addAwtPS(String awt, PSFont psfnt){
        if(this.isEchoMapping()){
            System.out.println(awt+"="+psfnt.getPSDesc());
        }
        awtps.put(awt, psfnt);
    }
    
    /** Gives a PSfont from a awt one.
     * If no font is founded it return @see{PSFont.DEFAULT}.
     */
    public PSFont searchPSFont(Font fnt){
        PSFont psfnt = null;
        
        String s = fnt.getFamily()+dot+fnt.getStyle()+dot+fnt.getSize2D();
        psfnt = (PSFont)this.getAwtPSMapping().get(s);
        if(psfnt != null) return psfnt;
        
        s = fnt.getFamily()+dot+fnt.getStyle();
        psfnt = (PSFont)this.getAwtPSMapping().get(s);
        if(psfnt != null) {
            return psfnt.setSize(fnt.getSize2D());
        }
        
        s = fnt.getFamily();
        psfnt = (PSFont)this.getAwtPSMapping().get(s);
        if(psfnt != null) {
            return psfnt.setSize(fnt.getSize2D());
        }
        
        if(this.askForUnkownAWTFont()){
            AWTPSLink link = this.askForUnkown(fnt);
            this.addAwtPS(link.getAWTDesc(), link.getPsfont());
            return this.searchPSFont(fnt); // only one recursive !
        }
        
        return PSFont.DEFAULT.setSize(fnt.getSize2D());
    }
    
    public boolean askForUnkownAWTFont() {
        return askForUnkownAWTFont;
    }
    
    public void askForUnkownAWTFont(boolean askForUnkownAWTFont) {
        this.askForUnkownAWTFont = askForUnkownAWTFont;
    }
    
    private AWTPSLink askForUnkown(Font awt) {
        PSFont psfnt = null;
        
        JDialog jd = new JDialog((JFrame)null, "Link awt/Sytem font with postscript", true);
        AskForUnknownFontPanel jp = new AskForUnknownFontPanel(jd, awt, availableDevicePSFont);
        jd.setContentPane(jp);
        jd.pack();
        jd.setLocationRelativeTo(null);
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jd.setVisible(true);
        
        AWTPSLink l = null;
        if(jp.isLinked()){
            l = new AWTPSLink(jp.getPSFont(), jp.getAWTID());
        }
        
        return l;
    }
    
    /** Clone the AWT->PSFont mapping and return it.
     */
    public Map getAwtPSMapping() {
        return new HashMap(awtps);
    }
    
    public final static void main(String[] args){
        PSFontFinder psff = new PSFontFinder();
        psff.setAvailableDeviceFont(new String[]{"Times-Roman", "Helvetica", "Courier", "Times-Italic", "Times-Bold", "Helvetica-Bold"});
        psff.askForUnkownAWTFont(true);
        psff.setEchoMapping(true);
        psff.searchPSFont(new Font("Dialog", Font.BOLD+Font.ITALIC, 13));
    }
    
    private static class AWTPSLink{
        private PSFont psfont;
        private String awtDesc;
        
        AWTPSLink(PSFont psfnt, String awtd){
            this.psfont = psfnt;
            this.awtDesc = awtd;
        }
        
        public PSFont getPsfont() {
            return psfont;
        }
        
        public String getAWTDesc() {
            return awtDesc;
        }
    }

    public boolean isEchoMapping() {
        return echoMapping;
    }

    public void setEchoMapping(boolean echoMapping) {
        this.echoMapping = echoMapping;
    }
}
