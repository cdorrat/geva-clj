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

package geva.gui.Fractal;

import java.io.IOException;
import java.io.StringReader;
import java.util.Stack;

/**
 * LSystem algorithm. So, I seen that this algorithm appears twice in
 *  LSystem.java and twice again in largeLSystem.java, and I figured, one
 *  more. Written from scratch (rather than copy/paste), and with everything
 *  that's not LSystem related (such as GUI stuff that appears in the other
 *  four implementations) ripped out and put into callback/listeners
 * @author eliott bartley
 */
public class LSystem2
{

    private String macro;
    /**
     * Angle by which LSystem turns for each turn operation, in radians.
     *  This is passed in in degrees; the constructor converts to radians
     */
    private float angle;

    /**
     * Create a new LSystem and compile the grammar (recurse 'grammar',
     *  'depth' times) with axiom of l-value of grammar or 'F'
     * @param grammar GEVA grammar to execute
     * @param depth Depth grammar should recurse to 'compile' it
     * @param angle Angle LSystem turns for each turn operation, in degrees
     */
    public LSystem2(String grammar, int depth, float angle)
    {   this(null, grammar, depth, angle);
    }

    /**
     * Create a new LSystem and compile the grammar (recurse 'grammar',
     *  'depth' times)
     * @param axiom Axiom to which grammar is applied
     * @param grammar GEVA grammar to execute
     * @param depth Depth grammar should recurse to 'compile' it
     * @param angle Angle LSystem turns for each turn operation, in degrees
     */
    public LSystem2(String axiom, String grammar, int depth, float angle)
    {   this.angle = (float)(angle * Math.PI / 180);
        compile
        (   axiom == null ? null : axiom.replaceAll("\\s", ""),
            grammar.replaceAll("\\s", ""),
            depth
        );
    }

    /**
     * Taking the grammar and given a depth, recursively build the grammar
     *  from itself and store the results in 'macro'
     * @param depth Number of times to recurse grammar
     */
    private void compile(String axiom, String grammar, int depth)
    {   String[] parts = grammar.split("=");

        // If there's an ='s part, use the l-value as the axiom (if one is not
        //  explicitly specified), and the r-value as the grammar
        if(axiom == null || axiom.length() == 0)
        {   axiom = parts.length > 1 ? parts[0] : "F";
            if(axiom == null || axiom.length() == 0)
                axiom = "F";
        }
        grammar = parts.length > 1 ? parts[1] : parts[0];

        // Compile
        macro = grammar;
        for(; depth > 1; depth--)
            macro = macro.replace("F", grammar);
        // After the fractal part is done, apply it to the axiom
        macro = axiom.replace("F", macro);

        // If there are no turns, the angle is of no importance, so zero it.
        //  This is done so that, when comparing two LSystems that don't turn,
        //  they will be equal
        if(macro.indexOf('-') == -1 && macro.indexOf('+') == -1)
            angle = 0;

    }

    /**
     * Execute the 'compiled' grammar calling the StateTrace event handler
     *  for each move in the execution. Only moves can be listened to, turns
     *  and stack pusp/pops cannot
     * @param stateTrace Object implementing StateTrace to receive listened
     *  events. Can be null.
     */
    void execute(LSystem2StateTrace stateTrace)
    {   execute(null, stateTrace);
    }

    /**
     * Execute the 'compiled' grammar calling the StateTrace event handler
     *  for each move in the execution. Only moves can be listened to, turns
     *  and stack pusp/pops cannot
     * @param user Any user specific data. This is passed onto the listener
     *  through LSystem2StateTraceEvent.user. Can be null
     * @param stateTrace Object implementing StateTrace to receive listened
     *  events. Can be null.
     */
    void execute(Object user, LSystem2StateTrace stateTrace)
    {   StringReader instruction = new StringReader(macro);
        Stack<LSystem2State> stack = new Stack<LSystem2State>();
        int fetch;
        LSystem2State state = new LSystem2State();

        try
        {

            // Fetch
            while((fetch = instruction.read()) != -1)
                // Decode
                switch((char)fetch)
                {   // Execute
                    case 'F': move(state, true, user, stateTrace); break;
                    case 'f': move(state, false, user, stateTrace); break;
                    case '+': state.turn(angle); break;
                    case '-': state.turn(-angle); break;
                    case '[': stack.push(new LSystem2State(state)); break;
                    case ']': if(stack.empty() == false) state = stack.pop();
                }

        }
        catch(IOException e) { }

    }

    /**
     * Get the grammar expanded to the specified depth
     * @return the grammar expanded to the specified depth
     */
    public String getDerivedGrammar()
    {   return macro;
    }

    /**
     * Get the angle this LSystem turns by for each + - input
     * @return the angle this LSystem turns by for each + - input
     */
    public float getAngle()
    {   return angle;
    }

    /**
     * Helper to perform a forward move in the LSystem. This also handles
     *  calling the listener and recording trace information (trace is the
     *  previous position the LSystem's state was at)
     * @param state The current position and orientation of the 'cursor'.
     *  This was cursor in the other four implementations, but I call it
     *  state, go me!
     * @param doTrace Specify whether the previous position should be
     *  recorded and passed on to the listener. More specifically, this
     *  differentiates between a move that should draw a line (F) and a move
     *  that just moves (f)
     * @param user The user specific data to pass on to the callback. Can be
     *  null
     * @param stateTrace The object that will listen to events. Can be null
     */
    private void move
    (   LSystem2State           state,
        boolean               doTrace,
        Object                   user,
        LSystem2StateTrace stateTrace
    ){  LSystem2State           trace = null;
        LSystem2StateTraceEvent event = new LSystem2StateTraceEvent();

        // Make a copy of the state before the move
        if(doTrace == true)
            trace = new LSystem2State(state);

        // Perform the move
        state.move();

        // Prepare and call the listener
        if(stateTrace != null)
        {   event.state = state;
            event.trace = trace;
            event.user = user;
            stateTrace.Update(event);
        }

    }

}
