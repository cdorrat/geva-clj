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
import JSci.awt.GraphDataListener;
import geva.gui.Util.I18N;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Override the JSci Graph2DModel to do some custom graph data extraction
 * @author eliott bartley
 */
public class GEVAGraphModel implements Graph2DModel
{

    private static class Serii extends ArrayList<GEVAGraphSeries> { }

    // serii: plural of series, cause I want to use 'series' to reference a
    //  singular series picked from this plural series
    private Serii serii = new Serii();
    // Track the active series when being iterated by the graph library
    private int activeSeries = 0;
    // Store which series should be rendered last, and therefor on top of all
    //  others
    private int topSeries = -1;

    ////////////////////////////////////////////////////////////////////////////
    // Serii methods (applies to all series as a group)
    //

    /**
     * Add a new series to the graph
     * @return Series index. Used in *Series* methods, e.g. getSeriesColour(..)
     */
    public int addSeries()
    {   serii.add(new GEVAGraphSeries());
        return serii.size() - 1;
    }

    /**
     * Add a single instance of statistics data to each of the series
     * @param data Array of data, one for each series
     */
    public void addData(double[] data)
    {   int i = 0;
        for(GEVAGraphSeries series : serii)
            series.add((float)data[i++]);
    }

    /**
     * The min will always be 0. Should I have even bothered adding this method?
     *  Well I did!
     * @return 0, always 0. In fact, from now on, I'm never going to say zero
     *  again, I'll just say "get-x-extrama-min-open-bracket-close-bracket"!
     */
    public float getXExtremaMin()
    {   return 0;
    }

    /**
     * Get the maximum X value on the graph. This will be equal to the last
     *  generation parsed
     * @return The maximum X value on the graph
     */
    public float getXExtremaMax()
    {   if(serii.size() == 0
        || serii.get(0).length() < 10)
            return 10;
        else
            return serii.get(0).length();
    }

    /**
     * Calculate the minimum Y value on the graph. This is calculated by finding
     *  the minimum Y value across all visible statistics (invisible are
     *  ignored). Setting the graphs minimum Y value to this will pack the
     *  display to the visible data
     * @return The minimum Y value on the graph
     */
    public float calcYExtremaMin()
    {   float minimum = Float.MAX_VALUE;
        for(GEVAGraphSeries series : serii)
            if(series.isVisible() == true
            && series.isErrorSeries() == false
            && series.getStatistics().getExtremaMin()
             * series.getScale()
             + series.getOffset() < minimum)
                minimum = series.getStatistics().getExtremaMin()
                        * series.getScale()
                        + series.getOffset();
        if(minimum == Float.MAX_VALUE)
            minimum = 0;
        return minimum;
    }

