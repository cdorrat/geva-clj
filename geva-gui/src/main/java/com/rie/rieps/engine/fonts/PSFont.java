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
 * PSFont.java
 *
 * Created on 26 fevrier 2007, 16:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine.fonts;

import com.rie.rieps.exception.RiepsException;

/**
 *
 * @author Yves Piel
 */
public class PSFont {
    
    public final static PSFont DEFAULT = new PSFont();
    
    private final static String SP = " ";
    private final static String SIN = "sin";
    private final static String COS = "cos";
    private final static String MUL = "mul";
    private final static String NEG = "neg";
    private final static String ZERO = "0";
    
    private String name = "Times-Roman";
    private String matrix_a = "10 0 cos mul";
    private String matrix_b = "10 0 sin mul";
    private String matrix_c = "10 0 sin neg mul";
    private String matrix_d = "10 0 cos mul";
    private String matrix_e = "0";
    private String matrix_f = "0";
    
    private float size = 10;
    private float rotate = 0;
    
    /** Creates a new instance of PSFont  */
    public PSFont() {
    }
    
    public PSFont(String name, float size){
        this.name = name;
        this.size = size;
        this.genMatrix();
    }
    
    public PSFont(String s, float size, float degree){
        this.name = s;
        this.size = size;
        this.rotate = degree;
        this.genMatrix();
    }
    
    public PSFont(String name, String a, String b, String c, String d, String e, String f){
        this.name = name;
        this.matrixA(a);
        this.matrixB(b);
        this.matrixC(c);
        this.matrixD(d);
        this.matrixE(e);
        this.matrixF(f);
    }
    
    public PSFont(PSFont psf){
        this.name = psf.getName();
        this.size = psf.getSize();
        this.rotate = psf.getRotate();
        this.matrixA(psf.getMatrix_a());
        this.matrixB(psf.getMatrix_b());
        this.matrixC(psf.getMatrix_c());
        this.matrixD(psf.getMatrix_d());
        this.matrixE(psf.getMatrix_e());
        this.matrixF(psf.getMatrix_f());
    }
    
    public PSFont(String desc) throws RiepsException{
        String[] attr = desc.split(",");
        if(attr.length != 9){
            throw new RiepsException("The PSFont description is illegal. There is not 9 elements : "+attr.length);
        }
        float size = 0;
        float rot = 0;
        try{
            size = Float.parseFloat(attr[1]);
            rot = Float.parseFloat(attr[2]);
        }
        catch(Exception e){
            throw new RiepsException("The PSFont description is illegal. The size or the rotation can't be converted to float : "+attr[1]+", "+attr[2]);
        }
        
        this.name = attr[0];
        this.size = size;
        this.rotate = rot;
        this.matrixA(attr[3]);
        this.matrixB(attr[4]);
        this.matrixC(attr[5]);
        this.matrixD(attr[6]);
        this.matrixE(attr[7]);
        this.matrixF(attr[8]);
    }
    
    public PSFont setSize(float size){
        PSFont f = new PSFont(this);
        f.resize(size);
        return f;
    }
    
    private void resize(float size){
        this.size = size;
        this.genMatrix();
    }
    
    public PSFont setRotate(float degree){
        PSFont f = new PSFont(this);
        f.rotate(degree);
        return f;
    }

    private void rotate(float degree){
        this.rotate = degree;
        this.genMatrix();
    }
    
    private void genMatrix(){
        StringBuffer sba = new StringBuffer();
        sba.append(this.getSize());
        sba.append(SP);
        sba.append(this.getRotate());
        sba.append(SP);
        sba.append(COS);
        sba.append(SP);
        sba.append(MUL);
        this.matrixA(sba.toString());
        this.matrixD(sba.toString());
        
        
        StringBuffer sbb = new StringBuffer();
        sbb.append(this.getSize());
        sbb.append(SP);
        sbb.append(this.getRotate());
        sbb.append(SP);
        sbb.append(SIN);
        sbb.append(SP);
        sbb.append(MUL);
        this.matrixB(sbb.toString());
        
        StringBuffer sbc = new StringBuffer();
        sbc.append(this.getSize());
        sbc.append(SP);
        sbc.append(this.getRotate());
        sbc.append(SP);
        sbc.append(SIN);
        sbc.append(SP);
        sbc.append(NEG);
        sbc.append(SP);
        sbc.append(MUL);
        this.matrixC(sbc.toString());
        
        this.matrixE(ZERO);
        this.matrixF(ZERO);
    }
    
