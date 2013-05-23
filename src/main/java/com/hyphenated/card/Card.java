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
package com.hyphenated.card;

import static com.hyphenated.card.Rank.ACE;
import static com.hyphenated.card.Rank.EIGHT;
import static com.hyphenated.card.Rank.FIVE;
import static com.hyphenated.card.Rank.FOUR;
import static com.hyphenated.card.Rank.JACK;
import static com.hyphenated.card.Rank.KING;
import static com.hyphenated.card.Rank.NINE;
import static com.hyphenated.card.Rank.QUEEN;
import static com.hyphenated.card.Rank.SEVEN;
import static com.hyphenated.card.Rank.SIX;
import static com.hyphenated.card.Rank.TEN;
import static com.hyphenated.card.Rank.THREE;
import static com.hyphenated.card.Rank.TWO;
import static com.hyphenated.card.Suit.CLUBS;
import static com.hyphenated.card.Suit.DIAMONDS;
import static com.hyphenated.card.Suit.HEARTS;
import static com.hyphenated.card.Suit.SPADES;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Enumeration of all cards used in Texas Hold'em.
 */
//Serialize a member of this enum to JSON using the enum's toString method
@JsonSerialize(using=ToStringSerializer.class)
public enum Card {

	TWO_OF_CLUBS(TWO, CLUBS, 1),
	THREE_OF_CLUBS(THREE, CLUBS, 5),
	FOUR_OF_CLUBS(FOUR, CLUBS, 9),
	FIVE_OF_CLUBS(FIVE, CLUBS, 13),
	SIX_OF_CLUBS(SIX, CLUBS, 17),
	SEVEN_OF_CLUBS(SEVEN, CLUBS, 21),
	EIGHT_OF_CLUBS(EIGHT, CLUBS, 25),
	NINE_OF_CLUBS(NINE, CLUBS, 29),
	TEN_OF_CLUBS(TEN, CLUBS, 33),
	JACK_OF_CLUBS(JACK, CLUBS, 37),
	QUEEN_OF_CLUBS(QUEEN, CLUBS, 41),
	KING_OF_CLUBS(KING, CLUBS, 45),
	ACE_OF_CLUBS(ACE, CLUBS, 49),

	TWO_OF_DIAMONDS(TWO, DIAMONDS, 2),
	THREE_OF_DIAMONDS(THREE, DIAMONDS, 6),
	FOUR_OF_DIAMONDS(FOUR, DIAMONDS, 10),
	FIVE_OF_DIAMONDS(FIVE, DIAMONDS, 14),
	SIX_OF_DIAMONDS(SIX, DIAMONDS, 18),
	SEVEN_OF_DIAMONDS(SEVEN, DIAMONDS, 22),
	EIGHT_OF_DIAMONDS(EIGHT, DIAMONDS, 26),
	NINE_OF_DIAMONDS(NINE, DIAMONDS, 30),
	TEN_OF_DIAMONDS(TEN, DIAMONDS, 34),
	JACK_OF_DIAMONDS(JACK, DIAMONDS, 38),
	QUEEN_OF_DIAMONDS(QUEEN, DIAMONDS, 42),
	KING_OF_DIAMONDS(KING, DIAMONDS, 46),
	ACE_OF_DIAMONDS(ACE, DIAMONDS, 50),

	TWO_OF_HEARTS(TWO, HEARTS, 3),
	THREE_OF_HEARTS(THREE, HEARTS, 7),
	FOUR_OF_HEARTS(FOUR, HEARTS, 11),
	FIVE_OF_HEARTS(FIVE, HEARTS, 15),
	SIX_OF_HEARTS(SIX, HEARTS, 19),
	SEVEN_OF_HEARTS(SEVEN, HEARTS, 23),
	EIGHT_OF_HEARTS(EIGHT, HEARTS, 27),
	NINE_OF_HEARTS(NINE, HEARTS, 31),
	TEN_OF_HEARTS(TEN, HEARTS, 35),
	JACK_OF_HEARTS(JACK, HEARTS, 39),
	QUEEN_OF_HEARTS(QUEEN, HEARTS, 43),
	KING_OF_HEARTS(KING, HEARTS, 47),
	ACE_OF_HEARTS(ACE, HEARTS, 51),

	TWO_OF_SPADES(TWO, SPADES, 4),
	THREE_OF_SPADES(THREE, SPADES, 8),
	FOUR_OF_SPADES(FOUR, SPADES, 12),
	FIVE_OF_SPADES(FIVE, SPADES, 16),
	SIX_OF_SPADES(SIX, SPADES, 20),
	SEVEN_OF_SPADES(SEVEN, SPADES,24),
	EIGHT_OF_SPADES(EIGHT, SPADES,28),
	NINE_OF_SPADES(NINE, SPADES, 32),
	TEN_OF_SPADES(TEN, SPADES, 36),
	JACK_OF_SPADES(JACK, SPADES, 40),
	QUEEN_OF_SPADES(QUEEN, SPADES, 44),
	KING_OF_SPADES(KING, SPADES, 48),
	ACE_OF_SPADES(ACE, SPADES, 52);

	/**
	 * Card rank.
	 */
	private final Rank rank;

	/**
	 * Card suit.
	 */
	private final Suit suit;
	
	private int evaluation;

	private Card(Rank rank, Suit suit, int evaluation) {
		this.rank = rank;
		this.suit = suit;
		this.evaluation = evaluation;
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
	
	public int getEvaluation(){
		return evaluation;
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
