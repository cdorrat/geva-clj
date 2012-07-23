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

import java.text.Format;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Internationalisation (I18N) helper
 * @author eliott bartley
 */
public class I18N
{

    private I18N() { }

    /**
     * Get a string message from UI.res.locale.messages using the default
     *  language to determine which messages file to use where additional
     *  languages can be added by adding messages files with _language_country
     *  codes e.g. messages_es_EC.properties for Spanish (Ecuador)
     * @param name Name of the string to get
     * @param params If named string contains parameters placeholders, fill them
     *  with these values. If params contains names too, that exist in the
     *  messages file, these are resolved and use the params that follow the
     *  params that would be used for the message, e.g. given the messages..
     *  ma=1:[{0}],[{1}]
     *  mb=2:[{0}]
     *  mc=3:[{0}{0}]
     *  md=4:{1}
     *  ..calling get("ma", "mb", "t1", "mc", "md", "t2", "t3"); would output..
     *  1:[2:[3:[4:t34:t3]]],[t1]
     *  ..because..
     *  ma expanded to 1:[{0}],[{1}], its parameters are the next two values
     *   "mb" and "t1" so mb expands to 2:[{0}] and it takes the parameter
     *   following the parameters used by ma, which is mc, mc expands to
     *   3:[{0}{0}] and it takes the parameters following what mb used, md,
     *   which expands to 4:{1} which uses "t2" and "t3", so fill {1} with t3,
     *   which files 3:[{0}{0}] with 4:t3 twice, which fills 2:[{0}] with
     *   3:[4:t34:t3], which fills 1:[{0}],[{1}] with 3:[4:t34:t3] and ma's
     *   second parameter "t1"
     * @return Named string with parameters filled (if any) in the current
     *  language, or nearest matching language, or default language (English)
     */
    public static String get(String name, Object... params)
    {   String message = getMessage(name);
        resolveParams(params);
        return formatMessage(message, 0, params);
    }

    /**
     * Get a named message from the messages file that's a best match for the
     *  current system language. If the named message doesn't exist,
     *  MissingResourceException is thrown
     * @param name Name of the message to get from the messages file
     * @return Named message string
     */
    private static String getMessage(String name)
    {   Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle
        (   "UI.res.locale.messages",
            locale
        );
        return bundle.getString(name);
    }

    /**
     * Go through each parameter and see if it matches the name of a message
     *  in the messages file, if it does, replace the parameter with the message.
     *  If it doesn't, leave the parameter unchanged
     * @param params
     */
    private static void resolveParams(Object[] params)
    {   String message;
        for(int paramIndex = 0; paramIndex < params.length; paramIndex++)
            try
            {   if(params[paramIndex] instanceof String)
                    if((message = getMessage
                    (   params[paramIndex].toString())
                    ) != null)
                        params[paramIndex] = message;
            }
            catch(java.util.MissingResourceException e)
            {   // If getMessage doesn't have a message to get, don't modify
                //  this param, just go straight on to the next param
            }
    }

    /**
     * Given a message and parameters, resolve all message's parameter
     *  placeholders with the parameter value. The firstParam can change which
     *  parameter relates to {0} placeholder in the message, and all increment
     *  from this index. If any of the parameters also have placeholders, this
     *  recursively calls itself to fill their placeholders, setting the
     *  firstParam to the index following all parameters that are used by the
     *  current message so params must be in the order p0..pN, p00..p0N..pMN,
     *  p000..p00N..p0MN..pLMN... where each additional index is for nested
     *  placeholders (ones in params) and assumes every message/param contains
     *  N M L placeholders; any that don't contain placeholders can have their
     *  pXXX.. taken out, so long as the order of remaining params don't change
     * @param message Message to format
     * @param firstParam Index of parameter that relates to {0} placeholder,
     *  all parameters following this one relate to incrementing placeholders
     * @param params The parameters used to fill the placeholders
     * @return Message with all placeholders filled with relative parameters
     */
    private static String formatMessage
    (   String message,
        int firstParam,
        Object[] params
    ){

        // Only need to do any formatting if there are parameters to do the
        //  formatting with. If there are none, the message input is returned
        //  unmodified
        if(params != null && firstParam < params.length)
        {   MessageFormat parser;
            Locale locale = Locale.getDefault();
            Format[] formats;

            // Set up
            parser = new MessageFormat("");
            parser.setLocale(locale);
            parser.applyPattern(message);
            // Used only to count how many parameters are needed by this message
            formats = parser.getFormatsByArgumentIndex();

            // Recursively format the parameters used by this message
            for(int paramIndex = 0; paramIndex < formats.length; paramIndex++)
                if(params[firstParam + paramIndex] instanceof String)
                    params[firstParam + paramIndex] = formatMessage
                    (   params[firstParam + paramIndex].toString(),
                        firstParam + formats.length,
                        params
                    );

            // Format the message using the formatted parameters
            message = parser.format(getParams
            (   params,
                firstParam,
                firstParam + formats.length
            ));

        }

        return message;

    }

    /**
     * MessageFormat.format always matches the placeholder's index directly to
     *  the param array index, but because placeholders can be nested, the
     *  zeroth placeholder may not match the zeroth array element, so this
     *  method copies the array so that only the required array elements are
     *  present, and start from the zeroth element
     * @param params Complete param array
     * @param firstParam First element that will be used in complete param array
     * @param lastParam Last element that will be used in complete param array
     * @return Param array containing just the elements from
     *  firstParam..lastParam-1. If lastParam is less or equal to firstParam,
     *  null is returned rather than an empty array
     */
    private static Object[] getParams
    (   Object[]    params,
        int     firstParam,
        int      lastParam
    ){  Object[] newParams = null;
        if(firstParam < lastParam)
        {   newParams = new Object[lastParam - firstParam];
            for(int i = firstParam; i < lastParam; i++)
                newParams[i - firstParam] = params[i];
        }
        return newParams;
    }

}
