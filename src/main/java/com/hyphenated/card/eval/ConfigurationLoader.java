/*
The MIT License (MIT)

Copyright (c) 2013 Jacob Kanipe-Illig

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.hyphenated.card.eval;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.google.common.io.Closeables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for loading configuration and resource files
 */
public class ConfigurationLoader {
	
	private static final int HAND_RANK_SIZE = 32487834;
	private static Logger log = LoggerFactory.getLogger(ConfigurationLoader.class);

	/**
	 * Load hand rank lookup table for poker hands.
	 * This will load the file and do the byte conversions so we get a nice integer array back.
	 * @param name file name of the precomputed hand rank file
	 * @return integer array of hand rank lookup values in accordance with the 2+2 hand evaluation algorithm.
	 * @throws RuntimeException If loading this file fails, prepare to crash because hand evals will not work
	 */
	public int[] loadHandRankResource(String name)
			throws RuntimeException {
		int handRankArray[] = new int[HAND_RANK_SIZE];
		try {
			int tableSize = HAND_RANK_SIZE * 4;
	        byte[] b = new byte[tableSize];
	        InputStream br = null;
			try {
				br = new BufferedInputStream(ConfigurationLoader.class.getResourceAsStream(name));
				int bytesRead = br.read(b, 0, tableSize);
	            if (bytesRead != tableSize) {
	                log.error("Read " + bytesRead + " bytes out of " + tableSize);
	            }
			} finally {
				Closeables.closeQuietly(br);
			}
			for (int i = 0; i < HAND_RANK_SIZE; i++) {
	            handRankArray[i] = littleEndianByteArrayToInt(b, i * 4);
	        }
			return handRankArray;
		} catch (IOException e) {
			throw new RuntimeException("cannot read resource " + name, e);
		}
	}
	
	private static final int littleEndianByteArrayToInt(byte[] b, int offset) {
        return (b[offset + 3] << 24) + ((b[offset + 2] & 0xFF) << 16)
                + ((b[offset + 1] & 0xFF) << 8) + (b[offset] & 0xFF);
    }

}
