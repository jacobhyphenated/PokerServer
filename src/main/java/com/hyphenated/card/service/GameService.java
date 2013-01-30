package com.hyphenated.card.service;

import java.util.List;
import java.util.Set;

import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.Player;

/**
 * Service to handle the overall game operations
 * 
 * @author jacobhyphenated
 */
public interface GameService {

	/**
	 * Get the game from the persistent context based on the unique identifier
	 * @param id unique id for the game
	 * @param fetchPlayers true to eagerly fetch the Player List for the game.
	 * if this parameter is false, the {@link Game} return object will not have eagerly fetched
	 * the player list, and there is no guarantee of the list accuracy or existence
	 * @return {@link Game} from the persistent context
	 */
	public Game getGameById(long id, boolean fetchPlayers);
	
	/**
	 * Save any changes to the {@link Game} object to the persistent context
	 * @param game game to be saved
	 * @return saved game attached to the persistent context
	 */
	public Game saveGame(Game game);
	
	/**
	 * Start a game. This begins the current game tracking.  Setup of the game is completed.  If it
	 * is a tournament, all players should be registered at this time.<br /><br />
	 * 
	 * This will assign starting positions to all of the players.  This will not start the blind level,
	 * that will happen at the start of the first hand.
	 * @param game
	 * @return
	 */
	public Game startGame(Game game);
	
	/**
	 * Add a new player to an existing game
	 * @param game game to add the player to
	 * @param player {@link Player} to add to the game
	 * @return Player with persisted context.  Null if the game is not accepting new players.
	 */
	public Player addNewPlayerToGame(Game game, Player player);
	
	/**
	 * Persist any changes to a {@link Player} domain object.  Or create a new one.
	 * @param player Player to be saved
	 * @return Player attached to the persistent context
	 */
	public Player savePlayer(Player player);
	
	/**
	 * Based on the game id, get a list of players, sorted by order to act
	 * @param gameId unique id of the game
	 * @return List of players
	 */
	public List<Player> getSortedListOfPlayers(long gameId);
	
	/**
	 * Based on the game domain object, get a list of players, sorted by order to act
	 * @param game {@link Game} object with persistent context
	 * @return List of players
	 */
	public List<Player> getSortedListOfPlayers(Game game);
	
	/**
	 * Sort the list of players based on order to act
	 * @param players List of {@link Player} objects to be sorted
	 * @return sorted list of players
	 */
	public List<Player> getSortedListOfPlayers(List<Player> players);
	
	/**
	 * Sort the list of players based on order to act
	 * @param players set of {@link Player} objects to be sorted
	 * @return sorted list of players
	 */
	public List<Player> getSortedListOfPlayers(Set<Player> players);
}
