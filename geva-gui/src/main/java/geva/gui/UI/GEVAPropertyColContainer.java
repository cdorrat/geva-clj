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
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 * Property container that shows the name value property as name above value.
 *  Name becomes a heading to value, and value takes the whole space of the
 *  container so only one name/value control can be added
 * @author eliottbartley
 */
public class GEVAPropertyColContainer extends GEVAPropertyContainer
{

    private JPanel guiProperties;

    public GEVAPropertyColContainer
    (   GEVADirtyListener dirtyListener,
        GEVAPageContainer parent
    ){  super(dirtyListener, parent, "PropertyColContainer", null, null);
        guiProperties = new JPanel();
        guiProperties.setLayout(new BorderLayout());
        parent.add(this);
    }

    @Override
    public void add(GEVAControl control)
    {   super.add(control);
        guiProperties.add(control.getComponent(0), BorderLayout.NORTH);
        guiProperties.add(control.getComponent(1), BorderLayout.CENTER);
    }

    public Component getComponent(int index)
    {   return guiProperties;
    }

    public int countComponents() { return 1; }

}

