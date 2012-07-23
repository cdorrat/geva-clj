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
 * PSFactory.java
 *
 * Created on 26 fevrier 2007, 10:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine.factories;

import com.rie.rieps.engine.DeviceParameter;
import com.rie.rieps.engine.Frame;
import com.rie.rieps.engine.LineFormat;
import com.rie.rieps.engine.fonts.PSFont;
import com.rie.rieps.engine.Page;
import com.rie.rieps.engine.TextParameter;
import com.rie.rieps.engine.color.Color;
import com.rie.rieps.engine.color.Gray;
import com.rie.rieps.engine.image.Image;
import com.rie.rieps.exception.RiepsException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * Todocd compact postscript command : /mt {moveto} def, for example
 * Todo verify if I need/when I need to use 'newpath'
 * @author Yves Piel
 */
public class PSFactory implements Factory{
    
    
    private final static String PROTECTED_ANTI_SLASH = "\\\\";
    private final static String ANTI_SLASH = "\\";
    private final static String PROTECTED_LEFT_BRACKET = "\\(";
    private final static String PROTECTED_RIGHT_BRACKET = "\\)";
    private final static String LEFT_BRACKET = "(";
    private final static String RIGHT_BRACKET = ")";
    private final static String SLASH = "/";
    private final static String LEFT_HOOK = "[";
    private final static String RIGHT_HOOK = "]";
    
    
    
    private final static String SP = " ";
    private final static String RET = "\n";
    private final static String PAGE_NUMBER = "%%Page :";
    private final static String SHOW_PAGE = "%%EndPage\nshowpage\n";
    
    private final static String SET_FNT = "makefont setfont-setlh";
    private final static String FIND_FNT = "findfont";
    private final static String ROLL = "roll";
    private final static String MAKELINES = "makelines";
    private final static String APPEND_ARRAY = "aa";
    private final static String XPRINTLINES = "xprintlines";
    private final static String LINE = "line";
    private final static String RECTAR = "rectar";
    private final static String ELLIPSE = "ellipse";
    private final static String GSAVE = "gs";
    private final static String GRESTORE = "gr";
    private final static String TRANSLATE = "trl";
    private final static String ROTATE = "rt";
    private final static String SCALE = "scale";
    
    private final static String pd_open = "<< ";
    private final static String pd_close = " >> setpagedevice\n";
    private final static String pd_ps = "/PageSize [ ";
    
    
    
    private float pageHeight = 0;
    
    public void setTopLeftOrigin(float pageHeight) {
        this.pageHeight = pageHeight;
    }
    
    private BufferedReader getResourceReader(String s) throws IOException{
        BufferedReader br = null;
        InputStream is = getClass().getResourceAsStream(s);
        br = new BufferedReader(new InputStreamReader(is));
        return br;
    }
    
