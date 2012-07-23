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

package geva.gui.UI.Run;

import geva.gui.Util.Constants;

/**
 * Parse lines that GEVA output about the configuration it is using
 * @author eliott bartley
 */
public class GEVAConfigurationStreamParser
     extends GEVAStreamParser<GEVAConfigurationStreamParser.Event>
{

    private static Event event = new Event();

    protected void parseLine(Line line)
    {   assert line.getStreamId() == GEVAStreamParser.ID_STD_OUT
             : "GEVAConfigurationStreamParser!=" + line.getStreamId();

        // Accumulate all configuration details without generating events for
        //  them, and then, after all are dealt with, generate one event passing
        //  them all together
        if(line.getLine().indexOf(Constants.GRAPH_DATA_GENERATIONS) != -1)
            event.setGenerations(Integer.parseInt(line.getLine().substring(12)));
        else
        // Only generate one event after all configuration details have been
        //  parsed. Unlike other parsers which generate events every time it
        //  reads something it understands. This means <var>event</var> is a
        //  class-global always-allocated value that is updated as details are
        //  parsed and should contain all the values by the time graph data
        //  begins to be output
        if(line.getLine().trim().equals(Constants.GRAPH_DATA_BEGIN) == true)
            super.fireParserListener(event);

    }

    /**
     * Store all the configuration details in one class. Only when all are
     *  parsed is the event sent on to the listeners
     */
    public static class Event extends GEVAStreamParser.Event
    {

        // TODO - add more configuration details as they're needed
        private int generations;

        /**
         * Get the number of generations GEVA will run for
         */
        public int getGenerations()
        {   return generations;
        }

        /**
         * Record the number of generations GEVA will run for
         */
        protected void setGenerations(int generations)
        {   this.generations = generations;
        }

    }

}
