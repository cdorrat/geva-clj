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

package geva.gui.UI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author eliottbartley
 */
public class GEVAMessage
{

    private GEVAMessage() { }

    /**
     * Use the default option and/or type
     */
    public static final int DEFAULT = 0x00;
    /**
     * User clicks OK
     */
    public static final int OK_OPTION = 0x01;
    /**
     * User clicks Yes
     */
    public static final int YES_OPTION = 0x01;
    /**
     * User clicks No
     */
    public static final int NO_OPTION = 0x02;
    /**
     * User clicks Cancel, or closes with the window X
     */
    public static final int CANCEL_OPTION = 0x04;

    /**
     * showMessage should display an option yes/no dialog
     */
    public static final int YES_NO_OPTION = YES_OPTION
                                          | NO_OPTION;
    /**
     * showMessage should display an option yes/no/cancel dialog
     */
    public static final int YES_NO_CANCEL_OPTION = YES_OPTION
                                                 | NO_OPTION
                                                 | CANCEL_OPTION;
    /**
     * showMessage should display an option ok/cancel dialog
     */
    public static final int OK_CANCEL_OPTION = OK_OPTION
                                             | CANCEL_OPTION;

    /**
     * showMessage should display the information icon
     */
    public static final int INFORMATION_TYPE = 0x10;
    /**
     * showMessage should display the question icon
     */
    public static final int QUESTION_TYPE = 0x20;
    /**
     * showMessage should display the warning icon
     */
    public static final int WARNING_TYPE = 0x30;
    /**
     * showMessage should display the error icon
     */
    public static final int ERROR_TYPE = 0x40;

    /**
     * Displays a message to the user in a JOptionPane message dialog.
     * Same as showMessage(owner, message, GEVAMessage.DEFAULT);
     * @see #showMessage(Component, String, int, String[])
     */
    public static int showMessage
    (   Component  owner,
        String   message,
        String... params
    ){  return showMessage(owner, message, DEFAULT, params);
    }

    /**
     * Displays a message to the user in a JOptionPane message dialog.
     * The message can begin with a brief statement or question. If this is the
     *  case, that brief is put into the message-box's title, rather than its
     *  body. A statement is made by ending the brief with an exclamation (!)
     *  and a question is made by ending the brief with a question mark (?).
     *  Note: if a *_TYPE is not specified (see flags), the brief will assign
     *  types of WARNING_TYPE for statements (!) and QUESTION_TYPE for questions
     *  (?) A brief can be no more than 3 words;
     *  e.g. "Save? Do you want to save?" becomes<br/>
     *  Title:Save?<br/>
     *  Body:[?] Do you want to Save?<br/>
     *  A word is identified by a space character, regardless of non-white-space
     *  around it, and with 3 words, means only two spaces can be in the brief,
     *  so "Save   ?" is not a brief, it has 3 white-spaces. A brief cannot
     *   contain white-space other than space characters. Any other white-space
     *   (tabs, newline; ASCII less than 32) found will cause the brief to be
     *   ignored, and it will be output to the body.
     * The message can also contain placeholders and these are replaced by the
     *  values passed to params before display. Placeholders are in the form {N}
     *  where N is the index of the param to replace it with, e.g.
     *  showMessage
     *  (   null,
     *      "{0} {0}! {1}'s {2}? {3}! {3} {1}? {3} {4}.",
     *      GEVAMessage.DEFAULT_OPTION_TYPE,
     *      "knock",
     *      "who",
     *      "there",
     *      "Joe",
     *      "King"
     *  );
     *  will display "knock knock!" (which, with !, is a statement, and appears
     *   as the title. The body displays "who's there? Joe! Joe who? Joe King."
     *  If {0} was meant to be output, and not replaced, use \{0}. This also
     *   means that to output \, you need to use \\, tho in general, a single
     *   \ will output as \. So to actually output \\, use \\\ or \\\\, etc.
     * This method also formats the message so that long messages don't go
     *  outside the width of the screen by wrapping them at ~3/4 the width of
     *  the screen
     * @param owner The parent window to the message
     * @param message The message to display. If the message has a brief
     *  question or statement at the start of the message, it is used in the
     *  message boxes title, rather than message area (see above)
     * @param flags The type and options for the message displayed, can be one
     *  of INFORMATION_TYPE, QUESTION_TYPE, WARNING_TYPE, ERROR_TYPE or'd with
     *  one of OK_OPTION, YES_NO_OPTION, YES_NO_CANCEL_OPTION, OK_CANCEL_OPTION.
     * @param params Parameter values to fill placeholders
     * @return The option clicked by the user, will be one of
     *  OK_OPTION, YES_OPTION, NO_OPTION, CANCEL_OPTION
     */
    public static int showMessage
    (   Component  owner,
        String   message,
        int        flags,
        String... params
    ){  String     brief = "";
        int    briefAtEx;
        int    briefAtQu;
        int      jOption = JOptionPane.DEFAULT_OPTION;
        int        jType = JOptionPane.PLAIN_MESSAGE;

        // Fix up parameters ---------------------------------------------------

        if(params.length != 0)
            message = applyParams(message, params);

        // Parse out title -----------------------------------------------------

        // Get the index of the punctuation
        briefAtEx = message.indexOf('!');
        briefAtQu = message.indexOf('?');

        // Store the punctuation character to use in length
        if(briefAtQu != -1)
            if(briefAtEx != -1 && briefAtEx > briefAtQu
            || briefAtEx == -1)
                briefAtEx = briefAtQu;

        // Extract out brief and message parts. Don't take the brief if it is
        //  more than 3 words long. A word is determined as a space only - a
        //  brief cannot contain any other white-space characters will
        if(briefAtEx != -1)
        {

            // Set jType to match the brief - this will be overridden (below)
            //  by getTypeFromFlags if the user specified a particular type
            if(message.charAt(briefAtEx) == '!')
                jType = JOptionPane.WARNING_MESSAGE;
            else
                jType = JOptionPane.QUESTION_MESSAGE;

            // spaceCount is set to 3 if a non-space white-space is found in the
            //  brief. wordCount is assumed to be spaceCount + 1. Even if there
            //  is only 1 word followed by 3 spaces, it is counted as 4 words
            if(getSpaceCount(briefAtEx, message) < 3)
            {   brief = message.substring(0, briefAtEx + 1);
                message = message.substring(briefAtEx + 1).trim();
            }

        }

        // Fix up options ------------------------------------------------------

        jOption = mapToJOption(flags, jOption);
        jType = mapToJType(flags, jType);

        // Show time! ----------------------------------------------------------

        // Don't use JOptionPane.show*Dialog as it doesn't support wrapping text
        //  to fit the screen
        jOption = OptionPane.show(owner, message, brief, jOption, jType);

        return mapFromJResult(jOption);

    }