    public String getName() {
        return name;
    }
    
    public PSFont setName(String name) {
        PSFont f = new PSFont(this);
        f.rename(name);
        return f;
    }
    
    private void rename(String name){
        this.name = name;
    }
    
    public String getMatrix_a() {
        return matrix_a;
    }
    
    public PSFont setMatrix_a(String matrix_a) {
        PSFont f = new PSFont(this);
        f.matrixA(matrix_a);
        return f;
    }
    private void matrixA(String m){
        this.matrix_a = m;
    }
    
    public String getMatrix_b() {
        return matrix_b;
    }
    
    public PSFont setMatrix_b(String matrix_b) {
        PSFont f = new PSFont(this);
        f.matrixB(matrix_b);
        return f;
    }
    private void matrixB(String m){
        this.matrix_b = m;
    }
    
    public String getMatrix_c() {
        return matrix_c;
    }
    
    public PSFont setMatrix_c(String matrix_c) {
        PSFont f = new PSFont(this);
        f.matrixC(matrix_c);
        return f;
    }
    private void matrixC(String m){
        this.matrix_c = m;
    }
    
    public String getMatrix_d() {
        return matrix_d;
    }
    
    public PSFont setMatrix_d(String matrix_d) {
        PSFont f = new PSFont(this);
        f.matrixD(matrix_d);
        return f;
    }
    private void matrixD(String m){
        this.matrix_d = m;
    }
    
    public String getMatrix_e() {
        return matrix_e;
    }
    
    public PSFont setMatrix_e(String matrix_e) {
        PSFont f = new PSFont(this);
        f.matrixE(matrix_e);
        return f;
    }
    private void matrixE(String m){
        this.matrix_e = m;
    }
    
    public String getMatrix_f() {
        return matrix_f;
    }
    
    public PSFont setMatrix_f(String matrix_f) {
        PSFont f = new PSFont(this);
        f.matrixF(matrix_f);
        return f;
    }
    private void matrixF(String m){
        this.matrix_f = m;
    }
    
    public PSFont setMatrix(String a, String b, String c, String d, String e, String f){
        PSFont fnt = new PSFont(this);
        fnt.matrixA(a);
        fnt.matrixB(b);
        fnt.matrixC(c);
        fnt.matrixD(d);
        fnt.matrixE(e);
        fnt.matrixF(f);
        return fnt;
    }
    
    public float getSize() {
        return size;
    }
    
    public float getRotate() {
        return rotate;
    }
    
    
    /** Return the postscript font description linked to the awt one.
     * It's like : FontFamily,size,rotate,matrixa,matrixb,matrixc,matrixd,matrixe,matrixf
     * Some fields can be empty except the family name.
     */
    public String getPSDesc(){
        StringBuffer sb = new StringBuffer();
        
        String coma = ",";
        sb.append(this.getName());
        sb.append(coma);
        sb.append(this.getSize());
        sb.append(coma);
        sb.append(this.getRotate());
        sb.append(coma);
        sb.append(this.getMatrix_a());
        sb.append(coma);
        sb.append(this.getMatrix_b());
        sb.append(coma);
        sb.append(this.getMatrix_c());
        sb.append(coma);
        sb.append(this.getMatrix_d());
        sb.append(coma);
        sb.append(this.getMatrix_e());
        sb.append(coma);
        sb.append(this.getMatrix_f());
        
        return sb.toString();
    }

}