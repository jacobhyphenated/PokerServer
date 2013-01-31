package com.hyphenated.card.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.hyphenated.card.AbstractSpringTest;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameType;
import com.hyphenated.card.domain.Player;

/**
 * Basic DAO test.  Tests some of the simple CRUD methods of the generic DAO using the GameDao.
 * @author jacobhyphenated
 *
 */
public class GameDaoTest extends AbstractSpringTest {

	@Autowired
	private GameDao gameDao;
	
	@Autowired
	private PlayerDao playerDao;
	
	@Test
	@Rollback(value=false)
	public void testCreateGame(){
		Game game = new Game();
		game.setName("Test Game - JUnit");
		game.setGameType(GameType.TOURNAMENT);
		game.setPlayersRemaining(0);
		game.setStarted(false);
		gameDao.save(game);
		assertNotNull(game);
		assertTrue(game.getId() > 0);
		assertTrue(gameDao.findAll().size() == 1);
	}
	
	@Test
	public void testGetGames(){
		List<Game> games = gameDao.findAll();
		assertTrue(games.size() == 1);
		assertTrue(games.get(0).getGameType() == GameType.TOURNAMENT);
		assertTrue(games.get(0).getId() > 0);
	}
	
	@Test
	@Rollback(value=false)
	public void testAddPlayerToGame(){
		Game game = gameDao.findAll().get(0);
		assertTrue(game.getId() > 0);
		Player p = new Player();
		p.setName("test 1");
		p.setGame(game);
		p.setChips(5000);
		assertTrue(p.getId() == 0);
		playerDao.save(p);
		assertTrue(p.getId() > 0);
	}
	
	@Test
	public void testPlayerInGame(){
		Game game = gameDao.findAll().get(0);
		assertTrue("Wrong size, expected 1 and was " + game.getPlayers().size(), game.getPlayers().size() == 1 );
	}
	
	@Test
	@Rollback(value=false)
	public void testAddSecondGame(){
		Game game = new Game();
		game.setGameType(GameType.CASH);
		game.setName("Second Test Game");
		game.setPlayersRemaining(0);
		game.setStarted(false);
		gameDao.save(game);
		assertTrue(gameDao.findAll().size() == 2);
	}
	
	@Test
	@Rollback(value=false)
	public void testRemoveGame(){
		List<Game> games = gameDao.findAll();
		assertTrue(games.size() == 2);
		for(Game game : games){
			if(game.getGameType() == GameType.TOURNAMENT){
				assertTrue(game.getPlayers().size()==1);
				playerDao.remove(game.getPlayers().iterator().next());
				gameDao.remove(game);
			}
		}
		assertTrue(gameDao.findAll().size() == 1);
	}
	
	@Test
	public void testModifyGame(){
		Game game = gameDao.findAll().get(0);
		assertTrue(game.getGameType() == GameType.CASH);
		assertTrue(game.getPlayersRemaining() == 0);
		game.setPlayersRemaining(8);
		Game gameUpdated = gameDao.merge(game);
		assertTrue(gameUpdated.getPlayersRemaining() == 8);
	}
}
