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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyphenated.card.dao.GameDao;
import com.hyphenated.card.dao.PlayerDao;
import com.hyphenated.card.domain.BlindLevel;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameStructure;
import com.hyphenated.card.domain.GameType;
import com.hyphenated.card.domain.Player;

@Service
public class GameServiceImpl implements GameService {
	
	@Autowired
	private GameDao gameDao;
	
	@Autowired
	private PlayerDao playerDao;

	@Override
	@Transactional(readOnly=true)
	public Game getGameById(long id, boolean fetchPlayers) {
		Game game = gameDao.findById(id);
		//Player list is lazy fetched.  force fetch for players if necessary
		if(fetchPlayers){
			for(Player p : game.getPlayers()){
				p.getId();
			}
		}
		return game;
	}
	
	@Override
	@Transactional
	public Game saveGame(Game game){
		return gameDao.save(game);
	}
	
	@Override
	@Transactional
	public Game startGame(Game game){
		game = gameDao.merge(game);
		if(game.getPlayers().size() < 2){
			throw new IllegalStateException("Not Enough Players");
		}
		if(game.getPlayers().size() > 10){
			throw new IllegalStateException("Too Many Players");
		}
		if(game.isStarted()){
			throw new IllegalStateException("Game already started");
		}
		
		//Set started flag
		game.setStarted(true);
		//Start at the first blind level for the game
		GameStructure gs = game.getGameStructure();
		List<BlindLevel> blinds = gs.getBlindLevels();
		Collections.sort(blinds);
		gs.setCurrentBlindLevel(blinds.get(0));
		
		//Get all players associated with the game. 
		//Assign random position.  Save the player.
		List<Player> players = new ArrayList<Player>();
		players.addAll(game.getPlayers());
		Collections.shuffle(players);
		for(int i = 0; i < players.size(); i++){
			Player p = players.get(i);
			p.setGamePosition(i+1);
			playerDao.save(p);
		}
		
		//Set Button and Big Blind.  Button is position 1 (index 0)
		Collections.sort(players);
		game.setPlayerInBTN(players.get(0));
		
		//Save and return the updated game
		return gameDao.merge(game);
	}
	
	@Override
	@Transactional
	public Player addNewPlayerToGame(Game game, Player player){
		if(game.isStarted() && game.getGameType() == GameType.TOURNAMENT){
			throw new IllegalStateException("Tournament in progress, no new players may join");
		}
		game = gameDao.merge(game);
		if(game.getPlayers().size() >= 10){
			throw new IllegalStateException("Cannot have more than 10 players in one game");
		}
		player.setGame(game);
		//Set up player according to game logic.
		if(game.getGameType() == GameType.TOURNAMENT){
			player.setChips(game.getGameStructure().getStartingChips());
		}
		
		player = playerDao.save(player);
		if(player == null){
			return null;
		}
		game.setPlayersRemaining(game.getPlayersRemaining() + 1);
		game = gameDao.save(game);
		player.setGame(game);
		return player;
	}
	
	@Override
	@Transactional
	public Player savePlayer(Player player){
		return playerDao.save(player);
	}
	
}
