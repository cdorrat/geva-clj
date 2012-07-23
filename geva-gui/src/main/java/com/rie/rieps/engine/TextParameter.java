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
 * TextParameter.java
 *
 * Created on 27 fevrier 2007, 15:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine;

import com.rie.rieps.engine.color.Color;
import com.rie.rieps.engine.color.Gray;

/**
 *
 * @author yves
 */
public class TextParameter {
    public final static int HZ_LEFT = 0;
    public final static int HZ_CENTER = 1;
    public final static int HZ_RIGHT = 2;
    public final static int HZ_JUSTIFY = 3;
    
    public final static int VERT_TOP = 0;
    public final static int VERT_MIDDLE = 1;
    public final static int VERT_BOTTOM = 2;
    
    private int horizontal;
    private int vertical;
    private Color color = Gray.BLACK;
    private float rotate = 0;
    private boolean clip = false;
    
    public TextParameter(int hz, int v, Color col, float rotate, boolean verticalclip){
        this.horizontal = hz;
        this.vertical = v;
        this.color = col;
        this.rotate = rotate;
        this.clip = verticalclip;
    }
    
    public TextParameter setHorizontal(int hz){
       return new TextParameter(hz, this.getVertical(), this.getColor(), this.getRotate(), this.isClip());
    }
    
    public TextParameter setVertical(int v){
        return new TextParameter(this.getHorizontal(), v, this.getColor(), this.getRotate(), this.isClip());
    }

    public int getHorizontal() {
        return horizontal;
    }

    public int getVertical() {
        return vertical;
    }

    public Color getColor() {
        return color;
    }

    public TextParameter setColor(Color color) {
        return new TextParameter(this.getHorizontal(), this.getVertical(), color, this.getRotate(), this.isClip());
    }

    public float getRotate() {
        return rotate;
    }

    public TextParameter setRotate(float rotate) {
        return new TextParameter(this.getHorizontal(), this.getVertical(), this.getColor(), rotate, this.isClip());
    }

    public boolean isClip() {
        return clip;
    }

    public TextParameter setClip(boolean clip) {
        return new TextParameter(this.getHorizontal(), this.getVertical(), this.getColor(), this.getRotate(), clip);
    }
    
}
