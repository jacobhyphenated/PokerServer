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
 * A CardHolder for storing five community cards.
 */
public class Board extends CardHolder {

	private static final long serialVersionUID = -4373461696894251713L;

	/**
	 * Creates new board with five community cards.
	 * 
	 * @param flop1
	 *            first card of the flop
	 * @param flop2
	 *            second card of the flop
	 * @param flop3
	 *            third card of the flop
	 * @param turn
	 *            turn card
	 * @param river
	 *            river card
	 */
	public Board(Card flop1, Card flop2, Card flop3, Card turn, Card river) {
		super(flop1, flop2, flop3, turn, river);
	}

}
