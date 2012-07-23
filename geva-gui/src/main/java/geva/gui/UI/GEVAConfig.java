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
import geva.gui.Util.GEVAHelper;
import geva.gui.Util.I18N;
import geva.gui.Util.PathTools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Hold all the configuration details for the life-time of the GUI
 * @author eliottbartley
 */
public class GEVAConfig
{

    private GEVAConfig() { }

    // The configuration as it was read from the properties file
    private static Properties properties = new Properties();
    // The configuration as it was read from the command line - this is
    //  separate to properties because the properties is saved every time the
    //  GUI closes (to save the selected GEVA properties file), but this save
    //  shouldn't overwrite the properties file with the values passed on the
    //  command line. So only the values that are modified by the user are
    //  the ones saved, all values taken from the command line won't be saved
    private static Properties commands = new Properties();
    // When setting, the properties that will be saved to file and the
    //  properties parsed from command line are updated (see set(String, String))
    //  but when setting defaults, the set methods are called too. So in
    //  this case, the command shouldn't be updated, so when setting defaults
    //  properto goes to true to prevent commands been modified
    private static boolean properto = false;
    // When setting, the properties that will be saved to file and the
    //  properties parsed from command line are updated (see set(String, String))
    //  but when parsing the command line, the set methods are called too. So in
    //  this case, the properties shouldn't be updated, so when parsing commands
    //  commando goes to true to prevent properties been modified
    private static boolean commando = false;

    /**
     * Get a configuration value. This will first try the value passed on the
     *  command line (<var>commands</var> (if the value has been set up in
     *  Constants.cmdFlags)), and if that fails, try the value in the gui.config
     *  file (<var>properties</var>), and if that fails, use the default value
     *  (<var>properties</var> too, but always set (during initialise))
     * @param name The named item's value to get
     * @return The value of the named item
     */
    private static String get(String name)
    {   String value = commands.getProperty(name);
        if(value == null)
            value = properties.getProperty(name);
        //System.out.format("Get [%s] as [%s]%n", name, value);
        return value;
    }

    /**
     * Set a configuration value. After calling this, calling get for the same
     *  named item will return the value set here
     * @param name
     * @param value
     */
    private static void set(String name, String value)
    {   if(commando == false)
            properties.setProperty(name, value);
        // Command is set here too because it is tested first when getting,
        //  and because the only other thing that could happen here would be to
        //  set commands to null for the named property, so that properties will
        //  become the 'get' value, might as well just give it the value so get
        //  will just take it from command - properties is also set here because
        //  it is the only thing that will be written out to the file
        if(properto == false)
            commands.setProperty(name, value);
    }

    /**
     * Get the properties file that was selected in the GUI last time it was
     *  run. Saved so that the same properties file will be selected after
     *  closing the GUI
     */
    public static String getSelectedPropertiesFile()
    {   return get(Constants.cfgSelectedProps);
    }

    /** @see #getSelectedPropertiesFile() */
    public static void setSelectedPropertiesFile(String selectedPropertiesFile)
    {   set(Constants.cfgSelectedProps, selectedPropertiesFile);
    }

    /**
     * Get the path to GUI's configuration directory, the one that contains
     *  ff.config. This may be relative or absolute. To get the guaranteed
     *  absolute path, call getConfigAbsPath(). When converting a relative path
     *  into an absolute one, the current working path is used
     */
    private static String getConfigPath()
    {   return get(Constants.cfgConfigPath);
    }

    /** @see #getConfigPath() */
    public static String getConfigRelPath()
    {   return PathTools.getSafePath(PathTools.getRelativePath
        (   getConfigPath(),
            PathTools.getAbsolutePath(".")
        ));
    }

    /** @see #getConfigPath() */
    public static String getConfigAbsPath()
    {   return PathTools.getSafePath(PathTools.getAbsolutePath
        (   getConfigPath(),
            PathTools.getAbsolutePath(".")
        ));
    }

