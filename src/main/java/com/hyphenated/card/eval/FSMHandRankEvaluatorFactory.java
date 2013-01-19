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

/**
 * Factory for FSM-based hand rank evaluators.
 */
public class FSMHandRankEvaluatorFactory {

	/**
	 * Path to the transition table used for evaluation "rank" hand types.
	 */
	public static final String RANK_TRANS_SER = "/rank_states.ser";

	/**
	 * Path to the transition table used for evaluation "suit hand types"
	 * (flushes).
	 */
	public static final String SUIT_TRANS_SER = "/suit_states.ser";

	/**
	 * Path to the table holding strength of the "suit hand types" (flushes).
	 */
	public static final String SUIT_VALUES_SER = "/suit_transitions.ser";

	/**
	 * Creates new FSM-based hand rank evaluator.
	 * 
	 * @return evaluator
	 */
	public static HandRankEvaluator create() {
		ConfigurationLoader reader = new ConfigurationLoader();
		short[][] rankTrans = reader.loadObjectFromResource(RANK_TRANS_SER);
		short[][] suitTrans = reader.loadObjectFromResource(SUIT_TRANS_SER);
		short[][] suitValues = reader.loadObjectFromResource(SUIT_VALUES_SER);
		return new FSMHandRankEvaluator(rankTrans, suitTrans, suitValues);
	}

}
