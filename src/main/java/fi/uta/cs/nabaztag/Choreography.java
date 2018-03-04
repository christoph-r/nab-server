/* 
* ###################################################################
* 
* jNabServer - java Nabaztag/tag server
*
* Nabaztag (fi/uta/cs/nabaztag/)
*
* Copyright (c) 1998-2007 University of Tampere
* Speech-based and Pervasive Interaction Group
* Tampere Unit for Human-Computer Interaction (TAUCHI)
* Department of Computer Sciences
*
* http://www.cs.uta.fi/hci/spi/jnabserver/
* nabaztag@cs.uta.fi
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or any later version.
*  
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
* USA
* 
* ###################################################################
*/

package fi.uta.cs.nabaztag;

import java.util.ArrayList;

/**
 * Choreography file generator.
 * 
 * 
 * @author Juha-Pekka Rajaniemi
 * @author Ville Antila
 */
public class Choreography {
	
	/** ArrayList containing the choreography data for the Nabaztag -object
	 */
	private ArrayList<Integer> data;
	private int[] lastBytes = new int [] {0, 0, 0, 0};
	
	/** Constant integer variable for specifying the bottomled
	 */
	public static final int LED_BOTTOM = 0;
	/** Constant integer variable for specifying the leftled
	 */
	public static final int LED_LEFT = 1;
	/** Constant integer variable for specifying the centerled
	 */
	public static final int LED_CENTER = 2;
	/** Constant integer variable for specifying the rightled
	 */
	public static final int LED_RIGHT = 3;
	/** Constant integer variable for specifying the topled
	 */
	public static final int LED_TOP = 4;
	
	/** Constant integer variable for specifying the right ear
	 */
	public static final int EAR_RIGHT = 0;
	/** Constant integer variable for specifying the left ear
	 */
	public static final int EAR_LEFT = 1;
	
	/** Constant integer variable for specifying the direction to forward
	 */
	public static final int DIRECTION_FORWARD = 0;
	/** Constant integer variable for specifying the direction to backward
	 */
	public static final int DIRECTION_BACKWARD = 1;
	
	/** Instance variable String specifying the name of the choreography  
	 */
	private String name;
	
	public Choreography(String name) {
		data = new ArrayList<Integer>();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	/**
	 * Generates all data and returns it
	 * 
	 * @return
	 */
	public int[] getContent() {
		int [] returnData = new int[data.size()+8];
		byte [] lengthBytes = intToByteArray(data.size());
		
		returnData[0] = 0;
		returnData[1] = lengthBytes[1];
		returnData[2] = lengthBytes[2];
		returnData[3] = lengthBytes[3];
		returnData[data.size()+4] = lastBytes[0];
		returnData[data.size()+5] = lastBytes[1];
		returnData[data.size()+6] = lastBytes[2];
		returnData[data.size()+7] = lastBytes[3];
		
		for (int i = 0; i < data.size(); i++) {
			returnData[4+i] = data.get(i);
		}
		
		return returnData;
	}
	
	/**
	 * Sets tempo on time
	 * 
	 * @param time
	 * @param tempo
	 */
	public void addTempo(int time, int tempo) {
		data.add(time);
		data.add(1);
		data.add(tempo);
	}
	
	/**
	 * Changes led to some value
	 * For example to change top led to bright red
	 * addLedCommand(20, Choreography.LED_TOP, 255, 0, 0) 
	 * 
	 * @param time at tempo beat number
	 * @param led led color to set
	 * @param r red value of led (0-255)
	 * @param g green value of led (0-255)
	 * @param b blue value of led (0-255)
	 */
	public void addLedCommand(int time, int led, int r, int g, int b) {
		data.add(time);
		data.add(7);
		data.add(led);
		data.add(r);
		data.add(g);
		data.add(b);
		data.add(0);
		data.add(0);
	}
	
	/**
	 * Moves ear to some position
	 * Example:
	 * addEarCommand(30, Cheography.EAR_RIGHT, 5, Cheography.DIRECTION_FORWARD);
	 * 
	 * @param time at tempo beat number 
	 * @param ear which ear to move
	 * @param pos what position (0-12)
	 * @param direction which direction
	 */
	public void addEarMove(int time, int ear, int pos, int direction) {
		data.add(time);
		data.add(8);
		data.add(ear);
		data.add(pos);
		data.add(direction);
	}
	
	/**
	 * Sets led to value that has been set in MessageBlock with PL command
	 * 
	 * @param time
	 * @param led
	 * @param value
	 */
	public void addSetPaletteForLed(int time, int led, int value) {
		data.add(time);
		data.add(0xE);
		data.add(led);
		data.add(value);
	}
	
	/**
	 * Plays random midi on tempo beat number
	 * 
	 * @param time
	 */
	public void addPlayRandomMidi(int time) {
		data.add(time);
		data.add(0x10);
	}
	
	/**
	 * Moves ear n steps forward
	 * 
	 * @param time
	 * @param ear ear to move
	 * @param steps steps to move
	 */
	public void addEarMoveForward(int time, int ear, int steps) {
		data.add(time);
		data.add(0x11);
		data.add(ear);
		data.add(steps);
	}
	
	/**
	 * Creates four byte long version of int
	 * Command
	 * @param value
	 * @return
	 */
	private byte[] intToByteArray(int value) {
		return new byte[]{(byte)(value >> 32 & 0xff), (byte)(value >> 16 & 0xff), (byte)(value >> 8 & 0xff), (byte)(value & 0xff)};
	}
}
