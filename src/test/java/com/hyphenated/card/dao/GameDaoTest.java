package com.hyphenated.card.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"file:src/main/webapp/WEB-INF/spring/spring-context.xml",
		"classpath:db-context-test.xml"
})
@Transactional
public class GameDaoTest {

	@Autowired
	private GameDao gameDao;
	
	@Test
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
		assertTrue(gameDao.findAll().size() == 0);
	}
}
