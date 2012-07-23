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

package geva.gui.UI.Run.JSci;

import JSci.awt.Graph2DModel;
import JSci.awt.GraphDataEvent;
import java.awt.Graphics;
import java.awt.Point;
import JSci.swing.JGraph2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Point2D.Float;
import javax.swing.BorderFactory;

/**
 * Override the JSci JGraph2D to do some custom rendering
 * @author eliott bartley
 */
public class GEVAGraph extends JGraph2D
{

    private int interleave = 0;

    public GEVAGraph(Graph2DModel model)
    {   super(model);
        super.setGridLines(true);
        super.setBorder(BorderFactory.createEtchedBorder());
    }

    @Override
    public void dataChanged(GraphDataEvent event)
    {   // Does nothing.. or does it? Left to the super-class, dataChanged will
        //  cause the graph to be redrawn; but the graph is flooded with data --
        //  and redrawing every time incurs nifty delay times. Note: the redraw
        //  is instead done lazily on a separate thread in GEVAGraphPane
    }

    @Override
    protected void offscreenPaint(Graphics graphics)
    {   // Make the background white - why don't I just call
        //  super.setBackground(Color.white)? Because, for some reason, it
        //  doesn't fill out to the borders on all sides. It leaves a gray
        //  border top and left
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        super.offscreenPaint(graphics);
    }

    @Override
    @SuppressWarnings("hiding")
    // 'super.model' being hidden by local 'model' - suppressed as both refer to
    //   the same thing, but where super.model is a Graph2DModel (see JSci/
    //   swing/JGraph2D.java), local model is a GEVAGraphModel (which implements
    //   Graph2DModel) so it's not so much hiding it, as making it more visible
    protected void drawData(Graphics graphics)
    {   assert model instanceof GEVAGraphModel : model.getClass().getName();
        assert graphics instanceof Graphics2D : graphics.getClass().getName();
        GEVAGraphModel model = (GEVAGraphModel)super.model;
        Graphics2D graphicsEx = (Graphics2D)graphics;
        Stroke defaultPen = graphicsEx.getStroke();
        Stroke graphPen;
        AveragePoint p1 = null; // previous point
        AveragePoint p2 = null; // current point
        AveragePoint p3 = null; // previous error offset
        AveragePoint p4 = null; // current error offset
        int x;
        int lastX = 0;
        boolean hasError = false;

        // Reset label interleave every time it renders, as it also means the
        //  labels are going to be redrawn too. @see drawXLabel
        interleave = 0;

        // Go through each series and render them
        for(model.firstSeries(); model.lastSeries() == false; model.nextSeries())
            // Don't render the series if it's hidden or is an error series.
            //  Error series are rendered as part of the series they are
            //  attached to, not as their own series
            if(model.isVisible() == true && model.isErrorSeries() == false)
            {

                // Does this series have an error series (and is that series
                //  visible)
                hasError = model.isErrorVisible();

                // If there is no error series, set the colour once. If there
                //  is an error series, the colour will switch between error
                //  and actual colours in the render loop
                if(hasError == false)
                    graphics.setColor(model.getColour());

                // Set the pen based on whether this series is highlighed
                if(model.isSelected() == true)
                    graphicsEx.setStroke(graphPen = new BasicStroke(4));
                else
                    graphicsEx.setStroke(graphPen = defaultPen);

                p1 = p2 = p3 = p4 = null;

                // Get the visible minimum and maximum range. Don't bother doing
                //  anything outside this range
                int start = (int)super.getXMinimum() - 1;
                int end = (int)super.getXMaximum() + 2;
                // Clamp it to the minimum/maximum data values
                if(start < 0) start = 0;
                if(end > model.seriesLength()) end = model.seriesLength();

                // The following loop only renders across pixel changes, not
                //  data changes, so if the data is packed into a single pixel,
                //  only the average data value for that pixel is rendered.
                //  First the loop calculates in all data that fits into the
                //  first pixel and stores that as p2, then it puts p2 into p1
                //  and calculates the second pixel and stores in p2, renders
                //  from p1->p2, and then stores p2 in p1 (making it the new p1)
                //  and recalcuates a new p2 for the next render. If there are
                //  error values, they are calculated into p4 and render as a
                //  line from p2->p4 vertically +/-. This renders from p2->p4
                //  because p2 was the value just calculated, so is the value
                //  that's related to p4. This means the very first p4 is lost
                //  because the first pixel can't render as it fills p2 only,
                //  and can't do the p1->p2 part, but for this case, when p1 is
                //  null, it does actually do a render, but only renders the
                //  error bars. Update (2008y08M06d): added p3 to store error
                //  value of previous point, stored from p4 after it is
                //  calculated. If zoomed into the graph so that the difference
                //  between p1 and p2 is more than one pixel on the x-axis,
                //  instead of rendering the error value as a line off p2, it is
                //  rendered as a filled polygon from p1 +/- p3 -> p2 +/- p4,
                //  but is still rendered as a p2->p4 vertical line if p3 is
                //  null or difference between p1 and p2 is only one pixel

                // Render each point in the data
                for(int i = start; i < end; i++)
                {

                    // Calculate x point on screen to determine when to render
                    //  i.e. only render when there's a change in the integer x
                    //  value
                    x = getX(model.getXCoord(i));

                    // Don't render if this is the very first point, must wait
                    //  until p1 and p2 are ready
                    if(i == start)
                        lastX = x;
                    else
                    // Only render if doing so will draw a line from X to >X,
                    //  i.e. don't render any vertical lines
                    if(x != lastX)
                    {

                        // Wait for a start point to be calculated
                        // p4 will only ever get a non-null value if this series
                        //  has an error series
                        // p1 could be null at this point, which is handled in
                        //  render and means only the error bars should be
                        //  rendered
                        drawDatum
                        (   graphicsEx,
                            defaultPen,
                            graphPen,
                            model,
                            p1, p2, p3, p4
                        );

                        // Make the end point the new start point, and reset
                        p1 = p2; p2 = null;
                        p3 = p4; p4 = null;
                        lastX = x;

                    }

                    // Calculate the end point
                    if(p2 == null)
                        p2 = new AveragePoint(x, model.getYCoord(i));
                    else
                        // Note: setY is not explicitly setting a Y value, it is
                        //  accumulating y values for an average. (setY is in
                        //  AveragePoint class)
                        p2.setY(model.getYCoord(i));

                    // Calculate the error bars (if any)
                    if(hasError == true)
                        if(p4 == null)
                            p4 = new AveragePoint(0, model.getZCoord(i));
                        else
                            // Note: setY is not setting explicitly a Y value,
                            //  it is accumulating y values for an average.
                            //  (setY is in AveragePoint class)
                            p4.setY(model.getZCoord(i));

                }

                // Render the last point, if there is one
                if(p2 != null)
                    drawDatum
                    (   graphicsEx,
                        defaultPen,
                        graphPen,
                        model,
                        p1, p2, p3, p4
                    );

            }

    }

