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

package geva.UI;

import geva.Fractal.LSystem2Control;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * View LSystems in a stand-alone project
 * @author eliott bartley
 */
public class LSystemViewer extends JFrame
{

    private LSystem2Control guiLSystem;
    private JButton      cmdClose = new JButton("Close");

    public LSystemViewer(String grammar, int depth, float angle)
    {   guiLSystem = new LSystem2Control(grammar, depth, angle, cmdClose);
        initialiseWindow();
    }
    
    private void initialiseWindow()
    {   super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setSize(400, 500);
        super.setLayout(new BorderLayout());

        cmdClose.addActionListener(new ActionListener()
        {   public void actionPerformed(ActionEvent e)
            {   dispose();
            }
        });

        super.add(guiLSystem, BorderLayout.CENTER);

        super.setVisible(true);
        
    }
    
}
