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

import geva.Individuals.Individual;
import geva.Util.Random.MersenneTwisterFast;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * @author eliott barley
 */
public class SimpleUserSelect extends UserSelect
{

    private ArrayList<JCheckBox> checks;

    @Override
    public void userPick(List<Individual> operands)
    {   super.userPick(operands);
    }

    @Override
    protected void display(List<Individual> operands)
    {   int row = 1;
        checks = new ArrayList<JCheckBox>(operands.size());

        super.enableDone(true);

        getPanel().setLayout(new GridBagLayout());

        gridAdd(getPanel(), new JLabel("Pick"), 0, 0, 0);
        gridAdd(getPanel(), new JLabel("Phenotype"), 1, 0, 0);

        for(Individual operand : operands)
        {

            JCheckBox chkPick = new JCheckBox();
            JTextField txtPheno = new JTextField();
            txtPheno.setText(operand.getPhenotype().getString());

            gridAdd(getPanel(), chkPick, 0, row, 0);
            gridAdd(getPanel(), txtPheno, 1, row, 0);

            checks.add(chkPick);
            
            row++;

        }

        super.display(operands);
        
    }

    protected void select(List<Individual> operands)
    {
        for(int index = 0; index < operands.size(); index++)
            if(checks.get(index).isSelected() == true)
                super.selectedPopulation.add(operands.get(index).clone());
    }

}
