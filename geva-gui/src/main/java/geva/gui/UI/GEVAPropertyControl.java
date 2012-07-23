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
import java.awt.Color;
import javax.swing.JLabel;

/**
 * Base for controls that fit into a name/value container
 * Row contaier shows as a 2xN GridBoxLayout as a [ Name | Value ]
 * Col contaier shows as a name above value header/value
 * @author eliottbartley
 */
public abstract class GEVAPropertyControl extends GEVAControl
{

    private JLabel lblName;
    private JLabel lblReason;

    protected String initial;
    protected String[] params;

    /**
     * Let there be GEVAPropertyControl
     */
    GEVAPropertyControl
    (   GEVADirtyListener     dirtyListener,
        GEVAPropertyContainer parent,
        String                type,
        String                title,
        String                name,
        String                comment,
        String                initial,
        String                params
    ){  super(dirtyListener, parent, type, title, name, comment);
        lblName = new JLabel(title);
        lblReason = new JLabel();
        lblReason.setForeground(Color.red);
        resetInvalidReason();
        this.initial = initial;
        if(params != null) this.params = params.split(",");
    }

    /**
     * Helper - get params without exception.
     * @param index The index of the param to get.
     * @param alternative If the indexed param is outside the number of params
     *  or is emtpy, this value is returned instead
     * @return The indexed param. If the index is outside the
     *  actual params, or the param is an empty string, <var>alternative</var>
     *  is returned
     */
    public String getParam(int index, String alternative)
    {   if(params != null
        && index >= 0 && index < params.length
        && params[index].trim().length() != 0)
            return params[index];
        else
            return alternative;
    }
    /**
     * Helper - get params without exception.
     * @param index The index of the param to get.
     * @return The indexed param. If the index is outside the
     *  actual params, or the param is an empty string, null is returned
     */
    public String getParam(int index)
    {   return getParam(index, null);
    }

    /**
     * Helper - get integer params without exception.
     * @param index The index of the param to get.
     * @param alternative The value to return if index is out of range or no
     *  legal value can be parsed from the param
     * @return The indexed param parsed as a double. If the
     *  index is outside the actual params, or the param is not
     *  parsable to int, <var>alternative</var> is returned
     */
    public int getParamInt(int index, int alternative)
    {   try
        {   if(params != null
            && index >= 0 && index < params.length
            && params[index].trim().length() != 0)
                return Integer.parseInt(params[index]);
            else
                return alternative;
        } catch(NumberFormatException e)
        {   return alternative;
        }
    }
    /**
     * Helper - get integer params without exception.
     * @param index The index of the param to get.
     * @return The indexed param parsed as a double. If the
     *  index is outside the actual params, or the param is not
     *  parsable to int, 0 is returned
     */
    public int getParamInt(int index)
    {   return getParamInt(index, 0);
    }

    /**
     * Helper - get double params without exception.
     * @param index The index of the param to get.
     * @param alternative The value to return if index is out of range or no
     *  legal value can be parsed from the param
     * @return The indexed param parsed as a double. If the
     *  index is outside the actual params, or the param is not
     *  parsable to double, <var>alternative</var> is returned
     */
    public double getParamDouble(int index, double alternative)
    {   try
        {   if(params != null
            && index >= 0 && index < params.length
            && params[index].trim().length() != 0)
                return Double.parseDouble(params[index]);
            else
                return alternative;
        } catch(NumberFormatException e)
        {   return alternative;
        }
    }
    /**
     * Helper - get double params without exception.
     * @param index The index of the param to get.
     * @return The indexed param parsed as a double. If the
     *  index is outside the actual params, or the param is not
     *  parsable to double, 0.0 is returned
     */
    public double getParamDouble(int index)
    {   return getParamDouble(index, 0.0);
    }

    /**
     * Helper - get boolean params without exception.
     * @param index The index of the param to get.
     * @param alternative The value to return if index is out of range or no
     *  legal value can be parsed from the param
     * @return The indexed param parsed as a boolean. If the
     *  index is outside the actual params, or the param is not
     *  parsable to double, <var>alternative</var> is returned
     */
    public boolean getParamBoolean(int index, boolean alternative)
    {   try
        {   if(params != null
            && index >= 0 && index < params.length
            && params[index].trim().length() != 0)
                return Boolean.parseBoolean(params[index]);
            else
                return alternative;
        } catch(NumberFormatException e)
        {   return alternative;
        }
    }
    /**
     * Helper - get boolean params without exception.
     * @param index The index of the param to get.
     * @return The indexed param parsed as a boolean. If the
     *  index is outside the actual params, or the param is not
     *  parsable to double, false is returned
     */
    public boolean getParamBoolean(int index)
    {   return getParamBoolean(index, false);
    }
    /**
     * Helper - get whether the parameter is equal to a specific value
     * @param index The index of the param to test
     * @param value The values to test against
     * @return true if the parameter at the specified index is equal to value
     */
    public boolean getParamEqual(int index, String value)
    {   return getParam(index, "").equalsIgnoreCase(value);
    }

    public Component getComponent(int index)
    {   switch(index)
        {   case 0: return lblName;
            case 2: return lblReason;
        }
        return null;
    }

    /**
     * Classes that inherits this must have a 'second' component that
     *  will be returned to getComponent(1); This will handle the other 2
     */
    public int countComponents() { return 3; }

    public void setVisibleControlGroup(String name, boolean show)
    {   parent.setVisibleControlGroup(name, show);
    }

    public void setVisible(boolean show)
    {   getComponent(0).setVisible(show);
        getComponent(1).setVisible(show);
        if(lblReason.getText().length() != 0)
            getComponent(2).setVisible(show);
    }

    /**
     * Clear out all previous invalid reasons.
     * Done before performing a new validation
     */
    protected void resetInvalidReason()
    {   lblReason.setText("");
        lblReason.setVisible(false);
    }

    /**
     * Add a error when validating. All errors are accumulated until
     * resetInvalidReason is called
     * @param reason The error caused during validation
     * @return false always
     */
    protected boolean addInvalidReason(String reason)
    {   if(getComponent(0).isVisible() == true)
            lblReason.setVisible(true);
        String text = lblReason.getText();
        if(text.length() == 0)
            text = "<html>" + reason;
        else
            text += "<br>" + reason;
        lblReason.setText(text);
        return false;
    }

}

