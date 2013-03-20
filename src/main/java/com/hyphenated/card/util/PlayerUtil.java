package com.hyphenated.card.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	
	/**
	 * For a showdown, there are multiple possible outcomes.  There is one winner; there are multiple winners;
	 * there are multiple winners with different amounts won because of side pots.
	 * <br /><br />
	 * This method will take a {@link HandEntity} and determine what players win what amount.  This will
	 * take into account split pots and side pots.
	 * @param hand Hand that has gotten to the river and is at showdown
	 * @return A {@link Map} of {@link Player} objects to the Integer value of chips that they are awarded as a result
	 * of the hand.  The map will contain only winning players (of some kind) during this hand.  Null is returned
	 * if the {@link HandEntity} is in the incorrect state (not at the river) or the proper showdown conditions have
	 * not been met.
	 */
	public static Map<Player, Integer> getAmountWonInHandForAllPlayers(HandEntity hand){
		if(hand.getBoard().getRiver() == null){
			return null;
		}
		
		Map<Player, Integer> winnersMap = new HashMap<Player, Integer>();
		Set<PlayerHand> playersInvolved = hand.getPlayers();
		PlayerHand allInPlayer = null;
		int allInBetRunningTotal = 0;
		do{
			//Determine all in player if applicable
			allInPlayer = null;
			PlayerHand potentialAllInPlayer = null;
			int minimumBetAmountPerPlayer = 0;
			for(PlayerHand ph : playersInvolved){
				if(minimumBetAmountPerPlayer == 0){
					minimumBetAmountPerPlayer = ph.getBetAmount();
					potentialAllInPlayer = ph;
				}
				else if(minimumBetAmountPerPlayer > ph.getBetAmount()){
					minimumBetAmountPerPlayer = ph.getBetAmount();
					allInPlayer = ph;
					potentialAllInPlayer = allInPlayer;
				}
				else if(minimumBetAmountPerPlayer < ph.getBetAmount()){
					allInPlayer = potentialAllInPlayer;
				}
			}
			//minimum bet per player is just for this pot (of many possible side pots).
			//discount any chips or bets that may have been awarded in previous pots
			minimumBetAmountPerPlayer = minimumBetAmountPerPlayer - allInBetRunningTotal;
			allInBetRunningTotal += minimumBetAmountPerPlayer;
			
			//If no player is all in, resolve the hand.
			if(allInPlayer == null){
				applyWinningAndChips(winnersMap, hand, minimumBetAmountPerPlayer, playersInvolved);
				break;
			}
			
			//If there are two players with one player committing more chips to the pot than the other
			if(playersInvolved.size() == 2){
				//refund player money for amount not called by all in
				playersInvolved.remove(allInPlayer);
				PlayerHand overbet = playersInvolved.iterator().next();
				int amountOverbet = overbet.getBetAmount() - allInPlayer.getBetAmount();
				Integer bigIValue = winnersMap.get(overbet.getPlayer());
				int i = (bigIValue == null)?0:bigIValue;
				winnersMap.put(overbet.getPlayer(), amountOverbet + i);
				playersInvolved.add(overbet);
				playersInvolved.add(allInPlayer);
				
				//Determine winner
				applyWinningAndChips(winnersMap, hand, allInBetRunningTotal, playersInvolved);
				break;
			}
			
			//Handle this side pot. Remove the all in player, then re-evaluate for the remaining players.
			applyWinningAndChips(winnersMap, hand, minimumBetAmountPerPlayer, playersInvolved);
			playersInvolved.remove(allInPlayer);
		
		}while(allInPlayer != null);
		
		return winnersMap;
	}
	
	private static void applyWinningAndChips(Map<Player, Integer> winnersMap, HandEntity hand,
			int minPlayerBetAmount, Set<PlayerHand> playersInvolved){
		List<Player> winners = PlayerUtil.getWinnersOfHand(hand, playersInvolved);
		int potSplit = (minPlayerBetAmount * playersInvolved.size() ) / winners.size();
		//Odd chips go to first player in game order
		int remaining = hand.getPot() % winners.size();
		for(Player player : winners){
			Integer bigIValue = winnersMap.get(player);
			int i = (bigIValue == null)?0:bigIValue;
			winnersMap.put(player, potSplit + remaining + i);
			remaining = 0;
		}
	}
}
