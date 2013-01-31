package com.hyphenated.card.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyphenated.card.AbstractSpringTest;
import com.hyphenated.card.dao.GameDao;
import com.hyphenated.card.dao.PlayerDao;
import com.hyphenated.card.domain.BlindLevel;
import com.hyphenated.card.domain.CommonTournamentFormats;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameStructure;
import com.hyphenated.card.domain.GameType;
import com.hyphenated.card.domain.Player;

public class HandServiceTest extends AbstractSpringTest {

	@Autowired
	private PokerHandService handService;
	
	@Autowired
	private GameDao gameDao;
	@Autowired
	private PlayerDao playerDao;
	
	@Test
	public void verifyGameSetup(){
		Game game = setupGame();
		assertTrue(game.getId() > 0);
		assertEquals(4,game.getPlayers().size());
		assertEquals(BlindLevel.BLIND_10_20, game.getGameStructure().getCurrentBlindLevel());
		assertNull(game.getGameStructure().getCurrentBlindEndTime());
		assertTrue(game.isStarted());
		
		for(Player p : game.getPlayers()){
			assertTrue(p.getGamePosition() > 0);
		}
	}
	
	private Game setupGame(){
		Game game = new Game();
		game.setName("Test Game");
		game.setPlayersRemaining(4);
		game.setStarted(true);
		game.setGameType(GameType.TOURNAMENT);
		GameStructure gs = new GameStructure();
		CommonTournamentFormats format =  CommonTournamentFormats.TWO_HR_SIXPPL;
		gs.setBlindLength(format.getTimeInMinutes());
		gs.setBlindLevels(format.getBlindLevels());
		gs.setStartingChips(2000);
		gs.setCurrentBlindLevel(BlindLevel.BLIND_10_20);
		game.setGameStructure(gs);
		
		game = gameDao.save(game);
		
		Player p1 = new Player();
		p1.setName("Player 1");
		p1.setGame(game);
		p1.setChips(gs.getStartingChips());
		p1.setGamePosition(1);
		playerDao.save(p1);
		
		Player p2 = new Player();
		p2.setName("Player 2");
		p2.setGame(game);
		p2.setChips(gs.getStartingChips());
		p2.setGamePosition(2);
		playerDao.save(p2);
		
		Player p3 = new Player();
		p3.setName("Player 3");
		p3.setGame(game);
		p3.setChips(gs.getStartingChips());
		p3.setGamePosition(3);
		playerDao.save(p3);
		
		Player p4 = new Player();
		p4.setName("Player 4");
		p4.setGame(game);
		p4.setChips(gs.getStartingChips());
		p4.setGamePosition(4);
		playerDao.save(p4);
		
		flushAndClear();
		
		return gameDao.findById(game.getId());
	}
}
