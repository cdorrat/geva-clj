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

import geva.gui.UI.Run.GEVAStreamParser.Line;
import geva.gui.Util.Constants;
import geva.gui.Util.GEVAHelper;

/**
 * Parse data lines - these are lines containing GRAPH_DATA_BEGIN (Currently
 *  (2008y08M11d) "#---Data---"), which marks the start of data, the line
 *  immediately following GRAPH_DATA_BEGIN, which is taken as the labels for the
 *  data, and then every line that follows that can be split into the same
 *  number of parts as the label line, and where all parts are convertable to a
 *  double
 * @author eliott bartley
 */
public class GEVAGraphStreamParser
{

    public static final String EXT_GRAPH = Constants.extGraph;

    private LabelStreamParser labelInstance;
    private DataStreamParser dataInstance;
    private String[] labels;

    public class LabelStreamParser
         extends GEVAStreamParser<LabelStreamParser.Event>
    {

        @Override
        protected void parseLine(Line line)
        {   Event event;

            if(isLabelLine(line) == false)
                return;

            GEVAGraphStreamParser.Extension extension
                = (GEVAGraphStreamParser.Extension)line.getExtension(EXT_GRAPH);
            if(extension == null)
                line.setExtension
                (   EXT_GRAPH,
                    extension = new GEVAGraphStreamParser.Extension()
                );

            event = new Event();
            labels = GEVAHelper.prune(line.getLine().split("\\s"));
            event.labels = labels;
            extension.isGraphable = true;

            super.fireParserListener(event);

        }

        public class Event extends GEVAStreamParser.Event
        {   private String[] labels;
            public String[] getLabels()
            {   return labels;
            }
        }

        private boolean isLabelLine(Line line)
        {   GEVAGlobalStreamParser.Extension extension
                = (GEVAGlobalStreamParser.Extension)line.getExtension
                (   GEVAGlobalStreamParser.EXT_GLOBAL
                );

            if(extension == null || extension.isLabelHint() == false)
                return false;

            return true;

        }

    }

    public class DataStreamParser
         extends GEVAStreamParser<DataStreamParser.Event>
    {

        @Override
        protected void parseLine(Line line)
        {   Event event;
            String[] words;
            GEVAGraphStreamParser.Extension extension
                = (GEVAGraphStreamParser.Extension)line.getExtension(EXT_GRAPH);
            if(extension != null)
                extension.isGraphable = false;

            if(isDataLine(line) == false)
                return;

            event = new Event();
            words = GEVAHelper.prune(line.getLine().split("\\s"));
            // Ignore if there's an incorrect amount of data, there must be one
            //  datum for every label
            if(words.length == labels.length)
                try
                {
                    event = new Event();
                    event.data = new double[words.length];
                    // Attempt to convert all data to numeric float. If any part
                    //  cannot be converted (and an exception is thrown), this
                    //  is not a data line
                    int i = 0;
                    for(String word : words)
                        event.data[i++] = GEVAHelper.parseFloat(word);
                    // Data is available, send it
                    extension.isGraphable = true;
                    super.fireParserListener(event);
                }
                catch(NumberFormatException e)
                {   // The data could not be converted to numeric, which can
                    //  only mean the line didn't contain graphable data. Which
                    //  means this is a normal text line and not an error
                }

        }

        public class Event extends GEVAStreamParser.Event
        {   private double[] data;
            public double[] getData()
            {   return data;
            }
        }

        public boolean isDataLine(Line line)
        {   GEVAGlobalStreamParser.Extension extension
                = (GEVAGlobalStreamParser.Extension)line.getExtension
                (   GEVAGlobalStreamParser.EXT_GLOBAL
                );

            if(extension == null || extension.isDataHint() == false)
                return false;

            return true;

        }

    }

    /**
     * Extension. Communicate with output parser that it's not to output what
     *  the graph has already dealt with
     */
    public static class Extension extends GEVAStreamParser.Extension
    {

        private boolean isGraphable = false;

        /**
         * Returns true if the current Line is for graphing, and therefor, not
         *  for text output
         */
        public boolean isGraphable()
        {   return isGraphable;
        }

        @Override
        public Object getData(int index)
        {   switch(index)
            {   case 0: return isGraphable;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void setData(int index, Object data)
        {   assert false : "This extension is read-only";
        }

        @Override
        public int getLength()
        {   return 1;
        }

    }

}
