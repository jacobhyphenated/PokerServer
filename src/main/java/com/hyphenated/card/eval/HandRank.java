package com.hyphenated.card.eval;

import java.io.Serializable;

import com.hyphenated.card.HandType;

/**
 * Representation of the poker hand strength.
 */
public class HandRank implements Comparable<HandRank>, Serializable {

	private static final long serialVersionUID = 6897360347770643227L;

	private final int rankValue;

	public HandRank(int rankValue) {
		super();
		this.rankValue = rankValue;
	}

	/**
	 * Compares the strength of the hand (the values of the ranks).
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(HandRank rank) {
		return rankValue < rank.rankValue ? -1
				: (rankValue == rank.rankValue ? 0 : 1);
	}

	@Override
	public final int hashCode() {
		return rankValue;
	}

	@Override
	public final boolean equals(Object obj) {
		return (obj instanceof HandRank)
				&& (rankValue == ((HandRank) obj).rankValue);
	}

	public final int getValue() {
		return rankValue;
	}
	
	/**
	 * The type of hand as represented by {@link HandType}
	 * @return
	 */
	public HandType getHandType(){
		return HandType.values()[rankValue >> 12];
	}

}
