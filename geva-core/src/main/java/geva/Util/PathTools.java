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

package geva.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Collection of file path related stuff
 * @author eliott bartley
 */
public class PathTools
{

    private static class PathList extends ArrayList<String> { }

    private PathTools() { }

    /**
     * Turn a relative path or filename into an absolute one
     * @param relativePath The relative path to convert to an absolute one
     * @return The absolute path of the <var>relativePath</var>
     */
    public static String getAbsolutePath(String relativePath)
    {   return getAbsolutePath(relativePath, null, true);
    }

    /**
     * Turn a relative path or filename into an absolute one, relative to a
     *  directory other than the current directory
     * @param relativePath The relative path to convert to an absolute one
     * @param workingPath The home path from which the relative path is from. If
     *  this is null, the current working path is used
     * @return The absolute path of the <var>relativePath</var>
     */
    public static String getAbsolutePath(String relativePath, String workingPath)
    {   return getAbsolutePath(relativePath, workingPath, true);
    }

    /**
     * Turn a relative path or filename into an absolute one
     * @param relativePath The relative path to convert to an absolute one
     * @param addSlash Specify true if returned path must end with a '/', false
     *  if it's not important
     * @return The absolute path of the <var>relativePath</var>
     */
    public static String getAbsolutePath(String relativePath, boolean addSlash)
    {   return getAbsolutePath(relativePath, null, addSlash);
    }

    /**
     * Turn a relative path or filename into an absolute one, relative to a
     *  directory other than the current directory
     * @param relativePath The relative path to convert to an absolute one
     * @param addSlash Specify true if returned path must end with a '/', false
     *  if it's not important
     * @return The absolute path of the <var>relativePath</var>
     */
    public static String getAbsolutePath
    (   String relativePath,
        String workingPath,
        boolean addSlash
    ){  File file;
        String path;
        if(relativePath == null)
            return null;

        file = new File(relativePath);
        if(file.isAbsolute() == false)
        {   file = new File(workingPath, relativePath);
            try { path = file.getCanonicalPath(); }
            catch(IOException e) { path = file.getAbsolutePath(); }
        }
        else
            path = relativePath;

        // Make sure the path end with a '/' if one is required
        //  (addSlash == true)
        if(addSlash == true && file.isDirectory() == true)
            path = getSafePath(path);
        return path.replace("\\", "/");
    }

    /**
     * Break a path down into individual elements and add to a list.
     *  example : if a path is /a/b/c/d.txt, the breakdown will be [d.txt,c,b,a]
     * @param relativeFile input file
     * @return a List collection with the individual elements of the path in
     *  reverse order
     */
	private static PathList getPathList
    (   String relativePath,
        String workingPath
    ){  PathList pathList = new PathList();
        File relativeFile = new File(relativePath);
        File pathParser;
        try
        {

            if(workingPath == null || relativeFile.isAbsolute() == true)
                pathParser = relativeFile.getCanonicalFile();
            else
                pathParser = new File
                (   workingPath,
                    relativePath
                ).getCanonicalFile();

            for
            (   ;
                pathParser != null;
                pathParser = pathParser.getParentFile()
            )   if(pathParser.getName().length() != 0)
                    pathList.add(pathParser.getName());
                else
                    pathList.add(pathParser.getPath());

        }
        catch(IOException e)
        {   // This is for debugging only - the user is notified of the error
            //  when the calling code gets a pathList of null back and tries
            //  to use it as a valid path, and fails, and then tells the user
            //  'path does not exist!' or so -- so this exception shouldn't show
            //  an error message, other than the debugging one it's showing
            //  currently (2008y07M31d), which could be removed in the future,
            //  or, if you don't see it (just under this comment), was removed
            //  in the past.
            System.err.format
            (   "relativePath [%s] workingPath [%s]%n",
                relativePath,
                workingPath
            );
            e.printStackTrace();
            pathList = null;
        }
        return pathList;
    }

