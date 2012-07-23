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

import geva.gui.Fractal.LSystem2Control;
import geva.gui.Util.GEVAUncaught;
import geva.gui.Util.I18N;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * View LSystems in a stand-alone project.
 *  Some neato grammars that testing has evolved..
 *   Dragons - F=[F+[F-[F-F]]]-F+[F+F]+F, depth=3, angle=20
 *             F=[F+F-[F-F]]-[F+F+[F-F]]+F+F, depth=3, angle=25
 *   Tree    - F=F+[F-F+[F+F-F+F+F]]-[F-[F+F-F-F+[F+F]]]-F+F-[[F-F]-[F+F]]+F, depth=3, angle=25
 *   Funky   - F=F-F+F-F, depth=7, angle=4.25,32.75-38.25,41.75-46.75,48.5,51.25,54,59-60,61.25-63.75,65.5-68.5,69.25-70.5
 *              (try each of the angles, angle ranges should be tried in 0.25 degree increments)
 *             F+F+F+F-F-F-F=F+F-[F-F]+F-F, depth=4, angle=72
 * @author eliott bartley
 */
public class LSystemViewer extends JFrame
{

    private static final String[][] cmdFlags =
    {   {"1", "-x", "--axiom", I18N.get("ui.lsv.args.1")},
        {"2", "-g", "--grammar", I18N.get("ui.lsv.args.2")},
        {"3", "-d", "--depth", I18N.get("ui.lsv.args.3")},
        {"4", "-a", "--angle", I18N.get("ui.lsv.args.4")},
        {"5", "--help", "-h", "-?", "/?", I18N.get("ui.lsv.args.5")}
    };

    private LSystem2Control guiLSystem;
    private JButton cmdClose = new JButton(I18N.get("ui.lsv.exit"));

    /**
     * Show the LSystem using configuration taken from the command-line args
     */
    public LSystemViewer(String[] args)
    {   super(I18N.get("ui.lsv.name"));
        if(initialiseConfig(args) == true)
            initialiseWindow();
    }

    /**
     * Show the LSystem using textual configuration. Internally, this calls
     *  LSystemViewer(String[]) passing the given parameters in the format of
     *  command-line arguments
     */
    public LSystemViewer
    (   String axiom,
        String grammar,
        String depth,
        String angle
    ){  this(new String[]
        {   cmdFlags[0][1], axiom,
            cmdFlags[1][1], grammar,
            cmdFlags[2][1], depth,
            cmdFlags[3][1], angle
        });
    }

    /**
     * Show the LSystem using specific configuration details
     */
    public LSystemViewer(String axiom, String grammar, int depth, float angle)
    {   guiLSystem = new LSystem2Control(axiom, grammar, depth, angle, cmdClose);
        initialiseWindow();
    }

    /**
     * Parse the command-line arguments and create an LSystemPane from them
     */
    private boolean initialiseConfig(String[] args)
    {   int flag = 0;
        boolean help = false;
        String axiom = "";
        String grammar = "";
        int depth = 0;
        float angle = 0.0f;

        try
        {

            for(String arg : args)
            {   switch(flag)
                {   case 0:
                        if((flag = parseFlag(arg)) == -1)
                        {   help = true;
                            break;
                        }
                        continue;
                    case 1: axiom = arg; break;
                    case 2: grammar = arg; break;
                    case 3: depth = Integer.parseInt(arg); break;
                    case 4: angle = Float.parseFloat(arg); break;
                    default: help = true; break;
                }
                flag = 0;
            }
            if(flag != 0)
                help = true;

        }
        catch(NumberFormatException e)
        {   JOptionPane.showMessageDialog(this, e.toString());
        }

        if(help == true)
        {   displayHelp();
            return false;
        }
        else
            guiLSystem = new LSystem2Control
            (   axiom,
                grammar,
                depth,
                angle,
                cmdClose
            );

        return true;

    }

    /**
     * Set up the display window
     */
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

    private int parseFlag(String arg)
    {   int index = 0;
        for(String[] flag : cmdFlags)
            for(index = 1; index < flag.length - 1; index++)
                if(flag[index].compareToIgnoreCase(arg) == 0)
                    return Integer.parseInt(flag[0]);
        return -1;
    }

    /**
     * When displaying help, display in a dialog window rather than dumping to
     *  a console as this class not only acts as a stand-alone application, but
     *  is also used by GEVA to display LSystems to the user as a child window
     *  to one of GEVA's windows
     */
    private void displayHelp()
    {   int index = 0;
        StringBuilder help = new StringBuilder();

        // Display usage message
        help.append(I18N.get("ui.lsv.args.u"));
        help.append("\n");
        help.append("  ");
        help.append(I18N.get("ui.lsv.args.f"));
        help.append("\n");

        // Display all flags
        for(String[] flag : cmdFlags)
        {

            // Display each flag synonym
            for(index = 1; index < flag.length - 1; index++)
            {   if(index == 1)
                    help.append("  ");
                else
                    help.append(" ");
                help.append(flag[index]);
            }

            // Display flag description
            help.append("\n");
            help.append("  ");
            help.append("  ");
            help.append(flag[flag.length - 1] + "\n");

        }

        JOptionPane.showMessageDialog(this, help);
        super.dispose();

    }

    public static void main(String[] args)
    {   Thread.setDefaultUncaughtExceptionHandler
        (   new GEVAUncaught.GEVAGlobalUncaught()
        );
        new LSystemViewer(args);
    }

}
