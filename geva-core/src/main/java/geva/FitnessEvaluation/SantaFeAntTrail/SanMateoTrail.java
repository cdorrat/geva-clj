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
import java.util.LinkedList;
/**
 * Taken from lib GE and refactored to java
 * @author erikhemberg
 */
public class SanMateoTrail extends Trail {

    public static final int GRID_WIDTH = 13;
    public static final int GRID_HEIGHT = 13;
    public static LinkedList<int[][]> trails;

    public static int[][] _trail1 = {
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,1,1,0,0,0,0},
	{0,0,0,0,0,0,0,0,1,0,0,0,0},
	{0,0,0,0,0,0,0,0,1,1,0,0,0},
	{0,0,0,0,0,0,0,0,0,1,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,1,0,0,0},
	{0,0,0,0,0,0,0,0,0,1,0,0,0}
    };
    public static int[][] _trail2 = {
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,1,1,0,0,0,0,0,0,0},
	{0,0,0,1,0,0,0,0,0,0,0,0,0},
	{0,0,0,1,0,0,0,0,0,0,0,0,0},
	{0,0,0,1,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,1,0,0,0,0,0,0,0,0,0,0},
	{0,0,1,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,1,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,1,0,0,0,0,0,0,0,0,0,0}
    };
    public static int[][] _trail3 = {
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,1,0,0,0,0},
	{0,0,0,0,0,0,0,0,1,0,0,0,0},
	{0,0,0,0,0,0,0,0,1,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,1,0,0},
	{0,0,0,0,0,0,0,0,0,0,1,0,0},
	{0,0,0,0,0,0,0,0,0,0,1,0,0},
	{0,0,0,0,0,0,0,0,0,0,1,0,0},
	{0,0,0,0,0,0,0,0,0,0,1,0,0}
    };
    public static int[][] _trail4 = {
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,1,1,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,1,0,0,0},
	{0,0,0,0,0,0,0,0,0,1,0,0,0},
	{0,0,0,0,0,0,0,0,0,1,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,1,0,0},
	{0,0,0,0,0,0,0,0,0,0,1,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,1,0,0},
	{0,0,0,0,0,0,0,0,0,0,1,0,0}
    };
    public static int[][] _trail5 = {
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,1,1,0,0,0,0,0,0,0},
	{0,0,1,0,0,0,0,0,0,0,0,0,0},
	{0,0,1,0,0,0,0,0,0,0,0,0,0},
	{0,0,1,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,1,0,0,0,0,0,0,0,0,0},
	{0,0,0,1,0,0,0,0,0,0,0,0,0}
    };
    public static int[][] _trail6 = {
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,1,1,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,1,0,0},
	{0,0,0,0,0,0,0,0,0,0,1,0,0},
	{0,0,0,0,0,0,0,0,0,0,1,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,1,0,0,0},
	{0,0,0,0,0,0,0,0,0,1,0,0,0}
    };
    public static int[][] _trail7 = {
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,1,0,1,0,0,0,0,0,0},
	{0,0,0,0,1,0,0,0,0,0,0,0,0},
	{0,0,0,0,1,0,0,0,0,0,0,0,0},
	{0,0,1,0,0,0,0,0,0,0,0,0,0},
	{0,0,1,0,0,0,0,0,0,0,0,0,0},
	{0,0,1,0,0,0,0,0,0,0,0,0,0},
	{0,0,1,0,0,0,0,0,0,0,0,0,0},
	{0,0,1,0,0,0,0,0,0,0,0,0,0}
    };
    public static int[][] _trail8 = {
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,1,0,0,1,0,0,0,0,0,0},
	{0,0,0,1,0,0,0,0,0,0,0,0,0},
	{0,0,0,1,0,0,0,0,0,0,0,0,0},
	{0,0,0,1,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,1,0,0,0,0,0,0,0,0,0}
    };
    public static int[][] _trail9 = {
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,0,0,0,0},
	{0,0,0,0,0,0,1,0,0,1,0,0,0},
	{0,0,0,0,0,0,0,0,0,1,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,1,0,0,0},
	{0,0,0,0,0,0,0,0,0,0,0,0,0},
	{0,0,0,0,0,0,0,0,0,1,0,0,0}
    };

    public int turn;
    public int move;
    public int trailCnt;

    /** Creates a new instance of Trail.
      */
    public SanMateoTrail() {
	super(615);
	//	System.out.println("----------WOHU-------------");
    }

    /**
     * Create new instance
     * @param energy ant stamina
     */
    public SanMateoTrail(int energy) {
        initGEtrail(energy);
    }

    /**
     * Initiate the trail
     * @param e energy for the trail
     */
    void initGEtrail(int e){
	//	System.out.println("initGEtrail e:"+e);
	trails = new LinkedList<int[][]>();
	trails.add(_trail1);
	trails.add(_trail2);
	trails.add(_trail3);
	trails.add(_trail4);
	trails.add(_trail5);
	trails.add(_trail6);
	trails.add(_trail7);
	trails.add(_trail8);
	trails.add(_trail9);
        _picked_up = 0;
        food = 96;
	//Get first trail
	getTrail(0);
    }

    /**
     * Get the trail. If Energy is <= 0 a new trail is started.
     * If there are more trails true is returned
     * @return if there are more trails
     */
    public boolean getTrail(int energy) {
	//	System.out.println("getTrail e:"+energy+" tS:"+trails.size()+" t:"+turn+" m:"+move);
	boolean ret = true;
	if(energy <=0) {
	    if(trails.size() > 0) {
		final int[][] cTrail = trails.removeFirst();
		//		System.out.println("getTrail t:"+trails.size());
		newTrail(cTrail);
	    } else {
		//		System.out.println("getTrail t:"+trails.size()+" false");
		ret = false;
	    }
	}
	return ret;
    }

    
    /**
     * According to Koza in GP2 doorstop 
     * each trail has 120 turns and 80 moves avalible
     */
    void newTrail(int[][] t) {
        _current_X=0;
        _current_Y=6;
        _facing_current_X=1;
        _facing_current_Y=6;
	turn = 0;
	move = 0;
	_energy = 615;
	_trail = t;
	_working_trail = new int[GRID_WIDTH][GRID_HEIGHT];
	for(int i=0; i<GRID_WIDTH; i++) {
	    for(int j=0; j<GRID_HEIGHT; j++) {
		_working_trail[i][j] = _trail[i][j];
	    }
	}
	//	System.out.println("newTrail:"+trails.size()+" t:"+turn+" m:"+move+" e:"+_energy);
    }

    @Override
    public void right() {
	//        System.out.print("r");
	turn++;
	super.right();
    }

    @Override
    public void left() {
	//        System.out.print("l");
	turn++;
	super.left();
    }

    @Override
    public int get_Energy() {
	if(move > 80 || turn > 120) {
	    //	    System.out.println("Full moved m:"+move+" t:"+turn+" e:"+_energy);
	    _energy = 0;
	}
	return _energy;
    }

    /**
     * Move the ant forward
     * Death on the electric fence
     */
    @Override
    public void move(){
	//        System.out.print("m");
        int old_current_X,old_current_Y;
	if(get_Energy() > 0) {
	    old_current_X=_current_X;
	    old_current_Y=_current_Y;
	    _energy--;
	    move++;
	    if( (_facing_current_X<GRID_WIDTH) && !(_facing_current_X<0)
                && (_facing_current_Y<GRID_HEIGHT) && !(_facing_current_Y<0)){
		_current_X=_facing_current_X;
		_current_Y=_facing_current_Y;
		if(_working_trail[_current_X][_current_Y]==1){
		    //		    System.out.print("P");
		    _picked_up++;
		    _working_trail[_current_X][_current_Y]=0;
		}
		if(old_current_X<_current_X){
		    _facing_current_X=_current_X+1;
		    _facing_current_Y=_current_Y;
		}
		if(old_current_X>_current_X){
		    _facing_current_X=_current_X-1;
		    _facing_current_Y=_current_Y;
		}
		if(old_current_Y<_current_Y){
		    _facing_current_Y=_current_Y+1;
		    _facing_current_X=_current_X;
		}
		if(old_current_Y>_current_Y){
		    _facing_current_Y=_current_Y-1;
		    _facing_current_X=_current_X;
		}
	    } else{
		//		System.out.println("Electocution");
		if(_facing_current_X>GRID_WIDTH-1){
		    _current_X=0;
		    _facing_current_X=1;
		    _energy = -1;
		} else if(_facing_current_X<0){
		    _current_X=GRID_WIDTH-1;
		    _facing_current_X=GRID_WIDTH-2;
		    _energy = -1;
		} else{
		    if(_facing_current_Y>GRID_HEIGHT-1){
			_current_Y=0;
			_facing_current_Y=1;
			_energy = -1;
		    } else if(_facing_current_Y<0){
			_current_Y=GRID_HEIGHT-1;
			_facing_current_Y=GRID_HEIGHT-2;
			_energy = -1;
		    }
		}
		if(_working_trail[_current_X][_current_Y]==1 && _energy > -1){
		    //		    System.out.print("P");
		    _picked_up++;
		    _working_trail[_current_X][_current_Y]=0;
		}
	    }
	    _working_trail[_current_X][_current_Y]=8;
	}
    }

    /**
     * Check if there is food in the square ahead. 0 is false, 1 is true.
     * @return food ahead is 1, else 0
     */
    public int food_ahead(){
        int is_there=0;
        if( (_facing_current_X<GRID_WIDTH) && !(_facing_current_X<0)
                && (_facing_current_Y<GRID_HEIGHT) && !(_facing_current_Y<0)){
            if(_working_trail[_facing_current_X][_facing_current_Y]==1){
                is_there=1;
            } else{
                is_there=0;
            }
        } else{
            if(_facing_current_X>GRID_WIDTH-1){
                if(_working_trail[0][_current_Y]==1) is_there=1;
                else is_there=0;
            } else if(_facing_current_X<0){
                if(_working_trail[GRID_WIDTH-1][_current_Y]==1) is_there=1;
                else is_there=0;
            } else{
                if(_facing_current_Y>GRID_HEIGHT-1){
                    if(_working_trail[_current_X][0]==1) is_there=1;
                    else is_there=0;
                } else if(_facing_current_Y<0){
                    if(_working_trail[_current_X][GRID_HEIGHT-1]==1) is_there=1;
                    else is_there=0;
                }
            }
        }
        return is_there;
    }


}