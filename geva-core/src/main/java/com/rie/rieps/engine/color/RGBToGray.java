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
 * RGBToGray.java
 *
 * Created on 5 mars 2007, 12:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine.color;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Yves Piel
 */
public class RGBToGray implements Color{
    
    private static Map colors = new HashMap();
    
    private Color gray;
    
    /** Creates a new instance of RGBToGray 
     * The value of (gray is red+blue+green)/3
     */
    private RGBToGray(int red, int green, int blue){
        //this.gray = new Gray(((red+blue+green)/3f)/255f);
        this.gray = new Gray((float)((red*0.2125f + green*0.7154f + blue*0.0721)/255f));
    }
    
    public float[] getComposite() {
        return gray.getComposite();
    }

    public String getPSCommande() {
        return gray.getPSCommande();
    }
    
    public static Color createGrayColor(int red, int green, int blue){
        Color c = (Color)colors.get(((""+red)+green)+blue);
        if(c == null){
            c = new RGBToGray(red, green, blue);
            colors.put(((""+red)+green)+blue, c);
        }
        return c;
    }
    
}