    /**
     * Calculate the maximum Y value on the graph. This is calculated by finding
     *  the maximum Y value across all visible statistics (invisible are
     *  ignored). Setting the graphs maximum Y value to this will pack the
     *  display to the visible data
     * @return The maximum Y value on the graph
     */
    public float calcYExtremaMax()
    {   float maximum = Float.MIN_VALUE;
        for(GEVAGraphSeries series : serii)
            if(series.isVisible() == true
            && series.isErrorSeries() == false
            && series.getStatistics().getExtremaMax()
             * series.getScale()
             + series.getOffset() > maximum)
                maximum = series.getStatistics().getExtremaMax()
                        * series.getScale()
                        + series.getOffset();
        if(maximum == Float.MIN_VALUE)
            maximum = 100;
        return maximum;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Series methods (applies only to a specified series)
    //

    /**
     * Get the total number of data indexes for the specified series.
     *  getSeriesValueAt(seriesIndex, dataIndex)'s dataIndex will run through
     *  [0..getSeriesLength(seriesIndex) - 1]
     * @param seriesIndex The series index (returned from addSeries) to modify
     * @return The total number of data indexes
     */
    public int getSeriesLength(int seriesIndex)
    {   return serii.get(seriesIndex).length();
    }

    /**
     * Get statistical information about the specified series
     * @param seriesIndex The series index (returned from addSeries) to modify
     * @return statistical information about the specified series
     */
    public GEVAGraphStatistics getSeriesStatistics(int seriesIndex)
    {   return serii.get(seriesIndex).getStatistics();
    }

    /**
     * Set the vertical scaling factor for the specified series. i.e. if the
     *  series has values in the range 50..100, and a scale of 0.5 is set for it,
     *  it will show its values in the range 25..50
     * @param seriesIndex The series index (returned from addSeries) to modify
     * @param scale The scaling factor. Must be > 0 or the graph will throw. It
     *  is best to be kept above 0.0001 or so as very low values can confuse the
     *  extrema calculations causing the graph to throw too
     */
    public void setSeriesScale(int seriesIndex, float scale)
    {   serii.get(seriesIndex).setScale(scale);
    }

    /**
     * Get the vertical scaling factor for the specified series. i.e. if the
     *  series has values in the range 50..100, and a scale of 0.5 is set for it,
     *  it will show its values in the range 25..50
     * @param seriesIndex The series index (returned from addSeries) to modify
     * @return the scaling factor
     */
    public float getSeriesScale(int seriesIndex)
    {   return serii.get(seriesIndex).getScale();
    }

    /**
     * Set the vertical offset amount for the specified series. i.e. if the
     *  series has values in the range 50..100, and an offset of 10 is set for
     *  it, it will show its values ini the range 60..110
     * @param seriesIndex The series index (returned from addSeries) to modify
     * @param offset The offset amount. Can be positive or negative to offset
     *  up or down the graph respectively
     */
    public void setSeriesOffset(int seriesIndex, float offset)
    {   serii.get(seriesIndex).setOffset(offset);
    }

    /**
     * Get the vertical offset amount for the specified series. i.e. if the
     *  series has values in the range 50..100, and an offset of 10 is set for
     *  it, it will show its values in the range 60..110
     * @param seriesIndex The series index (returned from addSeries) to modify
     * @return the offset
     */
    public float getSeriesOffset(int seriesIndex)
    {   return serii.get(seriesIndex).getOffset();
    }

    /**
     * Set the colour used when rendering the specified seriesIndex in the GUI
     * @param seriesIndex The series index (returned from addSeries) to modify
     * @param colour The colour to render as
     */
    public void setSeriesColour(int seriesIndex, Color colour)
    {   serii.get(seriesIndex).setColour(colour);
    }

    /**
     * Get the colour used when rendering the specified seriesIndex in the GUI
     * @param seriesIndex The series index (returned from addSeries) to modify
     * @return the colour used when rendering the specified seriesIndex in the
     *  GUI
     */
    public Color getSeriesColour(int seriesIndex)
    {   return serii.get(seriesIndex).getColour();
    }

    /**
     * Set whether the specified seriesIndex should be visible in the GUI
     * @param seriesIndex The series index (returned from addSeries) to modify
     * @param visible whether it should be visible
     */
    public void setSeriesVisible(int seriesIndex, boolean visible)
    {   serii.get(seriesIndex).setVisible(visible);
    }

    /**
     * Get whether the specified seriesIndex is visible in the GUI
     * @param seriesIndex The series index (returned from addSeries) to test
     * @return whether the specified seriesIndex is visible in the GUI
     */
    public boolean isSeriesVisible(int seriesIndex)
    {   return serii.get(seriesIndex).isVisible();
    }

    /**
     * Set whether the specified seriesIndex should be highlighted in the GUI
     * @param seriesIndex The series index (returned from addSeries) to modify
     * @param selected whether it should be highlighted
     */
    public void setSeriesSelected(int seriesIndex, boolean selected)
    {   serii.get(seriesIndex).setSelected(selected);
    }

    /**
     * Get whether the specified seriesIndex is highlighted in the GUI
     * @param seriesIndex The series index (returned from addSeries) to test
     * @return whether the specified seriesIndex is highlighted in the GUI
     */
    public boolean isSeriesSelected(int seriesIndex)
    {   return serii.get(seriesIndex).isSelected();
    }

    /**
     * Set a seriesIndex to use another seriesIndex as its error bars. A
     *  seriesIndex cannot be its own error bars
     * @param seriesIndex The series index (returned from addSeries) to add
     *  error bars to
     * @param errorIndex The series index (returned from addSeries) that will
     *  become the error bars
     */
    public void setSeriesErrorSeries(int seriesIndex, int errorIndex)
    {   if(seriesIndex == errorIndex)
            throw new IllegalArgumentException(I18N.get("ui.err.grmo.seri"));
        serii.get(seriesIndex).setErrorSeries(serii.get(errorIndex));
    }

    /**
     * Get the seriesIndex that the specified seriesIndex is using as its error
     *  bars. If no seriesIndex is the error bars of the specified one, -1 is
     *  returned
     * @param seriesIndex The series index (returned from addSeries) get the
     *  error bars of
     * @return The index of the series that is the error bars of
     *  <var>seriesIndex</var>
     */
    public int getSeriesErrorSeries(int seriesIndex)
    {   GEVAGraphSeries errorIndex = serii.get(seriesIndex).getErrorSeries();
        if(errorIndex == null)
            return -1;
        return serii.indexOf(errorIndex);
    }

    /**
     * Return whether the specified seriesIndex is the error bars of another
     *  seriesIndex
     * @param seriesIndex The series index (returned from addSeries) to test
     * @return true if the specified seriesIndex is the error bars of another
     *  one, else false
     */
    public boolean isSeriesErrorSeries(int seriesIndex)
    {   return serii.get(seriesIndex).isErrorSeries();
    }

    /**
     * Get the data value of a particular series at a particular point
     * @param seriesIndex The series index (returned from addSeries) to get from
     * @param dataIndex The X co-ordinate of the data index (generation)
     * @return The value of the series at the specified generation
     */
    public float getSeriesValueAt(int seriesIndex, int dataIndex)
    {   return serii.get(seriesIndex).get(dataIndex);
    }

    /**
     * Get the last data value recorded for this series
     * @param seriesIndex The series index (returned from addSeries) to get from
     * @return the last data value recorded
     */
    public float getSeriesValue(int seriesIndex)
    {   GEVAGraphSeries series = serii.get(seriesIndex);
        if(series.length() > 0)
            return series.get(series.length() - 1);
        return 0f;
    }

    /**
     * Set the specified series as being the most important. It is rendered
     *  last and is therefor rendered on top of all others. Set to -1 to make no
     *  series the top series
     * @param seriesIndex The series index (returned from addSeries) to test
     */
    public void setSeriesOnTop(int seriesIndex)
    {   topSeries = seriesIndex;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Iterator methods (applies only to the active series)
    //

    /**
     * All data is recorded on integer steps, so this will always return the
     *  same value as what's passed in
     * @param dataIndex The X co-ordinate of the data index (generation)
     * @return Each generation is output on the X co-ordinate it was recorded
     *  at, so this will be equal to the dataIndex
     */
    public float getXCoord(int dataIndex)
    {   return dataIndex;
    }

    /**
     * Get the value that was recorded for the active series (see
     *  firstSeries/nextSeries) at the generation specified by dataIndex
     * @param dataIndex The generation to use
     * @return The value of the active series at the specified generation
     */
    public float getYCoord(int dataIndex)
    {   return serii.get(activeSeries).get(dataIndex)
             * serii.get(activeSeries).getScale()
             + serii.get(activeSeries).getOffset();
    }

    /**
     * Get the error bar value that was recorded for the active series (see
     *  firstSeries/nextSeries) at the generation specified by dataIndex
     * @param dataIndex The generation to use
     * @return The value of the error series at the specified generation. If no
     *  error series is set for the active series, this returns 0. Use
     *  hasErrorSeries()
     */
    public float getZCoord(int dataIndex)
    {   int errorSeries = getSeriesErrorSeries(activeSeries);
        return serii.get(errorSeries).get(dataIndex)
             * serii.get(activeSeries).getScale()
             * serii.get(errorSeries).getScale();
    }

    /**
     * Get the colour to render the active series in the GUI
     * @return The colour to render the active series in the GUI
     */
    public Color getColour()
    {   return getSeriesColour(activeSeries);
    }

    /**
     * Get the colour to render the error series of the active series
     * @return the colour to render the error series of the active series or
     *  null if there is no error series for the active series
     */
    public Color getErrorColour()
    {   if(hasErrorSeries() == false)
            return null;
        return getSeriesColour(getSeriesErrorSeries(activeSeries));
    }

    /**
     * Get whether the active series is visible
     * @return Whether the active series is visible
     */
    public boolean isVisible()
    {   return isSeriesVisible(activeSeries);
    }

    /**
     * Get whether the error series of the active series is visible
     * @return whether the error series of the active series is visible or
     *  false if there is no error series for the active series
     */
    public boolean isErrorVisible()
    {   if(hasErrorSeries() == false)
            return false;
        return isSeriesVisible(getSeriesErrorSeries(activeSeries));
    }

    /**
     * Get whether the active series is selected
     * @return Whether the active series is selected
     */
    public boolean isSelected()
    {   return isSeriesSelected(activeSeries);
    }

    /**
     * Get whether the active series has another series as its error series
     * @return whether the active series has another series as its error series
     */
    public boolean hasErrorSeries()
    {   return getSeriesErrorSeries(activeSeries) != -1;
    }

    /**
     * Get whether the active series is an error series for another series
     * @return Whether the active series is an error series for another series
     */
    public boolean isErrorSeries()
    {   return isSeriesErrorSeries(activeSeries);
    }

    /**
     * Get the length of the active series (see firstSeries/nextSeries)
     */
    public int seriesLength()
    {   return serii.get(activeSeries).length();
    }

    /**
     * Set the active series to the first series. If setSeriesOnTop is set to 0,
     *  this will set the series to the first series following the 'on-top'
     *  series
     */
    public void firstSeries()
    {   if(topSeries == 0)
            activeSeries = 1;
        else
            activeSeries = 0;
    }

    /**
     * Set the active series to the next series. This will be in order of the
     *  series array except if setSeriesOnTop is set to a value other that -1,
     *  where the order will change so that the 'on-top' series will be moved to
     *  be the last series
     */
    public boolean nextSeries()
    {   // Exit early if the end-of-series index has already been reached
        if(lastSeries() == true)
            return false;

        // This method calculates what the next series is to be. Because the
        //  series order can be changed (so that 'top' series (setSeriesOnTop())
        //  is rendered last, but the order of the series array cannot be
        //  changed, (as all get/setSeries functions use that index to refer to
        //  it), this method has to do a bit of fiddling with the ordering..

        // If the previous series that was active was the 'top' series, (it is
        //  always the last series if set), it means the next series is the end-
        //  of-series index (meaning lastSeries() will return true)
        if(activeSeries == topSeries)
            activeSeries = serii.size();
        else
        // If the previous series was not the 'top' series, just increment as
        //  normal to the next series
        {   activeSeries++;
            // If incrementing has moved on to the 'top' series, it shouldn't
            //  become active now, it should only be active as the very last
            //  series, so immediately just increment to the next series
            if(activeSeries == topSeries)
                activeSeries++;
            // If either of above incrementings has now gone to the end-of-
            //  series index, and a 'top' series has been defined, the next
            //  series is then the 'top' series. When the 'top' series is done
            //  with, it will set the series to the end-of-series index (see
            //  above)
            if(activeSeries == serii.size() && topSeries != -1)
                activeSeries = topSeries;
        }
        // Return false if there was no next series, else true
        return !lastSeries();
    }

    /**
     * Get whether there are any more active series. If this returns true, all
     *  methods that rely on the active series will throw an exception
     * @return true if the last series has been reached, else false
     */
    public boolean lastSeries()
    {   return activeSeries >= serii.size();
    }

    ////////////////////////////////////////////////////////////////////////////
    // And the rest..
    //

    public void addGraphDataListener(GraphDataListener listener) { }
    public void removeGraphDataListener(GraphDataListener listener) { }

}
