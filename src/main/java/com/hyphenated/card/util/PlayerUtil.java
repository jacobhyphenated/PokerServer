package com.hyphenated.card.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hyphenated.card.domain.Player;
import com.hyphenated.card.domain.PlayerHand;

/**
 * Utility class with helper methods for Player interactions
 * @author jacobhyphenated
 */
public class PlayerUtil {

	/**
	 * Get the next player to act
	 * @param players List of players involved
	 * @param startPlayer Start position.  The player to the left of this player should be the next to act
	 * @return {@link Player} who is next to act
	 */
	public static Player getNextPlayerInGameOrder(List<Player> players, Player startPlayer){
		//Sorted list by game order
		Collections.sort(players);
		for(int i = 0; i < players.size(); i++){
			//Find the player we are starting at
			if(players.get(i).equals(startPlayer)){
				//The next player is either the next in the list, or the first in the list if startPlayer is at the end
				return (i == players.size() - 1) ? players.get(0) : players.get(i+1);
			}
		}
		return null;
	}
	
	/**
	 * Get the next player to act in the hand
	 * @param playerHands List of players involved in the hand
	 * @param startPlayer Start position.  The Player to the left of this player should be the next to act
	 * @return {@link Player} who is next to act
	 */
	public static Player getNextPlayerInGameOrderPH(List<PlayerHand> playerHands, Player startPlayer){
		List<Player> players = new ArrayList<Player>();
		for(PlayerHand ph: playerHands){
			players.add(ph.getPlayer());
		}
		return getNextPlayerInGameOrder(players, startPlayer);
	}
}
