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
 * Document.java
 *
 * Created on 23 fevrier 2007, 17:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine;

import com.rie.rieps.engine.DeviceParameter;
import com.rie.rieps.engine.factories.Factory;
import com.rie.rieps.exception.RiepsException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Document can generate the output postscript page per page.
 * The methode open must be called before creating page.
 * The include of PSFontFile and PSIncludeFile must be done before the call of open().
 *
 * @author Yves Piel
 */
public class Document {
    
    private boolean openned = false;
    private boolean closed = false;
    
    private Job ownerJob;
    private int nbPages;
    
    private Map deviceParameters = new HashMap();
    
    private int currentPageNumber = 0;
    
    private float pageHeight = 0;
    private float pageWidth = 0;
    
    
    Document(Job owner, int nbPages, float ph, float pw) {
        this.ownerJob = owner;
        this.nbPages = nbPages;
        this.pageHeight = ph;
        this.pageWidth = pw;
        this.deviceParameters.put(DeviceParameter.PAGE_SIZE, new float[]{pw, ph});
    }
    
    public Job getOwnerJob(){
        return this.ownerJob;
    }
    
    public synchronized void open() throws RiepsException, IOException{
        if(this.isOpenned()){
            return;
        }
        this.openned = true;
        this.getOwnerJob().writeToOutput(this.getOwnerJob().getFactory().getBeginJob(this.nbPages, this.pageWidth, this.pageHeight));
    }
    
    public synchronized boolean isOpenned(){
        return this.openned;
    }
    
    public synchronized void close() throws RiepsException, IOException{
        if(!this.isOpenned()){
            return;
        }
        this.getOwnerJob().writeToOutput(this.getOwnerJob().getFactory().getEndJob());
        this.closed = true;
    }
    
    public synchronized boolean isClosed(){
        return this.closed;
    }
    
    Factory getFactory(){
        return this.getOwnerJob().getFactory();
    }
    
    public Page createPage(){
        Page p = new Page(this, this.pageHeight);
        p.setDeviceParameter(this.deviceParameters);
        return p;
    }
    
    public void setDeviceParameter(DeviceParameter param, Object value){
        this.deviceParameters.put(param, value);
    }
    
    /** Send a complete page to the outputStream.
     */
    public synchronized void showPage(Page page) throws IOException, RiepsException{
        this.currentPageNumber++;
        StringBuffer sb = this.getFactory().showPage(page, this.currentPageNumber, this.nbPages);
        this.getOwnerJob().writeToOutput(sb);
    }
}
