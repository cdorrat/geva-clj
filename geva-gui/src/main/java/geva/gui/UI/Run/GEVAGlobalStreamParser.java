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

/**
 * Parse data that's globally useful for all other parsers. This parser should
 *  be the first called in the parser chain and will update the Line object so
 *  other parsers will know what it's done
 * @author eliott bartley
 */
public class GEVAGlobalStreamParser
     extends GEVAStreamParser<GEVAGlobalStreamParser.Event>
{

    public static final String EXT_GLOBAL = Constants.extGlobal;

    protected void parseLine(Line line)
    {   Extension extension = (Extension)line.getExtension(EXT_GLOBAL);
        if(extension == null)
            line.setExtension(EXT_GLOBAL, extension = new Extension());

        if(line.getLine().trim().equals(Constants.GRAPH_DATA_BEGIN) == true)
            // Be default, the configuration hint will be true, this ends that
            extension.isConfigurationHint = false;
        else
        if(extension.isConfigurationHint == false
        && extension.isLabelHint == false
        && extension.isDataHint == false)
            // After the configuration hint has gone to false, one parsed line
            //  later, the label hint goes true
            extension.isLabelHint = true;
        else
        if(extension.isConfigurationHint == false
        && extension.isLabelHint == true)
        {   // After the label hint has gone true, one parsed line later, the
            //  data hint goes true, and will remain in this state until the end
            //  of GEVA
            extension.isLabelHint = false;
            extension.isDataHint = true;
        }

    }

    /**
     * Unique empty type for overloading
     */
    public static class Event extends GEVAStreamParser.LineEvent
    {   public Event(String line) { super(line); }
    }

    /**
     * Hint to other parsers about when they should start parsing. This is an
     *  optimisation, which reduces the number of parsers working out what a
     *  line of text means, this parser works out what the line potentially
     *  means, and the other parsers can decide to run based on this
     */
    public class Extension extends GEVAStreamParser.Extension
    {

        private boolean isConfigurationHint = true;
        private boolean isLabelHint = false;
        private boolean isDataHint = false;

        /**
         * Returns true if the current Line is potentially parsable as a
         *  configuration line
         */
        public boolean isConfigurationHint()
        {   return isConfigurationHint;
        }

        /**
         * Returns true if the current Line is potentially parsable as a
         *  label line
         */
        public boolean isLabelHint()
        {   return isLabelHint;
        }

        /**
         * Returns true if the current Line is potentially parsable as a
         *  data line
         */
        public boolean isDataHint()
        {   return isDataHint;
        }

        @Override
        public Object getData(int index)
        {   switch(index)
            {   case 0: return isConfigurationHint;
                case 1: return isLabelHint;
                case 2: return isDataHint;
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void setData(int index, Object data)
        {   assert false : "This extension is read-only";
        }

        @Override
        public int getLength()
        {   return 3;
        }

    }

}
