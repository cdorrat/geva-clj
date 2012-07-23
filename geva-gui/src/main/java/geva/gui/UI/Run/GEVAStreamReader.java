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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A class dedicated to reading the output and error streams of an external
 *  process (GEVA for example). This is done in a separate thread which the
 *  class handles
 * @author eliott bartley
 */
public class GEVAStreamReader extends Thread
{

    private static class StreamListeners
                 extends ArrayList<Listener> { }

    /**
     * Write using output formatting
     */
    public static final int ID_STD_OUT = 0;
    /**
     * Write using error formatting
     */
    public static final int ID_ERR_OUT = 1;

    private StreamListeners streamListeners = new StreamListeners();
    private InputStream stream;
    private int streamId;

    /**
     * Create a thread to read a stream and output to a string property
     * @param stream The stream to read
     * @param streamId The streamId of stream this be, either ID_STD_OUT or
     *  ID_ERR_OUT
     */
    public GEVAStreamReader
    (   ThreadGroup threadGroup,
        InputStream      stream,
        int            streamId
    ){  super(threadGroup, threadGroup.getName());
        this.stream = stream;
        this.streamId = streamId;
        this.start();
    }

    /**
     * Run the blocking stream reader in its own thread. This thread
     *  can only be terminated by the stream coming to an end (process
     *  ended) or by killing the process whose stream is being read
     */
    @Override
    public void run()
    {   BufferedReader reader;
        String line;

        try
        {

            reader = new BufferedReader(new InputStreamReader(stream));
            while((line = reader.readLine()) != null)
                fireStreamListeners(line, this.streamId);

        }
        catch(IOException e)
        {   // TODO - exceptions should be written out to the error stream
            //  reader. This will mean passing in the GEVAStreamReader that
            //  handles errors, and injecting this error into its stream. As it
            //  is, errors reading the processes stream are pretty much
            //  invisible to the user. The user will just see GEVA end
            //  unexpectedly without reason
            System.out.println(e);
        }

        // Notify all the listeners that the stream is dead to the stream-reader
        fireStreamListeners(null, this.streamId);

    }

    /**
     * Add a listener that can respond to new lines being read in from the
     *  stream. TODO (LISTENERS_TALK_TODO) the order in which listeners are
     *  added is important as listeners are free(ish) to modify the event
     *  listened to, and any listener that modifies the event will affect all
     *  listeners that were added after it was added. This is by design and
     *  allows the listeners to 'talk' to each other (hacky, but this system is
     *  the complete rewrite of what was a worse hack, and when the time is
     *  right, this hack will go too (maybe) but until then, this is an
     *  optimisation - i.e. when parsing, the things parsed by the graph-parser
     *  should not be parsed by the standard-parser, because it has the
     *  potential to cripple GEVA as it's too much data for the standard-
     *  parser's listeners to handle, but easily handled by the graph-parser's
     *  listeners, so it's important that the graph-parser can tell the
     *  standard-parser to ignore the lines it has parsed. I have not been able
     *  to think of a generic OO way to do this cleanly, so I wont even try.
     *  Update (2008y0M11d) ability for listeners to talk to each other has been
     *  restricted (they can no longer modify the line read from the stream)
     *  but has now been implemented through the use of 'extensions' of the
     *  stream parser's Line class, which acts as a proxy to this class's Event
     *  class. Note. this update does not change the requirement that the order
     *  of the listeners is important
     * @param streamListener The object that can respond to new lines read from
     *  stream
     */
    public void addStreamListener(Listener streamListener)
    {   streamListeners.add(streamListener);
    }

    // Unleash the magic
    void fireStreamListeners(String line, int streamId)
    {   for(Listener streamListener : streamListeners)
            streamListener.streamListener(new Event(line, streamId));
    }

    /**
     * Listen to stream reading events
     */
    public static interface Listener
    {   /**
         * Called when the stream reads a new line of input. This is also called
         *  when the end of the stream is reached, where event.isEndOfStream
         *  will be true and event.getLine will be null
         */
        public void streamListener(Event event);
    }

    /**
     * Event data for stream reading listeners
     */
    public static class Event
    {

        private String line;
        private int streamId;
        private boolean endOfStream;

        /**
         * Create an event which is a line read from the stream plus the id of
         *  the stream read from (ID_STD_OUT or ID_ERR_OUT)
         * @param line The line read or null if the end of stream has been
         *  reached
         */
        public Event(String line, int streamId)
        {   this.line = line;
            this.streamId = streamId;
            this.endOfStream = line == null;
        }

        /**
         * Get the line read from the stream. If the end-of-stream has been
         *  reached, this will return null - however, this should not be used
         *  to test for an end-of-stream, as other listeners are free to modify
         *  the line data (see LISTENERS_TALK_TODO) and they may set it to null.
         *  To correctly test for end-of-stream, call isEndOfStream, which
         *  should be called first by any listener to decide whether it needs to
         *  continue
         */
        public String getLine()
        {   return line;
        }

        /**
         * Get the id of the stream that the line was read from, either
         *  ID_STD_OUT or ID_ERR_OUT
         */
        public int getStreamId()
        {   return streamId;
        }

        /**
         * Returns true when the event has been generated because the stream
         *  ended. This will happen when either the process finished normally,
         *  it crashed, or even, if just reading the stream failed and can no
         *  longer be read. Basically, when it's determined that no more events
         *  will be generated by this stream again.
         */
        public boolean isEndOfStream()
        {   return endOfStream;
        }

    }

}
