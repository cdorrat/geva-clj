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
 * Job.java
 *
 * Created on 26 fevrier 2007, 09:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine;

import com.rie.rieps.engine.factories.Factory;
import com.rie.rieps.engine.factories.PSFactory;
import com.rie.rieps.engine.fonts.PSFont;
import com.rie.rieps.engine.fonts.PSFontFile;
import com.rie.rieps.engine.fonts.PSFontFinder;
import com.rie.rieps.exception.RiepsException;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Todo open a dictionnary at the beginning of the job and close it  when close method is called
 * Todo the page size must be given ! so %boundedbox can be set
 * @author Yves Piel
 */
public class Job {
    
    private Factory factory;
    private OutputStream os;
    private String outputCharset = null;
    private List psFontFile = new ArrayList();
    private float pageHeight = 0;
    private float pageWidth = 0;
    
    private boolean openned = false;
    private boolean closed = false;
    
    private PSFontFinder psff;
    
    public static Job createJob(OutputStream os, Factory factory, float pageWidth, float pageHeight){
        return new Job(os, factory, pageWidth, pageHeight);
    }
    
    /**
     * Change the origine of the postscript to upper left corner of the page.
     */
    public static Job createPSTopLeftOriginJob(OutputStream os, float pageWidth, float pageHeight){
        PSFactory psfact = new PSFactory();
        return createJob(os, psfact, pageWidth, pageHeight);
    }
    
    public synchronized void open() throws RiepsException{
        if(this.isClosed()){
            throw new RiepsException("This job has already been closed.");
        }
        if(this.isOpenned()){
            return;
        }
        this.openned = true;
        //this.writeToOutput(this.getFactory().getBeginJob());
    }
    
    public synchronized void close() throws RiepsException {
        if(!this.isOpenned()){
            throw new RiepsException("You can't close a job which hasn't been openned.");
        }
        this.openned = false;
        this.closed = true;
    }
    
    public void setAvailableDeviceFont(String[] deviceFnt){
        this.psff.setAvailableDeviceFont(deviceFnt);
    }
    
    public void addAwtPSFontMappingPropertiesFile(File f) throws IOException{
        this.psff.addMappingFile(f);
    }
    
    public void askForUnmappedAWTFont(boolean ask){
        this.psff.askForUnkownAWTFont(ask);
    }
    
    public boolean askForUnmappedAWTFont(){
        return psff.askForUnkownAWTFont();
    }
    
    public void echoFontMapping(boolean b){
        this.psff.setEchoMapping(b);
    }
    
    public PSFont getPSFontFromAWT(Font fnt){
        return this.psff.searchPSFont(fnt);
    }
    
    Factory getFactory(){
        return this.factory;
    }
    
    /** Creates a new instance of Job */
    private Job(OutputStream os, Factory factory, float pageWidth, float pageHeight) {
        this.factory = factory;
        this.os = os;
        this.psff = new PSFontFinder();
        this.pageHeight = pageHeight;
        this.pageWidth = pageWidth;
    }
    
    private OutputStream getOutputStream(){
        return this.os;
    }
    
    public void setOutputCharset(String charset){
        this.outputCharset = charset;
    }
    
    public String getOutputCharset(){
        return this.outputCharset;
    }
    
    public Document createDocument(int nbPages) throws RiepsException{
        if(!this.isOpenned()){
            throw new RiepsException("You have to open Job before create a document.");
        }
        return new Document(this, nbPages, this.pageHeight, this.pageWidth);
    }
    
    
    public void addPSFontFile(PSFontFile psff){
        this.psFontFile.add(psff);
    }
    
    void writeToOutput(StringBuffer sb) throws IOException, RiepsException{
        if(!this.isOpenned()){
            throw new RiepsException("You have to open Job before write to the output stream.");
        }
        
        this.getOutputStream().write((this.outputCharset == null) ? sb.toString().getBytes() : sb.toString().getBytes(this.getOutputCharset()));
    }
    
    private boolean isOpenned() {
        return this.openned;
    }
    
    private boolean isClosed(){
        return this.closed;
    }
    
}
