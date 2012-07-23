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

/**
 * Base class for rendering an LSystem. Extend and override drawLine to render
 *  to a particular context, calling setDimension and render beforehand
 * @author eliott bartley
 */
public abstract class LSystem2Renderer
{

    /**
     * LSystem algorithm runner
     */
    private LSystem2 lSystem;
    private float width;
    private float height;

    /**
     * Create a new renderable LSystem
     * @param grammar The grammar of the LSystem
     * @param depth The recursive depth of the grammar
     * @param angle The angle distance each turn should make, in degrees
     */
    public LSystem2Renderer(String axiom, String grammar, int depth, float angle)
    {   lSystem = new LSystem2(axiom, grammar, depth, angle);
    }

    /**
     * Execute the LSystem while tracking the area it covers and return, in
     *  scale, the minimum bounding square that can contain the result
     * @param scale Store bounded box area LSystem passes through
     */
    private void scale(LSystemScale scale)
    {   // Get the LSystem to execute with a listener set up to determine the
        //  minimum bounding box that will fit all the movements
        lSystem.execute(scale, new LSystem2StateTrace()
        {   public void Update(LSystem2StateTraceEvent event)
            {   LSystem2State state = event.state;
                LSystemScale scale = (LSystemScale)event.user;
                if(state.getX() < scale.minX) scale.minX = state.getX();
                if(state.getY() < scale.minY) scale.minY = state.getY();
                if(state.getX() > scale.maxX) scale.maxX = state.getX();
                if(state.getY() > scale.maxY) scale.maxY = state.getY();
            }
        });
        // Update the bounding box so that it is square. When scaled to fit the
        //  page, having it square will prevent one axis becoming scaled more
        //  than the other
        scale.fix();
    }

    /**
     * Execute the LSystem and calls drawLine for each drawing the LSystem will
     *  make
     * @param render Details of render
     */
    private void render(LSystemRender render)
    {   // Get the LSystem to execute with a listener set up to render the
        //  movements. Movements are not rendered if no previous position is
        //  returned, as this means the move was due to an (f) command and not
        //  an (F)
        lSystem.execute(render, new LSystem2StateTrace()
        {   public void Update(LSystem2StateTraceEvent event)
            {   LSystem2State state = event.state;
                LSystem2State trace = event.trace;
                LSystemRender render = (LSystemRender)event.user;
                int x, y, u, v;
                if(trace != null)
                {   x = render.getX(trace.getX());
                    y = render.getY(trace.getY());
                    u = render.getX(state.getX());
                    v = render.getY(state.getY());
                    drawLine(render.user, x, y, u, v);
                }
            }
        });
    }

    /**
     * Set the width and hight to scale the render to. All calls to drawLine
     *  will have (x u) and (y v)  inside the range [0..width) and [0..height)
     * @param width
     * @param height
     */
    public void setDimension(float width, float height)
    {   this.width = width;
        this.height = height;
    }

    /**
     * Render the LSystem, calls on drawLine to do actual drawing
     * @param user User object that is passed to drawLine
     */
    public void render(Object user)
    {   LSystemScale scale;
        LSystemRender render;
        scale = new LSystemScale();
        render = new LSystemRender();
        render.user = user;
        render.scale = scale;
        render.width = width;
        render.height = height;
        scale(scale);
        render(render);
    }

    protected abstract void drawLine(Object user, int x, int y, int u, int v);

    /**
     * Get the grammar expanded to the specified depth
     * @return the grammar expanded to the specified depth
     */
    public String getDerivedGrammar()
    {   return lSystem.getDerivedGrammar();
    }

    /**
     * Keep details of the bounded box which the LSystem uses. These details are
     *  used to scale to the bounding box of the page during render
     */
    class LSystemScale
    {

        public float minX = Float.MAX_VALUE;
        public float minY = Float.MAX_VALUE;
        public float maxX = Float.MIN_VALUE;
        public float maxY = Float.MIN_VALUE;

        /**
         * Square the bounded box so that one axis is not scaled more than the
         *  other
         */
        public void fix()
        {   float size;
            if(maxX - minX > maxY - minY)
            {   size = maxX - minX;
                size = (size - (maxY - minY)) / 2;
                maxY += size;
                minY -= size;
            }
            else
            {   size = maxY - minY;
                size = (size - (maxX - minX)) / 2;
                maxX += size;
                minX -= size;
            }
        }

    }

    /**
     * Keep details about rendering
     */
    class LSystemRender
    {

        /**
         * User data that is passed back to the user
         */
        public Object user;
        /**
         * The bounded square to which the LSystem's drawings will fit
         */
        public LSystemScale scale;
        /**
         * The width of the render area, scale's bounded box is scaled to fit
         */
        public float width;
        /**
         * The height of the render area, scale's bounded box is scaled to fit
         */
        public float height;

        /**
         * Given an X co-ordinate from the LSystem, return the related
         *  co-ordinate based on the render width
         * @param x X co-ordinate from LSystem
         * @return X co-ordinate to render context
         */
        public int getX(float x)
        {   x = (x - scale.minX) * width
              / (scale.maxX - scale.minX);
            return (int)x;
        }

        /**
         * Given an Y co-ordinate from the LSystem, return the related
         *  co-ordinate based on the render height
         * @param y Y co-ordinate from LSystem
         * @return Y co-ordinate to render context
         */
        public int getY(float y)
        {   y = (y - scale.minY) * height
              / (scale.maxY - scale.minY);
            return (int)y;
        }

    }

}