    /**
     * Render the data along with the error bars
     * @param graphics Render context
     * @param errorPen Pen used to render error bars
     * @param graphPen Pen used to render graph data
     * @param model Data model
     * @param p1 Start point
     * @param p2 End point
     * @param p3 Error bars (previous position)
     * @param p4 Error bars (rendered on end point). If null is passed, error
     *  bars are not rendered, and colour is not changed. If non-null is passed
     *  errors bars are rendered and on exit, the colour will be set to colour
     *  of the data
     */
    private void drawDatum
    (   Graphics2D  graphics,
        Stroke      errorPen,
        Stroke      graphPen,
        GEVAGraphModel model,
        AveragePoint      p1,
        AveragePoint      p2,
        AveragePoint      p3,
        AveragePoint      p4
    ){  assert p2 != null : "p2, why are you null?";

        // Note. when drawing lines, pN.getX() is precalculated where pN.getY()
        //  is not, so pN.getY() is wrapped in getY(..)

        if(p4 != null)
        {   // Render the error bars first (so the data is not hidden by them)
            graphics.setColor(model.getErrorColour());
            graphics.setStroke(errorPen);
            if(p1 == null || p1.getX() + 1 >= p2.getX())
                graphics.drawLine
                (   p2.getX(), getY(p2.getY() - p4.getY()),
                    p2.getX(), getY(p2.getY() + p4.getY())
                );
            else
            {   Polygon polygon = new Polygon();
                // +1 on p1.x so that previous data value isn't overwritten,
                //  and +1 on p2.x because of some rounding error which is
                //  causing white vertical lines to appear
                polygon.addPoint(p1.getX() + 1, getY(p1.getY() - p3.getY()));
                polygon.addPoint(p2.getX() + 1, getY(p2.getY() - p4.getY()));
                polygon.addPoint(p2.getX() + 1, getY(p2.getY() + p4.getY()));
                polygon.addPoint(p1.getX() + 1, getY(p1.getY() + p3.getY()));
                graphics.fillPolygon(polygon);
            }
            // Render the data - p1 will be null if were in here only to render
            //  error bars
            if(p1 != null)
            {   graphics.setColor(model.getColour());
                graphics.setStroke(graphPen);
                graphics.drawLine
                (   p1.getX(), getY(p1.getY()),
                    p2.getX(), getY(p2.getY())
                );
            }
        }
        else
        // Render the data
        if(p1 != null)
            graphics.drawLine
            (   p1.getX(), getY(p1.getY()),
                p2.getX(), getY(p2.getY())
            );

    }

