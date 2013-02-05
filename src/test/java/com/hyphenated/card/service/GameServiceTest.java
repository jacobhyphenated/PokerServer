package com.hyphenated.card.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyphenated.card.AbstractSpringTest;
import com.hyphenated.card.domain.BlindLevel;
import com.hyphenated.card.domain.CommonTournamentFormats;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameStructure;
import com.hyphenated.card.domain.GameType;
import com.hyphenated.card.domain.Player;

public class GameServiceTest extends AbstractSpringTest {

	@Autowired
	private GameService gameService;
	
	@Test
	public void testCreateGame(){
		Game game = gameService.saveGame(createTestGame());
		assertTrue(game.getId() > 0);
	}
	
	@Test
	public void testAddPlayers(){
		Game game = gameService.saveGame(createTestGame());
		assertTrue(!game.isStarted());
		assertNotNull(game.getGameStructure());
		
		addPlayersToGame(game);
		
		flushAndClear();
		
		game = gameService.getGameById(game.getId(), true);
		assertEquals(2,game.getPlayersRemaining());
		
		Set<Player> players = game.getPlayers();
		assertEquals(2,players.size());
		for(Player p : players){
			assertTrue(p.getId() > 0);
			assertEquals(game.getGameStructure().getStartingChips(), p.getChips());
		}
		
		assertFalse(game.isStarted());
	}
	
	@Test
	public void testStartGame(){
		Game game = gameService.saveGame(createTestGame());
		addPlayersToGame(game);
		
		flushAndClear();
		
		game = gameService.getGameById(game.getId(), true);
		
		assertFalse(game.isStarted());
		assertNull(game.getGameStructure().getCurrentBlindLevel());
		assertNull(game.getGameStructure().getCurrentBlindEndTime());
		
		game = gameService.startGame(game);
		assertEquals(BlindLevel.BLIND_10_20, game.getGameStructure().getCurrentBlindLevel());
		assertNull(game.getGameStructure().getCurrentBlindEndTime());
		assertTrue(game.isStarted());
		
		for(Player p : game.getPlayers()){
			assertTrue(p.getGamePosition() > 0);
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void testCantStartGamePlayers(){
		Game game = gameService.saveGame(createTestGame());
		gameService.addNewPlayerToGame(game, new Player());
		
		flushAndClear();
		game = gameService.getGameById(game.getId(), true);
		assertNotNull(game.getPlayers());
		gameService.startGame(game);
	}
	
	@Test
	public void testAssignPlayersWithTwo(){
		Game game = gameService.saveGame(createTestGame());
		addPlayersToGame(game);
		
		flushAndClear();
		
		game = gameService.getGameById(game.getId(), false);
		game = gameService.startGame(game);
		assertNotNull(game.getPlayerInBTN());
		
		List<Player> players = new ArrayList<Player>();
		players.addAll(game.getPlayers());
		Collections.sort(players);
		assertEquals(players.get(0), game.getPlayerInBTN());
	}
	
	@Test
	public void testAssignPlayersWithMany(){
		Game game = gameService.saveGame(createTestGame());
		addPlayersToGame(game);
		addPlayersToGame(game);
		
		flushAndClear();
		
		game = gameService.getGameById(game.getId(), false);
		game = gameService.startGame(game);
		
		List<Player> players = new ArrayList<Player>();
		players.addAll(game.getPlayers());
		Collections.sort(players);
		assertEquals(players.get(0), game.getPlayerInBTN());
	}
	
	private Game createTestGame(){
		Game g = new Game();
		g.setName("TestGame-Service");
		g.setPlayersRemaining(0);
		g.setGameType(GameType.TOURNAMENT);
		g.setStarted(false);
		
		GameStructure gs = new GameStructure();
		CommonTournamentFormats format =  CommonTournamentFormats.TWO_HR_SIXPPL;
		gs.setBlindLength(format.getTimeInMinutes());
		gs.setBlindLevels(format.getBlindLevels());
		gs.setStartingChips(2000);
		g.setGameStructure(gs);
		return g;
	}
	
	private void addPlayersToGame(Game game){
		Player p1 = new Player();
		p1.setChips(game.getGameStructure().getStartingChips());
		p1.setName("TestPlayer1");
		p1 = gameService.addNewPlayerToGame(game, p1);
		assertTrue(p1.getId() > 0);
		
		Player p2 = new Player();
		p2.setChips(game.getGameStructure().getStartingChips());
		p2.setName("TestPlayer2");
		p2 = gameService.addNewPlayerToGame(game, p2);
		assertTrue(p2.getId() > 0);
	}
}
