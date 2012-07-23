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
 * InitializationException.java
 *
 * Created on 12 October 2006, 15:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geva.Exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Thrown when the initialization of an Individual is exceptional
 * @author EHemberg
 */
public class InitializationException extends Exception {
	private Log logger = LogFactory.getLog(InitializationException.class);
    
    /** Creates a new instance of InitializationException */
    public InitializationException() {
   	 logger.error("InitializationException");
    }
    
    /**
     * Constructor
     * @param s Message
     */
    public InitializationException(String s) {
   	 logger.error("InitializationException:"+s);
    }
    
}
