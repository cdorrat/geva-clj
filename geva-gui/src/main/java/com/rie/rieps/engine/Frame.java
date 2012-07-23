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
 * Frame.java
 *
 * Created on 6 mars 2007, 12:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine;

import com.rie.rieps.engine.color.Color;
import com.rie.rieps.engine.fonts.PSFont;

/**
 *
 * @author Yves Piel
 */
public class Frame {
    
    private float top;
    private float left;
    private float width;
    private float height;
    
    private float leftPadding;
    private float topPadding;
    private float rightPadding;
    private float bottomPadding;
    
    private LineFormat lfLeft;
    private LineFormat lfTop;
    private LineFormat lfRight;
    private LineFormat lfBottom;
    
    private float rotate = 0;
    private String text = null;
    private PSFont psfnt = null;
    private TextParameter tp = null;
    
    private boolean fill = false;
    private Color fillColor;
    
    /** Creates a new instance of Frame */
    public Frame(float left, float top, float width, float height, float leftPadding, float topPadding, float rightPadding, float bottomPadding, LineFormat lfLeft, LineFormat lfTop, LineFormat lfRight, LineFormat lfBottom, boolean fill, Color fillColor, float rotate, String text, PSFont psfnt, TextParameter tp) {
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;
        
        this.leftPadding = leftPadding;
        this.topPadding = topPadding;
        this.rightPadding = rightPadding;
        this.bottomPadding = bottomPadding;
        
        this.lfLeft = lfLeft;
        this.lfTop = lfTop;
        this.lfRight = lfRight;
        this.lfBottom = lfBottom;
        
        this.rotate = rotate ;
        this.text = text ;
        this.psfnt = psfnt ;
        this.tp = tp ;
        
        this.fill = fill ;
        this.fillColor = fillColor;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getLeftPadding() {
        return leftPadding;
    }

    public void setLeftPadding(float leftPadding) {
        this.leftPadding = leftPadding;
    }

    public float getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(float topPadding) {
        this.topPadding = topPadding;
    }

    public float getRightPadding() {
        return rightPadding;
    }

    public void setRightPadding(float rightPadding) {
        this.rightPadding = rightPadding;
    }

    public float getBottomPadding() {
        return bottomPadding;
    }

    public void setBottomPadding(float bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public LineFormat getLfLeft() {
        return lfLeft;
    }

    public void setLfLeft(LineFormat lfLeft) {
        this.lfLeft = lfLeft;
    }

    public LineFormat getLfTop() {
        return lfTop;
    }

    public void setLfTop(LineFormat lfTop) {
        this.lfTop = lfTop;
    }

    public LineFormat getLfRight() {
        return lfRight;
    }

    public void setLfRight(LineFormat lfRight) {
        this.lfRight = lfRight;
    }

    public LineFormat getLfBottom() {
        return lfBottom;
    }

    public void setLfBottom(LineFormat lfBottom) {
        this.lfBottom = lfBottom;
    }

    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PSFont getPsfnt() {
        return psfnt;
    }

    public void setPsfnt(PSFont psfnt) {
        this.psfnt = psfnt;
    }

    public TextParameter getTextParameter() {
        return tp;
    }

    public void setTextParameter(TextParameter tp) {
        this.tp = tp;
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
}