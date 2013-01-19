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

import java.util.Iterator;

import com.hyphenated.card.Card;
import com.google.common.collect.Iterators;

/**
 * Iterator used for iterating over cards contained in several card holders.
 */
public class CardIterator implements Iterator<Card> {

	private final Iterator<CardHolder> cardHolderIterator;
	private Iterator<Card> cardIterator;

	public CardIterator(CardHolder... cardHolders) {
		super();
		this.cardHolderIterator = Iterators.forArray(cardHolders);
		this.cardIterator = Iterators.emptyIterator();
	}

	@Override
	public boolean hasNext() {
		scrollToNonemptyCardHolder();
		return cardIterator.hasNext();
	}

	@Override
	public Card next() {
		scrollToNonemptyCardHolder();
		return cardIterator.next();
	}

	/**
	 * Scrolls the iterators to the closest nonempty card holder.
	 */
	private void scrollToNonemptyCardHolder() {
		while (!cardIterator.hasNext() && cardHolderIterator.hasNext()) {
			cardIterator = cardHolderIterator.next().iterator();
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("operation not supported");
	}

}
