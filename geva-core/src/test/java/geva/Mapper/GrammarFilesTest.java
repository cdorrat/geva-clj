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

package geva.Mapper;

import geva.Exceptions.MalformedGrammarException;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test all grammar files in the param/Grammar directory
 * @author erikhemberg
 */
public class GrammarFilesTest extends ContextFreeGrammarTest {

    public GrammarFilesTest() {
        super();
    }

    /**
     * Test the Grammar Files in the param/Grammar directory
     **/
    @Test
    public void testGrammarDirectory() throws MalformedGrammarException {
        System.out.println("* GrammarFilesTest: testGrammarDirectory");
        String dirName = System.getProperty("user.dir") + File.separator + ".." + File.separator + "param" + File.separator + "Grammar";
        File dir = new File(dirName);
        this.visitDirs(dir);
    }

    private void visitDirs(File file) throws MalformedGrammarException {
        System.out.println("+" + file.getName());
        if (file.isDirectory()) {
            String[] children = file.list();
            for (String c : children) {
                File f = new File(c);
                visitDirs(f);
            }
        } else {
            if (file.exists()) {
                System.out.println("-" + file.getName());
                if (file.getName().endsWith("bnf")) {
                    super.file_name = file.getName();
                    testReadBNFString();
                }
            }
        }
    }

    /**
     * Test of readBNFString method, of class ContextFreeGrammar.
     * @param file name
     */
    @Test
    @Override
    public void testReadBNFString() throws MalformedGrammarException {
        System.out.println("* GrammarFilesTest: readBNFString");
        System.out.println("* GrammarFilesTest: " + super.file_name);
        ContextFreeGrammarMock instance = new ContextFreeGrammarMock();
        String bnfString = instance.readBNFFileToString(super.file_name);
        boolean result = instance.readBNFString(bnfString);
        assertEquals(true, result);
    }
}