    /**
     * Figure out a string representing the relative path of
     * 'absolutePath' with respect to 'workingPath'
     * @param workingPath home path
     * @param absolutePath path of file
     */
    private static String parsePathLists
    (   PathList workingPath,
        PathList absolutePath
    ){  int i;
        int j;
        StringBuilder relativePath = new StringBuilder();
        // start at the beginning of the lists
        // iterate while both lists are equal
        i = workingPath.size() - 1;
        j = absolutePath.size() - 1;

       	// first eliminate common root
       	while
        (   i >= 0 && j >= 0
         && workingPath.get(i).equals(absolutePath.get(j))
        ){  i--;
            j--;
        }

        // for each remaining level in the home path, add a ..
        for(; i>=0; i--)
            relativePath.append(".." + File.separator);

        // for each level in the file path, add the path
        for(; j>=0; j--)
            if(j > 0)
                relativePath.append(absolutePath.get(j) + File.separator);
            else
                relativePath.append(absolutePath.get(j));

        return relativePath.toString();

    }

    /**
     * Get relative path of absolutePath from the current directory. The
     *  resulting path will end with a path separator
     * @param absolutePath file to generate path for
     * @return path from workingPath to absolutePath as a string
     */
    public static String getRelativePath
    (   String absolutePath
    ){  return getRelativePath(absolutePath, null, true);
    }
    /**
     * Get relative path of absolutePath from the current directory, optionally
     *  appending the result with a path separator
     * @param absolutePath file to generate path for
     * @param addSlash true to terminate the result with a path separator
     * @return path from workingPath to absolutePath as a string
     */
    public static String getRelativePath
    (   String absolutePath,
        boolean addSlash
    ){  return getRelativePath(absolutePath, null, addSlash);
    }
    /**
     * Get relative path of absolutePath from the current directory. The
     *  resulting path will end with a path separator
     * @param absolutePath file to generate path for
     * @param workingPath base path, should be a directory, not a file, or it
     *  doesn't make sense
     * @return path from workingPath to absolutePath as a string
     */
    public static String getRelativePath
    (   String absolutePath,
        String workingPath
    ){  return getRelativePath(absolutePath, workingPath, true);
    }
    /**
     * Get relative path of absolutePath from workingPath, optionally
     *  appending the result with a path separator
     * @param absolutePath file to generate path for
     * @param workingPath base path, should be a directory, not a file, or it
     *  doesn't make sense
     * @param addSlash true to terminate the result with a path separator
     * @return path from workingPath to absolutePath as a string
     */
    public static String getRelativePath
    (   String absolutePath,
        String workingPath,
        boolean addSlash
    ){  String path;
        if(workingPath == null)
            workingPath = getAbsolutePath(".");
        absolutePath = absolutePath.trim();
        workingPath = workingPath.trim();
        path = parsePathLists
        (   getPathList(workingPath, null),
            getPathList(absolutePath, workingPath)
        );
        // Make sure the paths end with a '/' if one is required
        if(addSlash == true
        && new File(getAbsolutePath(path, workingPath)).isDirectory() == true)
            path = getSafePath(path);
        return path.replace("\\", "/");
    }

    /**
     * Ensure a path ends with a '/'
     * @param path The path to fix
     * @return The <var>path</var>, with a trailing '/'
     */
    public static String getSafePath(String path)
    {   if(path == null || path.length() == 0)
            return "./";
        if(path.charAt(path.length() - 1) != '/'
        && path.charAt(path.length() - 1) != '\\')
            path += "/";
        return path;
    }

    /**
     * Build a file list of all the files in the specified directories matching
     *  the specified wildcard. pathList can contain several paths separated by
     *  a semi-colon (;) e.g. "c:/.*\\.txt;c:/.*\\.dat" search for *.txt or
     *  *.dat in c:/. Wildcards use regular expression syntax e.g.
     *  "c:/[0-9]{5}\\.txt" search for files made up of 5 numeric digits
     *  followed by .txt. Wildcards can also appear in folder names, e.g.
     *  "c:/my .* /.*" search for all files under c that are in a folder
     *  starting my.. i.e. my document, my videos, etc. Folder searches can be
     *  recursive too, by starting the folder name with N: where N is the
     *  maximum depth of the search, or * for no limit, e.g. "c:/*:my .* /.*"
     *  search for all files under all folders in c: (search the whole drive)
     *  that start my.., or "c:/3:.* /.*\\.txt" search for .txt files in all
     *  folders up to a maximum of 3 levels deep, i.e. is the same as
     *  "c:/.* /.* /.* /.*\\.txt". Note: because \\ is regular expression syntax,
     *  it cannot be used as a path separator, use only / as a path separator
     * @param pathList The list of semi-colon (;) separated paths to search
     * @return Array of all files matching the path, includes pathname
     */
    public static String[] fileList(String pathList)
    {   PathList files = new PathList();
        String[] paths = pathList.split(";");

        for(String path : paths)
            fileList("", null, path.split("/"), 0, files, null, 0);

        return files.toArray(new String[files.size()]);

    }

