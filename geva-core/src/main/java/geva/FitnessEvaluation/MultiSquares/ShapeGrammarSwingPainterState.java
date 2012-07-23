/*
Grammatical Evolution in Java
Release: GEVA-v1.2.zip
Copyright (C) 2008 Michael O'Neill, Erik Hemberg, Anthony Brabazon, Conor Gilligan 
Contributors Patrick Middleburgh, Eliott Bartley, Jonathan Hugosson, Jeff Wrigh

Separate licences for asm, bsf, antlr, groovy, jscheme, commons-logging, jsci is included in the lib folder. 
Separate licence for rieps is included in src/com folder.

This licence refers to GEVA-v1.2.

This software is distributed under the terms of the GNU General Public License.


This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
/>.
*/

package geva.FitnessEvaluation.MultiSquares;

import java.awt.Color;

public class ShapeGrammarSwingPainterState{

    public double rot;        // angle of rotation
    public int x, y;          // shape x and y coordinates
    public int startX, startY;// shape initial x and y coordinates
    public int w, h;          // shape width and height
    public double sclX, sclY; // shape scaling factors
    public double shrX, shrY; // shape shearing factors
    public int trltX, trltY;  // shape translating factors
    public Color color;       // color of the drawings

    public ShapeGrammarSwingPainterState(double initRot,
					 int initX, int initY,
					 int initStartX, int initStartY,
					 int initW, int initH,
					 double initSclX, double initSclY,
					 double initShrX, double initShrY,
					 int initTrltX, int initTrltY,
					 Color clr){
	
	x = initX;            // x coordinate
	y = initY;            // y coordinate
	rot = initRot;        // angle of rotation
	startX = initStartX;  // initial shape x coordinate
	startY = initStartY;  // initial shape y coordinate
	w = initW;            // shape width
	h = initH;            // shape height
	sclX = initSclX;      // scaling x factor
	sclY = initSclY;      // scaling y factor
	shrX = initShrX;      // shearing x factor
	shrY = initShrY;      // shearing y factor
	trltX = initTrltX;    // translating x factor
	trltY = initTrltY;    // translating y factor
	color = clr;
    }

    /**
     * Create a new state cloned from another
     * @param state The state to clone
     */
    public ShapeGrammarSwingPainterState(ShapeGrammarSwingPainterState state){   
	x = state.x;
	y = state.y;
	rot = state.rot;
	startX = state.startX;
	startY = state.startY;
	w = state.w;
	h = state.h;
	sclX = state.sclX;
	sclY = state.sclY;
	shrX = state.shrX;
	shrY = state.shrY;
	trltX = state.trltX;
	trltY = state.trltY;
	color = state.color;
    }
}
