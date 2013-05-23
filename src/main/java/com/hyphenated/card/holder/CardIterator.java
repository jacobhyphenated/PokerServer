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