    /**
     * Convert from data x to screen x
     * @param x data x
     * @return screen x
     */
    protected int getX(float x)
    {   return super.dataToScreen(x, 0).x;
    }

    /**
     * Convert from data y to screen y
     * @param y data y
     * @return screen y
     */
    protected int getY(float y)
    {   return super.dataToScreen(0, y).y;
    }

    /**
     * Helper for setXExtrema to prevent illegal values being passed
     * @param minimum
     * @param maximum
     */
    public void setXExtremaSafe(float minimum, float maximum)
    {   assert model instanceof GEVAGraphModel
             : model.getClass().getName();
        float temp;
        // Avoid nonsense values
        if(java.lang.Float.isInfinite(minimum) == true
        || java.lang.Float.isNaN(minimum) == true
        || java.lang.Float.isInfinite(minimum) == true
        || java.lang.Float.isNaN(minimum) == true)
        {   minimum = ((GEVAGraphModel)model).getXExtremaMin();
            maximum = ((GEVAGraphModel)model).getXExtremaMax();
        }
        // Keep minimum < maximum
        if(maximum < minimum)
        {   temp = minimum;
            minimum = maximum;
            maximum = temp;
        }
        // Don't go too small
        if(maximum - minimum < 0.01)
            maximum = minimum + 0.01f;
        super.setXExtrema(minimum, maximum);
    }

    /**
     * Helper for setYExtrema to prevent illegal values being passed
     * @param minimum
     * @param maximum
     */
    public void setYExtremaSafe(float minimum, float maximum)
    {   assert model instanceof GEVAGraphModel
             : model.getClass().getName();
        float temp;
        // Avoid nonsense values
        if(java.lang.Float.isInfinite(minimum) == true
        || java.lang.Float.isNaN(minimum) == true
        || java.lang.Float.isInfinite(minimum) == true
        || java.lang.Float.isNaN(minimum) == true)
        {   minimum = ((GEVAGraphModel)model).getXExtremaMin();
            maximum = ((GEVAGraphModel)model).getXExtremaMax();
        }
        // Keep minimum < maximum
        if(maximum < minimum)
        {   temp = minimum;
            minimum = maximum;
            maximum = temp;
        }
        // Don't go too small
        if(maximum - minimum < 0.01)
            maximum = minimum + 0.01f;
        super.setYExtrema(minimum, maximum);
    }

    /**
     * X labels are being packed together too tightly, overlapping each other.
     *  This only renders every second label to space them out
     * @param graphics
     * @param value
     * @param point
     */
    @Override
    protected void drawXLabel(Graphics graphics, double value, Point point)
    {
        if(interleave == 0)
            super.drawXLabel(graphics, value, point);
        interleave = (interleave + 1) % 2;
    }

