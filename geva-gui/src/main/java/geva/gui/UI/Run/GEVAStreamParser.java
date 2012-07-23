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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Base class from which all parsers are derived. This also handles dispatching
 *  the event to the listeners of the event as well as contains the base Event
 *  class. Inheriting classes should also fill the E template with the type
 *  of events it generates when it parses. Note. all the parsers that inherit
 *  from this need to be registered with the GEVAStreamReader through the
 *  addListener method and this is currently (2008y08M08d) done in GEVARun
 *  under the comment '//[BEGIN_STREAM_LISTENER['
 * To create a new parser, first create a new class that extends from this class.
 *  In the new class, add a new nested Event class that extends from this
 *  class's nested Event class and pass the event class name as the template
 *  parameter when extending this class in the format ClassName.EventName.
 * All current (2008y08M08d) extended classes of this class use a singleton
 *  pattern, so this could be added too.
 * Implement the abstract parseLine which takes in a Line object (defined as
 *  nested class within this class). This can be used to get the line text as
 *  well as communicate with other parsers (all parsers receive the same Line
 *  object which is mutable). Parse the line text and if it's something that the
 *  parser understands, generate an event for it by calling
 *  super.fireParserListener passing as instance of the nested Event class and
 *  it will be dispatched to all listeners.
 * To add listeners to the new parser, this is currently (2008y08M08d) done in
 *  GEVARun under the comment '//[BEGIN_STREAM_LISTENER['
 * Note. if adding a new parser to parse the output GEVA writes about its
 *  configuration, instead, update the GEVAConfigurationStreamParser's Event and
 *  parseLine details. GEVAConfigurationStreamParser is set up to parse all
 *  these details, and only generate one event after they've all been parsed,
 *  rather than one event per line like most other parsers. The end of the
 *  configuration is determined by watching for the line containing
 *  Constants.GRAPH_DATA_BEGIN (currently (2008y08M08d) "#---Data---")
 * @author eliott bartley
 */
public abstract class GEVAStreamParser<E extends GEVAStreamParser.Event>
           implements GEVAStreamReader.Listener
{

    private class Listeners extends ArrayList<Listener<E>> { }
    private static class Extensions extends HashMap<String, Extension> { }

    /**
     * Write using output formatting
     */
    public static final int ID_STD_OUT = GEVAStreamReader.ID_STD_OUT;
    /**
     * Write using error formatting
     */
    public static final int ID_ERR_OUT = GEVAStreamReader.ID_ERR_OUT;

    private Listeners listeners = new Listeners();

    private static Extensions globalExtensions = null;
    private Extensions extensions;

    public static void begin()
    {   globalExtensions = new Extensions();
    }

    public static void end()
    {   globalExtensions = null;
    }

    public GEVAStreamParser()
    {   assert globalExtensions != null
             : "Wrap all GEVAStreamParser (+subtype) allocations in begin/end";
        extensions = globalExtensions;
    }

    /**
     * Listens to the stream reader for new lines to be parsed and sends them on
     *  to the inheriting class for parsing. This will not pass on line
     *  containing null values
     */
    public final void streamListener(GEVAStreamReader.Event event)
    {   if(event.isEndOfStream() == true)
            fireParseListener();
        else
        if(event.getLine() != null)
            parseLine(new Line(event));
    }

    /**
     * Fake a stream reader event causing all listeners to be notified of the
     *  <var>line</var> passed in as if it came from the given
     *  <var>streamId</var>
     */
    public void injectLine(String text, int streamId)
    {   parseLine(new Line(new GEVAStreamReader.Event(text, streamId)));
    }

    /**
     * Fake a stream reader event causing all listeners to be notified of the
     *  <var>line</var> passed in as if it came from ID_STD_OUT
     */
    public void injectLine(String text)
    {   parseLine(new Line(new GEVAStreamReader.Event(text, ID_STD_OUT)));
    }

    /**
     * Parse the line and generate the necessary events by calling
     *  fireParserListener. This will not be called for end-of-stream events
     */
    protected abstract void parseLine(Line line);

    /**
     * Add a class to listen to parsing events by the inheriting parser
     */
    public void addParserListener(Listener<E> listener)
    {   listeners.add(listener);
    }

    /**
     * Remove a class from listen to parsing events
     */
    public void removeParserListener(Listener<E> listener)
    {   listeners.remove(listener);
    }

    /**
     * Called by inheriting classes when they have to say lineParsed to any
     *  listening listeners. Inheriting classes must determine the E
     *  template
     */
    protected void fireParserListener(E event)
    {   for(Listener<E> listener : listeners)
            listener.lineParsed(event);
    }
    /**
     * Called by this class when the end of parsing has been reached
     */
    private void fireParseListener()
    {   for(Listener<E> listener : listeners)
            listener.streamParsed();
    }

    /**
     * Listener interface which inheriting classes must extend, even if just
     *  to an empty type, so that listeners can listen on the specific listener
     *  for a given class
     */
    protected static interface Listener<E>
    {   /**
         * Called when a single line has been acceptedly parsed
         */
        public void lineParsed(E event);
        /**
         * Called when the stream has finished parsing. This is always called,
         *  regardless of whether there were any lines parsed (lineParsed) and
         *  once it is called, it and lineParsed will not be be called again
         *  (for this run)
         */
        public void streamParsed();
    }

    /**
     * Base listen event class. Does nothing except define what all events must
     *  be derived from
     */
    protected static class Event { }

    /**
     * Base listen event class, which simply gives the listener the line that
     *  was (or wasn't, as the case may be when using this event) parsed
     */
    protected static class LineEvent extends Event
    {   private String line;
        public LineEvent(String line)
        {   this.line = line;
        }
        /**
         * Get the line, unmodified, that was parsed when this event was
         *  generated
         */
        public String getLine()
        {   return line;
        }
    }

    /**
     * Proxy to GEVAStreamReader.Event to pass on to inheriting classes so they
     *  don't have to deal with the Reader directly.
     */
    protected class Line
    {

        private GEVAStreamReader.Event event;

        protected Line(GEVAStreamReader.Event event)
        {   this.event = event;
        }

        /** @link GEVAStreamReader.Event#getLine() */
        public String getLine()
        {   return event.getLine();
        }

        /** @link GEVAStreamReader.Event#getStreamId() */
        public int getStreamId()
        {   return event.getStreamId();
        }

        /** @link GEVAStreamReader.Event#isEndOfStream() */
        public boolean isEndOfStream()
        {   return event.isEndOfStream();
        }

        /**
         * Get a named extension. Each parser will have its own name and data
         *  type for its extension, see the related parser for details. Names
         *  of extensions will be defined in the parser as EXT_*
         */
        public Extension getExtension(String name)
        {   return extensions.get(name);
        }

        /**
         * Set a named extension. Each parser will have its own name and data
         *  type for its extension, see the related parser for details. Names
         *  of extensions will be defined in the parser as EXT_*
         */
        public void setExtension(String name, Extension extension)
        {   extensions.put(name, extension);
        }

    }

    /**
     * Base from which all extensions inherit
     */
    public static abstract class Extension
    {   public abstract Object getData(int index);
        public abstract void setData(int index, Object data);
        public abstract int getLength();
    }

}