    /**
     * Replace all placeholders with their parameter values.
     *  Update (2008y08M01d): this is pretty much useless now since I added I18N
     *  yesterday, as now all showMessage calls take their string values from
     *  I18N and I18N has its own version of this formatting, except it uses
     *  Java's formatter (with all the internationalisation features Java
     *  provides). But I'm leaving this in because it might be useful where
     *  showMessage is called for debugging purposes and needs a quick way to
     *  pass parameters.. Actually, I'm really leaving it in because getting the
     *  regular expression syntax for this working was a pain, plus look at the
     *  comments to code ratio here, it's off the hook! How could I possibly
     *  just delete it all?
     */
    private static String applyParams(String message, String[] params)
    {

        // For each input parameter, replace the placeholder with the parameter
        //  value. Placeholders are in the format {N}, where N is the integer
        //  index of the parameter passed to this method.
        for(int i = 0; i < params.length; i++)
            message = message.replaceAll
            (   // This expression is made up of the following parts..
                // ([^\\]|^)  Don't replace {N} if an odd number of \ appear
                //             before it - \{0} \\\{0} skip, \\{0} replace
                //             that is, the first character in the match must be
                //             either, the start of the line, or anything other
                //             than \
                // ((\\\\)*)  Account for all even number of \ before {0}. As
                //             the character before it is not a \, once all \\
                //             pairs are consumed, the next character will
                //             either be a \ (odd number of \'s), a '{' (will
                //             continue the match for a replace), or any other
                //             character, (ignore, not a placeholder)
                // \{         Start with {
                // \s*        Lead white-space is ok - {0} == {  0}
                // 0*         Zeros are ok - {0} == {00} == { 00} != { 0 0}
                // i          Index of actual parameter
                // \s*        Trail white-space is ok - {0} == {0  }
                // \}         End with }
                // Note the \ doubles in java and regex, so double here means
                //  regex escape, quadruple means a literal \ character
                "([^\\\\]|^)((\\\\\\\\)*)\\{\\s*0*" + i + "\\s*\\}",
                // Output starts with $1$2; because matching consumes \'s, these
                //  restore those consumed characters, that is, output any
                //  character that's not a \, followed by the even number of \'s
                //  found, e.g. [\\{0}] $1=='[' $2=='\\'
                // Output is then followed by the actual parameter
                //  So [\\{0}] matches '[\\{0}', $1='[', $2='\\' so the output
                //  is <value>] (']' didn't match from the input) and <value>
                //  becomes '[' + '\\' + <param> becomes [\\<param>]
                "$1$2" + params[i]
            );

        // Because \ is an 'escape' character, replace all \\ with a real \
        //  Any single \'s that are not replaced here are untouched, so single
        //  \'s can be used to represent themselves where they don't escape
        //  anything else
        return message.replaceAll("\\\\\\\\", "\\\\");

    }

