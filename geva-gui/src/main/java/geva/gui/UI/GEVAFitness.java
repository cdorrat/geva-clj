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

import geva.gui.Util.GEVAHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Store stuff about each fitness function. This class is populated by
 *  initialiseFFConfig* in GEVAPropertiesGUI and stored in
 *  GEVAPropertiesGUI.fitnessDetails which is a hash map that maps the fitness
 *  function (by class name) to these details
 * @author eliott bartley
 */
public class GEVAFitness
{

    public static class Names extends ArrayList<String> { }
    public static class FitnessCommands extends HashMap<String, String> { }

    /**
     * Default grammar file associated with this fitness function
     */
    public String grammarFile;
    /**
     * Names of the jar class paths that are associated with this fitness
     *  function
     */
    public Names names = new Names();
    /**
     * Store list of additional commands to pass on the command line when
     *  running GEVA using this fitness function
     */
    public static FitnessCommands fitnessCommands = new FitnessCommands();

    /**
     * Get all the jar files in the collection of jars by name, but also
     *  the collection of names, and also the global class path specified on the
     *  command line. This can be cancelled if the list of jars is still being
     *  populated by the GEVAJarHunter
     * @return null if cancelled, else string containing all jar files or empty
     *  string if there are no jar files
     */
    public String getJar()
    {   StringBuilder jarFiles = null;
        String jarFile;
        if(GEVAConfig.getClassRelPath() != null
        && GEVAConfig.getClassRelPath().length() != 0)
        {   jarFiles = new StringBuilder();
            jarFiles.append(GEVAHelper.quote(GEVAConfig.getClassRelPath()));
        }
        for(String name : names)
            if((jarFile = GEVAJarHunter.get(name)) == null)
                return null;
            else
                if(jarFile.length() != 0)
                    if(jarFiles == null)
                    {   jarFiles = new StringBuilder();
                        jarFiles.append(jarFile);
                    }
                    else
                        jarFiles.append
                        (   System.getProperty("path.separator")
                          + jarFile
                        );
        if(jarFiles == null)
            return "";
        return jarFiles.toString();
    }
    public String getCmd()
    {   StringBuilder cmdExtras = new StringBuilder();
        String cmd;
        for(String name : names)
            if((cmd = fitnessCommands.get(name)) != null && cmd.length() != 0)
                cmdExtras.append(" " + cmd);
        cmdExtras.append(' ');
        return cmdExtras.toString();
    }
}
