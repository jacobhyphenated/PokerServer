package com.hyphenated.card.eval;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.google.common.io.Closeables;

/**
 * Utility class for loading configuration and resource files
 */
public class ConfigurationLoader {
	
	private static final int HAND_RANK_SIZE = 32487834;
	private static Logger log = Logger.getLogger(ConfigurationLoader.class);

	/*
	 * Load hand rank lookup table for Poker hands
	 */
	public int[] loadHandRankResource(String name)
			throws RuntimeException {
		int HR[] = new int[HAND_RANK_SIZE];
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
	            HR[i] = littleEndianByteArrayToInt(b, i * 4);
	        }
			return HR;
		} catch (IOException e) {
			throw new RuntimeException("cannot read resource " + name, e);
		}
	}
	
	private static final int littleEndianByteArrayToInt(byte[] b, int offset) {
        return (b[offset + 3] << 24) + ((b[offset + 2] & 0xFF) << 16)
                + ((b[offset + 1] & 0xFF) << 8) + (b[offset] & 0xFF);
    }

}
