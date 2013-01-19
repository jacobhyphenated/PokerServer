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

import com.hyphenated.card.holder.Board;
import com.hyphenated.card.holder.Hand;

/**
 * Evaluator of the Texas Hold'em poker hands. Evaluators need not to be thread
 * safe.
 */
public interface HandRankEvaluator {

	/**
	 * Evaluates player's hand strength.
	 * 
	 * @param board
	 *            community cards
	 * @param hand
	 *            player's hand
	 * @return the hand rank of the particular card configuration
	 */
	HandRank evaluate(Board board, Hand hand);

}
