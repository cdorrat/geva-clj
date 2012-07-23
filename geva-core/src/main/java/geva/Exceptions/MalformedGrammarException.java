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

/*
 * MalformedGrammarException.java
 *
 * Created as ThisGrammarBeBadException.java on 12 October 2006, 15:26
 * Renamed to MalformedGrammarException.java 19 November 2008
 *
 */

package geva.Exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Thrown if the Grammar file fails to parse.
 * @author EHemberg
 */
public class MalformedGrammarException extends Exception {
	private Log logger = LogFactory.getLog(MalformedGrammarException.class);
	
    /** Creates a new instance of MalformedGrammarException */
    public MalformedGrammarException() {
   	 logger.error("Malformed Grammar.");
    }
   
    /**
     * Creates a new instance of MalformedGrammarException
     * @param s parameter
     */
    public MalformedGrammarException(String s) {
   	 logger.error("Malformed Grammar: " + s);
    }
    
}
