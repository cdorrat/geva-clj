/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Rieps - Free Java  postscript API
 * Copyright (C) 2007 TIIE http://www.tiie.fr
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * TIIE/RIE Technologies
 * Z.A de la Duquerie
 * 37390 Chanceaux sur Choisille FRANCE
 * http://www.tiie.fr
 *
 * main developper : Yves Piel ( yvespielusenet AT free.fr )
 */

/*
 * DeviceParameter.java
 *
 * Created on 26 fevrier 2007, 11:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rie.rieps.engine;

/**
 *
 * @author Yves Piel
 */
public class DeviceParameter {
    
    /** Parameter to set the page size.
     * The value must be a float array : [width, height].
     */
    public static DeviceParameter PAGE_SIZE = new DeviceParameter();
    
    /** Parameter to set duplex mode.
     * The value must be a boolean.
     */
    public static DeviceParameter DUPLEX = new DeviceParameter();
    
    /** Parameter to set the media type of pages.
     * The value must be an integer.
     */
    public static DeviceParameter MEDIA_TYPE = new DeviceParameter();
    
    /** Parameter to rotate 180 degrees the page.
     *the value must be a boolean.
     */
    public static DeviceParameter TUMBLE = new DeviceParameter();
    
    /** Creates a new instance of DeviceParameter */
    private DeviceParameter() {
    }
    
}
