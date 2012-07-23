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
 * LineFormat.java
 *
 * Created on 26 fevrier 2007, 12:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine;

import com.rie.rieps.engine.color.Color;
import com.rie.rieps.engine.color.Gray;

/**
 *
 * @author Yves Piel
 */
public class LineFormat {
    public final static LineFormat DEFAULT = new LineFormat();
    
    private float width = 1;
    private int[] dash = new int[]{};
    private int dashOffset = 0;
    private Color color = Gray.BLACK;
    
    
    public LineFormat(){
    }
    
    public LineFormat(LineFormat lf){
        this.width = lf.getWidth();
        this.dash = lf.getDashPattern();
        this.dashOffset = lf.getDashOffset();
        this.color = lf.getColor();
    }
    
    public LineFormat(float width, int dashLength, Color c){
        this(width, dashLength==0 ? new int[0] : new int[]{dashLength}, 0, c);
    }
    
    public LineFormat(float width, int[] pattern, int offset, Color c){
        this.rewidth(width);
        this.redash(pattern, offset);
        this.recolor(c);
    }
    
    public LineFormat(float width, Color c){
        this(width, new int[0], 0, c);
    }
    
    public LineFormat setWidth(float width){
        LineFormat lf = new LineFormat(this);
        lf.rewidth(width);
        return lf;
    }
    
    private void rewidth( float width ){
        this.width = width;
    }
    
    public float getWidth(){
        return this.width;
    }
    
    public LineFormat setDash(int length){
        LineFormat lf = new LineFormat(this);
        int[] d = new int[1];
        d[0] = length;
        lf.redash(d, 0);
        return lf;
    }
    
    public LineFormat setDash(int[] pattern, int offset){
        LineFormat lf = new LineFormat(this);
        lf.redash(pattern, offset);
        return lf;
    }
    
    private void redash( int[] pattern, int offset ){
        this.dash = new int[pattern.length];
        System.arraycopy(pattern, 0, this.dash, 0, pattern.length);
        this.dashOffset = offset;
    }
    
    public int[] getDashPattern(){
        int[] tmp = new int[this.dash.length];
        System.arraycopy(this.dash, 0, tmp, 0, tmp.length);
        return tmp;
    }
    
    public int getDashOffset(){
        return this.dashOffset;
    }
    
    public Color getColor(){
        return this.color;
    }
    
    public LineFormat setColor(Color c){
        LineFormat lf = new LineFormat(this);
        lf.recolor(c);
        return lf;
    }
    
    private void recolor(Color c){
        this.color = c;
    }
}
