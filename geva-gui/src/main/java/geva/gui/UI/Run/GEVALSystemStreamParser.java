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

/**
 * Parse lines that relate to LSystems
 * @author eliott bartley
 */
public class GEVALSystemStreamParser
     extends GEVAStreamParser<GEVALSystemStreamParser.Event>
{

    private boolean isLSystem = false;

    protected void parseLine(Line line)
    {   int at;
        String temp;
        Event event;

        if(isLSystem == false
        && line.getLine().indexOf("fitness_function=") != -1
        && line.getLine().indexOf("LSystem") != -1)
            isLSystem = true;
        else
        if(isLSystem == true
        && line.getLine().indexOf("Rank:") != -1
        && line.getLine().indexOf("Fit:") != -1
        && (at = line.getLine().indexOf("Phenotype:")) != -1)
        {

            event = new Event();

            temp = line.getLine().substring(at + 10);

            at = temp.indexOf(" ");
            event.depth = Integer.parseInt(temp.substring(0, at));
            temp = temp.substring(at + 1);

            at = temp.indexOf(" ");
            event.angle = Float.parseFloat(temp.substring(0, at));
            event.grammar = temp.substring(at + 1);

            super.fireParserListener(event);

        }

    }

    /**
     * LSystem parsed event
     */
    public static class Event extends GEVAStreamParser.Event
    {   private String grammar;
        private int depth;
        private float angle;
        public String getGrammar()
        {   return grammar;
        }
        public int getDepth()
        {   return depth;
        }
        public float getAngle()
        {   return angle;
        }
    }

}
