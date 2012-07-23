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

import geva.gui.Util.Constants;
import geva.gui.Util.FileNameExtensionFilter;
import geva.gui.Util.GEVAHelper;
import geva.gui.Util.I18N;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

/**
 * View LSystems
 * @author eliott bartley
 */
public class LSystem2Control extends JPanel
{

    private JTextField    txtAxiom = new JTextField();
    private JTextField    txtGrammar = new JTextField();
    private JSpinner      txtDepth = new JSpinner
                          (   new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1)
                          );
    private JSpinner      txtAngle = new JSpinner
                          (   new SpinnerNumberModel(0, -360, 360, 0.25)
                          );
    private JTextField    txtDerived = new JTextField();
    private JTextField    txtDimension = new JTextField();
    private LSystem2Panel guiLSystem;
    private JButton       cmdSavePS = new JButton(I18N.get("ui.run.lsys.save"));
    private Component     cmdClose = null;

    public LSystem2Control
    (   String grammar,
        int depth,
        float angle,
        Component close
    ){  this("", grammar, depth, angle, close);
    }

    public LSystem2Control
    (   String    axiom,
        String  grammar,
        int       depth,
        float     angle,
        Component close
    ){  guiLSystem = new LSystem2Panel(axiom, grammar, depth, angle);
        txtAxiom.setText(axiom.replaceAll("\\s", ""));
        txtGrammar.setText(grammar.replaceAll("\\s", ""));
        txtDepth.setValue(depth);
        txtAngle.setValue(angle);
        txtDerived.setText(guiLSystem.getDerivedGrammar());
        calcFractalDimension();
        cmdClose = close;
        initialisePane();
    }

    private void initialisePane()
    {   super.setLayout(new BorderLayout());

        // Houses text inputs at top
        JPanel grid = new JPanel();
        // Houses command buttons at bottom
        JPanel flow = new JPanel();

        grid.setLayout(new GridBagLayout());
        flow.setLayout(new FlowLayout());

        txtAxiom.addCaretListener(new CaretListener()
        {   public void caretUpdate(CaretEvent e)
            {   guiLSystem.setAxiom(txtAxiom.getText());
                txtDerived.setText(guiLSystem.getDerivedGrammar());
                calcFractalDimension();
            }
        });
        txtGrammar.addCaretListener(new CaretListener()
        {   public void caretUpdate(CaretEvent e)
            {   guiLSystem.setGrammar(txtGrammar.getText());
                txtDerived.setText(guiLSystem.getDerivedGrammar());
                calcFractalDimension();
            }
        });
        txtDepth.addChangeListener(new ChangeListener()
        {   public void stateChanged(ChangeEvent e)
            {   guiLSystem.setDepth((Integer)txtDepth.getValue());
                txtDerived.setText(guiLSystem.getDerivedGrammar());
                calcFractalDimension();
            }
        });
        txtAngle.addChangeListener(new ChangeListener()
        {   public void stateChanged(ChangeEvent e)
            {   guiLSystem.setAngle(getFloatValue(txtAngle.getValue()));
                txtDerived.setText(guiLSystem.getDerivedGrammar());
                calcFractalDimension();
            }
        });
        txtDerived.setEditable(false);
        txtDimension.setEditable(false);
        cmdSavePS.addActionListener(new ActionListener()
        {   public void actionPerformed(ActionEvent e)
            {   savePS();
            }
        });

        gridAddI18NLabelControl(grid, "axim", txtAxiom, 0);
        gridAddI18NLabelControl(grid, "gram", txtGrammar, 1);
        gridAddI18NLabelControl(grid, "dept", txtDepth, 2);
        gridAddI18NLabelControl(grid, "angl", txtAngle, 3);
        gridAddI18NLabelControl(grid, "deri", txtDerived, 4);
        gridAddI18NLabelControl(grid, "fdim", txtDimension, 5);

        flow.add(cmdSavePS);
        if(cmdClose != null)
            flow.add(cmdClose);

        super.add(grid, BorderLayout.NORTH);
        super.add(guiLSystem, BorderLayout.CENTER);
        super.add(flow, BorderLayout.SOUTH);

        super.setVisible(true);

    }

    /**
     * Helper for calling the gridAdd helper method, used just above
     * @param grid The grid pane to add the label to
     * @param label The I18N label name (appended to known "ui.run.lsys.labl.")
     * @param control The input control that is placed next to the label
     * @param gridY Grid element to add label to
     */
    private void gridAddI18NLabelControl
    (   JPanel grid,
        String label,
        Container control,
        int gridY
    ){  GEVAHelper.gridAdd
        (   grid,
            new JLabel(I18N.get("ui.run.lsys.labl." + label)),
            0, gridY, 0
        );
        GEVAHelper.gridAdd(grid, control, 1, gridY, 1);
    }

    private void savePS()
    {   PSLSystem2 lSystem = new PSLSystem2
        (   txtAxiom.getText(),
            txtGrammar.getText(),
            (Integer)txtDepth.getValue(),
            getFloatValue(txtAngle.getValue())
        );
        JFileChooser chooser = new JFileChooser();
        FileFilter filter = new FileNameExtensionFilter
        (   I18N.get("ui.run.lsys.post"),
            Constants.txtPostScriptExt
        );
        String filename;
        chooser.setFileFilter(filter);
        if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        {   filename = chooser.getSelectedFile().toString();
            // TODO - file already exists, request if ok to overwrite. Currently
            //  this just overwrites
            if(filename.toLowerCase().endsWith
            (   Constants.txtPostScriptExt
            ) == false)
                filename += Constants.txtPostScriptExt;
            lSystem.save(filename);
        }
    }

    private float getFloatValue(Object value)
    {   return Float.parseFloat(String.valueOf(value));
    }

    /**
     * Calculate the fractal dimension using box-counting approximation
     */
    private void calcFractalDimension()
    {   txtDimension.setText
        (   String.format
            (   "%.2f",
                new LSystem2FDBoxCounting
                (   txtAxiom.getText(),
                    txtGrammar.getText(),
                    (Integer)txtDepth.getValue(),
                    getFloatValue(txtAngle.getValue()),
                    256
                ).calcFractalDimension()
            )
        );
    }

}
