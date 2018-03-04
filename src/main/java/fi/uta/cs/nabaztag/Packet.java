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
 * Standard packet to be send in to nabaztag. 
 * 
 * @author Juha-Pekka Rajaniemi
 *
 */
public class Packet {
	protected int [] data;
	
	//head byte
	protected final int headerByte = 0x7F;
	//foot byte 1
	protected final int footerByte1 = 0xFF;
	//foot byte 2
	protected final int footerByte2 = 0x0A;
	
	private ArrayList<Block> blocks;


	/**
	 * Cretes new empty Packet to fill with
	 * blocks and generate binary array.
	 *
	 */
	public Packet() {
		blocks = new ArrayList<Block>();
	}
	
	/**
	 * Parses data to blocks.
	 * 
	 * @param data data to parse
	 */
	public Packet(int [] data) {
		this();
		
		//set full data
		this.data = data;

		parseBlocks();
	}
	
	public void addBlock(Block b) {
		blocks.add(b);
	}
	
	public ArrayList<Block> getBlocks() {
		return blocks;
	}
	
	private void parseBlocks() {
		int pos = 1;
		
		//read while there is more than two int's left
		while (pos < data.length - 2) {
			int type = data[pos];
			int size = data[pos+1] + data[pos+2] + data[pos+3];
			
			pos += 4;
			
			int [] binary = new int[size];
			
			for (int i = 0; i < size; i++) {
				binary[i] = data[pos+i];
			}
			
			pos += size;
			
			switch (type) {
				case 0x0A:
					blocks.add(new MessageBlock(size, binary));
					break;
				case 0x04:
					blocks.add(new AmbientBlock(size, binary));
					break;
				case 0x03:
					blocks.add(new PingIntervalBlock(binary));
					break;
				default:
					blocks.add(new Block(type, size, binary));
			}
			
			
		}
	}
	
	/**
	 * 
	 * Generates int array for to sending
	 * 
	 * TODO: fix size. Currently supports size to 65305 bytes
	 * 
	 * @return packet
	 */
	public int [] generatePacket() {
		ArrayList<Integer> packet = new ArrayList<Integer>();
		
		packet.add(headerByte);

		//read through blocks and create packet
		for (Block b : blocks) {
			packet.add(b.getType());
			
			byte [] sizeBytes = intToByteArray(b.getSize());
			
			packet.add((int) sizeBytes[0]);
			packet.add((int) sizeBytes[1]);
			packet.add((int) sizeBytes[2]);
			
			int [] data = b.getData();
			for (int i = 0; i < data.length; i++) {
				packet.add(data[i]);
			}
		}
		
		packet.add(footerByte1);
		packet.add(footerByte2);
		
		//converting from Integer ArrayList to int[] array
		int [] temp = new int[packet.size()];
		
		int j = 0;
		for (int i : packet) {
			temp[j] = i;
			j++;
		}
		
		return temp;
	}
	
	/**
	 * Return 3 byte long version of int...
	 * 
	 * @param value
	 * @return
	 */
	private byte[] intToByteArray(int value) {
		return new byte[]{(byte)(value >> 16 & 0xff), (byte)(value >> 8 & 0xff), (byte)(value & 0xff)};
	}
	
}
