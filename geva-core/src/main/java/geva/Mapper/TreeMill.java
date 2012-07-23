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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geva.Mapper;

import geva.Util.Constants;

/**
 *
 * @author erikhemberg
 */
public class TreeMill {

    public static DerivationTree getDerivationTree(final GEGrammar gegrammar) {
        final DerivationTree derivationTree;
        if(gegrammar.getDerivationString() == null) {
            throw new NullPointerException("GEGrammar.derivationTreeType is null. Use key -"+Constants.DERIVATION_TREE+" and value is the class name");
        }
        if(gegrammar.getDerivationString().equals(DerivationTree.class.getName())) {
            derivationTree = new DerivationTree(gegrammar, gegrammar.getGenotype());
        } 
        else if (gegrammar.getDerivationString().equals(ContextualDerivationTree.class.getName()))
        {
           derivationTree = new ContextualDerivationTree(gegrammar, gegrammar.getGenotype());
        }
        else
        {
           derivationTree = null;
           throw new IllegalArgumentException("Illegal derivation tree:"+gegrammar.getDerivationString());
        }
        
        return derivationTree;
    }

}
