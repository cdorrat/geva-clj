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

package geva.gui.Fractal;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * View LSystems in a JPanel
 * @author eliott bartley
 */
public class LSystem2Panel extends JPanel
{

    private LSystem2Renderer lSystem;
    private String           axiom;
    private String           grammar;
    private int              depth;
    private float            angle;
    private int              centerX = 0;
    private int              centerY = 0;
    private float            scale = 1;

    public LSystem2Panel(String grammar, int depth, float angle)
    {   this(null, grammar, depth, angle);
    }

    public LSystem2Panel(String axiom, String grammar, int depth, float angle)
    {   this.axiom = axiom;
        this.grammar = grammar;
        this.depth = depth;
        this.angle = angle;
        createLSystem();
    }

    public void setAxiom(String axiom)
    {   this.axiom = axiom;
        createLSystem();
        super.repaint();
    }

    public void setGrammar(String grammar)
    {   this.grammar = grammar;
        createLSystem();
        super.repaint();
    }

    public void setDepth(int depth)
    {   this.depth = depth;
        createLSystem();
        super.repaint();
    }

    public void setAngle(float angle)
    {   this.angle = angle;
        createLSystem();
        super.repaint();
    }

    protected void setScale(float scale)
    {   this.scale = scale;
        super.repaint();
    }

    public String getDerivedGrammar()
    {   return lSystem.getDerivedGrammar();
    }

    @Override
    public void paint(Graphics graphics)
    {   super.paint(graphics);
        int size = Math.min(this.getWidth(), this.getHeight());
        size *= scale;
        centerX = (this.getWidth() - size) / 2;
        centerY = (this.getHeight() - size) / 2;
        lSystem.setDimension(size - 1, size - 1);
        lSystem.render(graphics);
    }

    private void createLSystem()
    {   lSystem = new LSystem2Renderer
        (   axiom,
            grammar,
            depth,
            angle
        ){  protected void drawLine(Object user, int x, int y, int u, int v)
            {   assert user instanceof Graphics : user.getClass().getName();
                Graphics graphics = (Graphics)user;
                graphics.setColor(Color.green);
                graphics.drawLine
                (   x + centerX,
                    y + centerY,
                    u + centerX,
                    v + centerY
                );
            }
        };

    }

}
