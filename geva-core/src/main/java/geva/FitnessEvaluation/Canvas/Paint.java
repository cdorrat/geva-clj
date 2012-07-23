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

package geva.FitnessEvaluation.Canvas;

import geva.FitnessEvaluation.FitnessFunction;
import geva.Individuals.Individual;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.Properties;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author eliott bartley
 */
public class Paint implements FitnessFunction
{

//    private JFrame window = null;
    private Painting painting = null;
    private double best = Double.MAX_VALUE;

    public void setProperties(Properties p)
    {
    }

    public void getFitness(Individual i)
    {   double result;
        init();
        
        //System.out.println(i.getPhenotype().getString());
        painting.paint(i.getPhenotype().getString());
        result = painting.evaluate();
        if(result < best)
        {   best = result;
            painting.mirror();
        }
        painting.invalidate();
        i.getFitness().setDouble(result);
        
    }

    public boolean canCache()
    {   return true;
    }
    
    private void init()
    {

        if(painting != null)
            return;
        
        painting = new Painting();
        painting.setSize(256, 128);

/*
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(256, 128);
        window.setVisible(true);
        window.getContentPane().setLayout(new BorderLayout());
        window.getContentPane().add(painting);
*/
    }
    
    class Painting extends JPanel
    {

        private BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
        private BufferedImage bestImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);

        @Override
        protected void paintComponent(Graphics graphics)
        {   super.paintComponent(graphics);
            render(graphics);
        }
     
        public void paint(String actionString)
        {   int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
            int pixel;
            int index = 0;
            String[] actions = actionString.split(" ");
            float r, g, b;
            int[] colour;

            if((actions.length % 6) != 0)
                return;

            colour = new int[actions.length / 6];

            int i;
            for(i = 0; i < actions.length; i += 6, index++)
            {   r = Float.parseFloat("0." + actions[i + 0] + actions[i + 1]);
                g = Float.parseFloat("0." + actions[i + 2] + actions[i + 3]);
                b = Float.parseFloat("0." + actions[i + 4] + actions[i + 5]);
                colour[index] = 0xFF000000
                              | (int)(0xFF * r) << 16
                              | (int)(0xFF * g) << 8
                              | (int)(0xFF * b);
            }

            index = 0;
            for(pixel = 0; pixel < pixels.length; pixel++)
            {

                pixels[pixel] = colour[index];

                index++;
                if(index >= colour.length)
                    index = 0;

            }

            render(this.getGraphics());
            
        }

        public void mirror()
        {   bestImage.getGraphics().drawImage(image, 0, 0, null);
        }
        
        private void render(Graphics graphics)
        {   
/*
            graphics.drawImage
            (   image,
                0, 0, window.getWidth() / 2, window.getHeight(),
                0, 0, image.getWidth(), image.getHeight(),
                null
            );
            graphics.drawImage
            (   bestImage,
                window.getWidth() / 2, 0, window.getWidth(), window.getHeight(),
                0, 0, bestImage.getWidth(), bestImage.getHeight(),
                null
            );
*/
        }
    
        public double evaluate()
        {   int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
            int pixel = 0;
            double result = 0.0;
            double thisResult;
            int account;
            int[] r = new int[256];
            int[] g = new int[256];
            int[] b = new int[256];

            for(int y = 0; y < image.getHeight(); y++)
            for(int x = 0; x < image.getWidth(); x++)
            {

                thisResult = 0;
                account = 0;

                if(x > 0)
                {   thisResult += evaluate(pixels[pixel], pixels[pixel - 1]);
                    account++;
                }
                if(x < image.getWidth() - 1)
                {   thisResult += evaluate(pixels[pixel], pixels[pixel + 1]);
                    account++;
                }
                if(y > 0)
                {   thisResult += evaluate(pixels[pixel], pixels[pixel - image.getWidth()]);
                    account++;
                }
                if(y < image.getHeight() - 1)
                {   thisResult += evaluate(pixels[pixel], pixels[pixel + image.getWidth()]);
                    account++;
                }

                r[(pixels[pixel] & 0x00FF0000) >> 16]++;
                g[(pixels[pixel] & 0x0000FF00) >>  8]++;
                b[ pixels[pixel] & 0x000000FF       ]++;

                result += thisResult / account;

                pixel++;

            }

            result /= pixel;
            result = evaluate(r, g, b);

            return result;

        }

        private double evaluate(int pixela, int pixelb)
        {

            int r = ((pixela & 0x00FF0000) >> 16) - ((pixelb & 0x00FF0000) >> 16);
            int g = ((pixela & 0x0000FF00) >> 8)  - ((pixelb & 0x0000FF00) >> 8);
            int b = ((pixela & 0x000000FF))       - ((pixelb & 0x000000FF));
            int a = Math.abs(r + g + b);

            if(a <= 10)
                a = (10 - a) * (10 - a);

            return a;

        }

        private double evaluate(int[] r, int[] g, int[] b)
        {   

            double sr = stdDev(r);
            double sg = stdDev(g);
            double sb = stdDev(b);
            double st;

            if(sr < sg) { st = sr; sr = sg; sg = st; }
            if(sr < sb) { st = sr; sr = sb; sb = st; }
            if(sg < sb) { st = sg; sg = sb; sg = st; }

            //for(int c : r) System.out.print(c + " "); System.out.println();
            //for(int c : g) System.out.print(c + " "); System.out.println();
            //for(int c : b) System.out.print(c + " "); System.out.println();
            //System.out.println(sr + ", " + sg + ", " + sb + " : " + ((sr + sg / 2 + sb / 4)));

            return sr + sg / 2 + sb / 4;

        }

        private double stdDev(int[] v)
        {   double[] w = new double[v.length];
            double mean = 0;

            for(int i = 0; i < v.length; i++)
                mean += v[i];
            mean /= v.length;

            for(int i = 0; i < v.length; i++)
                w[i] = Math.pow(v[i] - mean, 2);

            mean = 0;
            for(int i = 0; i < w.length; i++)
                mean += w[i];
            mean /= w.length;

            return Math.sqrt(mean);

        }
    
    }
    
}
