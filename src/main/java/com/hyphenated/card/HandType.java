package com.hyphenated.card;
/**
 * Enum that represents the possible types of poker hands.
 * <br /><br />
 * The ordering here corresponds to the handtypes defined by the highest order
 * bit in the two plus two hand evaluation score.
 * 
 * @author jacobhypehanted
 * Copyright (c) 2013
 */
public enum HandType {
	BAD,
	HIGH_CARD,
	PAIR, 
	TWO_PAIR,
	THREE_OF_A_KIND,
	STRAIGHT,
	FLUSH,
	FULL_HOUSE,
	FOUR_OF_A_KIND,
	STRAIGHT_FLUSH;
}
