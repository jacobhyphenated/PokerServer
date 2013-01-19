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

import java.util.Iterator;

import com.hyphenated.card.Card;
import com.hyphenated.card.holder.Board;
import com.hyphenated.card.holder.CardIterator;
import com.hyphenated.card.holder.Hand;

/**
 * Hand rank evaluator implementation. Uses Finite State Machines for
 * evaluation.
 */
public class FSMHandRankEvaluator implements HandRankEvaluator {

	/**
	 * Transition table used for evaluation "rank" hand types.
	 */
	private final short[][] rankTrans;

	/**
	 * Transition table used for evaluation "suit hand types" (flushes).
	 */
	private final short[][] suitTrans;

	/**
	 * Table holding strength of the "suit hand types" (flushes).
	 */
	private final short[][] suitValues;

	public FSMHandRankEvaluator(short[][] rankTrans, short[][] suitTrans,
			short[][] suitValues) {
		super();
		this.rankTrans = rankTrans;
		this.suitTrans = suitTrans;
		this.suitValues = suitValues;
	}

	@Override
	public final HandRank evaluate(Board board, Hand hand) {
		int rankState = 0;
		int[] suitValue = new int[4];
		int[] suitState = new int[4];

		Iterator<Card> cardIterator = new CardIterator(board, hand);
		
		// loop without updating suit state values
		for (int i = 0; i < 4; i++) {
			Card card = cardIterator.next();
			int rank = card.getRank().ordinal();
			int suit = card.getSuit().ordinal();
			rankState = rankTrans[rankState][rank];
			suitState[suit] = suitTrans[suitState[suit]][rank];
		}

		// loop updating suit state values
		for (int i = 0; i < 2; i++) {
			Card card = cardIterator.next();
			int rank = card.getRank().ordinal();
			int suit = card.getSuit().ordinal();
			rankState = rankTrans[rankState][rank];
			int currentState = suitState[suit];
			suitValue[suit] = suitValues[currentState][rank];
			suitState[suit] = suitTrans[currentState][rank];
		}

		// suit state transitions not changed
		Card card = cardIterator.next();
		int rank = card.getRank().ordinal();
		int suit = card.getSuit().ordinal();
		rankState = rankTrans[rankState][rank];
		suitValue[suit] = suitValues[suitState[suit]][rank];

		// only one suit value is nonzero
		int value = suitValue[0] | suitValue[1] | suitValue[2] | suitValue[3];
		return new HandRank(rankState > value ? rankState : value);
	}

}
