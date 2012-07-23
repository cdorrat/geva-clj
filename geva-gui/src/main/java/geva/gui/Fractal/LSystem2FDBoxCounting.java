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
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Extended LSystem renderer with box-counting fractal dimension calculating
 * @author eliott bartley
 */
public class LSystem2FDBoxCounting
     extends LSystem2Renderer
  implements FractalDimension
{

    private BufferedImage image;

    public LSystem2FDBoxCounting
    (   String   axiom,
        String grammar,
        int      depth,
        float    angle,
        int       size
    ){  super(axiom, grammar, depth, angle);
        setBoxSize(size);
    }

    /**
     * Change the box-size. The area of the fractal will be divided evenly by
     *  the squared box-size value - i.e. 2 will divide the fractal into 4
     *  squares, 2 across, 2 down - 4 will divide into 16 squares, 4 across, 4
     *  down
     */
    public void setBoxSize(int size)
    {   if(size <= 1)
            throw new IllegalArgumentException(">1");
        image = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY);
    }

    /**
     * Approximate the fractal dimension using box-counting. This is done by
     *  rendering the fractal unto an image of dimensions specified by the box-
     *  size, and counting the pixels rendered
     */
    public double calcFractalDimension()
    {   assert image != null : "setBoxSize must have failed!";
        int size = image.getWidth();
        int count = 0;

        // Clear the image
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, size, size);

        // Render the fractal
        super.setDimension(size, size);
        super.render(null);

        // Count the pixels affected by the render
        byte[] pixels = ((DataBufferByte)image
            .getRaster()
            .getDataBuffer())
            .getData();
        for(byte pixel : pixels)
            if(pixel != 0)
                count++;

        // Return the approximate fractal dimension
        return Math.log(count) / Math.log(size);

    }

    @Override
    protected void drawLine(Object user, int x, int y, int u, int v)
    {   Graphics graphics = image.getGraphics();
        graphics.setColor(Color.white);
        graphics.drawLine(x, y, u, v);
    }

}
