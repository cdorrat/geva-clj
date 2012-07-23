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

package geva.Operator.Operations;


import geva.Exceptions.BadParameterException;
import geva.Individuals.Individual;
import geva.Util.Constants;
import geva.Util.Random.MersenneTwisterFast;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * The top N individuals are visually offered to the user and the user picks
 *  who should win. All chosen winners keep their fitness evaluation score and
 *  all loosers are modified to have the worst possible fitness
 * @author eliott bartley
 */
public abstract class UserSelect extends SelectionOperation
{

	private static Log logger = LogFactory.getLog(UserSelect.class);
    private JDialog dialog = null;
    private JPanel guiPanel = null;
    private JButton cmdDone = new JButton();
    private Rectangle rectangle;
    private int pickSize;
    private boolean userActive = true;
    private boolean doneUsed = false;

    /** Creates a new instance of TournamentSelect
     * @param size size of selected population
     * @param pickSize size of population to pick from
     */
    public UserSelect(int size, int pickSize)
    {   super(size);
        this.pickSize = pickSize;
    }
    
    /**
     * New instantion
     */
    public UserSelect()
    {   super();
    }
    
    @Override
    public void setProperties(Properties p) {
        super.setProperties(p);
        int value;
                
        String key;
        try {
            key = Constants.USERPICK_SIZE;
            value = Integer.parseInt(p.getProperty(key));
            if(value < 1) {
                throw new BadParameterException(key);
            }
        } catch(Exception e) {
            value = 10;
            logger.warn(e+" using default: "+value);
        }
        this.pickSize = value;
    }
    
    public void doOperation(Individual operand) { }
    
    /**
     * @param operands geva.Individuals to be selected from
     */
    public void doOperation(List<Individual> operands)
    {   ArrayList<Individual> sortedOperands;
        ArrayList<Individual> pickOperands;
        int pickSizeEx = pickSize;
        // Copy and sort individuals
        sortedOperands = new ArrayList<Individual>();
        for(Individual operand : operands)
            if(operand.isValid() == true)
                sortedOperands.add(operand);
        Collections.sort(sortedOperands);
        // Pick the top N individuals
        if(pickSizeEx > sortedOperands.size())
            pickSizeEx = sortedOperands.size();
        pickOperands = new ArrayList<Individual>(pickSizeEx);
        for(int index = 0; index < pickSizeEx; index++)
            pickOperands.add(sortedOperands.get(index));
        // Get the user to re-evaluate them
        userPick(pickOperands);
    }

    public void userPick(List<Individual> operands)
    {   doneUsed = false;
        initialiseDialog();
        if(userActive == true)
        {   display(operands);
            select(operands);
        }
        if(doneUsed == false)
            userActive = false;
        defaultSelect(operands);
        terminateDialog();
    }

    private void initialiseDialog()
    {   dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        if(rectangle != null)
            dialog.setBounds(rectangle);
        else
            dialog.setSize(400, 300);
        dialog.setModal(true);
        cmdDone.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {   doneUsed = true;
                dialog.setVisible(false);
            }
        });
        enableDone(false);
        guiPanel = new JPanel();
        dialog.add(new JScrollPane(guiPanel), BorderLayout.CENTER);
        dialog.add(cmdDone, BorderLayout.SOUTH);
    }

    private void terminateDialog()
    {   if(rectangle == null)
            rectangle = new Rectangle();
        dialog.getBounds(rectangle);
        dialog.dispose();
        dialog = null;
    }

    protected void display(List<Individual> operands)
    {   dialog.setVisible(true);
    }
    
    protected abstract void select(List<Individual> operands);
    
    protected void defaultSelect(List<Individual> operands)
    {
        MersenneTwisterFast random = new MersenneTwisterFast();
        while(super.selectedPopulation.size() < 2)
            super.selectedPopulation.add
            (   operands.get(random.nextInt(operands.size()))
            );
    }

    protected void enableDone(boolean enable)
    {   if(enable == true)
            cmdDone.setText("Continue");
        else
            cmdDone.setText("Make a selection");
        cmdDone.setEnabled(enable);
    }
    
    protected JPanel getPanel()
    {   return guiPanel;
    }

    /**
     * gridAdd(Container, Container, int, int, int, int, double, double)
     *  overload. When calling on gridAdd(..), gridW and gridH are set to 1, and
     *  weightY is set to 0
     * @param container The control to add <var>control</var> to
     * @param control The control being added to <var>container</var>
     * @param gridX The grid column to add the control
     * @param gridY The grid row to add the control
     * @param weightX The amount of horizontal space this column should take
     *  relative to other columns
     */
    protected static void gridAdd
    (   Container container,
        Container control,
        int       gridX,
        int       gridY,
        double    weightX
    ){  gridAdd(container, control, gridX, gridY, 1, 1, weightX, 0);
    }

    /**
     * Helper for adding a control to a GridBagLayout control
     * @param container The control to add <var>control</var> to
     * @param control The control being added to <var>container</var>
     * @param gridX The grid column to add the control
     * @param gridY The grid row to add the control
     * @param gridW The number of columns to span
     * @param gridH The number of rows to span
     * @param weightX The amount of horizontal space this column should take
     *  relative to other columns
     * @param weightY The amount of vertical space this column should take
     *  relative to other rows
     */
    protected static void gridAdd
    (   Container container,
        Container control,
        int       gridX,
        int       gridY,
        int       gridW,
        int       gridH,
        double    weightX,
        double    weightY
    ){  container.add
        (   control,
            new GridBagConstraints
            (   gridX, gridY,
                gridW, gridH,
                weightX, weightY,
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1),
                0, 0
            )
        );
    }

}

