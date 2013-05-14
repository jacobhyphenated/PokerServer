package com.hyphenated.card;

/**
 * Enumeration of the card suits.
 */
public enum Suit {

	CLUBS("c"),
	DIAMONDS("d"),
	HEARTS("h"),
	SPADES("s");

	/**
	 * Short string representation of the suit (one letter symbol).
	 */
	private final String name;

	private Suit(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the suit.
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}
