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

import java.util.ArrayList;

/**
 * Keep track of all running GEVA instances. Each GEVARun is responsible for
 *  removing itself from this list. GEVAPropertiesGUI adds them to the list and
 *  also calls on closeAll when the GUI is closed
 * @author eliott bartley
 */
public class GEVARunning
{

    private static class RunList extends ArrayList<GEVARun> { }

    private GEVARunning() { }

    private static RunList runList = new RunList();

    public static void add(GEVARun run)
    {   runList.add(run);
    }

    /**
     * Called by GEVAPropertiesGUI when it is closed, to get all its children
     *  to close. Closing the child windows also causes the processes that those
     *  child windows are monitoring to be terminated (so the real purpose of
     *  this is to stop all forked processes)
     * @return true if all windows were closed and the GUI can exit. false if
     *  the user cancelled closing any window -- the GUI should remain open
     */
    public static boolean closeAll()
    {   while(runList.size() > 0)
            if(close(runList.get(0)) == false)
                return false;
        return true;
    }

    /**
     * Called by GEVARun when its window is closed
     * @param run
     */
    static void remove(GEVARun run)
    {   runList.remove(run);
    }

    private static boolean close(GEVARun run)
    {   // Implicitly calls remove -- when window closes, its last action
        //  is to call remove to remove itself
        run.close();
        // After disposing, the list should no longer contain the run. If it's
        //  still in the list, it means the run didn't remove itself, meaning
        //  the user must have cancelled the close, and wants GEVA to continue
        //  running
        return runList.contains(run) == false;
    }

}