    public StringBuffer getBeginJob(int totalPageNumber, float width, float height) {
        StringBuffer sb = new StringBuffer(5000);
        sb.append("%!PS-Adobe-3.0\n");
        
        sb.append("%%Pages: ");
        if(totalPageNumber > 0){
            sb.append(totalPageNumber);
        } else{
            sb.append("(attend)");
        }
        sb.append("\n%%Creator: RiePS\n");
        sb.append("%%CreationDate: ");
        sb.append((new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new java.util.Date()));
        
        sb.append("\n%%BoundingBox: 0 0 ");
        sb.append(width);
        sb.append(SP);
        sb.append(height);
        
        sb.append("\n%%EndComments\n");
        try{
            BufferedReader br = getResourceReader("/com/rie/rieps/engine/resources/PSFactory.begin.ps");
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(RET);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return sb;
    }
    
    public StringBuffer getEndJob() {
        StringBuffer sb = new StringBuffer(5000);
        try{
            BufferedReader br = getResourceReader("/com/rie/rieps/engine/resources/PSFactory.end.ps");
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(RET);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        sb.append("%%EOF");
        return sb;
    }
    
    public StringBuffer showPage(Page page, int pageNumber, int totalPageNumber) {
        StringBuffer nbp = new StringBuffer();
        nbp.append(PAGE_NUMBER);
        nbp.append(SP);
        nbp.append(pageNumber);
        nbp.append(SP);
        if(totalPageNumber > 0){
            nbp.append(totalPageNumber);
        } else{
            nbp.append(pageNumber);
        }
        nbp.append(RET);
        
        nbp.append(this.makePageDevice(page));
        
        /*StringBuffer sb = this.page.getPageStringBuffer();
        sb.insert(0, nbp.toString());
        sb.append(this.SHOW_PAGE);*/
        
        StringBuffer sbp = new StringBuffer(page.getPageStringBuffer().length()+nbp.length()+50);
        sbp.append(nbp);
        sbp.append(page.getPageStringBuffer());
        sbp.append(SHOW_PAGE);
        
        return sbp;
    }
    
    public void showText(Page page, float x, float y, float weight, float height, String txt, TextParameter tp) {
        StringBuffer sb = new StringBuffer();
        
        String[] stxts = txt.split(RET);
        StringBuffer[] sbtxts = new StringBuffer[stxts.length];
        
        for(int i=0; i<stxts.length; i++){
            sbtxts[i] = this.escapeText(stxts[i]);
        }
        
        
        if(page.getCurrentFont() != null){
            StringBuffer fnt = this.makeFont(page.getCurrentFont(), tp.getColor());
            sb.append(fnt);
            sb.append(RET);
        }
        if(tp == null){
            tp = new TextParameter(TextParameter.HZ_LEFT, TextParameter.VERT_TOP, Gray.BLACK, 0, true);
        }
        
        
        for(int i=0; i<sbtxts.length; i++){
            if(i > 0){
                sb.append(SP);
            }
            sb.append(weight);
            sb.append(SP);
            sb.append(LEFT_BRACKET);
            sb.append(sbtxts[i].toString());
            sb.append(RIGHT_BRACKET);
            sb.append(SP);
            sb.append(MAKELINES);
            if(i > 0){
                sb.append(SP);
                sb.append(APPEND_ARRAY);
            }
        }
        
        sb.append(RET);
        sb.append(tp.isClip());
        sb.append(SP);
        sb.append(tp.getVertical());
        sb.append(SP);
        sb.append(height);
        sb.append(SP);
        sb.append(tp.getHorizontal());
        sb.append(SP);
        sb.append(tp.getRotate());
        sb.append(SP);
        sb.append(weight);
        sb.append(SP);
        sb.append(x);
        sb.append(SP);
        sb.append(y);
        sb.append(SP);
        sb.append(9);
        sb.append(SP);
        sb.append(-1);
        sb.append(SP);
        sb.append(ROLL);
        sb.append(SP);
        sb.append(XPRINTLINES);
        sb.append(RET);
        
        page.getPageStringBuffer().append(sb);
        
    }
    
    public void drawLine(Page page, float x1, float y1, float weight, float height) {
        StringBuffer sb = new StringBuffer();
        
        LineFormat lf = page.getCurrentLineFormat();
        
        float[] cc = lf.getColor().getComposite();
        for(int i=0; i<cc.length; i++){
            sb.append(cc[i]);
            sb.append(SP);
        }
        
        sb.append(LEFT_HOOK);
        int[] dp = lf.getDashPattern();
        for(int i=0; i<dp.length; i++){
            if(i>0)
                sb.append(SP);
            sb.append(dp[i]);
        }
        sb.append(RIGHT_HOOK);
        sb.append(SP);
        sb.append(lf.getDashOffset());
        sb.append(SP);
        sb.append(lf.getWidth());
        sb.append(SP);
        sb.append(x1);
        sb.append(SP);
        sb.append(y1);
        sb.append(SP);
        sb.append(weight);
        sb.append(SP);
        sb.append(height);
        sb.append(SP);
        sb.append(LINE);
        sb.append(RET);
        
        page.getPageStringBuffer().append(sb);
    }
    
    public void drawRect(Page page, float x, float y, float width, float height, float rotate, float radius, boolean border, boolean fill) {
        StringBuffer sb = new StringBuffer();
        
        LineFormat lf = page.getCurrentLineFormat();
        
        if(border){
            float[] cc = lf.getColor().getComposite();
            for(int i=0; i<cc.length; i++){
                sb.append(cc[i]);
                sb.append(SP);
            }
            
            sb.append(LEFT_HOOK);
            int[] dp = lf.getDashPattern();
            for(int i=0; i<dp.length; i++){
                if(i>0)
                    sb.append(SP);
                sb.append(dp[i]);
            }
            sb.append(RIGHT_HOOK);
            sb.append(SP);
            sb.append(lf.getDashOffset());
            sb.append(SP);
            sb.append(lf.getWidth());
            sb.append(SP);
        }
        sb.append(border);
        sb.append(SP);
        
        if(fill){
            float[] cc = page.getCurrentFillColor().getComposite();
            for(int i=0; i<cc.length; i++){
                sb.append(cc[i]);
                sb.append(SP);
            }
        }
        sb.append(fill);
        sb.append(SP);
        sb.append(rotate);
        sb.append(SP);
        sb.append(x);
        sb.append(SP);
        sb.append(y);
        sb.append(SP);
        sb.append(width);
        sb.append(SP);
        sb.append(height);
        sb.append(SP);
        sb.append(radius);
        sb.append(SP);
        sb.append(RECTAR);
        sb.append(RET);
        
        page.getPageStringBuffer().append(sb);
    }
    
    public void drawEllipse(Page page, float x, float y, float width, float height, float rotate, boolean border, boolean fill) {
        StringBuffer sb = new StringBuffer();
        
        LineFormat lf = page.getCurrentLineFormat();
        
        if(border){
            float[] cc = lf.getColor().getComposite();
            for(int i=0; i<cc.length; i++){
                sb.append(cc[i]);
                sb.append(SP);
            }
            
            sb.append(LEFT_HOOK);
            int[] dp = lf.getDashPattern();
            for(int i=0; i<dp.length; i++){
                if(i>0)
                    sb.append(SP);
                sb.append(dp[i]);
            }
            sb.append(RIGHT_HOOK);
            sb.append(SP);
            sb.append(lf.getDashOffset());
            sb.append(SP);
            sb.append(lf.getWidth());
            sb.append(SP);
        }
        sb.append(border);
        sb.append(SP);
        
        if(fill){
            float[] cc = page.getCurrentFillColor().getComposite();
            for(int i=0; i<cc.length; i++){
                sb.append(cc[i]);
                sb.append(SP);
            }
        }
        sb.append(fill);
        sb.append(SP);
        sb.append(rotate);
        sb.append(SP);
        sb.append(x);
        sb.append(SP);
        sb.append(y);
        sb.append(SP);
        sb.append(width);
        sb.append(SP);
        sb.append(height);
        sb.append(SP);
        sb.append(ELLIPSE);
        sb.append(RET);
        
        page.getPageStringBuffer().append(sb);
    }
    
    public void drawImage(Page page, float x, float y, Image img, float width, float height) throws RiepsException {
        StringBuffer sb = new StringBuffer();
        
        sb.append(this.GSAVE);
        sb.append(this.SP);
        
        sb.append(x);
        sb.append(this.SP);
        sb.append(y);
        sb.append(this.SP);
        sb.append(this.TRANSLATE);
        sb.append(this.SP);
        
        sb.append(width);
        sb.append(this.SP);
        sb.append(height);
        sb.append(this.SP);
        sb.append(this.SCALE);
        sb.append(this.RET);
        
        sb.append(img.getPSImage());
        sb.append(this.RET);
        
        sb.append(this.GRESTORE);
        sb.append(this.RET);
        
        page.getPageStringBuffer().append(sb);
    }
    
    public void drawFrame(Page page, Frame frm) {
        if(frm.isFill()){
            Color c = page.getCurrentFillColor();
            page.setColor(frm.getFillColor());
            this.drawRect(page, frm.getLeft(), frm.getTop(), frm.getWidth(), frm.getHeight(), frm.getRotate(), 0, false, true);
            page.setColor(c);
        }
        
        page.getPageStringBuffer().append(this.GSAVE);
        page.getPageStringBuffer().append(this.SP);
        page.getPageStringBuffer().append(frm.getLeft());
        page.getPageStringBuffer().append(this.SP);
        page.getPageStringBuffer().append(frm.getTop());
        page.getPageStringBuffer().append(this.SP);
        page.getPageStringBuffer().append(TRANSLATE);
        page.getPageStringBuffer().append(this.SP);
        page.getPageStringBuffer().append(frm.getRotate());
        page.getPageStringBuffer().append(this.SP);
        page.getPageStringBuffer().append(ROTATE);
        page.getPageStringBuffer().append(this.RET);
        
        LineFormat clf = page.getCurrentLineFormat();
        
        if(frm.getLfTop() != null){
            LineFormat lf = frm.getLfTop();
            page.setLineFormat(lf);
            this.drawLine(page, 0, 0, frm.getWidth(), 0);
        }
        if(frm.getLfRight() != null){
            LineFormat lf = frm.getLfRight();
            page.setLineFormat(lf);
            this.drawLine(page, frm.getWidth(), 0, 0, frm.getHeight() * -1);
        }
        if(frm.getLfBottom() != null){
            LineFormat lf = frm.getLfBottom();
            page.setLineFormat(lf);
            this.drawLine(page, 0, frm.getHeight() * -1, frm.getWidth(), 0);
        }
        if(frm.getLfLeft() != null){
            LineFormat lf = frm.getLfLeft();
            page.setLineFormat(lf);
            this.drawLine(page, 0, 0, 0, frm.getHeight() * -1);
        }
        
        page.setLineFormat(clf);
        page.getPageStringBuffer().append(this.GRESTORE);
        page.getPageStringBuffer().append(this.SP);
        
        float xt = frm.getLeft() + frm.getLeftPadding();
        float yt = frm.getTop() + frm.getTopPadding();
        float wt = frm.getWidth() - frm.getLeftPadding() - frm.getRightPadding();
        float ht = frm.getHeight() - frm.getTopPadding() - frm.getBottomPadding();
        
        this.showText(page, frm.getLeft(), frm.getTop(), frm.getWidth(), frm.getHeight(), frm.getText(), frm.getTextParameter());
        
    }
    
    private StringBuffer escapeText(String s){
        StringBuffer tmp = new StringBuffer(s);
        
        int ind = -2;
        while((ind = tmp.indexOf(ANTI_SLASH, ind+2)) >= 0){
            tmp = tmp.replace(ind, ind+1, PROTECTED_ANTI_SLASH);
        }
        
        ind = -2;
        while((ind = tmp.indexOf(LEFT_BRACKET, ind+2)) >= 0){
            tmp = tmp.replace(ind, ind+1, PROTECTED_LEFT_BRACKET);
        }
        
        ind = -2;
        while((ind = tmp.indexOf(RIGHT_BRACKET, ind+2)) >= 0){
            tmp = tmp.replace(ind, ind+1, PROTECTED_RIGHT_BRACKET);
        }
        
        if(tmp.length() >= 255){
            int n = tmp.length() / 255;
            for(int i=1; i<=n; i++){
                tmp.insert(i*255 + (i-1), RET);
            }
        }
        
        return tmp;
    }
    private StringBuffer makeFont(PSFont fnt, Color color){
        StringBuffer sb = new StringBuffer(160);
        if(fnt == null){
            fnt = PSFont.DEFAULT;
        }
        
        sb.append(this.makeColor(color));
        
        sb.append(this.SLASH);
        sb.append(fnt.getName());
        sb.append(this.SP);
        sb.append(this.FIND_FNT);
        sb.append(this.SP);
        
        sb.append(this.LEFT_HOOK);
        sb.append(fnt.getMatrix_a());
        sb.append(this.SP);
        sb.append(fnt.getMatrix_b());
        sb.append(this.SP);
        sb.append(fnt.getMatrix_c());
        sb.append(this.SP);
        sb.append(fnt.getMatrix_d());
        sb.append(this.SP);
        sb.append(fnt.getMatrix_e());
        sb.append(this.SP);
        sb.append(fnt.getMatrix_f());
        sb.append(this.RIGHT_HOOK);
        sb.append(this.SP);
        
        sb.append(this.SET_FNT);
        sb.append(this.SP);
        
        return sb;
    }
    
    private StringBuffer makeColor(Color color){
        StringBuffer sb = new StringBuffer(25);
        if(color == null){
            color = Gray.BLACK;
        }
        
        float[] compo = color.getComposite();
        for(int i=0; i<compo.length; i++){
            sb.append(compo[i]);
            sb.append(this.SP);
        }
        sb.append(color.getPSCommande());
        sb.append(this.SP);
        
        return sb;
    }
    
    private StringBuffer makeLineFormat(LineFormat lf){
        StringBuffer sb = new StringBuffer(10);
        
        float[] cc = lf.getColor().getComposite();
        for(int i=0; i<cc.length; i++){
            sb.append(cc[i]);
            sb.append(SP);
        }
        
        sb.append(LEFT_HOOK);
        int[] dp = lf.getDashPattern();
        for(int i=0; i<dp.length; i++){
            if(i>0)
                sb.append(SP);
            sb.append(dp[i]);
        }
        sb.append(RIGHT_HOOK);
        sb.append(SP);
        sb.append(lf.getDashOffset());
        sb.append(SP);
        sb.append(lf.getWidth());
        
        return sb;
    }
    
    private StringBuffer makePageDevice(Page page){
        StringBuffer sb = new StringBuffer();
        
        sb.append(pd_open);
        
        if(page.getDeviceParameters().containsKey(DeviceParameter.PAGE_SIZE)){
            try{
                float[] pz = (float[]) page.getDeviceParameters().get(DeviceParameter.PAGE_SIZE);
                sb.append(pd_ps);
                sb.append(pz[0]);
                sb.append(SP);
                sb.append(pz[1]);
                sb.append(SP);
                sb.append(RIGHT_HOOK);
            } catch(Exception e){
                System.err.print("PAGE_SIZE value is not float[2] : "+e);
            }
        }
        
        sb.append(pd_close);
        
        return sb;
    }
    
}