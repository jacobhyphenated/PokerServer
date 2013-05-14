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
 * Copyright (c) 2013
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
	 * @return
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
