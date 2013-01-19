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
package com.hyphenated.card;

import static com.hyphenated.card.Rank.*;
import static com.hyphenated.card.Suit.*;

/**
 * Enumeration of all cards used in Texas Hold'em.
 */
public enum Card {

	TWO_OF_CLUBS(TWO, CLUBS),
	THREE_OF_CLUBS(THREE, CLUBS),
	FOUR_OF_CLUBS(FOUR, CLUBS),
	FIVE_OF_CLUBS(FIVE, CLUBS),
	SIX_OF_CLUBS(SIX, CLUBS),
	SEVEN_OF_CLUBS(SEVEN, CLUBS),
	EIGHT_OF_CLUBS(EIGHT, CLUBS),
	NINE_OF_CLUBS(NINE, CLUBS),
	TEN_OF_CLUBS(TEN, CLUBS),
	JACK_OF_CLUBS(JACK, CLUBS),
	QUEEN_OF_CLUBS(QUEEN, CLUBS),
	KING_OF_CLUBS(KING, CLUBS),
	ACE_OF_CLUBS(ACE, CLUBS),

	TWO_OF_DIAMONDS(TWO, DIAMONDS),
	THREE_OF_DIAMONDS(THREE, DIAMONDS),
	FOUR_OF_DIAMONDS(FOUR, DIAMONDS),
	FIVE_OF_DIAMONDS(FIVE, DIAMONDS),
	SIX_OF_DIAMONDS(SIX, DIAMONDS),
	SEVEN_OF_DIAMONDS(SEVEN, DIAMONDS),
	EIGHT_OF_DIAMONDS(EIGHT, DIAMONDS),
	NINE_OF_DIAMONDS(NINE, DIAMONDS),
	TEN_OF_DIAMONDS(TEN, DIAMONDS),
	JACK_OF_DIAMONDS(JACK, DIAMONDS),
	QUEEN_OF_DIAMONDS(QUEEN, DIAMONDS),
	KING_OF_DIAMONDS(KING, DIAMONDS),
	ACE_OF_DIAMONDS(ACE, DIAMONDS),

	TWO_OF_HEARTS(TWO, HEARTS),
	THREE_OF_HEARTS(THREE, HEARTS),
	FOUR_OF_HEARTS(FOUR, HEARTS),
	FIVE_OF_HEARTS(FIVE, HEARTS),
	SIX_OF_HEARTS(SIX, HEARTS),
	SEVEN_OF_HEARTS(SEVEN, HEARTS),
	EIGHT_OF_HEARTS(EIGHT, HEARTS),
	NINE_OF_HEARTS(NINE, HEARTS),
	TEN_OF_HEARTS(TEN, HEARTS),
	JACK_OF_HEARTS(JACK, HEARTS),
	QUEEN_OF_HEARTS(QUEEN, HEARTS),
	KING_OF_HEARTS(KING, HEARTS),
	ACE_OF_HEARTS(ACE, HEARTS),

	TWO_OF_SPADES(TWO, SPADES),
	THREE_OF_SPADES(THREE, SPADES),
	FOUR_OF_SPADES(FOUR, SPADES),
	FIVE_OF_SPADES(FIVE, SPADES),
	SIX_OF_SPADES(SIX, SPADES),
	SEVEN_OF_SPADES(SEVEN, SPADES),
	EIGHT_OF_SPADES(EIGHT, SPADES),
	NINE_OF_SPADES(NINE, SPADES),
	TEN_OF_SPADES(TEN, SPADES),
	JACK_OF_SPADES(JACK, SPADES),
	QUEEN_OF_SPADES(QUEEN, SPADES),
	KING_OF_SPADES(KING, SPADES),
	ACE_OF_SPADES(ACE, SPADES);

	/**
	 * Card rank.
	 */
	private final Rank rank;

	/**
	 * Card suit.
	 */
	private final Suit suit;

	private Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}

	/**
	 * Returns the card rank.
	 * 
	 * @return rank card rank
	 */
	public Rank getRank() {
		return rank;
	}

	/**
	 * Returns the card suit.
	 * 
	 * @return suit card suit
	 */
	public Suit getSuit() {
		return suit;
	}

	/**
	 * Returns the name of the card.
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return rank.toString() + suit.toString();
	}

}
