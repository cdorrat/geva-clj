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

import java.awt.Component;
import javax.swing.JPanel;

/**
 * Base class for any panes that show up on the GEVARun window. If a new pane
 *  is created that extends from this one, to use it, add to GEVARun's
 *  initialiseUI(), under the comment '//[BEGIN_PANE_INIT[', the allocation of
 *  the new pane and below this, under the comment '//[BEGIN_PANE_LISTENER[',
 *  add the new pane to be a listener of whichever parsing events it needs to
 *  handle. Note. because Java uses erasure in generic types, if a pane needs to
 *  listen to event from multiple parsers, it cannot have overloaded methods
 *  that differ only by a generic type's template, so, the best solution I've
 *  found so far (which I've implemented in GEVAOutputPane) is to create
 *  multiple nested classes which each handle the specific generic type and
 *  expose them through a getXListener() and getYListener() etc.
 *  Next, in the new run pane's constructor, call the super constructor, passing
 *   the paneManager (which is GEVARun and is passed in when constructing the
 *   run pane during initialiseUI) followed by the title that should appear in
 *   the GEVARun window tab for this run pane.
 *  Finally, implement the abstract lineParsed and streamParsed, which are
 *   called when the parser has some data to give and when all parsing has ended
 *   respectively.. When parsing of understood lines begins, and the new run
 *   pane is ready to start displaying its results, call the super method,
 *   viewMe(reason) where reason is a simple description of why the run pane
 *   wants to be shown. Only when viewMe is called is the run pane finally made
 *   visible to the user as a tab entry. It also brings that run pane to the
 *   foreground so the user can see it. viewMe(reason) can be called several
 *   times given the same reason, but will only have an effect the first time
 *   its called, so it can be in a parsing loop yet only force the run pane to
 *   the foreground the first time it is called (this is so, if another run pane
 *   has something to show, it can show it without immediately losing to
 *   something that's already been shown but is still looping new data). If
 *   however, the run pane has very important information to show, and wants to
 *   guarantee it will be force to the foreground, it can call viewMe() with no
 *   reason.
 *  If the run pane has buttons or other UI components, it can call on the super
 *   method, addTool(Component), specifying the button or other UI control. This
 *   is then added to the toolbar of the GEVARun window and will only be
 *   displayed when the run pane it relates to is selected
 *  If a new parser is needed to capture some specific information from GEVA,
 *   see GEVAStreamParser for details on how to create one
 * @author eliott bartley
 */
public abstract class GEVAPane
              extends JPanel
{

    private GEVAPaneManager paneManager;

    public GEVAPane(GEVAPaneManager paneManager, String title)
    {   this.paneManager = paneManager;
        this.paneManager.addPane(this, title);
    }

    public void viewMe()
    {   paneManager.viewPane(this);
    }

    public void viewMe(String reason)
    {   paneManager.viewPane(this, reason);
    }

    public boolean isViewingMe()
    {   return paneManager.isViewingPane(this);
    }

    public void addTool(Component component)
    {   paneManager.addPaneTool(this, component);
    }

    public void removeTool(Component component)
    {   paneManager.removePaneTool(this, component);
    }

    public void addStatus(Component component)
    {   paneManager.addPaneStatus(this, component);
    }

    public void removeStatus(Component component)
    {   paneManager.removePaneStatus(this, component);
    }

}
