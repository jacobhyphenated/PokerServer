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

import com.hyphenated.card.Card;

/**
 * A CardHolder for storing 5 community cards.
 */
public class Hand extends CardHolder {

	private static final long serialVersionUID = 6963213701843967416L;

	/**
	 * Creates new hand with two cards.
	 * 
	 * @param card1
	 *            first card
	 * @param card2
	 *            second card
	 */
	public Hand(Card card1, Card card2) {
		super(card1, card2);
	}

}
