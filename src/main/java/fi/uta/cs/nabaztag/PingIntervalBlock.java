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

public class PingIntervalBlock extends Block {
	
	/**
	 * Setting ping interval with data
	 * 
	 * @param data
	 */
	public PingIntervalBlock(int [] data) {
		super(3, 1, data);
	}
	
	/**
	 * Create ping interval block with given seconds
	 * 
	 * @param length ping interval in seconds
	 */
	public PingIntervalBlock(int length) {
		super(3);
		size = 1;
		data = new int[] { length };
	}
	
	public String toString() {
		String old = super.toString();
		old += "\n[Ping interval block set for "+data[0]+" seconds]";
		return old;
	}
}
