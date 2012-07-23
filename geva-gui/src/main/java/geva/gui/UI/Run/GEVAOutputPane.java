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

import geva.gui.Util.I18N;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

/**
 * Formatted text pane with no wrapping
 * @author eliottbartley
 */
  class GEVAOutputPane
extends GEVAPane
{

    private JTextPane txtOutput;
    private StandardListener standardListener = new StandardListener();
    private ErrorListener errorListener = new ErrorListener();

    /**
     * Create a formatted text pane with no wrapping
     */
    public GEVAOutputPane(GEVAPaneManager paneManager)
    {   super(paneManager, I18N.get("ui.run.cons.name"));
        super.setLayout(new BorderLayout());

		txtOutput = new JTextPane();
        txtOutput.setEditable(false);
        StyleConstants.setFontFamily
        (   txtOutput.addStyle("out", null),
            "Monospaced"
        );
        StyleConstants.setForeground
        (   txtOutput.addStyle("err", txtOutput.getStyle("out")),
            Color.red
        );

		// no wrap by adding text pane to a panel using border layout
        //  this pane expands with the text and the scroll pane scrolls over it
		JPanel guiNoWrap = new JPanel();
		guiNoWrap.setLayout(new BorderLayout());
		guiNoWrap.add(txtOutput);

		JScrollPane guiScroll = new JScrollPane(guiNoWrap);
        super.add(guiScroll);

    }

    /**
     * Write <var>line</var> string to the text window using the specified
     *  <var>style</var> which can be txtOutput.getStyle("out") or "err"
     */
    private void addOutput(String line, Style style)
    {   boolean autoScroll = false;

        // If the caret is at the end of the output, set it to auto-scroll,
        //  i.e. keep the caret at the end of the output, otherwise, keep
        //  the caret in its current position and let the text be added
        //  without being chased
        if(txtOutput.getCaretPosition() == txtOutput.getDocument().getLength())
            autoScroll = true;

        // Add the new line of text
        try
        {   txtOutput.getDocument().insertString
            (   txtOutput.getDocument().getLength(),
                line + "\n",
                style
            );
        }
        catch(BadLocationException e)
        {   // If fast inserting failed, use the slow method instead
            txtOutput.setText(txtOutput.getText() + line + "\n");
        }

        // Move the caret to the end now that the documents been updated
        if(autoScroll == true)
            txtOutput.setCaretPosition(txtOutput.getDocument().getLength());

    }

    /**
     * Get the listener for events from the StandardStreamParser. This is a
     *  hack work-around thanks to Java's erasure, which disallows multiple
     *  inheritence of generic types
     */
    public StandardListener getStandardListenee()
    {   return standardListener;
    }

    /**
     * Get the listener for events from the ErrorStreamParser. This is a
     *  hack work-around thanks to Java's erasure, which disallows multiple
     *  inheritence of generic types
     */
    public ErrorListener getErrorListenee()
    {   return errorListener;
    }

    /**
     * Because Java doesn't allow multiple inheritence of generic types, this
     *  nested class deals specifically with one version of the generic
     *  GEVAStreamParser (encapsulated in GEVAStandardStreamParser), where
     *  ErrorListener below deals with another (encapsulated in
     *  GEVAErrorStreamParser)
     */
    public class StandardListener
      implements GEVAStandardStreamParser.Listener
                 <GEVAStandardStreamParser.Event>
    {   public void lineParsed(GEVAStandardStreamParser.Event event)
        {   GEVAOutputPane.super.viewMe("out");
            addOutput(event.getLine(), txtOutput.getStyle("out"));
        }
        public void streamParsed() { }
    }

    /**
     * Because Java doesn't allow multiple inheritence of generic types, this
     *  nested class deals specifically with one version of the generic
     *  GEVAStreamParser (encapsulated in GEVAErrorStreamParser), where
     *  StandardListener above deals with another (encapsulated in
     *  GEVAStandardStreamParser)
     */
    public class ErrorListener
      implements GEVAErrorStreamParser.Listener
                 <GEVAErrorStreamParser.Event>
    {   public void lineParsed(GEVAErrorStreamParser.Event event)
        {   GEVAOutputPane.super.viewMe();
            addOutput(event.getLine(), txtOutput.getStyle("err"));
        }
        public void streamParsed() { }
    }

}
