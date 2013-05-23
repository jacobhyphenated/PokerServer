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
package com.hyphenated.card.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hyphenated.card.AbstractSpringTest;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameType;
import com.hyphenated.card.domain.Player;

/**
 * Basic DAO test.  Tests some of the simple CRUD methods of the generic DAO using the GameDao.
 * 
 * @author jacobhyphenated
 */
@Transactional
public class GameDaoTest extends AbstractSpringTest {

	@Autowired
	private GameDao gameDao;
	
	@Autowired
	private PlayerDao playerDao;
	
	@Test
	public void testCreateGame(){
		Game game = gameDao.save(createTestGame());
		assertNotNull(game);
		assertTrue(game.getId() > 0);
		assertTrue(gameDao.findAll().size() == 1);
	}
	
	@Test
	public void testGetGames(){
		gameDao.save(createTestGame());
		List<Game> games = gameDao.findAll();
		assertTrue(games.size() == 1);
		assertTrue(games.get(0).getGameType() == GameType.TOURNAMENT);
		assertTrue(games.get(0).getId() > 0);
	}
	
	@Test
	public void testAddPlayerToGame(){
		gameDao.save(createTestGame());
		Game game = gameDao.findAll().get(0);
		assertTrue(game.getId() > 0);
		Player p = new Player();
		p.setName("test 1");
		p.setGame(game);
		p.setChips(5000);
		assertTrue(p.getId() == 0);
		playerDao.save(p);
		assertTrue(p.getId() > 0);
		
		flushAndClear();
		Game game2 = gameDao.findAll().get(0);
		assertEquals(1, game2.getPlayers().size() );
	}
	
	@Test
	public void testAddSecondGame(){
		gameDao.save(createTestGame());
		Game game = new Game();
		game.setGameType(GameType.CASH);
		game.setName("Second Test Game");
		game.setPlayersRemaining(0);
		game.setStarted(false);
		gameDao.save(game);
		assertTrue(gameDao.findAll().size() == 2);
	}
	
	@Test
	public void testRemoveGame(){
		gameDao.save(createTestGame());
		Game g = createTestGame();
		g.setName("test 2");
		g.setGameType(GameType.CASH);
		gameDao.save(g);
		List<Game> games = gameDao.findAll();
		assertTrue(games.size() == 2);
		for(Game game : games){
			if(game.getGameType() == GameType.TOURNAMENT){
				gameDao.remove(game);
			}
		}
		assertTrue(gameDao.findAll().size() == 1);
	}
	
	@Test
	public void testModifyGame(){
		gameDao.save(createTestGame());
		Game game = gameDao.findAll().get(0);
		assertTrue(game.getGameType() == GameType.TOURNAMENT);
		assertTrue(game.getPlayersRemaining() == 0);
		game.setPlayersRemaining(8);
		Game gameUpdated = gameDao.merge(game);
		assertTrue(gameUpdated.getPlayersRemaining() == 8);
	}
	
	private Game createTestGame(){
		Game game = new Game();
		game.setName("Test Game - JUnit");
		game.setGameType(GameType.TOURNAMENT);
		game.setPlayersRemaining(0);
		game.setStarted(false);
		return game;
	}
}
