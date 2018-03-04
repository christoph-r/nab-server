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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class MessageBlock extends Block {
	
	private byte [] chars;

	//this is the inverse (module 256) of (2x+1) for x=0 to 127
	int invtable[] = new int[] {1, 171, 205, 183, 57, 163, 197, 239, 241, 27, 61, 167, 41, 19, 53, 223, 225, 139, 173, 151, 25, 131, 165, 207, 209, 251, 29, 135, 9, 243, 21, 191, 193, 107, 141, 119, 249, 99, 133, 175, 177, 219, 253, 103, 233, 211, 245, 159, 161, 75, 109, 87, 217, 67, 101, 143, 145, 187, 221, 71, 201, 179, 213, 127, 129, 43, 77, 55, 185, 35, 69, 111, 113, 155, 189, 39, 169, 147, 181, 95, 97, 11, 45, 23, 153, 3, 37, 79, 81, 123, 157, 7, 137, 115, 149, 63, 65, 235, 13, 247, 121, 227, 5, 47, 49, 91, 125, 231, 105, 83, 117, 31, 33, 203, 237, 215, 89, 195, 229, 15, 17, 59, 93, 199, 73, 51, 85, 255};
	
	/**
	 * When creating new empty messageblock this contructor
	 * should be used and then encode() method
	 *
	 */
	public MessageBlock() {
		super(0x0A, 0, null);
	}
	
	/**
	 * When decoding data to readable text this constructor
	 * should be used.
	 * 
	 * @param size
	 * @param data
	 */
	public MessageBlock(int size, int [] data) {
		super(0x0A, size, data);
	}
	
	/**
	 * Returns current data in readable form. If data has not 
	 * been decoded then this method will do it.
	 * 
	 * @return decoded data content
	 */
	public String getContent() {
		if (chars == null)
			decode();
		
		try {
			return new String(chars, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			return new String(chars);
		}
	}
	
	/**
	 * Decodes current data for getContent()
	 *
	 */
	public void decode() {
		chars = new byte[size];
		
		char currentChar = 35;
		
		for(int i = 1; i < data.length; i++) {
			char code = (char) data[i];
			currentChar = (char) (((code-47)*(1+2*currentChar))%256);
			chars[i] = (byte) currentChar;
		}
	}
	
	/**
	 * Encodes and sets given string
	 * 
	 * @param text String to encode
	 */
	public void encode(String text) {
		// Create the encoder and decoder for ISO-8859-1
	    Charset charset = Charset.forName("ISO-8859-1");
	    CharsetDecoder decoder = charset.newDecoder();
	    CharsetEncoder encoder = charset.newEncoder();
	    
	    String newData = null;
	    
	    try {
	        // Convert a string to ISO-LATIN-1 bytes in a ByteBuffer
	        // The new ByteBuffer is ready to be read.
	        ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(text));
	    
	        // Convert ISO-LATIN-1 bytes in a ByteBuffer to a character ByteBuffer and then to a string.
	        // The new ByteBuffer is ready to be read.
	        CharBuffer cbuf = decoder.decode(bbuf);
	        newData = cbuf.toString();
	        
	    } catch (CharacterCodingException e) {}
		
		size = newData.length() + 1;
		data = new int[size];
		
		data[0] = 1;
		
		int previousChar = 35;
		char currentChar;
		int code;
		
		for (int i = 0; i < newData.length(); i++) {
			currentChar = (char) newData.charAt(i);
			code = ((invtable[previousChar % 128]*currentChar+47) % 256);
			previousChar = currentChar;
			data[i+1] = code;
		}
		
		chars = null;
	}

	public String toString() {
		String old = super.toString();
		old += "\n[MessageBlock data: "+getContent()+"]";
		return old;
	}
}
