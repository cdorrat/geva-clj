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
  * ContextFreeGrammar.java
  *
  * Created on 11 October 2006, 18:01
  *
  */

package geva.Mapper;


import geva.Exceptions.MalformedGrammarException;
import geva.Main.State;
import geva.Util.Constants;
import geva.Util.Enums;
import geva.Util.Structures.Queue;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Abstract ContextFreeGrammar
 * @author EHemberg
 */
public abstract class ContextFreeGrammar extends Grammar {

	private static Log logger = LogFactory.getLog(ContextFreeGrammar.class);
    public abstract boolean genotype2Phenotype();
    public abstract boolean phenotype2Genotype();
    public abstract int getUsedCodons();
    public abstract int getUsedWraps();

    /** Creates a new instance of ContextFreeGrammar */
    ContextFreeGrammar() {
        super();
    }

    /** Copy constructor
     *  @param g grammar to copy
     */
    ContextFreeGrammar(ContextFreeGrammar g) {
        super(g);
    }

    /**Read a BNF file to a string and call readBNFString to parse the grammar
     * string. Find file from the class loader
     * @param file_name name of file
     * @return parse success
     */
    @SuppressWarnings({"UnusedReturnValue", "IOResourceOpenedButNotSafelyClosed"})
    public boolean readBNFFile(String file_name) throws MalformedGrammarException {
	StringBuffer contents = new StringBuffer();
        try {
            int bufferSize = 1024;
            String line;
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            InputStream is = loader.getResourceAsStream(file_name);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr, bufferSize);
	    assert (br != null) : "Cannot load resource from classloader: "+file_name;
            while( (line = br.readLine()) != null ) {
                contents.append(line);
                //readLine removes the lineseparator from http://www.javapractices.com/Topic42.cjp
                contents.append(System.getProperty("line.separator"));
            }
            br.close();
        } catch(FileNotFoundException e) {
            System.err.println("Grammar File not found: "+file_name);
            return false;
       } catch(NullPointerException e) {
            System.err.println("Grammar File not found in classloader: "+file_name);
            return false;
        } catch(IOException e) {
            System.err.println("IOException when looking for grammar file: "+file_name);
            return false;
        }
	contents.append("\n");
	return readBNFString(contents.toString());
    }

    /**Read a BNF file to a string and call readBNFString to parse the grammar
     * string.
     * @param file_name name of file to load
     * @return success of parsing the file
     */
    @SuppressWarnings({"UnusedReturnValue", "IOResourceOpenedButNotSafelyClosed"})
    boolean readBNFFileFromFilesystem(String file_name) throws MalformedGrammarException {
	StringBuffer contents = new StringBuffer();
        try {
            final int bufferSize = 1024;
            String line;
            File f = new File(file_name);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr, bufferSize);
            while( (line = br.readLine()) != null ) {
                contents.append(line);
                //readLine removes the lineseparator from http://www.javapractices.com/Topic42.cjp
                contents.append(System.getProperty("line.separator"));
            }
            br.close();
        } catch(FileNotFoundException e) {
            System.err.println("Grammar File not found: "+file_name);
            return false;
        } catch(IOException e) {
            System.err.println("IOException when opening grammar file: "+file_name);
            return false;
        }
	contents.append("\n");
	return readBNFString(contents.toString());
    }


    /**
     * Reads in the BNF grammar specified by its argument text.
     * Returns true if loading of grammar was successful, false otherwise.
     * @param bnfString grammar as a string
     * @return if parsing was correct
     */
    @SuppressWarnings({"ConstantConditions"})
    boolean readBNFString(String bnfString) throws MalformedGrammarException {
	if(bnfString == null) {
	    return false;
	}
        Rule newRule = new Rule(); // Used to create new rules for grammar
        boolean insertRule = false;// If newRule is to be inserted onto grammar
        Rule currentRule = null;// Used in pass 2 to add productions to current rule
        Production newProduction = new Production();// Used to create new productions for grammar
        Symbol newSymbol = new Symbol();// Used to create new symbols for grammar
        String symbolString;
        Symbol newTokenSeparator = new Symbol();// Used to create token separators for grammar
        int bnfString_size = bnfString.length();
        char currentChar;// Current char of input
        char separated = 0;// If there was a separator between previous token and current one
        boolean skip = false;// Skip an iteration on parser (for escaped newlines)
        boolean quoted = false;// If current char is quoted
        boolean non_terminal = false;// If current text is a non-terminal symbol
        StringBuffer currentBuffer = new StringBuffer(bnfString_size);// Buffer used to add new symbols to grammar
        // States of parser
        final int START = 0;
        final int START_RULE = 1;
        final int LHS_READ = 2;
        final int PRODUCTION = 3;
        final int START_OF_LINE = 4;
        int state=START;// Current state of parser

        int i ;
        try {
            for(int pass=0; pass<2; pass++){ //Do 2 passes over the string
                i = 0;
                while(i < bnfString_size) {
                    if(i < bnfString_size) {
                        currentChar = bnfString.charAt(i);
                    } else { // Simulate presence of endl at end of grammar
                        currentChar='\n';
                    }
                    if(bnfString.charAt(i) == '\\') { // Escape sequence
                        i++;
                        if(i>=bnfString_size){// Escape sequence as last char is invalid
                            throw new MalformedGrammarException("Escape sequence as last char is invalid");
                        } else {
                            if((non_terminal) && (bnfString.charAt(i) != '\n')){
                                // Only escaped newline allowed inside non-terminal
                                throw new MalformedGrammarException("Only escaped newline allowed inside non-terminal");
                            }
                        }
                        if(bnfString.charAt(i)=='\''){// Single quote
                            currentChar='\'';
                        } else if(bnfString.charAt(i)=='\''){// Double quote
                            currentChar='\'';
                        } else if(bnfString.charAt(i)=='\\'){// Backslash
                            currentChar='\\';
                        } else if(bnfString.charAt(i)=='0'){// Null character
                            currentChar='\0';
                        } else if(bnfString.charAt(i)=='a'){// Audible bell
                            currentChar='\007';
                        } else if(bnfString.charAt(i)=='b'){// Backspace
                            currentChar='\b';
                        } else if(bnfString.charAt(i)=='f'){// Formfeed
                            currentChar='\f';
                        } else if(bnfString.charAt(i)=='n'){// Newline
                            currentChar='\n';
                        } else if(bnfString.charAt(i)=='r'){// Carriage return
                            currentChar='\r';
                        } else if(bnfString.charAt(i)=='t'){// Horizontal tab
                            currentChar='\t';
                        } else if(bnfString.charAt(i)=='v'){// Vertical tab
                            currentChar='\013';
                        } else if(bnfString.charAt(i)=='\n'){// Escaped newline
                            skip=true;// Ignore newline
                        } else if(bnfString.charAt(i)=='\r'){// Escaped DOS return
                            skip=true;// Ignore newline
                            if(bnfString.charAt(++i) != '\n'){
                                throw new MalformedGrammarException("No newlinwe");
                            }
                        } else{// Normal character
                            currentChar=bnfString.charAt(i);
                        }
                        if((!skip) && (pass > 0)){
                            if(currentBuffer.length() == 0){//Empty
                                newSymbol = new Symbol();
                                newSymbol.setType(Enums.SymbolType.TSymbol);
                            }
                            currentBuffer.append(currentChar);
                        }
                    } else {
                        switch(state){
                            case(START):
                                if(currentChar=='\r'){
                                    break;// Ignore DOS newline first char
                                }
				if(currentChar=='#'){
				    // this line is a comment in the grammar so skip to end of line
				    while(i < bnfString_size && bnfString.charAt(i) != '\n') {
					//System.out.println("charAt:" + bnfString.charAt(i));
					i++;
				    }
				    // we have skipped to end of line, so exit the switch
				    // next time round, it will see the "\n" (or "\r\n") at end of line
				    break;
				}
                                switch(currentChar){
                                    case ' ':// Ignore whitespaces
                                    case '\t':// Ignore tabs
                                    case '\n':// Ignore newlines
                                        break;
                                    case '<':// START OF RULE
                                        newSymbol = new Symbol();
                                        newSymbol.setType(Enums.SymbolType.NTSymbol);
                                        currentBuffer.append(currentChar);
                                        state = START_RULE;
                                        break;
                                    default: // Illegal
                                        throw new MalformedGrammarException("Illegal character `" + currentChar + "' found at start of grammar");
                                }
                                break;
                            case(START_RULE):// Read the lhs Non-terminal symbol
                                if(currentChar=='\r'){
                                    break;// Ignore DOS newline first char
                                }
                                switch(currentChar){
                                    case '\n':// Newlines are illegal here
                                        throw new MalformedGrammarException("Misplaced newline");
                                    case '>': // Possible end of non-terminal symbol
                                        currentBuffer.append(currentChar);
                                        symbolString = currentBuffer.toString();
                                        if(pass==0){// First pass
                                            // Check if new symbol definition
                                            if(findRule(newSymbol) == null){// Create new rule for symbol
                                                insertRule=true;//We will add the newRule to Grammar.Rules
                                                newRule.setLHS(new Symbol(symbolString, Enums.SymbolType.NTSymbol));
                                            } else {
                                                insertRule=true;//We will not add a rule this time
                                            }
                                        } else {
                                            // Second pass
                                            // Point currentRule to previously defined rule
                                            currentRule = findRule(symbolString);
                                            if(currentRule == null){
                                                throw new MalformedGrammarException("Current rule is null: " + symbolString);
                                            }
                                        }
                                        currentBuffer.delete(0,currentBuffer.length());// Reset the buffer
                                        state=LHS_READ;// lhs for this rule has been read
                                        break;
                                    default:// Check for non-escaped special characters
                                        if(((currentChar=='"')||(currentChar=='|')||(currentChar=='<'))){
                                            throw new MalformedGrammarException("Non escaped special character");
                                        }
                                        currentBuffer.append(currentChar);
                                }
                                break;
                            case(LHS_READ):// Must read ::= token
                                if(currentChar=='\r'){
                                    break;// Ignore DOS newline first char
                                }
                                switch(currentChar){
                                    case ' ':// Ignore whitespaces
                                    case '\t':// Ignore tabs
                                    case '\n':// Ignore newlines
                                        break;
                                    case ':':// Part of ::= token
                                        currentBuffer.append(currentChar);
                                        break;
                                    case '=':// Should be end of ::= token
                                        currentBuffer.append(currentChar);
                                        String s = currentBuffer.toString();
                                        if( s.compareTo("::=")!=0){// Something other than ::= was read
                                            throw new MalformedGrammarException("Something other than ::= was read");
                                        }
                                        currentBuffer.delete(0,currentBuffer.length());
                                        // START OF PRODUCTION
                                        newProduction.clear();
                                        state = PRODUCTION;
                                        break;
                                    default: // Illegal
                                        throw new MalformedGrammarException("Illegal character `" + currentChar + "' found in ::= token");
                                }
                                break;
                            case(PRODUCTION):// Read everything until | token or \n, or EOL
                                if(currentChar=='\r'){
                                    break;// Ignore DOS newline first char
                                }
                                if(pass == 0){
                                    if (currentChar=='\n') {
                                        state=START_OF_LINE;
                                    }
                                    break;
                                } else {
                                    switch(currentChar){
                                        case '|':// Possible end of production
                                            if(quoted){// Normal character
                                                currentBuffer.append(currentChar);
                                                break;
                                            }
                                        case '\n':// End of production (and possibly rule)
                                            separated=0;// Reset separator marker
                                            if((currentBuffer.length() != 0)||(newProduction.size() == 0)){// There is a symbol to add
                                                if(currentBuffer.length() == 0){
                                                    // No symbol exists; create terminal empty symbol
                                                    newSymbol.setType(Enums.SymbolType.TSymbol);
                                                }
                                                if(non_terminal){// Current non-terminal symbol isn't finished
                                                    throw new MalformedGrammarException("Current non-terminal symbol isn't finished");
                                                }
                                                symbolString = currentBuffer.toString();
                                                newSymbol.setSymbolString(symbolString);
                                                if(newSymbol.getType() == Enums.SymbolType.NTSymbol){
                                                    // Find rule that defines this symbol
                                                    Rule tempRule = findRule(newSymbol);
                                                    if(tempRule != null){
                                                        newProduction.add(new Symbol(newSymbol));
                                                    } else{// Undefined symbol, insert anyway
                                                        newProduction.add(new Symbol(newSymbol));
                                                    }
                                                } else {// Add terminal symbol
                                                    newProduction.add(new Symbol(newSymbol));
                                                }
                                                newSymbol.clear();// Reset the symbol
                                            }
                                            // END OF PRODUCTION
                                            // Add production to current rule
                                            currentRule.add(new Production(newProduction));
                                            currentBuffer.delete(0,currentBuffer.length());// Reset the buffer
                                            if(currentChar=='\n')
                                                state=START_OF_LINE;
                                            else{
                                                // START OF PRODUCTION
                                                newProduction.clear();
                                            }
                                            break;
                                        case '<':// Possible start of non-terminal symbol
                                        case '>':// Possible end of non-terminal symbol
                                        case ' ':// Possible token separator
                                        case '\t':// Possible token separator
                                            if((quoted) || (((currentChar==' ') || (currentChar=='\t')) && (non_terminal))){// Spaces inside non-terminals are accepted
                                                currentBuffer.append(currentChar);
                                                if(!non_terminal) {
                                                    newSymbol.setType(Enums.SymbolType.TSymbol);
                                                }
                                                break;
                                            }
                                            if(currentChar=='>'){// This is also the end of a non-terminal symbol
                                                currentBuffer.append(currentChar);
                                                non_terminal = false;
                                            }
                                            if(currentBuffer.length() != 0){
                                                if(non_terminal){// Current non-terminal symbol isn't finished
                                                    throw new MalformedGrammarException("Current non-terminal symbol isn't finished");
                                                }
                                                if((currentChar==' ')||(currentChar=='\t')){// Token separator
                                                    separated = 1;
                                                }
                                                symbolString = currentBuffer.toString();
                                                newSymbol.setSymbolString(symbolString);
                                                if(newSymbol.getType()==Enums.SymbolType.NTSymbol){
                                                    // Find rule that defines this symbol
                                                    Rule tempRule = findRule(newSymbol);
                                                    if(tempRule != null){
                                                        newProduction.add(new Symbol(newSymbol));
                                                    } else{
                                                        // Undefined symbol, insert anyway
                                                        newProduction.add(new Symbol(newSymbol));
                                                    }
                                                } else{// Add terminal symbol
                                                    newProduction.add(new Symbol(newSymbol));
                                                }
                                                newSymbol.clear();// Reset the symbol
                                            } else{// Empty buffer
                                                if(((currentChar==' ') || (currentChar=='\t')) && (newProduction.size() != 0)){
                                                    // Probably a token separator after a non-terminal symbol
                                                    separated=1;
                                                }
                                            }
                                            currentBuffer.delete(0,currentBuffer.length());// Reset the buffer
                                            if(currentChar == '<'){// This is also the start of a non-terminal symbol
                                                // Special case; must create new Symbol here
                                                newSymbol.clear();
                                                newSymbol.setType(Enums.SymbolType.NTSymbol);
                                                currentBuffer.append(currentChar);
                                                non_terminal = true;// Now reading a non-terminal symbol
                                                if(separated == '1'){// Insert a token separator
                                                    separated=0;
                                                    newTokenSeparator.clear();
                                                    newTokenSeparator.setSymbolString(" ");
                                                    newTokenSeparator.setType(Enums.SymbolType.TSymbol);
                                                    newProduction.add(new Symbol(newTokenSeparator));
                                                }
                                            }
                                            break;
                                        default: // Add character to current buffer
                                            if(separated == '1'){// Insert a token separator
                                                separated=0;
                                                newTokenSeparator.clear();
                                                newTokenSeparator.setSymbolString(" ");
                                                newTokenSeparator.setType(Enums.SymbolType.TSymbol);
                                                newProduction.add(new Symbol(newTokenSeparator));
                                            }
                                            if(currentChar=='"'){// Start (or end) quoted section
                                                quoted = !quoted;
                                                newSymbol.setType(Enums.SymbolType.TSymbol);
                                                break;
                                            }
                                            if(currentBuffer.length() == 0){
                                                newSymbol.setType(Enums.SymbolType.TSymbol);
                                            }
                                            currentBuffer.append(currentChar);
                                    }
                                    break;

                                }
                            case(START_OF_LINE):
				if(currentChar=='#'){
				    // this line is a comment in the grammar so skip to end of line
				    while(i < bnfString_size && bnfString.charAt(i) != '\n') {
					//System.out.println("charAt:" + bnfString.charAt(i));
					i++;
				    }
				    // we have skipped to end of line, so exit the switch
				    // next time round, it will see the "\n" (or "\r\n") at end of line
				    break;
				}
                                if(currentChar=='\r'){
                                    break;// Ignore DOS newline first char
                                }
                                switch(currentChar){
                                    case ' ':// Ignore whitespaces
                                    case '\t':// Ignore tabs
                                    case '\n':// Ignore newlines
                                        break;
                                    case '|':// Start of new production
                                        state=PRODUCTION;
                                        if(pass==1){
                                            // START OF PRODUCTION
                                            newProduction.clear();
                                        }
                                        break;
                                    case '<':// Start of lhs non-terminal symbol
                                        // END OF RULE
                                        if(pass==0){
                                            // Add current rule
                                            if(insertRule){
                                                rules.add(new Rule(newRule));
                                            }
                                        }
                                        // START OF RULE
                                        newSymbol.setType(Enums.SymbolType.NTSymbol);
                                        currentBuffer.append(currentChar);
                                        state=START_RULE;
                                        break;
                                    default: // Illegal
                                        throw new MalformedGrammarException("Illegal character `" + currentChar + "' found at start of line");
                                }
                                break;
                            default://Impossible error, quit the program now!
                                throw new MalformedGrammarException("Impossible error, quit the program now!");
                        }
                    }
                    skip=false;
                    i++;
                }
                // END OF PASS
                if(state!=START_OF_LINE){// This must be the state of the parser
                    throw new MalformedGrammarException("START_OF_LINE must be the state of the parser");
                }
                if(pass==0){
                    // Add current rule
                    if(insertRule){
                        this.rules.add(new Rule(newRule));
                    }
                }
            }
            checkInfiniteRecursion();
        } catch (MalformedGrammarException ex) {
            this.setValidGrammar(false);
            logger.error("Exception parsing grammar", ex);
            throw ex;
        }
        updateRuleFields();
        setValidGrammar(true);
        genotype2Phenotype();
        return true;
    }

    /**
     * Find a rule given a symbol. Null return indicates no rule found
     * @param s symbol of left hand side of rule
     * @return found rule or null if no rule found
     */
    public Rule findRule(Symbol s){
	assert (s != null) : "Symbol in findRule is:" + s;
        Iterator<Rule> iter = rules.iterator();
        Rule r;
        while(iter.hasNext()) {
            r = iter.next();
            if(r.getLHS().equals(s)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Find a rule given a string. Null return indicates no rule found
     * @param s string of left hand side of rule
     * @return found rule or null if no rule found
     */
    public Rule findRule(String s){
        Iterator<Rule> iter = rules.iterator();
        Rule r;
        while(iter.hasNext()) {
            r = iter.next();
            if(r.getLHS().equals(s)) {
                return r;
            }
        }
        return null;
    }

    /** Update recursive and minimumDepth fields for every Rule
     * and Production in grammar.*/
    void updateRuleFields(){
        ArrayList<Rule> visitedRules = new ArrayList<Rule>();
        Iterator<Rule> ruleIt = rules.iterator();
        Rule r;
        clearRuleFields();
        // Go through each rule in the grammar
        while(ruleIt.hasNext()){
            r = ruleIt.next();
            visitedRules.clear();
            r.setRecursive(isRecursive(visitedRules,r));
        }

        ruleIt = rules.iterator();
        // Second pass calculating minimumDepth
        while(ruleIt.hasNext()){
            r = ruleIt.next();
            visitedRules.clear();
            //System.out.println("Start:"+r.getLHS().getSymbolString());
            calculateMinimumDepthRecursive(r, visitedRules);
            //System.out.println(r.getLHS().getSymbolString()+" minDepth:"+r.getMinimumDepth() +" isRecursive:"+r.getRecursive());
        }

        ruleIt = rules.iterator();
        // setting the minimum depth of the productions
        while(ruleIt.hasNext()){
            r = ruleIt.next();
            visitedRules.clear();
            //System.out.println("Start:"+r.getLHS().getSymbolString());
            setProductionMinimumDepth(r);
        }

    }

    /**
     * Set the minimum depth for productions of the rule to consist of only terminials
     * @param r rule investigated
     */
    void setProductionMinimumDepth(Rule r) {
        Iterator<Production> pIt;
        Iterator<Symbol> sIt;
        Symbol sym;
        Production prod;
        Rule rule;
        int minDepth;
        pIt = r.iterator();
        while(pIt.hasNext()) {
            minDepth = 0;
            prod = pIt.next();
            sIt = prod.iterator();
            while(sIt.hasNext()) {
                sym = sIt.next();
                if(sym.getType() == Enums.SymbolType.NTSymbol) {
                    rule = this.findRule(sym);
                    if(rule!=null) {
                        if(rule.getMinimumDepth() > minDepth) {
                            minDepth = rule.getMinimumDepth();
                        }
                    }
                }
            }
            prod.setMinimumDepth(minDepth);
        }
    }
    /** Update recursive and minimumDepth fields for every Rule
     * and Production in grammar.*/
    void clearRuleFields(){
        Iterator<Rule> ruleIt = rules.iterator();
        Rule r;
        // Reset minimum depths and recursion fields
        while(ruleIt.hasNext()){
            r = ruleIt.next();
            r.setMinimumDepth(Integer.MAX_VALUE>>1);
            r.setRecursive(false);
        }
    }

    /**
     * Recursively calculates the minimum depth of a rule
     * @param startRule the current rule investigated
     * @param visitedRules visited rules
     */
    void calculateMinimumDepthRecursive(Rule startRule, ArrayList<Rule> visitedRules) {
        Symbol tempSymbol;
        Production tempProd;
        Rule currentRule;
        Iterator<Symbol> symbIt;
        Iterator<Production> prodIt;

        if(!visitedRules.contains(startRule)) {
            // Loop through the startRule
            prodIt = startRule.iterator();
            while(prodIt.hasNext()) {
                tempProd = prodIt.next();
                tempProd.setMinimumDepth(0);
                symbIt = tempProd.iterator();
                while(symbIt.hasNext()) {
                    tempSymbol = symbIt.next();
                    if(tempSymbol.getType() == Enums.SymbolType.NTSymbol) {
                        currentRule = findRule(tempSymbol);
                        if(currentRule != null) {
                            visitedRules.add(startRule);
                            calculateMinimumDepthRecursive(currentRule, visitedRules);
                            if(tempProd.getMinimumDepth() < (currentRule.getMinimumDepth() + 1)) {
                                tempProd.setMinimumDepth(currentRule.getMinimumDepth() + 1);
                            }
                        }
                    } else {
                        if(tempProd.getMinimumDepth()<1) {
                            tempProd.setMinimumDepth(1);
                        }
                    }
                }
                if(startRule.getMinimumDepth() > tempProd.getMinimumDepth()) {
                    startRule.setMinimumDepth(tempProd.getMinimumDepth());
                    //System.out.println("-Setting:"+startRule.getLHS().getSymbolString()+" d:"+startRule.getMinimumDepth());
                }
            }
        }
    }

    /**
    * This method returns a vector of the rules with exclusively terminal productions
    * @return Vector
    */
    public ArrayList getTerminalRules()
    {
        ArrayList terminalRules = new ArrayList();
        for(Rule r : this.getRules())
            {
                boolean terminal = true;
                for(int i = 0; i< r.size();i++)
                {
                    if(r.get(i).getNTSymbols()>0)
                    {
                        terminal = false;
                    } 
                }
                if(terminal)
                {
                  terminalRules.add(r.getLHS());
                }
            }
        return terminalRules;
    }
    
    /**
    * This method returns a vector of the rules with exclusively terminal productions
    * @return Vector
    */
    public ArrayList getNonTerminalRules()
    {
        ArrayList nonTerminalRules = new ArrayList();
        for(Rule r : this.getRules())
            {
                boolean terminal = true;
                for(int i = 0; i< r.size();i++)
                {
                    if(r.get(i).getNTSymbols()>0)
                    {
                        terminal = false;
                    } 
                }
                if(!terminal)
                {
                  nonTerminalRules.add(r.getLHS());
                }
            }
        return nonTerminalRules;
    }    
       
    
    /**
     * Checks for infinite recursion in the grammar.
     * @throws MalformedGrammarException The rule is infinitely recursive
     **/
    private void checkInfiniteRecursion() throws MalformedGrammarException{
        Iterator<Rule> ruleIt = rules.iterator();
        Rule r;
        while(ruleIt.hasNext()) {
            r = ruleIt.next();
            if(isInfinitelyRecursive(r)) {
                throw new MalformedGrammarException("Infinite recursion: "+r);
            }
        }
    }

    /**
     * Check if a rule is infinitely recursive
     * @param startRule rule to investigate
     * @return if the rule is infinitely recursive
     */
    private boolean isInfinitelyRecursive(Rule startRule) throws MalformedGrammarException {
        Queue<Rule> rulesToVisit = new Queue<Rule>();
        ArrayList<Rule> visitedRules = new ArrayList<Rule>();

        Iterator<Symbol> symbIt;
        Iterator<Production> prodIt;
        Symbol tempSymbol;
        Rule currentRule  ;

        rulesToVisit.enqueue(startRule);
        while(!rulesToVisit.isEmpty()) {
            currentRule = rulesToVisit.dequeue();
            prodIt = currentRule.iterator();
            visitedRules.add(currentRule);
            while(prodIt.hasNext()) {
                symbIt = prodIt.next().iterator();
                while(symbIt.hasNext()) {
                    tempSymbol = symbIt.next();
                    if(tempSymbol.getType() == Enums.SymbolType.NTSymbol 
		       && ! tempSymbol.getSymbolString().startsWith(Constants.GE_CODON_VALUE_PARSING)) {
                        currentRule = this.findRule(tempSymbol);
                        if (currentRule == null) {
                            throw new MalformedGrammarException("No rule found for symbol " + tempSymbol.toString());
                        }
                        if(!visitedRules.contains(currentRule)) {
                            rulesToVisit.enqueue(currentRule);
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /** Returns the calculated recursive nature of the Rule passed as argument,
     * and updates its minimum mapping depth (minimumDepth)
     * @param visitedRules list of visited rules
     * @param currentRule current rule visited
     * @return if the rule is recursive
     */
    boolean isRecursive(ArrayList<Rule> visitedRules, Rule currentRule){
        Production tempProd;
        Iterator<Symbol> symbIt;
        Iterator<Production> prodIt;
        Symbol tempSymbol;
        Rule definingRule  ;

        if(visitedRules.size() == 0) {
            prodIt = currentRule.iterator();
        } else {
            prodIt = visitedRules.get(visitedRules.size()-1).iterator();
        }

        // Check if this is a recursive call to a previously visited rule
        if(visitedRules.contains(findRule(currentRule.getLHS()))){
            currentRule.setRecursive(true);
            return true;
        }

        // Go through each production in the rule
        while(prodIt.hasNext()){
            tempProd = prodIt.next();
            symbIt = tempProd.iterator();
            while(symbIt.hasNext()) {
                tempSymbol = symbIt.next();
                if(tempSymbol.getType() == Enums.SymbolType.NTSymbol) {
                    definingRule = findRule(tempSymbol);
                    if(definingRule != null) {
                        if(!visitedRules.contains(definingRule)) {
                            visitedRules.add(definingRule);
                            if(isRecursive(visitedRules, currentRule)) {
                                tempProd.setRecursive(true);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}







