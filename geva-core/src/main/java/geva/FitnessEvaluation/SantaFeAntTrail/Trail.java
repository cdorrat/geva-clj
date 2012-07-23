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
 * Trail.java
 *
 * Created on November 17, 2006, 5:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package geva.FitnessEvaluation.SantaFeAntTrail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Taken from lib GE and refactored to java
 * @author erikhemberg
 */
public class Trail {

    public static final int GRID_WIDTH = 32;
    public static final int GRID_HEIGHT = 32;
    public static final int EMPTY = 0;
    public static final int FOOD = 1;
    public static final int ANT = 8;
    public int _energy;
    public int _picked_up;
    public int[][] _trail = {
        {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
    public int[][] _working_trail = {
        {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
    public int food;
    public int _current_X,  _current_Y,  _facing_current_X,  _facing_current_Y;

    /** Creates a new instance of Trail.
     * Calling initGEtrail with 600 */
    public Trail() {
        initGEtrail(600);
    }

    /**
     * Create new instance
     * @param energy ant stamina
     */
    public Trail(int energy) {
        initGEtrail(energy);
    }

    /**
     * Get the energy
     * @return energy
     */
    public int get_Energy() {
        return this._energy;
    }

    /**
     * Get energy left
     * @return energy left
     */
    public boolean get_Energy_Left() {
        return this._energy > 0;
    }

    /**
     * Get food
     * @return food
     */
    public int getFood() {
        return this.food;
    }

    /**
     * get food picked up
     * @return food picked up
     */
    public int get_Picked_Up() {
        return this._picked_up;
    }

    /**
     * Get fitness. Food on trail - food picked up
     *
     * @return food left
     */
    public double getFitness() {
        return (double) (this.getFood() - this.get_Picked_Up());
    }

    /**
     * Initiate the trail
     * @param e energy for the trail
     */
    void initGEtrail(int e) {
        _current_X = 0;
        _current_Y = 0;
        _facing_current_X = 0;//1 TODO:make sure this is correct
        _facing_current_Y = 1;//0
        _energy = e;
        _picked_up = 0;
        food = 89;
    }

    /**
     * Read a trail from file
     * @param file_name name of file
     */
    @SuppressWarnings({"ConstantConditions", "IOResourceOpenedButNotSafelyClosed"})
    void readTrailGEtrail(String file_name) {
        int y = 0;
        int x;
        char ch;
        int bufferSize = 1024;
        String line;
        try {
            FileReader fr = new FileReader(file_name);
            BufferedReader br = new BufferedReader(fr, bufferSize);
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#")) {
                    for (x = 0; x < line.length(); x++) {
                        ch = line.charAt(x);
                        if (ch == '.' || ch == '0') {
                            _trail[x][y] = EMPTY;
                            _working_trail[x][y] = EMPTY;
                        } else if (ch == '1') {
                            _trail[x][y] = FOOD;
                            _working_trail[x][y] = FOOD;
                        }
                    }
                    y++;
                }
            }
            if (br != null) {
                br.close();
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file_name);
        } catch (IOException e) {
            System.err.println("IOException: " + file_name);
        }

        for (food = 0, y = 0; y < GRID_HEIGHT; y++) {
            for (x = 0; x < GRID_WIDTH; x++) {
                if (_trail[x][y] == FOOD) {
                    food++;
                }
            }
        }
    }

    /**
     * Turn the ant left
     */
    public void left() {
        //System.out.print("r");
        if (get_Energy_Left()) {
            _energy--;
            if (_facing_current_Y < 0) {
                _facing_current_X = _current_X + 1;
                _facing_current_Y = _current_Y;
            } else if (_facing_current_Y > GRID_HEIGHT) {
                _facing_current_X = _current_X - 1;
                _facing_current_Y = _current_Y;
            } else if (_facing_current_X < 0) {
                _facing_current_Y = _current_Y - 1;
                _facing_current_X = _current_X;
            } else if (_facing_current_X > GRID_WIDTH) {
                _facing_current_Y = _current_Y + 1;
                _facing_current_X = _current_X;
            } else if (_facing_current_Y < _current_Y) {
                _facing_current_X = _current_X + 1;
                _facing_current_Y = _current_Y;
            } else if (_facing_current_Y > _current_Y) {
                _facing_current_X = _current_X - 1;
                _facing_current_Y = _current_Y;
            } else if (_facing_current_X < _current_X) {
                _facing_current_X = _current_X;
                _facing_current_Y = _current_Y - 1;
            } else if (_facing_current_X > _current_X) {
                _facing_current_X = _current_X;
                _facing_current_Y = _current_Y + 1;
            }
        }
    }

    /**
     * Turn the ant right
     */
    public void right() {
        if (get_Energy_Left()) {
            //System.out.print("l");
            _energy--;
            if (_facing_current_Y < 0) {
                _facing_current_X = _current_X - 1;
                _facing_current_Y = _current_Y;
            } else if (_facing_current_Y > GRID_HEIGHT) {
                _facing_current_X = _current_X + 1;
                _facing_current_Y = _current_Y;
            } else if (_facing_current_X < 0) {
                _facing_current_Y = _current_Y + 1;
                _facing_current_X = _current_X;
            } else if (_facing_current_X > GRID_WIDTH) {
                _facing_current_Y = _current_Y - 1;
                _facing_current_X = _current_X;
            } else if (_facing_current_Y < _current_Y) {
                _facing_current_X = _current_X - 1;
                _facing_current_Y = _current_Y;
            } else if (_facing_current_Y > _current_Y) {
                _facing_current_X = _current_X + 1;
                _facing_current_Y = _current_Y;
            } else if (_facing_current_X < _current_X) {
                _facing_current_X = _current_X;
                _facing_current_Y = _current_Y + 1;
            } else if (_facing_current_X > _current_X) {
                _facing_current_X = _current_X;
                _facing_current_Y = _current_Y - 1;
            }
        }
    }

    /**
     * Move the ant forward
     */
    public void move() {
        if (get_Energy_Left()) {
            //System.out.print("m");
            int old_current_X, old_current_Y;
            old_current_X = _current_X;
            old_current_Y = _current_Y;
            _energy--;
            if ((_facing_current_X < GRID_WIDTH) && !(_facing_current_X < 0) && (_facing_current_Y < GRID_HEIGHT) && !(_facing_current_Y < 0)) {
                _current_X = _facing_current_X;
                _current_Y = _facing_current_Y;
                if (_working_trail[_current_X][_current_Y] == 1) {
                    //System.out.print("P");
                    _picked_up++;
                    _working_trail[_current_X][_current_Y] = 0;
                }
                if (old_current_X < _current_X) {
                    _facing_current_X = _current_X + 1;
                    _facing_current_Y = _current_Y;
                }
                if (old_current_X > _current_X) {
                    _facing_current_X = _current_X - 1;
                    _facing_current_Y = _current_Y;
                }
                if (old_current_Y < _current_Y) {
                    _facing_current_Y = _current_Y + 1;
                    _facing_current_X = _current_X;
                }
                if (old_current_Y > _current_Y) {
                    _facing_current_Y = _current_Y - 1;
                    _facing_current_X = _current_X;
                }
            } else {
                if (_facing_current_X > GRID_WIDTH - 1) {
                    _current_X = 0;
                    _facing_current_X = 1;
                } else if (_facing_current_X < 0) {
                    _current_X = GRID_WIDTH - 1;
                    _facing_current_X = GRID_WIDTH - 2;
                } else {
                    if (_facing_current_Y > GRID_HEIGHT - 1) {
                        _current_Y = 0;
                        _facing_current_Y = 1;
                    } else if (_facing_current_Y < 0) {
                        _current_Y = GRID_HEIGHT - 1;
                        _facing_current_Y = GRID_HEIGHT - 2;
                    }
                }
                if (_working_trail[_current_X][_current_Y] == 1) {
                    //System.out.print("P");
                    _picked_up++;
                    _working_trail[_current_X][_current_Y] = 0;
                }
            }
            _working_trail[_current_X][_current_Y] = 8;
        }
    }

    /**
     * Check if there is food in the square ahead. 0 is false, 1 is true.
     * @return food ahead is 1, else 0
     */
    public int food_ahead() {
        int is_there = 0;
        if ((_facing_current_X < GRID_WIDTH) && !(_facing_current_X < 0) && (_facing_current_Y < GRID_HEIGHT) && !(_facing_current_Y < 0)) {
            if (_working_trail[_facing_current_X][_facing_current_Y] == 1) {
                is_there = 1;
            } else {
                is_there = 0;
            }
        } else {
            if (_facing_current_X > GRID_WIDTH - 1) {
                if (_working_trail[0][_current_Y] == 1) {
                    is_there = 1;
                } else {
                    is_there = 0;
                }
            } else if (_facing_current_X < 0) {
                if (_working_trail[GRID_WIDTH - 1][_current_Y] == 1) {
                    is_there = 1;
                } else {
                    is_there = 0;
                }
            } else {
                if (_facing_current_Y > GRID_HEIGHT - 1) {
                    if (_working_trail[_current_X][0] == 1) {
                        is_there = 1;
                    } else {
                        is_there = 0;
                    }
                } else if (_facing_current_Y < 0) {
                    if (_working_trail[_current_X][GRID_HEIGHT - 1] == 1) {
                        is_there = 1;
                    } else {
                        is_there = 0;
                    }
                }
            }
        }
        return is_there;
    }
}