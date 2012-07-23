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

package geva.gui.Util;

import geva.gui.UI.GEVAMessage;

/**
 * In a last effort ditch attempt, try to notify the user that a worker thread
 *  crashed for an unexpected reason. Uncaught exceptions in worker threads
 *  will only be output to a console if one is visible, which it generally wont,
 *  what with this being a GUI app, so this catches the error before it reaches
 *  the console solution, and offers it as a message-box
 * @author eliott bartley
 */
public class GEVAUncaught extends ThreadGroup
{

    public static final GEVAUncaught global = newGEVAUncaught('x');
    public static final GEVAUncaught graph  = newGEVAUncaught('g');
    public static final GEVAUncaught output = newGEVAUncaught('o');
    public static final GEVAUncaught error  = newGEVAUncaught('e');
    public static final GEVAUncaught jar    = newGEVAUncaught('j');

    /**
     * Helper for creating GEVAUncaughts, label is inserted into strings
     *  ui.ssn*.bug and ui.ssp*.bug to get the name and purpose of the thread
     *  that this GEVAUncaught will be attached to
     * @param label
     */
    private static GEVAUncaught newGEVAUncaught(char label)
    {   return new GEVAUncaught
        (   "ui.ssn" + label + ".bug",
            "ui.ssp" + label + ".bug"
        );
    }

    private String purpose;

    private GEVAUncaught(ThreadGroup parent, String name, String purpose)
    {   super(parent, name);
        this.purpose = purpose;
    }

    private GEVAUncaught(String name, String purpose)
    {   super(name);
        this.purpose = purpose;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception)
    {
        String cause = "";
        try
        {   exception.printStackTrace();
            for(; exception != null; exception = exception.getCause())
                cause += "\n  " + exception.toString();
            // Try to give a meaningful message to the unknown, unexpected,
            //  uncaught error
            GEVAMessage.showMessage
            (   GEVAHelper.mainWindow,
                I18N.get("ui.uncg.bug", super.getName(), purpose, cause),
                GEVAMessage.OK_OPTION | GEVAMessage.ERROR_TYPE
            );
        }
        catch(Throwable e)
        {   // If the meaningful message itself threw an exception, last chance
            //  for any possible way to notify the user that the program is
            //  crashing at an alarming rate
            GEVAMessage.showMessage
            (   null,
                "hi" + e.toString(),
                GEVAMessage.OK_OPTION | GEVAMessage.ERROR_TYPE
            );
            exception.printStackTrace();
            e.printStackTrace();
        }

    }

    /**
     * Every possible internal unexpected exception should be handled by
     *  GEVAUncaught, but for external (and forgotted internal) unexpected
     *  exceptions, don't allow those to vanish into a non-existing console,
     *  instead, output an unhelpful message so the user at least knows there be
     *  an error. Currently (2008y08M31d) this is set up on the very first line
     *  in main in GEVAPropertiesGUI - I also put it into LSystemViewer main for
     *  good measure
     */
    public static class GEVAGlobalUncaught
             implements Thread.UncaughtExceptionHandler
    {   public void uncaughtException(Thread thread, Throwable exception)
        {   GEVAUncaught.global.uncaughtException(thread, exception);
        }
    }

}
