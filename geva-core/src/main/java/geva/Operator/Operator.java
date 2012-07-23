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
 * geva.Operator.java
 *
 * Created on October 26, 2006, 12:06 PM
 *
 */

package geva.Operator;

import geva.Operator.Operations.Operation;

/**
 * Interface for geva.Operator. The Opertor can get and set an operation.
 * @author erikhemberg
 */
public interface Operator extends Module {

    /**
     * Set operation that operator performs
     * @param op operation
     */
    public void setOperation(Operation op);

    /**
     * Get operation that operator performs
     * @return operation
     */
    public Operation getOperation();
}
