package com.hyphenated.card.service;

import com.hyphenated.card.domain.HandEntity;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.domain.PlayerStatus;

/**
 * Service class to handle actions the player involved in the hand can take.
 * 
 * @author jacobhyphenated
 */
public interface PlayerActionService {

	/**
	 * The player folds out of the hand
	 * @param player {@link Player} to fold
	 * @param hand {@link HandEntity} the player is folding from
	 * @return true if the player folds, false if the fold is not permitted
	 * (the player is out of position, the player is not in the hand).
	 */
	public boolean fold(Player player, HandEntity hand);
	
	/**
	 * The Player checks back their action
	 * @param player {@link Player} to check
	 * @param hand {@link HandEntity} the player is checking from.
	 * @return true if the player checks. False if a check is not permitted 
	 * (The player is out of turn, there is a bet and a check is not allowed).
	 */
	public boolean check(Player player, HandEntity hand);
	
	/**
	 * The Player places a bet
	 * @param player {@link Player} to bet
	 * @param hand {@link HandEntity} the player is betting from
	 * @param betAmount amount of the bet.  This is the total amount, so if there was a bet
	 * before, and this is the raise, this value represents the amount bet for only this raise from the player.
	 * <br />For example: Player 1 bets 100.  Player 2 raises to 300.  Player 2 calls this method with the
	 * betAmount parameter of 200.  Player 1 re-raises to 900 total (100 + 300 + 500 more).  The betAmount
	 * parameter is passed as 500.
	 * @return true if the bet is placed.  False if the bet is not permitted
	 * (The bet is too small, the player is out of turn).
	 */
	public boolean bet(Player player, HandEntity hand, int betAmount);
	
	/**
	 * Call the bet
	 * @param player {@link Player} to call the bet
	 * @param hand {@link HandEntity} the player is calling from
	 * @return true if the bet is successfully called. False if a call is not permitted 
	 * (there is not bet, it is not the players turn, etc.)
	 */
	public boolean call(Player player, HandEntity hand);
	
	/**
	 * Get the status in the game or hand of the player
	 * @param player {@link Player} to check the status of
	 * @return the {@link PlayerStatus}
	 */
	public PlayerStatus getPlayerStatus(Player player);
}
