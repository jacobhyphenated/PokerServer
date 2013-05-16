package com.hyphenated.card.util;

import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameStatus;
import com.hyphenated.card.domain.HandEntity;

/**
 * Static helper methods for controlling game functions and information
 * 
 * @author jacobhyphenated
 * Copyright (c) 2013
 */
public class GameUtil {

	/**
	 * Determine the current state of the game
	 * @param game {@link Game} object to get the state of
	 * @return {@link GameStatus} representing the current game state
	 */
	public static GameStatus getGameStatus(Game game){
		if(!game.isStarted()){
			return GameStatus.NOT_STARTED;
		}
		
		HandEntity hand = game.getCurrentHand();
		if(hand == null){
			return GameStatus.SEATING;
		}
		
		if(hand.getCurrentToAct() == null){
			return GameStatus.END_HAND;
		}
		
		if(hand.getBoard().getRiver() != null){
			return GameStatus.RIVER;
		}
		
		if(hand.getBoard().getTurn() != null){
			return GameStatus.TURN;
		}
		
		if(hand.getBoard().getFlop3() != null){
			return GameStatus.FLOP;
		}
		
		return GameStatus.PREFLOP;
	}
}
