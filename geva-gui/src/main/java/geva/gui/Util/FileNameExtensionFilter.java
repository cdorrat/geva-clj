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

package geva.gui.Util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Implementation for 1.5 compatibility of the 1.6 Java class of the same name,
 *  which I didn't realise at the time of using, wasn't part of a 1.5
 * @author eliott bartley
 */
public class FileNameExtensionFilter extends FileFilter
{

    private String description;
    private String[] extensions;

    public FileNameExtensionFilter(String description, String... extensions)
    {   this.description = description;
        this.extensions = GEVAHelper.trim(extensions);
        undotExtensions();
    }

    /**
     * Remove the dot at the start of the extension name, if any,
     *  .txt becomes txt
     */
    private void undotExtensions()
    {   for(int i = 0; i < extensions.length; i++)
            if(extensions[i].length() > 1 && extensions[i].charAt(0) == '.')
                extensions[i] = extensions[i].substring(1);
    }

    @Override
    public boolean accept(File f)
    {   if(extensions == null)
            return true;
        if(f.isDirectory() == true)
            return true;
        String fileExtension = f.getName();
        int dot = fileExtension.lastIndexOf('.') + 1;
        if(dot == 0)
            return false;
        fileExtension = fileExtension.substring(dot);
        for(String extension : extensions)
            if(extension.length() == 0
            || extension.equals(fileExtension) == true)
                return true;
        return false;
    }

    @Override
    public String getDescription()
    {   StringBuilder extensionList = null;
        if(extensions != null)
            for(String extension : extensions)
                if(extension.trim().length() != 0)
                    if(extensionList == null)
                    {   extensionList = new StringBuilder();
                        extensionList.append(extension);
                    }
                    else
                        extensionList.append("|" + extension);
        if(extensionList != null)
        {   extensionList.insert(0, " (");
            extensionList.append(')');
            return description + extensionList.toString();
        }
        return description;
    }

    public String[] getExtensions()
    {   return extensions;
    }

    @Override
    public String toString()
    {   if(extensions != null)
            return extensions[0];
        return null;
    }

}
