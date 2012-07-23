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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

/**
 * Property container that shows the name value property as name left-of value.
 * Name sits left of value, and takes maximum preferred height of name or value
 *  so several name/value controls can be added to the container and each one
 *  will sit below the prevously added control
 * @author eliottbartley
 */
public class GEVAPropertyRowContainer extends GEVAPropertyContainer
{

    private JPanel guiProperties;
    private int row = 0;

    public GEVAPropertyRowContainer
    (   GEVADirtyListener dirtyListener,
        GEVAPageContainer parent,
        String            title,
        String            comment
    ){  super(dirtyListener, parent, "PropertyRowContainer", title, comment);
        guiProperties = new JPanel();
        guiProperties.setLayout(new GridBagLayout());
        guiProperties.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        if(title != null)
            guiProperties.setBorder(BorderFactory.createTitledBorder(title));
        parent.add(this);
    }

    @Override
    public void add(GEVAControl control)
    {   super.add(control);
        guiProperties.add
        (   control.getComponent(0),
            new GridBagConstraints
            (   0, row,
                1, 1,
                0, 0,
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1),
                0, 0
            )
        );
        guiProperties.add
        (   control.getComponent(1),
            new GridBagConstraints
            (   1, row,
                1, 1,
                1, 0,
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1),
                0, 0
            )
        );
        row++;
        guiProperties.add
        (   control.getComponent(2),
            new GridBagConstraints
            (   1, row,
                1, 1,
                1, 0,
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1),
                0, 0
            )
        );
        row++;
    }

    public Component getComponent(int index)
    {   return guiProperties;
    }

    public int countComponents() { return 1; }

}
