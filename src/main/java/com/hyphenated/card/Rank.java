package com.hyphenated.card;

/**
 * Enumeration of the card ranks.
 */
public enum Rank {

	TWO("2"),
	THREE("3"),
	FOUR("4"),
	FIVE("5"),
	SIX("6"),
	SEVEN("7"),
	EIGHT("8"),
	NINE("9"),
	TEN("T"),
	JACK("J"),
	QUEEN("Q"),
	KING("K"),
	ACE("A");

	/**
	 * Short string representation of the rank.
	 */
	private final String name;

	private Rank(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the rank.
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}
