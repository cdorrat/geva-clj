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
 * SmallGrayImage.java
 *
 * Created on 1 mars 2007, 10:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine.image;

import com.rie.rieps.exception.RiepsException;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/** This class generate Black and White image from a file.
 *
 * @author Yves Piel
 */
public class SmallGrayImage implements Image{
    private final static int maxvalue = 100; // max hex values per line
    
    /** to convert color image int o gray, this method is the most simplest.
     * it does : (R+G+B)/3 for each pixel.
     */
    public final static int CONVERT_MODE_AVERAGE = 0;
    
    /** The 709 CIE (International Commission on Illumination), true colors.
     * it does : 0.2125*R + 0.7154*G + 0.0721*B
     */
    public final static int CONVERT_MODE_CIE_709 = 1;
    
    /** the 601 CIE (International Commission on Illumination), non-linear with gamma correction.
     * It does : 0.299*R + 0.587*G + 0.114*B
     */
    public final static int CONVERT_MODE_CIE_601 = 2;
    
    private StringBuffer psimg = null;
    
    private float red_coef = 1;
    private float green_coef = 1;
    private float blue_coef = 1;
    private float divide = 3;
    
    /**
     * If convert_mode is unknown value, it uses @see{CONVERT_MODE_AVERAGE}.
     * @param convert_mode the convert color to gray mode.
     */
    public SmallGrayImage(int convert_mode){
        if(convert_mode < 0 && convert_mode > 2){
            convert_mode = 0;
        }
        
        this.setMode(convert_mode);
    }
    
    private void setMode(int mode){
        switch(mode){
            case CONVERT_MODE_AVERAGE:
                this.red_coef = 1;
                this.green_coef = 1;
                this.blue_coef = 1;
                this.divide = 3;
                break;
            case CONVERT_MODE_CIE_709:
                this.red_coef = 0.2125f;
                this.green_coef = 0.7154f;
                this.blue_coef = 0.0721f;
                this.divide = 1;
                break;
            case CONVERT_MODE_CIE_601:
                this.red_coef = 0.299f;
                this.green_coef = 0.587f;
                this.blue_coef = 0.114f;
                this.divide = 1;
                break;
        }
    }
    
    public void load(BufferedImage img) throws RiepsException{
        if(psimg != null){
            throw new RiepsException("The image has already been loaded.");
        }
        
        this.loadImg(img);
    }

    public StringBuffer getPSImage() throws RiepsException{
        if(psimg == null){
            throw new RiepsException("You have to load the image before getting the postscript.");
        }
        
        return this.psimg;
    }
    
    private void loadImg(BufferedImage img){
        StringBuffer sb = new StringBuffer(1500);
        
        int width = img.getWidth();
        int height = img.getHeight();
        
        /*sb.append("%!PS-Adobe-3.0 EPSF-3.0\n");
        sb.append("%%Creator: Created from bitmaplib by Paul Bourke\n");
        sb.append("%%BoundingBox: 0 0 ");
        sb.append(width);
        sb.append(" ");
        sb.append(height);
        sb.append("\n");
        sb.append("%%LanguageLevel: 2\n");
        sb.append("%%Pages: 1\n");
        sb.append("%%DocumentData: Clean7Bit\n");
        sb.append(width);
        sb.append(" ");
        sb.append(height);
        sb.append(" scale\n");
        
        
        sb.append(width);
        sb.append(" ");
        sb.append(height);
        sb.append(" 8 [");
        sb.append(width);
        sb.append(" 0 0 -");*/
        
        sb.append(width);
        sb.append(" ");
        sb.append(height);
        sb.append(" 8 [");
        sb.append(width);
        sb.append(" 0  0 ");
        sb.append(height*-1);
        sb.append(" 0 ");
        sb.append(height);
        sb.append("]\n{<\n");
        sb.append(this.convertImg(img, width, height));
        sb.append(">} image");
        
        this.psimg = sb;
    }
    
    private StringBuffer convertImg(BufferedImage img, int w, int h){
        StringBuffer sb = new StringBuffer();
        
        Raster raster = img.getData();
        int[] pix = new int[4];
        int n = 0;
        for(int y=0; y<h; y++){
            for(int x=0; x<w; x++){
                pix = raster.getPixel(x, y, pix);
                int av = (int)((this.red_coef*pix[0] + this.green_coef*pix[1] + this.blue_coef*pix[2]) / this.divide);
                if(av < 16){
                    sb.append(0);
                }
                
                sb.append(Integer.toHexString(av));
                n++;
                if(n >= maxvalue){
                    sb.append("\n");
                    n = 0;
                }
            }
        }
        
        return sb;
    }
}
