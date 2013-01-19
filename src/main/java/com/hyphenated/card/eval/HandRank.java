/*
 * This file is part of fisth, an FSM-based Texas Hold'em hand evaluator.
 * Copyright (C) 2010 Robert Strack <robert.strack@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.hyphenated.card.eval;

import java.io.Serializable;

/**
 * Representation of the poker hand strength.
 */
public class HandRank implements Comparable<HandRank>, Serializable {

	private static final long serialVersionUID = 6897360347770643227L;

	/**
	 * Value of the rank (the higher the value is the stronger the hand is). The
	 * value is the number of all weaker card combinations. For instance, the
	 * hand rank value of the royal flush is equal to 7461 because there are
	 * exactly 7461 distinct weaker hands.
	 */
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

}
