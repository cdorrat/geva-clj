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
 * Factory.java
 *
 * Created on 26 fevrier 2007, 10:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine.factories;

import com.rie.rieps.engine.Frame;
import com.rie.rieps.engine.LineFormat;
import com.rie.rieps.engine.fonts.PSFont;
import com.rie.rieps.engine.Page;
import com.rie.rieps.engine.TextParameter;
import com.rie.rieps.engine.color.Color;
import com.rie.rieps.engine.image.Image;
import com.rie.rieps.exception.RiepsException;
import java.awt.image.BufferedImage;

/**
 * Little interface to add someother output than postscript.
 * @author Yves Piel
 */
public interface Factory {
    
    //public void setTopLeftOrigin(float pageHeight);
    
    public StringBuffer getBeginJob(int totalPageNumber, float pageWidth, float pageHeight);
    public StringBuffer getEndJob();
    
    public StringBuffer showPage(Page page, int pageNumber, int totalPageNumber);
    
    public void showText(Page page, float x, float y, float weight, float height, String txt, TextParameter tp);

    public void drawLine(Page page, float x1, float y1, float weight, float height);

    public void drawRect(Page page, float x, float y, float width, float height, float rotate, float radius, boolean border, boolean fill);

    public void drawEllipse(Page page, float x, float y, float width, float height, float rotate, boolean border, boolean fill);

    public void drawImage(Page page, float x, float y, Image img, float width, float height) throws RiepsException;
    
    public void drawFrame(Page page, Frame frm);
}
