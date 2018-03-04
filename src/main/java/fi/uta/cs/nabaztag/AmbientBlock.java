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

public class AmbientBlock extends Block {
	
	public static final int NOSE_NO_BLINK = 0;
	public static final int NOSE_SINGLE_BLINK = 1;
	public static final int NOSE_DOUBLE_BLINK = 2;
	
	/**
	 * Generates AmbientBlock from old data
	 * 
	 * @param size
	 * @param data
	 */
	public AmbientBlock(int size, int [] data) {
		super(0x04, size, data);
	}
	
	/**
	 * Generates empty message block
	 *
	 */
	public AmbientBlock() {
		super(0x04);
		
		//create new data with no blinking value
		data = new int[23];
		size = 23;
		
		//and fill it with nothing
		for (int i = 0; i < data.length; i++) {
			data[i] = 0;
		}
		
		data[0] = 0x7f;
		data[1] = 0xff;
		data[2] = 0xff;
		data[3] = 0xff;
	}
	
	public int getRightEarValue() {
		return data[20];
	}
	
	public int getLeftEarValue() {
		return data[21];
	}
	
	public void createTestData() {
		data = new int[24];
		
		data[0] = 127;
		data[1] = 255;
		data[2] = 255;
		data[3] = 255;
		data[4] = 1;
		for (int i = 5; i < 20; i++) {
			data[i] = 0;
		}
		
		//data[8] = 128;
		
		data[20] = 16;
		data[21] = 8;
		data[22] = 5;
		data[23] = 0;
		
		size = 24;
		type = 4;
	}
	
	public void setRightEarValue(byte value) {
		data[20] = value;
	}
	
	public void setLeftEarValue(byte value) {
		data[21] = value;
	}
	
	// 0 = no blinking / 1 = single blinking / 2 = double blinking
	public int getNoseValue() {
		return data.length - 23;
	}
	
	/**
	 * Setting nose value
	 * 
	 * @param value
	 */
	public void setNoseValue(int value) {
		//ignore wrong values
		if (value < NOSE_NO_BLINK || value > NOSE_DOUBLE_BLINK)
			return;
		
		int [] newdata = new int[23+value];
		
		for (int i = 0; i < 22; i++) 
			newdata[i] = data[i];
		
		for (int i = 0; i < value; i++)
			newdata[22+i] = 0x05;
		
		newdata[22+value] = 0;
		
		size = newdata.length;
		data = newdata;
	}
}
