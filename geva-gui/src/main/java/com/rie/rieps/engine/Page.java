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
 * Page.java
 *
 * Created on 23 fevrier 2007, 17:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine;

import com.rie.rieps.engine.color.Color;
import com.rie.rieps.engine.color.Gray;
import com.rie.rieps.engine.fonts.PSFont;
import com.rie.rieps.engine.image.Image;
import com.rie.rieps.exception.RiepsException;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Yves Piel
 */
public class Page {
    
    
    private Document owner;
    
    private LineFormat currentLineFormat = LineFormat.DEFAULT;
    private PSFont currentFont = PSFont.DEFAULT;
    private Color currentFillColor = Gray.BLACK;
    private float pageHeight= 0;
    
    private Map deviceParameters = new HashMap();
    
    private StringBuffer sbPage = new StringBuffer(500);
    
    /** the owner must be the Document which has created this page.
     */
    Page(Document owner, float pageHeight) {
        this.owner = owner;
        this.pageHeight = pageHeight;
    }
    
    public StringBuffer getPageStringBuffer(){
        return this.sbPage;
    }
    
    public Document getOwnerDocument(){
        return this.owner;
    }
    
    public void showPage() throws IOException, RiepsException{
        this.getOwnerDocument().showPage(this);
    }
    
    public void setDeviceParameter(DeviceParameter param, Object value){
        this.getDeviceParameters().put(param, value);
    }
    
    public void setDeviceParameter(Map parameters){
        this.getDeviceParameters().putAll(parameters);
    }
    
    public void setLineFormat(LineFormat lf){
        this.currentLineFormat = lf;
    }
    
    public void setPSFont(PSFont psf){
        this.currentFont = psf;
    }
    
    public void setAWTFont(Font fnt){
        PSFont psfnt = this.getOwnerDocument().getOwnerJob().getPSFontFromAWT(fnt);
        this.setPSFont(psfnt);
    }
    
    public void setColor(Color c){
        this.currentFillColor = c;
    }
    
    private float relativeY(float y){
        if(this.pageHeight > 0){
            y = this.pageHeight - y;
        }
            
        return y;
    }
    
    
    public void showText(float x, float y, float weight, float height, String txt, TextParameter tp){
        y = this.relativeY(y);
        this.getOwnerDocument().getFactory().showText(this, x, y, weight, height, txt, tp);
    }
    
    public void drawLine(float x, float y, float w, float h){
        y = this.relativeY(y);
        this.getOwnerDocument().getFactory().drawLine(this, x, y, w, h);
    }
    
        
    public void drawRect(float x, float y, float width, float height, float rotate, float radius, boolean border, boolean fill){
        y = this.relativeY(y);
        this.getOwnerDocument().getFactory().drawRect(this, x, y, width, height, rotate, radius, border, fill);
    }
    
    public void drawCircle(float x, float y, float radius, boolean border, boolean fill){
        y = this.relativeY(y);
        this.getOwnerDocument().getFactory().drawEllipse(this, x, y, radius, radius, 0, border, fill);
    }
    
    public void drawEllipse(float x, float y, float width, float height, float rotate, boolean border, boolean fill){
        y = this.relativeY(y);
        this.getOwnerDocument().getFactory().drawEllipse(this, x, y, width, height, rotate, border, fill);
    }
    
    public void drawImage(float x, float y, Image img, float width, float height) throws RiepsException{
        y = this.relativeY(y);
        this.getOwnerDocument().getFactory().drawImage(this, x, y, img, width, height);
    }
    
    public void drawFrame(Frame frm){
        float y = frm.getTop();
        y = this.relativeY(y);
        frm.setTop(y);
        PSFont fnt = this.getCurrentFont();
        this.setPSFont(frm.getPsfnt());
        this.getOwnerDocument().getFactory().drawFrame(this, frm);
        this.setPSFont(fnt);
    }

    public LineFormat getCurrentLineFormat() {
        return currentLineFormat;
    }

    public PSFont getCurrentFont() {
        return currentFont;
    }

    public Color getCurrentFillColor() {
        return currentFillColor;
    }

    public Map getDeviceParameters() {
        return deviceParameters;
    }
}
