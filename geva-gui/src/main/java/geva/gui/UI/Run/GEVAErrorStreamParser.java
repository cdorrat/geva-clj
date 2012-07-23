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
 * Parse lines from error out
 * @author eliott bartley
 */
public class GEVAErrorStreamParser
     extends GEVAStreamParser<GEVAErrorStreamParser.Event>
{

    /**
     * Fake a stream reader event causing all listeners to be notified of the
     *  <var>line</var> passed in as if it came from ID_ERR_OUT. Overridden
     *  to generate ID_ERR_OUT instead of ID_STD_OUT
     */
    @Override
    public void injectLine(String text)
    {   parseLine(new Line(new GEVAStreamReader.Event
        (   text,
            GEVAStreamReader.ID_ERR_OUT
        )));
    }

    protected void parseLine(Line line)
    {   assert line.getStreamId() == GEVAStreamParser.ID_ERR_OUT
             : "GEVAErrorStreamParser!=" + line.getStreamId();
        super.fireParserListener(new Event(line.getLine()));
    }

    /**
     * Unique empty type for overloading
     */
    public static class Event extends GEVAStreamParser.LineEvent
    {   public Event(String line) { super(line); }
    }

}