    /**
     * Count how many spaces appear in message from characters 0..length-1
     */
    private static int getSpaceCount
    (   int length,
        String message
    ){  int spaceCount = 0;
        int index;

        for(index = 0; spaceCount < 3 && index < length; index++)
            // Counting spaces is how words are counted
            if(message.charAt(index) == ' ')
                spaceCount++;
            else
            // Any white-space that's not a space, means this is not a brief,
            //  set <var>spaceCount</var> to 3 to terminate the loop and
            //  skip the brief
            if(message.charAt(index) < ' ')
                spaceCount = 3;

        return spaceCount;

    }

    /**
     * Map from internal flags to the JOptionPane option value
     */
    private static int mapToJOption(int flags, int alternative)
    {   switch(flags & 0x0F)
        {   case YES_NO_OPTION:        return JOptionPane.YES_NO_OPTION;
            case YES_NO_CANCEL_OPTION: return JOptionPane.YES_NO_CANCEL_OPTION;
            case OK_CANCEL_OPTION:     return JOptionPane.OK_CANCEL_OPTION;
        }
        return alternative;
    }

    /**
     * Map from internal flags to the JOptionPane type value
     */
    private static int mapToJType(int flags, int alternative)
    {   switch(flags & 0xF0)
        {   case INFORMATION_TYPE: return JOptionPane.INFORMATION_MESSAGE;
            case QUESTION_TYPE:    return JOptionPane.QUESTION_MESSAGE;
            case WARNING_TYPE:     return JOptionPane.WARNING_MESSAGE;
            case ERROR_TYPE:       return JOptionPane.ERROR_MESSAGE;
        }
        return alternative;
    }

    /**
     * Map from JOptionPane result to internal result
     */
    private static int mapFromJResult(int jOption)
    {   // Map jOptions back to internal options. Not using a switch
        //  as jOptions may use the same value for different results
        //  that supposedly could change in the future
        if(jOption == JOptionPane.YES_OPTION)
            return YES_OPTION;
        else
        if(jOption == JOptionPane.NO_OPTION)
            return NO_OPTION;
        else
        if(jOption == JOptionPane.CANCEL_OPTION)
            return CANCEL_OPTION;
        else
        if(jOption == JOptionPane.OK_OPTION)
            return OK_OPTION;
        // Unknown options will just be taken as cancelled
        return CANCEL_OPTION;
    }

}
/**
 * Override JOptionPane to wrap messages to ~3/4 of the screen width, so that
 *  long messages don't cause the dialog to be wider than the screen
 * @author eliott bartley
 */
class OptionPane extends JOptionPane
{

    private static int charPerLine = -1;

    /**
     * Calculate the approx. number of characters per line using the X
     *  character's width as the average width of all characters.
     */
    private static void init()
    {   Dimension       screen;
        FontMetrics    metrics;
        JOptionPane optionPane;
        JDialog         dialog;
        if(charPerLine == -1)
        {   optionPane = new OptionPane();
            dialog = optionPane.createDialog(null, null);
            metrics = dialog.getGraphics().getFontMetrics();
            screen = Toolkit.getDefaultToolkit().getScreenSize();
            charPerLine = (int)(screen.width / metrics.charWidth('X') * 0.75);
        }
    }

    public static int show
    (   Component        owner,
        String         message,
        String           title,
        int         optionType,
        int        messageType
    ){  JOptionPane optionPane;
        JDialog         dialog;

        // init runs only once, and calculates the approx. number of characters
        //  to display on a single line so that long text messages won't go off
        //  the width of the screen
        init();

        // Set up the JOptionPane, show it, and get the result
        optionPane = new OptionPane();
        optionPane.setMessage(message);
        optionPane.setMessageType(messageType);
        optionPane.setOptionType(optionType);
        dialog = optionPane.createDialog(owner, title);
        dialog.setVisible(true);
        return Integer.parseInt(optionPane.getValue().toString());

    }

    /**
     * The JOptionPane will format its message string to a specified number of
     *  characters per line if that information is given to it, which is what
     *  this does
     */
    @Override public int getMaxCharactersPerLineCount()
    {   return charPerLine;
    }

}