    /**
     * Calculate the value of the data for a series at a particular mouse
     *  location on the graph. This is 'calculate', rather than 'get', as a
     *  single pixel can cover several data points; this returns the average of
     *  all points under the specified point
     * @param seriesIndex Index of the series whose value is to be calculated
     * @param screenX The x point in pixels of the data value to calculate
     * @return The average of all the data points under the screen X
     */
    @SuppressWarnings("hiding")
    // 'super.model' being hidden by local 'model' - suppressed as both refer to
    //   the same thing, but where super.model is a Graph2DModel (see JSci/
    //   swing/JGraph2D.java), local model is a GEVAGraphModel (which implements
    //   Graph2DModel) so it's not so much hiding it, as making it more visible
    public float calcSeriesValueAt(int seriesIndex, int screenX)
    {   assert model instanceof GEVAGraphModel : model.getClass().getName();
        GEVAGraphModel model = (GEVAGraphModel)super.model;
        Float p1 = super.screenToData(new Point(screenX,     0));
        Float p2 = super.screenToData(new Point(screenX + 1, 0));
        int startIndex = (int)p1.x;
        int endIndex = (int)p2.x;
        // Make sure there's something to read
        if(startIndex == endIndex)
            endIndex++;
        float value = 0;
        for(int dataIndex = startIndex; dataIndex < endIndex; dataIndex++)
        {   try { value += model.getSeriesValueAt(seriesIndex, dataIndex); }
            catch(IndexOutOfBoundsException e)
            {   // When the end of the series is reached, all additional values
                //  added would be zero (identity), so just break
                break;
            }
        }
        if((endIndex - startIndex) > 1)
            return value / (endIndex - startIndex);
        return value;
    }

    /**
     * Get the data index at a screen coordinate relative to the graph. This is
     *  the value of the x axis on the graph
     */
    public float getDataIndexAt(int screenX)
    {   return super.screenToData(new Point(screenX, 0)).x;
    }

    /**
     * Get the data value at a screen coordinate relative to the graph. This is
     *  the value of the y axis on the graph
     */
    public float getDataValueAt(int screenY)
    {   return super.screenToData(new Point(0, screenY)).y;
    }

    /**
     * Get the range of data indexes between screen coordinates relative to the
     *  graph. This is the value along the x axis on the graph
     */
    public float getDataIndexBetween(int screenX, int screenU)
    {   return super.screenToData(new Point(screenX, 0)).x
             - super.screenToData(new Point(screenU, 0)).x;
    }

    /**
     * Get the range of data values between screen coordinates relative to the
     *  graph. This is the value along the y axis on the graph
     */
    public float getDataValueBetween(int screenY, int screenV)
    {   return super.screenToData(new Point(0, screenY)).y
             - super.screenToData(new Point(0, screenV)).y;
    }

    /**
     * Helper to keep the average value of a point on the y axis
     */
    class AveragePoint
    {

        private int x;
        private double y;
        private int step;

        /**
         * Create an average point of (0, 0)
         */
        public AveragePoint()
        {   x = 0;
            y = 0;
            step = 0;
        }

        /**
         * Create an average point of (x, y)
         */
        public AveragePoint(int x, int y)
        {   this.x = x;
            this.y = y;
            step = 1;
        }

        /**
         * Create an average point of (x, y)
         */
        public AveragePoint(int x, float y)
        {   this.x = x;
            this.y = y;
            step = 1;
        }

        /**
         * Change the x value to the specified value. Calling getX() will return
         *  the last value set here
         */
        public void setX(int x)
        {   this.x = x;
        }

        /**
         * Change the y value by the specified value. Calling getY() will return
         *  the average of all calls to setY. e.g. setY(6); setY(2); setY(4);
         *  getY() => 6 -- [(6+2+4)/3]
         */
        public void setY(int y)
        {   this.y += y;
            step++;
        }

        /**
         * Change the y value by the specified value. Calling getY() will return
         *  the average of all calls to setY. e.g. setY(6); setY(2); setY(4);
         *  getY() => 6 -- [(6+2+4)/3]
         */
        public void setY(float y)
        {   this.y += y;
            step++;
        }

        /**
         * Get the last value set by setX() or initialised in the ctor
         */
        public int getX()
        {   return x;
        }

        /**
         * Get the average value for y after all setY() have been accumulated.
         *  The average of all calls to setY. e.g. setY(6); setY(2); setY(4);
         *  getY() => 6 -- [(6+2+4)/3]
         */
        public float getY()
        {   if(step == 0)
                return 0;
            return (float)(y / step);
        }

    }

}
