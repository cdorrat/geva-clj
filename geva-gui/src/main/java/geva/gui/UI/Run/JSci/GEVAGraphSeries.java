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

import geva.gui.Util.I18N;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Keep all details about an individual series. Each series relates to each of
 *  the statistic numbers produced by GEVA
 * @author eliott bartley
 */
class GEVAGraphSeries
{

    private static class Data extends ArrayList<Float>
    {   public Data() { super(); }
        public Data(Data data) { super(data); }
    }

    // Array of data for this series
    private Data data = new Data();
    // If another series is the error bars of this one, this refers to that one
    private GEVAGraphSeries errorSeries;
    // If this series is the error bars for another one, this count totals how
    //  many other series are using this. When this count is zero, it is used
    //  to say that this series is not the error bars for any other (The graph
    //  will not render a series if it is an error bar for one or more other
    //  series)
    private int errorSeriesRefCount = 0;
    // Store amount to multiple by before rendering
    private float scale = 1.0f;
    // Store amount to offset by before rendering
    private float offset = 0.0f;
    // Store the colour used for rendering this series
    private Color colour;
    // Should this series be rendered?
    private boolean visible = true;
    // Should this series be highlighted
    private boolean selected = false;
    // Keep a reference to statistical information about this series
    private Statistics statistics = new Statistics();

    /**
     * Create a new series to store data about a particular statistic output
     * @param label The name of the output. This is taken from the first line
     *  output when the statistics output begins
     */
    public GEVAGraphSeries()
    {   setColour(null);
    }

    /**
     * Get the number of recorded points in the data set. This will be equal
     *  with the current generation
     * @return The number of recorded points in the data set
     */
    public int length()
    {   synchronized(data)
        {   return data.size();
        }
    }

    /**
     * Add to the data set. This will be called when a new stat line is read
     *  from GEVA and is parsed correctly
     * @param value The value to addDatum
     */
    public void add(float value)
    {   if(value < statistics.extremaMin)
            statistics.extremaMin = value;
        if(value > statistics.extremaMax)
            statistics.extremaMax = value;
        synchronized(data)
        {   data.add(value);
        }
    }

    /**
     * Get the value at a particular index. The index relates to the generation
     * @param dataIndex The generation whose datum is to be retrieved
     * @return
     */
    public float get(int dataIndex)
    {   synchronized(data)
        {   try
            {   // TODO - replace this array with a sparse data algorithm,
                //  plus some delete and approximate on complex data arrays for
                //  better memory consumption - this data seems to be the
                //  biggest consumer of memory in across GEVA and the GUI
                // NOTE - I've tested, and this is the only point where data is
                //  taken from, so by placing a sparse algorithm here, the rest
                //  of the GUI will be unaware of any changes, so long as that
                //  algorithm can map all dataIndex values to a data value, it
                //  will be ok
                return data.get(dataIndex);
            }
            catch(IndexOutOfBoundsException e)
            {   // If attempting to get an index that is out of range, it most
                //  likely means a series render is asking for its error series
                //  value, but that value isn't loaded yet (render is
                //  asynchronous) so just return 0.
                // Because this could also hide real errors, two additional
                //  conditons must be true for it to ignore the error; the
                //  request must be for an index one greater than the total (as
                //  the series can only ever exceed the error series by one),
                //  and also, this series being queried must be an error series.
                //  Otherwise, the exception is thrown again
                if(dataIndex == data.size() && isErrorSeries() == true)
                    return 0;
                throw e;
            }
        }
    }

    /**
     * Set the amount to multiply by before rendering
     * @param scale
     */
    public void setScale(float scale)
    {   this.scale = scale;
    }

    /**
     * Get the amount to multiple by before rendering
     * @return
     */
    public float getScale()
    {   return scale;
    }

    /**
     * Set the amount of offset by (on y axis) before rendering
     */
    public void setOffset(float offset)
    {   this.offset = offset;
    }

    /**
     * Get the amount of offset by (on y axis) before rendering
     */
    public float getOffset()
    {   return offset;
    }

    /**
     * Set the colour used when rendering this series in the GUI
     */
    public void setColour(Color colour)
    {   if(colour != null)
            this.colour = colour;
        else
            this.colour = new Color
            (   GEVAGraphColours.colourIndexes
                [   (int)(Math.random() * GEVAGraphColours.colourIndexes.length)
                ]
            );
    }

