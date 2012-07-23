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

/**
 * Track and update the state of the LSystem. This is Cursor in the other
 *  four implemtations
 * @author eliott bartley
 */
public class LSystem2State
{

    private float x;
    private float y;
    /**
     * The angle that this state currently points, in radians
     */
    private float angle;

    /**
     * Create a new state at position (0, 0) and at angle 270 degrees, or
     *  pointing vertically up, in screen co-ordinates, i.e. calling move
     *  would move up the screen
     */
    public LSystem2State()
    {   x = 0;
        y = 0;
        angle = -(float)Math.PI / 2;
    }

    /**
     * Create a new state cloned from another
     * @param state The state to clone
     */
    public LSystem2State(LSystem2State state)
    {   x = state.getX();
        y = state.getY();
        angle = state.getAngle();
    }

    /**
     * Move the state's co-ordinates one step along the facing angle
     */
    void move()
    {   x += Math.cos(angle);
        y += Math.sin(angle);
    }

    /**
     * Turn the state's angle by the distance amount
     * @param distance The angle by which to turn the state, in radians
     */
    void turn(float distance)
    {   angle += distance;
        if(distance > 0)
            while(angle >= 2 * Math.PI)
                angle -= 2 * Math.PI;
        else
            while(angle < 0)
                angle += 2 * Math.PI;
    }

    /**
     * Get the x co-ordinate value of the state
     * @return the x co-ordinate value of the state
     */
    public float getX()
    {   return x;
    }

    /**
     * Get the y co-ordinate value of the state
     * @return the y co-ordinate value of the state
     */
    public float getY()
    {   return y;
    }

    /**
     * Get the angle of the state in radians. This value will always be in
     *  the range [0..2pi)
     * @return the angle of the state in radians
     */
    public float getAngle()
    {   return angle;
    }

}
