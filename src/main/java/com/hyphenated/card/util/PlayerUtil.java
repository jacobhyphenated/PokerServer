package com.hyphenated.card.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.hyphenated.card.domain.HandEntity;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.domain.PlayerHand;
import com.hyphenated.card.eval.FSMHandRankEvaluatorFactory;
import com.hyphenated.card.eval.HandRank;
import com.hyphenated.card.eval.HandRankEvaluator;
import com.hyphenated.card.holder.Board;

/**
 * Utility class with helper methods for Player interactions
 * @author jacobhyphenated
 */
public class PlayerUtil {
	
	private static HandRankEvaluator evaluator =  FSMHandRankEvaluatorFactory.create();

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
	
	/**
	 * Determine the winner(s) of a hand.  Limit the players considered to be the winner to the list
	 * passed into the parameter.  This allows for a separation of concerns when dealing with split 
	 * pots and multiple side pots.
	 * @param hand {@link HandEntity} that has been concluded with a winner to be determined
	 * @param players {@link PlayerHand} set of players who are competing to win the hand.  These are the
	 * players who will be considered when deciding the winner, not the {@link PlayerHand}s associated with the hand.
	 * @return List of {@link Player}s who have won the hand.  If there is a tie, all players that have
	 * tied are returned in the list.
	 */
	public static List<Player> getWinnersOfHand(HandEntity hand, Set<PlayerHand> players){
		List<Player> winners = new ArrayList<Player>();
		Board board = new Board(hand.getBoard().getFlop1(), hand.getBoard().getFlop2(), 
				hand.getBoard().getFlop3(), hand.getBoard().getTurn(), hand.getBoard().getRiver());
		HandRank highestRank = null;
		for(PlayerHand ph : players){
			HandRank rank = evaluator.evaluate(board, ph.getHand());
			//First player to be checked
			if(highestRank == null){
				highestRank = rank;
				winners.clear();
				winners.add(ph.getPlayer());
				continue;
			}
			
			//Compare This player to current best player
			int comp = rank.compareTo(highestRank);
			if(comp > 0){
				//New best
				highestRank = rank;
				winners.clear();
				winners.add(ph.getPlayer());
			}
			else if(comp == 0){
				//Tie
				winners.add(ph.getPlayer());
			}
		}
		
		return winners;
	}
}