    /**
     * Get the colour used when rendering this series in the GUI
     */
    public Color getColour()
    {   return colour;
    }

    /**
     * Make this series visible/invisible in the GUI
     * @param visible
     */
    public void setVisible(boolean visible)
    {   this.visible = visible;
    }

    /**
     * Get whether this series is visible/invisible in the GUI
     * @return
     */
    public boolean isVisible()
    {   return visible;
    }

    /**
     * Set whether this series is selected. Selected series appear highlighted
     *  in the GUI
     * @param selected
     */
    public void setSelected(boolean selected)
    {   this.selected = selected;
    }

    /**
     * Get whether this series is selected. Selected series appear highlighted
     *  in the GUI
     */
    public boolean isSelected()
    {   return selected;
    }

    /**
     * Get the statistics recorded for this series
     * @return
     */
    public GEVAGraphStatistics getStatistics()
    {   return statistics;
    }

    /**
     * Set the series that renders the error bars for this series
     * @param errorSeries
     */
    public void setErrorSeries(GEVAGraphSeries errorSeries)
    {   if(this == errorSeries)
            throw new IllegalArgumentException(I18N.get("ui.err.grse.seri"));
        // Do nothing if making this refer to a series it already refers to
        if(this.errorSeries == errorSeries)
            return;
        else
        // Before referring to a new series, 'unlink' it from the pervious
        //  series that it was linked to (if any)
        if(this.errorSeries != null)
        {   this.errorSeries.errorSeriesRefCount--;
            assert this.errorSeries.errorSeriesRefCount >= 0
                 : this.errorSeries.errorSeriesRefCount;
        }
        // Make the link..
        this.errorSeries = errorSeries;
        // ..and tell the linked series that it has become the error bars for
        //  this series through the art of reference counting
        if(this.errorSeries != null)
        {   this.errorSeries.errorSeriesRefCount++;
            assert this.errorSeries.errorSeriesRefCount > 0
                 : this.errorSeries.errorSeriesRefCount;
        }
    }

    /**
     * Get the series that renders the error bars for this series
     * @return
     */
    public GEVAGraphSeries getErrorSeries()
    {   return errorSeries;
    }

    /**
     * Return whether this series renders the error bars for one or more other
     *  series
     * @return true if this is the error bars for another series, else false
     */
    public boolean isErrorSeries()
    {   return errorSeriesRefCount > 0;
    }

    /**
     * Record useful statistical information about each series. Should be thread
     *  safe
     */
    public class Statistics implements GEVAGraphStatistics
    {

        private float extremaMin = Float.MAX_VALUE;
        private float extremaMax = Float.MIN_VALUE;

        /**
         * The lowest this series has ever been
         */
        public float getExtremaMin()
        {   return extremaMin;
        }

        /**
         * The highest this series has ever been
         */
        public float getExtremaMax()
        {   return extremaMax;
        }

        /**
         * The average value
         */
        public float calcMean()
        {   float total = 0;
            synchronized(data)
            {   for(float datum : data)
                    total += datum;
                return total / data.size();
            }
        }

        /**
         * The middle value
         */
        public float calcMedian()
        {   Data median;
            int mid;

            synchronized(data)
            {   median = new Data(data);
            }

            Collections.sort(median);
            mid = median.size() / 2;

            if(median.size() == 0)
                return 0;
            else
            if(median.size() % 2 == 1)
                return median.get(mid);
            else
                return (median.get(mid - 1) + median.get(mid)) / 2;
        }

        /**
         * The spread of the values
         */
        public float calcStandardDeviation()
        {   Data dataEx;
            float mean;
            float wideMean;

            // Get the average of the data
            mean = calcMean();

            synchronized(data)
            {   dataEx = new Data(data);
            }

            // Spread the data so that those near the average remain near but
            //  those further away go real, real far away, and all in a positive
            //  direction. Then get the average of the spread data
            wideMean = 0;
            for(float datum : dataEx)
                wideMean += (float)Math.pow(datum - mean, 2);
            wideMean /= dataEx.size();

            // Unspread the result
            return (float)Math.sqrt(wideMean);

        }

    }

}
