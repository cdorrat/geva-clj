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
 * Operation.java
 *
 * Created on 08 March 2007, 02:34
 *
 */

package geva.Operator.Operations;


import geva.Individuals.Individual;
import geva.Parameter.ParameterI;

import java.util.List;

/**
 * Operation performs actions on a List<Individual> or a single Individual 
 * @author Conor
 */
public interface Operation extends ParameterI {

    /**
     * Performs the operation on the list passed
     * @param operands operands to be operated on
     */
    public void doOperation(List<Individual> operands);

    /**
     * Performs the operation on an operand
     * @param operand operand to perform operation on
     */
    public void doOperation(Individual operand);    
}
