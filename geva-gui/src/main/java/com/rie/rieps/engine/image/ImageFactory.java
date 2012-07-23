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
 * ImageFactory.java
 *
 * Created on 5 mars 2007, 10:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine.image;

import com.rie.rieps.exception.RiepsException;
import java.awt.image.BufferedImage;

/**
 *
 * @author Yves Piel
 */
public class ImageFactory {
    private final static int SMALL_GRAY_IMG = 0;
    
    private int type = 0;
    
    private int sg_convmode = -1;
    
    /** Creates a new instance of ImageFactory */
    private ImageFactory(int type) {
        this.type = type;
    }
    
    public Image create(BufferedImage bi) throws RiepsException{
        Image img = null;
        switch(type){
            case SMALL_GRAY_IMG:
                img = new SmallGrayImage(this.getSg_convmode());
                break;
        }
        img.load(bi);
        return img;
    }
    
    
    
    public static ImageFactory createSmallGrayImageFactory(int convert_mode){
        ImageFactory imgf = new ImageFactory(ImageFactory.SMALL_GRAY_IMG);
        imgf.setSg_convmode(convert_mode);
        return imgf;
    }

    private int getSg_convmode() {
        return sg_convmode;
    }

    private void setSg_convmode(int sg_convmode) {
        this.sg_convmode = sg_convmode;
    }
}
