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

/**
 * Mock class to test Grammar
 **/
package geva.Mapper;

import geva.Mapper.Grammar;

public class GrammarMock extends Grammar {

    public GrammarMock() {
    }

    public GrammarMock(Grammar g) {
        super(g);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean genotype2Phenotype() {
        return true;
    }

    public Object getGenotype() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getPhenotype() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean phenotype2Genotype() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setGenotype(Object g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPhenotype(Object p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public String getDerivationString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
