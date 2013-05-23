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
package com.hyphenated.card.service;

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
	
	//TODO List active games
	//Games that are not started, games that are cash games and still in progress
	//Need a way to weed out old/expired/inactive games
	
	//TODO Player add Chips method - for cash games and rebuy tournaments
	//Do this in service layer to enforce tournament logic.
	//Cash games could theoretically be done in the controller and call save
}