    /**
     * Recursively get a list of files matching a given wildcard
     * @param base The parent folder that was recursed
     * @param folder The folder this recursion is now looking into
     * @param folders Array of all folders making up the path
     * @param index Index into the folders array that is of importance now
     * @param files List of files currently found that matched
     * @param wildcard If a folder repeats a wildcard *:.*, the wildcard to
     *  repeat
     * @param repeat If a folder repeats a wildcard, how many more times it
     *  needs to repeat
     */
    private static void fileList
    (   String      base,
        String    folder,
        String[] folders,
        int        index,
        PathList   files,
        String  wildcard,
        int       repeat
    ){  File        file; // file referring to the current folder
        String[] results; // list of files found in current folder
        int        colon; // locate colon in repeat wildcard 3:.*
        int  localRepeat; // repeating wildcard at this level, hides wildcard
                          //  inherited during recursion (wilecard, repeat) with
                          //  (folders[index], localRepeat)

        // If the folder to check now is unknown, use the folder specified in
        //  the folder-array. The folder can differ from the folder-array if the
        //  folder-array had .*, folder will be the actual folder that matched
        //  that wildcard; folder is initially null, so fix it to the root
        //  folder to search
        if(folder == null)
            folder = folders[index];
        // Fix up the base so that it now refers to base + current folder
        //  base is initially ""
        base += folder + "/";
        // Move on to the next folder, it is what we're looking for now in this
        //  path
        index++;
        // Drop one level in the repeat wildcard search, while this is > 0, this
        //  folder will be searched for the actual folder[index], but also what
        //  the repeat wildcard is, so c:/*:.*/.*\\.txt will match a .txt in
        //  c:/a/x.txt, but this repeat will also repeat *:.* under a, so will
        //  also match c:/a/b/y.txt, etc.. Also, MAX_VALUE means infinity, so
        //  don't decrement in that case
        if(repeat > 0 && repeat < Integer.MAX_VALUE)
            repeat--;

        // Get a list of files under the current path
        file = new File(base);
        results = file.list();

        // Parse out the repeat information, if any
        colon = folders[index].indexOf(':');
        localRepeat = 0;
        if(colon != -1)
            try
            {
                // Repeat can be N: (where N is a number) or *: - this will not
                //  match c:/ as c isn't a number or an '*'
                String parseRepeat = folders[index].substring(0, colon);
                if(parseRepeat.equals("*") == true)
                    localRepeat = Integer.MAX_VALUE;
                else
                    localRepeat = Integer.parseInt(parseRepeat);
                // Update the folder so it no longer includes the X: part
                folders[index] = folders[index].substring(colon + 1);
            }
            catch(NumberFormatException e) { }

        // First check against the current wildcard
        if(results != null)
            // If still looking in folders..
            if(index < folders.length - 1)
                for(String result : results)
                {   if(result.matches(folders[index]) == true)
                        // Recursively search sub-folders if this folder matched
                        //  the wildcard
                        fileList
                        (   base,
                            result,
                            folders,
                            index,
                            files,
                            // if no repeat is set up, localRepeat will be 0 so
                            //  passing this repeat information will not
                            //  actually repeat if it's not set up to do so
                            folders[index],
                            localRepeat
                        );
                }
            else
            // If reached the last part of the search string, then these are the
            //  files being searched for, so store the matches
                for(String result : results)
                    if(result.matches(folders[index]) == true)
                        files.add(base + result);

        // Next test all the results against the repeat wildcard, if any
        if(results != null
        && repeat > 0)
            for(String result : results)
                if(result.matches(wildcard) == true)
                    // Recursively search sub-folders if this folder matched the
                    //  repeat wildcard
                    fileList
                    (   base,
                        result,
                        folders,
                        // As this is a repeat, bring the index back so the sub-
                        //  dir search uses the same folder as currently used -
                        //  this -1 will propagate all the way up the recursion
                        //  causing all sub-levels, no matter how deep, to reuse
                        //  the same index
                        index - 1,
                        files,
                        wildcard,
                        repeat
                    );

    }

}
