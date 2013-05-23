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
package com.hyphenated.card.eval;

import java.util.Iterator;

import com.hyphenated.card.Card;
import com.hyphenated.card.holder.Board;
import com.hyphenated.card.holder.CardIterator;
import com.hyphenated.card.holder.Hand;

/**
 * Poker hand Evaluation algorithm based on the two plus two 7 card hand evaluation algorithm.
 * 
 * @author jacobhyphenated
 */
public class TwoPlusTwoHandEvaluator implements HandRankEvaluator {

	private static final String HAND_RANKS = "/HandRanks.dat";
	private int[] handRanks;
	
	private static TwoPlusTwoHandEvaluator instance;
	
	public TwoPlusTwoHandEvaluator(){
		ConfigurationLoader reader = new ConfigurationLoader();
		handRanks = reader.loadHandRankResource(HAND_RANKS);
	}
	
	/**
	 * The two plust two lookup table is very memory intensive.  You should only ever create
	 * one instance of the class.  Use this method to keep the singleton pattern.
	 * @return {@link TwoPlusTwoHandEvaluator} instance
	 */
	public static TwoPlusTwoHandEvaluator getInstance(){
		if(instance == null){
			instance = new TwoPlusTwoHandEvaluator();
		}
		return instance;
	}
	
	@Override
	public HandRank evaluate(Board board, Hand hand) {
		Iterator<Card> cardIterator = new CardIterator(board, hand);
		int p = 53;
		while(cardIterator.hasNext()){
			Card card = cardIterator.next();
			p = handRanks[p + card.getEvaluation()];
		}
		return new HandRank(p);
	}

}
