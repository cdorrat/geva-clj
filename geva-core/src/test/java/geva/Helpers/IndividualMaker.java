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

package geva.Helpers;

import geva.Individuals.GEIndividual;
import geva.Individuals.Genotype;
import geva.Individuals.Phenotype;
import geva.Individuals.FitnessPackage.BasicFitness;
import geva.Individuals.FitnessPackage.Fitness;
import geva.Mapper.GEGrammar;
import geva.Mapper.Symbol;
import geva.Util.Enums;

import java.util.Properties;

public class IndividualMaker {

    private IndividualMaker() {
    }

    public static GEIndividual makeIndividual() {
        Properties p = GrammarCreator.getProperties();
        GEGrammar geg = GrammarCreator.getGEGrammar(p);
        Phenotype phen = new Phenotype();
        Genotype geno = new Genotype();
        geno.add(GrammarCreator.getGEChromosome());
        Fitness f = new BasicFitness();
        GEIndividual gei = new GEIndividual(geg, phen, geno, f);
        return gei;
    }

    public static GEIndividual makeIndividual(Properties p) {
        GEGrammar geg = GrammarCreator.getGEGrammar(p);
        Phenotype phen = new Phenotype();
        Genotype geno = new Genotype();
        geno.add(GrammarCreator.getGEChromosome());
        Fitness f = new BasicFitness();
        GEIndividual gei = new GEIndividual(geg, phen, geno, f);
        return gei;
    }

    /**
     * Pass in string, split on whitspace. Create terminal symbol.
     * @param s String to be parsed to Phenotypes
     * @return
     */
    public static Phenotype getPhenotype(String s) {
        Phenotype p = new Phenotype();
        String[] splits = s.split("\\s+");
        for (String ss : splits) {
            if(!ss.equals("")) {
                p.add(new Symbol(ss, Enums.SymbolType.TSymbol));
            }
        }
        return p;
    }
}

