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

import geva.gui.Util.Constants;

import java.util.Timer;
import java.util.TimerTask;
import java.awt.Window;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.JWindow;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

/**
 * Display splash screen with GEVA logo
 * @author eliottbartley
 */
public class GEVASplash extends JWindow
{

    public GEVASplash(Window owner)
    {   super(owner);
        Dimension screen;

        try
        {

            screen = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds
            (   (screen.width - 400) / 2,
                (screen.height - 300) / 2,
                400,
                300
            );
            add(new JLabel(new ImageIcon(ClassLoader.getSystemResource
            (   Constants.IMG_SPLASH
            ))));
            new Timer().schedule(this.new GEVASplashTimer(), 2000);
            setVisible(true);

        }
        catch(HeadlessException e)
        {   /* If splash cannot be loaded, simply don't show it */
        }

    }

    public void bye()
    {   dispose();
    }

    private class GEVASplashTimer extends TimerTask
    {   public void run()
        {   bye();
        }
    }

}