    /** @see #getConfigPath() */
    public static void setConfigPath(String configPath)
    {   set(Constants.cfgConfigPath, configPath);
    }

    /**
 * Get the heap memory to use for GEVA
 * @return amount of memory to allocate
 */
    public static String getHeapSize() {
        String ret = null;
        String tmpHeapSize = get(Constants.cfgHeapSize);
        //Only positive integers
        if(tmpHeapSize != null && tmpHeapSize.matches("^\\d+[mk]?$")) {
                ret = tmpHeapSize;
        }
        return ret;
    }

    public static void setHeapSize(String heapSize) {
        set(Constants.cfgHeapSize, heapSize);
    }

    /**
     * Use the server JVM
     *
     * @return use of server JVM
     */
    public static boolean isServer() {
        return get(Constants.cfgServer)!=null;
    }

    public static void setServer() {
        set(Constants.cfgServer, Constants.cfgServer);
    }

    /**
     * Get the path to GEVA's properties directory. This may be relative or
     *  absolute. To get the guaranteed absolute path, call getConfigAbsPath().
     *  When converting a relative path into an absolute one, the working path
     *  is used, see getWorkingAbsPath()
     */
    private static String getPropertiesPath()
    {   return get(Constants.cfgPropertiesPath);
    }

    /** @see #getPropertiesPath() */
    public static String getPropertiesRelPath()
    {   return PathTools.getSafePath(PathTools.getRelativePath
        (   getPropertiesPath(),
            getWorkingAbsPath()
        ));
    }

    /** @see #getPropertiesPath() */
    public static String getPropertiesAbsPath()
    {   return PathTools.getSafePath(PathTools.getAbsolutePath
        (   getPropertiesPath(),
            getWorkingAbsPath()
        ));
    }

    /** @see #getPropertiesPath() */
    public static void setPropertiesPath(String configPath)
    {   set(Constants.cfgPropertiesPath, configPath);
    }

    /**
     * Get the path to GEVA's grammar directory. This may be relative or
     *  absolute. To get the guaranteed absolute path, call getConfigAbsPath().
     *  When converting a relative path into an absolute one, the working path
     *  is used, see getWorkingAbsPath()
     */
    private static String getGrammarPath()
    {   return get(Constants.cfgGrammarPath);
    }

    /** @see #getGrammarPath() */
    public static String getGrammarRelPath()
    {   return PathTools.getSafePath(PathTools.getRelativePath
        (   getGrammarPath(),
            getWorkingAbsPath()
        ));
    }

    /** @see #getGrammarPath() */
    public static String getGrammarAbsPath()
    {   return PathTools.getSafePath(PathTools.getAbsolutePath
        (   getGrammarPath(),
            getWorkingAbsPath()
        ));
    }

    /** @see #getGrammarPath() */
    public static void setGrammarPath(String grammarPath)
    {   set(Constants.cfgGrammarPath, grammarPath);
    }

    /**
     * Get the path to GEVA classes. This must be relative to the working path.
     *  @see #getWorkingAbsPath()
     */
    public static String getJavaName()
    {   return get(Constants.cfgJavaName);
    }

    /** @see #getJavaName() */
    public static void setJavaName(String javaName)
    {   set(Constants.cfgJavaName, javaName);
    }

    /**
     * Get the path to GEVA classes. This must be relative to the working path.
     *  @see #getWorkingAbsPath(). If an absolute path is given, it will be
     *  converted to a relative path
     */
    public static String getClassRelPath()
    {   return get(Constants.cfgClassPath);
    }

    /** @see #getClassRelPath() */
    public static void setClassPath(String classPath)
    {   set(Constants.cfgClassPath, PathTools.getRelativePath
        (   classPath,
            getWorkingAbsPath()
        ));
    }

    /**
     * Get the name of the main GEVA class
     */
    public static String getClassName()
    {   return get(Constants.cfgClassName);
    }

