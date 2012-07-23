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

package geva.gui.UI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * Pages that appear in the Book control. Each page added to book add a new tab
 * Includes functionallity for showing scroll bars if the page is too big for
 * the UI
 * @author eliottbartley
 */
public class GEVAPageContainer extends GEVAContainerControl
{

    private JPanel guiPage;
    private JScrollPane guiPane;

    public GEVAPageContainer
    (   GEVADirtyListener dirtyListener,
        GEVABookContainer parent,
        String            title,
        String            comment
    ){  super(dirtyListener, parent, "page", title, comment);
        guiPane = new JScrollPane();
        guiPage = new Panel();
        guiPage.setLayout(new BoxLayout(guiPage, BoxLayout.Y_AXIS));
        guiPane.getViewport().add(guiPage);
        parent.add(this);
    }

    @Override
    public void add(GEVAControl control)
    {   super.add(control);
        guiPage.add(control.getComponent());
    }

    public Component getComponent(int index)
    {   return guiPane;
    }

    public int countComponents() { return 1; }

}

/**
 * Panel that sits in a ScrollPane and won't show horizontal scrollbars, always
 *  sizing to fit width, but will show vertical scrollbars when height is too
 *  small
 * @author eliott bartley
 */
class Panel extends JPanel implements Scrollable
{

    public Dimension getPreferredScrollableViewportSize()
    {   return super.getPreferredSize();
    }

    public int getScrollableBlockIncrement
    (   Rectangle visibleRect,
        int orientation,
        int direction
    ){  return (int)(getScrollableIncrement(visibleRect, orientation) * 0.9);
    }

    public boolean getScrollableTracksViewportHeight()
    {   return false;
    }

    public boolean getScrollableTracksViewportWidth()
    {   return true;
    }

    public int getScrollableUnitIncrement
    (   Rectangle visibleRect,
        int orientation,
        int direction
    ){  return getScrollableIncrement(visibleRect, orientation) / 10;
    }

    private int getScrollableIncrement
    (   Rectangle visibleRect,
        int orientation
    ){  if(orientation == SwingConstants.VERTICAL)
            return visibleRect.height;
        else
            return visibleRect.width;
    }

}
