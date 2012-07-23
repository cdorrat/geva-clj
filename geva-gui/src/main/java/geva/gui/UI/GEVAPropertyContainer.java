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

/**
 * Base class for property containers. These are added to Page controls to give
 *  the page one or more logical groups of controls. Controls cannot be added
 *  directly to a page, and must be in a property control. To simulate adding
 *  controls directly to the page, the property container can be created with
 *  a null title. This will create the container, but not display it with any
 *  visible border. If the GEVAPropertyRowContainer is used, passing title of
 *  null shows no border, "" shows a small border, and "..." shows a title
 *  border
 * @author eliottbartly
 */
public abstract class GEVAPropertyContainer extends GEVAContainerControl
{

    public GEVAPropertyContainer
    (   GEVADirtyListener dirtyListener,
        GEVAPageContainer parent,
        String            type,
        String            title,
        String            comment
    ){  super(dirtyListener, parent, type, title, comment);
    }

}
