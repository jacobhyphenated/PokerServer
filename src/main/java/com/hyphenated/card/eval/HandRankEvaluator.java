package com.hyphenated.card.eval;

import com.hyphenated.card.holder.Board;
import com.hyphenated.card.holder.Hand;

/**
 * Evaluator of the Texas Hold'em poker hands. Evaluators need not to be thread
 * safe.
 */
public interface HandRankEvaluator {

	/**
	 * Evaluates player's hand strength.  HandRank represents an absolute value guage of the hand strength
	 * 
	 * @param board 5 board cards
	 * @param hand 2 cards in players hand
	 * @return the {@link HandRank} of the particular card configuration
	 */
	HandRank evaluate(Board board, Hand hand);

}