    /** @see #getClassName() */
    public static void setClassName(String className)
    {   set(Constants.cfgClassName, className);
    }

    /**
     * Get that path used as the current directory when running GEVA, i.e. all
     *  paths within GEVA will be relative to this one. This also means that all
     *  paths in the GUI that are to be given to GEVA will also be relative to
     *  this one, and not the GUI's current directory. This is always an
     *  absolute path, but if it is set using a relative path, it is converted
     *  to an absolute one for the GUI's current directory
     */
    public static String getWorkingAbsPath()
    {   return PathTools.getSafePath(get(Constants.cfgWorkingAbsPath));
    }

    /** @see #getWorkingAbsPath() */
    public static void setWorkingPath(String workingAbsPath)
    {   set
        (   Constants.cfgWorkingAbsPath,
            PathTools.getAbsolutePath(workingAbsPath)
        );
    }

    /**
     * Get whether expert mode is on (--expert flag) which prevents the gui from
     *  disabling advanced features
     */
    public static boolean isExpert()
    {   String expert = get(Constants.cfgExpert);
        if(expert == null)
            return false;
        return expert.equalsIgnoreCase("true");
    }

    /** @see #isExpert() */
    public static void setExpert(boolean expert)
    {   set(Constants.cfgExpert, expert == true ? "true" : "false");
    }

    /**
     * Initialise the configuration details loading in default values, values
     *  from the configuration file, and values from the command line
     * @param args Command line arguements
     * @return false if the GUI should not start - currently (2008y08M20d) this
     *  only happens if the user specifies -h on the command line, so only the
     *  command line help is output, and the program should end (this class
     *  displays the help)
     */
    public static boolean initialise(String[] args)
    {

        // Set everything up with some default values
        properto = true;
        setSelectedPropertiesFile(Constants.defSelectedProps);
        setConfigPath(Constants.defConfigPath);
        setPropertiesPath(Constants.defPropertiesPath);
        setGrammarPath(Constants.defGrammarPath);
        setJavaName(Constants.defJavaName);
        setClassPath(Constants.defClassPath);
        setClassName(Constants.defClassName);
        setWorkingPath(Constants.defWorkingAbsPath);
        properto = false;

        // Currently (2008y08M21d) ignores 'load's return value; even if it
        //  fails, it must still show the GUI, because that's where the
        //  configuration dialog is, which is needed to fix any errors that
        //  occurred loading the config. Note. the GUI knows it needs to show
        //  the configuration dialog when it tries to load ff.config or
        //  graph.config and fails. Note2. returning false from this function
        //  causes the program to terminate and is currently (2008y08M20d) only
        //  used when the command-line switch '-h' is used, which displays help
        //  (and then ends)
        load();
        return parseCommandLine(args);

    }

    /**
     * Get the properties as they were when taken from the gui.config
     */
    public static Properties getProperties()
    {   return properties;
    }

    /**
     * Get the properties as they were when taken from the command line. This
     *  will be modified, along with the getProperties values, when properties
     *  are modified within the program
     */
    public static Properties getCommands()
    {   return commands;
    }

    /**
     * Save the state of all modified configuration values. This will only
     *  update values that have been modified by calling set* i.e. if the GUI
     *  was started with a command line argument that overrode the configuration
     *  properties, that value would be seen by calling get*, but would not be
     *  stored when calling save. If, however, set* was called for that property,
     *  save would then store the updated value. Save always goes to
     *  "./gui.config" i.e. save always goes to gui.config in the current
     *  directory of the GUI
     */
    public static void save() throws IOException
    {   FileOutputStream stream = new FileOutputStream(Constants.cfgPath);
        properties.store(stream, null);
        stream.close();
    }

    /**
     * Save and ignore any errors. Only called when auto-saving [unimportant]
     *  GUI layout, (behind the users back), so don't want to suddenly start
     *  bothering the user with errors if this ninja-like action fails
     */
    public static void saveQuiet()
    {   try { save(); }
        catch(IOException e) { }
    }

