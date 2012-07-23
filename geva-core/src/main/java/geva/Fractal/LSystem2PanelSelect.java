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

package geva.Fractal;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @author eliott bartley
 */
public class LSystem2PanelSelect extends LSystem2Panel
{

    private double fitness;
    
    public LSystem2PanelSelect
    (   String axiom,
        String grammar,
        int depth,
        float angle,
        double fitness
    ){  super(axiom, grammar, depth, angle);
        super.setScale(0.9f);
        this.fitness = fitness;
    }

    public LSystem2PanelSelect
    (   String grammar,
        int depth,
        float angle,
        double fitness
    ){  super(grammar, depth, angle);
        super.setScale(0.9f);
        this.fitness = fitness;
    }

    @Override
    public void paint(Graphics g)
    {   super.paint(g);

        /*// Render fitness as a red bar
        g.setColor(Color.red);
        g.fillRect
        (   2, this.getHeight() - (int)(this.getHeight() * fitness) + 2,
            5, (int)(this.getHeight() * fitness) - 4
        );
        */

        g.setColor(Color.gray);
        g.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 5, 5);
        
    }

}
