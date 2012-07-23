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
 * Image.java
 *
 * Created on 1 mars 2007, 10:03
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
public interface Image {
    
    public void load(BufferedImage img) throws RiepsException;
    public StringBuffer getPSImage() throws RiepsException;
    
}