    /**
     * Load gui.config from the current directory of the GUI. This will load
     *  all the values that are visible in the configuration dialog as well as
     *  some values that are set by how the user left the GUI before closing.
     *  All the values seen in the configuration dialog can be overridden by
     *  passing command line arguments to the GUI, and these overridden values
     *  will be visible in the configurations dialog, but will not overwrite the
     *  configuration file when a save occurs. The configuration file will only
     *  be overridden if the configuration dialog is shown, and each control is
     *  modified
     */
    private static boolean load()
    {   try
        {
            FileInputStream stream = new FileInputStream(Constants.cfgPath);
            properties.load(stream);
            stream.close();
        }
        catch(IOException e)
        {   GEVAMessage.showMessage
            (   GEVAHelper.mainWindow,
                I18N.get("ui.cfg.colo.err", e),
                GEVAMessage.ERROR_TYPE
            );
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean parseCommandLine(String[] args)
    {   boolean help = false;
        int flag = 0;
        boolean wait = false;

        // Go commando when paring command line, because set calls will normally
        //  perform set on properties and command, this will make it so it only
        //  sets command
        commando = true;

        // Get configured values
        for(String arg : args)
        {

            // flag == 0 means no flag was parsed, so parse one now
            if(flag == 0)
            {   flag = parseFlag(arg);
                // if the flag expects a value, wait prevents this loop
                //  attemptingto parse the value in this cycle (which would end
                //  up parsing the flag again)
                wait = true;
            }
            else
                // If the last flag expected a value and wait was true, the
                //  waiting has ended, this arg must be the value for the flag
                wait = false;

            // Parse stand-alone flags (stand-alone must be < 0)
            if(flag < 0)
                switch(flag)
                {   case -1: help = true;           break;
                    case -2: setExpert(true);       break;
                    case -3: setServer();           break;
                    default: help = true;           break;
                }
            else
            // (name/value flags must be > 0)
            if(flag > 0 && wait == false)
                // Parse name value flags
                switch(flag)
                {   case 1: setConfigPath(arg);     break;
                    case 2: setPropertiesPath(arg); break;
                    case 3: setGrammarPath(arg);    break;
                    case 4: setJavaName(arg);       break;
                    case 5: setClassPath(arg);      break;
                    case 6: setClassName(arg);      break;
                    case 7: setWorkingPath(arg);    break;
                    case 8: setHeapSize(arg);       break;
                    default: help = true;           break;
                }

            // Finished parsing a stand-alone or name/value flag, reset so next
            //  loop will parse a new flag
            if(flag < 0 || flag > 0 && wait == false)
                flag = 0;

        }
        // A name/value flag was specified, but had no value part
        if(flag != 0)
            help = true;

        // No more commando fun, 'set' from this point should also set
        //  properties
        commando = false;

        if(help == true)
        {   displayHelp();
            return false;
        }

        return true;

    }

    private static int parseFlag(String arg)
    {   int index = 0;
        for(String[] flag : Constants.cmdFlags)
            for(index = 1; index < flag.length - 1; index++)
                if(flag[index].compareToIgnoreCase(arg) == 0)
                    return Integer.parseInt(flag[0]);
        return 0;
    }

    private static void displayHelp()
    {   int index = 0;

        // Display usage message
        System.out.println(I18N.get("ui.gui.args.u"));
        System.out.println();
        System.out.println("  " + I18N.get("ui.gui.args.f"));
        System.out.println();

        // Display all flags
        for(String[] flag : Constants.cmdFlags)
        {

            // Display each flag synonym
            for(index = 1; index < flag.length - 1; index++)
            {   if(index == 1)
                    System.out.print("  ");
                else
                    System.out.print(" ");
                System.out.print(flag[index]);
            }

            // Display flag description
            System.out.println();
            System.out.print("    ");
            System.out.println(flag[flag.length - 1]);

        }

    }

}
