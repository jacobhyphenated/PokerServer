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
package com.hyphenated.card.holder;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

import com.hyphenated.card.Card;
import com.google.common.collect.Iterators;

/**
 * A container designed for storing cards.
 */
public class CardHolder implements Serializable {

	private static final long serialVersionUID = -6289334212535961129L;

	private final Card[] cards;

	protected CardHolder(Card... cards) {
		super();
		this.cards = cards;
	}

	/**
	 * Returns a card at particular index.
	 * 
	 * @param index
	 *            0 based card index
	 * @return a card at position index
	 */
	public Card getCard(int index) {
		return cards[index];
	}

	/**
	 * Returns the number of the cards stored in the container.
	 * 
	 * @return the number of cards
	 */
	public int getCardNumber() {
		return cards.length;
	}

	/**
	 * Creates a card iterator for the cards stored in the container.
	 * 
	 * @return unmodifiable card iterator
	 */
	public Iterator<Card> iterator() {
		return Iterators.forArray(cards);
	}

	@Override
	public String toString() {
		return Arrays.toString(cards);
	}

}
