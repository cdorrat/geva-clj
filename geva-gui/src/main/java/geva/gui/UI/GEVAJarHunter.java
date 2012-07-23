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
import geva.gui.Util.GEVAUncaught;
import geva.gui.Util.I18N;
import geva.gui.Util.PathTools;

import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Asynchronously build a list of all the jar files in the specified paths
 * @author eliott bartley
 */
public class GEVAJarHunter implements Runnable
{

    static class JarNames extends HashMap<String, String> { }
    static class JarFiles extends HashMap<String, String> { }

    private GEVAJarHunter() { }

    /**
     * Map the jar name to the search path; read from config file
     */
    static JarNames jarNames = new JarNames();
    /**
     * Map the jar name to the jar files; built up from searching the disk-drive
     *  in directories specified by the related jarNames
     */
    static JarFiles jarFiles = new JarFiles();
    /**
     * Do the search asynchronously
     */
    static Thread runJarHunter;
    /**
     * If the list of jars is not ready for getting, this blocks the get until
     *  the list of jars is updated
     */
    static Semaphore waitForUpdate = new Semaphore(1, true);

    /**
     * Add a new name and search path
     * @param name Name used to relate back to
     * @param paths Search path(s) - see GEVAHelper.fileList for more info
     */
    public static void add(String name, String paths)
    {   synchronized(jarNames)
        {   if(jarNames.containsKey(name) == false)
            {   jarNames.put(name, paths);
                jarFiles.put(name, null);
            }
        }
    }

    /**
     * Start the search
     */
    public static void start()
    {   if(runJarHunter == null)
        {   runJarHunter = new Thread(GEVAUncaught.jar, new GEVAJarHunter());
            runJarHunter.start();
        }
    }

    /**
     * Run the search
     */
    public void run()
    {   String using = "";
        String paths = null;
        String[] jars;
        StringBuilder jarBuilder = null;

        // While still possibly more to search, loop through each item added
        while(using != null)
        {

            using = null;
            // Lock the semaphore, so a 'get' will know a run is in the works
            waitForUpdate.acquireUninterruptibly();

            // Get the name of the next item that hasn't been searched. It is
            //  known to be searched when jarFiles[name] is not null, as it will
            //  have been updated with the names of the jars, or "" if there are
            //  no jars
            synchronized(jarNames)
            {   synchronized(jarFiles)
                {   for(String name : jarNames.keySet())
                    {   if(jarFiles.containsKey(name) == true
                        && jarFiles.get(name) == null)
                        {   using = name;
                            paths = jarNames.get(name);
                            break;
                        }
                    }
                }
            }

            // If using == null it means there are no more jar files to search
            //  for, and the loop (above) will exit
            if(using != null)
                // If paths == null it means the named jar files entry had no
                //  path specified to be searched, so the else will cause this
                //  to be ignored
                if(paths != null)
                {

                    // Do the search for jars..
                    jars = PathTools.fileList(paths);

                    // fileList returns an array of paths. Convert these into a
                    //  flat string separated by ; or : or whatever the version
                    //  of java uses to separate class paths
                    for(String jar : jars)
                        if(jarBuilder == null)
                        {   jarBuilder = new StringBuilder();
                            jarBuilder.append(GEVAHelper.quote(jar));
                        }
                        else
                            jarBuilder.append
                            (   System.getProperty("path.separator")
                              + GEVAHelper.quote(jar)
                            );
                    // Make sure there's at least an empty string if no jars
                    //  were found
                    if(jarBuilder == null)
                        jarBuilder = new StringBuilder();

                    // Put the string of jars into the files list related by
                    //  name
                    synchronized(jarFiles)
                    {   jarFiles.put(using, jarBuilder.toString());
                    }

                    jarBuilder = null;

                }
                else
                    // Put an empty string here so this list of jar files won't
                    //  be searched for again (as having no value for a named
                    //  jarFiles decides what needs to be searched for next)
                    synchronized(jarFiles)
                    {   jarFiles.put(using, "");
                    }
                

            // Unlock the semaphore, so a 'get' will know it can check if what
            //  it's looking for is now available
            waitForUpdate.release();

        }

        runJarHunter = null;

    }

    /**
     * Get the list of jar files by name. This method will block until the list
     *  of jar files has been created
     * @param name
     * @return null if the get was cancelled, else string containing all used
     *  jar files. If there are no jar files, an empty string is returned
     */
    public static String get(String name)
    {   String files;
        // Attempt to get the required files
        synchronized(jarFiles)
        {   // If the name being searched for doesn't exist, ignore the request
            if(jarFiles.containsKey(name) == false)
                return "";
            files = jarFiles.get(name);
        }
        // If the files have not yet been found, block in a loop until they
        //  are found. This loop is controlled by a semaphore so that the loop
        //  isn't constantly running, and will only run when a new jarFiles list
        //  has been added
        while(files == null)
        {   // Wait for the semaphore to become available, meaning the run has
            //  finished the next search, then immediately release it again so
            //  run can get started on its next search
            try
            {

                if(waitForUpdate.tryAcquire(5, TimeUnit.SECONDS) == true)
                    waitForUpdate.release();
                else
                    switch(GEVAMessage.showMessage
                    (   GEVAHelper.mainWindow,
                        I18N.get("ui.jar.look.wrn"),
                        GEVAMessage.YES_NO_OPTION
                      | GEVAMessage.QUESTION_TYPE
                    )){ case GEVAMessage.YES_OPTION: continue;
                        case GEVAMessage.NO_OPTION: return null;
                    }
            }
            catch(InterruptedException e)
            {   GEVAMessage.showMessage
                (   GEVAHelper.mainWindow,
                    I18N.get("ui.jar.wait.err", e)
                );
                e.printStackTrace();
                return null;
            }

            // Note: here will not be reached except in the event that run has
            //  finished a listing of jar files. There are three other possible
            //  paths that can be taken, each of which jump over this and
            //  following code if they are executed. These are..
            //  _case_ _description_________________________________ _action____
            //  error  an exception occurs while waiting for the jar return null
            //          file list to be generated
            //  yes    after waiting for jar file list, user is      continue
            //          asked if they want to continue waiting and
            //          they respond yes
            //  no     after waiting for jar file list, user is      return null
            //          asked if they want to continue waiting and
            //          they respond no

            // Attempt to get the required files again
            //  if this returns null again, the loop will continue
            //  When run has actually processed this particular named entry,
            //  files will either be the list of jar files, or it will be
            //  an empty string, it will not be null - null means it has not yet
            //  processed that named entry
            synchronized(jarFiles)
            {   files = jarFiles.get(name);
            }

        }
        // Files looked for have been found..
        return files;
    }

}
