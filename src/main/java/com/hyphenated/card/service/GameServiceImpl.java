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
		//Set started flag
		game.setStarted(true);
		game = gameDao.merge(game);
		if(game.getPlayers().size() < 2){
			throw new IllegalStateException("Not Enough Players");
		}
		
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
			playerDao.merge(p);
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
