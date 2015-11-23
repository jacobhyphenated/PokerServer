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

import com.hyphenated.card.AbstractSpringTest;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameType;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.repos.GameRepository;
import com.hyphenated.card.repos.PlayerRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Basic DAO test.  Tests some of the simple CRUD methods of the generic DAO using the gameRepository.
 *
 * @author jacobhyphenated
 */
@Transactional
@Rollback
public class GameDaoTest extends AbstractSpringTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    public void testCreateGame() {
        Game game = gameRepository.save(createTestGame());
        assertNotNull(game);
        assertTrue(game.getId() > 0);
        assertTrue(gameRepository.count() == 1);
    }

    @Test
    public void testGetGames() {
        gameRepository.save(createTestGame());
        List<Game> games = IteratorUtils.toList(gameRepository.findAll().iterator());
        assertTrue(games.size() == 1);
        assertTrue(games.get(0).getGameType() == GameType.TOURNAMENT);
        assertTrue(games.get(0).getId() > 0);
    }

    @Test
    public void testAddPlayerToGame() {
        gameRepository.save(createTestGame());
        List<Game> games = gameRepository.findByName("Test Game - JUnit");
        assertTrue(games.size() == 1);
        Game game = games.get(0);
        Player p = new Player();
        p.setName("test 1");
        p.setGame(game);
        p.setChips(5000);
        assertTrue(p.getId() == null);
        playerRepository.saveAndFlush(p);
        assertTrue(p.getId() != null);

        flushAndClear();
        Game game2 = gameRepository.findByName("Test Game - JUnit").get(0);
        Assert.assertEquals(1, game2.getPlayers().size());
    }

    @Test
    public void testAddSecondGame() {
        gameRepository.save(createTestGame());
        Game game = new Game();
        game.setGameType(GameType.CASH);
        game.setName("Second Test Game");
        game.setPlayersRemaining(0);
        game.setStarted(false);
        gameRepository.save(game);
        assertTrue(gameRepository.count() == 2);
    }

    @Test
    public void testRemoveGame() {
        gameRepository.save(createTestGame());
        Game g = createTestGame();
        g.setName("test 2");
        g.setGameType(GameType.CASH);
        gameRepository.save(g);
        List<Game> games = IteratorUtils.toList(gameRepository.findAll().iterator());
        assertTrue(games.size() == 2);
        for (Game game : games) {
            if (game.getGameType() == GameType.TOURNAMENT) {
                gameRepository.delete(game);
            }
        }
        assertTrue(gameRepository.count() == 1);
    }

    @Test
    public void testModifyGame() {
        gameRepository.save(createTestGame());
        Game game = gameRepository.findByName("Test Game - JUnit").get(0);
        assertTrue(game.getGameType() == GameType.TOURNAMENT);
        assertTrue(game.getPlayersRemaining() == 0);
        game.setPlayersRemaining(8);
        Game gameUpdated = gameRepository.saveAndFlush(game);
        assertTrue(gameUpdated.getPlayersRemaining() == 8);
    }

    private Game createTestGame() {
        Game game = new Game();
        game.setName("Test Game - JUnit");
        game.setGameType(GameType.TOURNAMENT);
        game.setPlayersRemaining(0);
        game.setStarted(false);
        return game;
    }
}
