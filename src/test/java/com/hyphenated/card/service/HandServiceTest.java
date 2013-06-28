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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyphenated.card.AbstractSpringTest;
import com.hyphenated.card.Card;
import com.hyphenated.card.dao.GameDao;
import com.hyphenated.card.dao.PlayerDao;
import com.hyphenated.card.domain.BlindLevel;
import com.hyphenated.card.domain.BoardEntity;
import com.hyphenated.card.domain.CommonTournamentFormats;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameStructure;
import com.hyphenated.card.domain.GameType;
import com.hyphenated.card.domain.HandEntity;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.domain.PlayerHand;

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
	
	@Test
	public void testFirstHandInGame(){
		Game game = setupGame();
		assertEquals(BlindLevel.BLIND_10_20, game.getGameStructure().getCurrentBlindLevel());
		assertNull(game.getGameStructure().getCurrentBlindEndTime());
		
		HandEntity hand = handService.startNewHand(game);
		assertTrue(hand.getId() > 0);
		assertEquals(BlindLevel.BLIND_10_20, hand.getBlindLevel());
		assertNotNull(game.getGameStructure().getCurrentBlindEndTime());
		assertEquals(hand.getBlindLevel(), game.getGameStructure().getCurrentBlindLevel());
		assertEquals(4, hand.getPlayers().size());
		assertNotNull(hand.getBoard());
		assertTrue(hand.getBoard().getId() > 0);
		assertNull(hand.getBoard().getFlop1());
		
		List<PlayerHand> players = new ArrayList<PlayerHand>();
		players.addAll(hand.getPlayers());
		Collections.sort(players);
		assertEquals(players.get(0).getPlayer(), game.getPlayerInBTN());
		assertEquals(players.get(1).getPlayer(), handService.getPlayerInSB(hand));
		assertEquals(players.get(2).getPlayer(), handService.getPlayerInBB(hand));
	}
	
	@Test
	public void testBlindLevelIncrease(){
		Game game = setupGame();
		HandEntity hand = handService.startNewHand(game);
		assertEquals(BlindLevel.BLIND_10_20, hand.getBlindLevel());
		assertNotNull(game.getGameStructure().getCurrentBlindEndTime());
		long firstBoardId = hand.getBoard().getId();
		
		game.getGameStructure().setCurrentBlindEndTime(new Date(new Date().getTime() - 100));
		HandEntity nextHand = handService.startNewHand(game);
		
		assertEquals(BlindLevel.BLIND_15_30, nextHand.getBlindLevel());
		assertEquals(nextHand.getBlindLevel(), game.getGameStructure().getCurrentBlindLevel());
		assertTrue(game.getGameStructure().getCurrentBlindEndTime().getTime() > new Date().getTime());
		assertTrue(nextHand.getBoard().getId() != firstBoardId);
	}
	
	@Test
	public void testFlop(){
		Game game = setupGame();
		HandEntity hand = handService.startNewHand(game);
		assertNull(hand.getBoard().getFlop1());
		
		removeBetValues(hand);
		hand = handService.flop(hand);
		assertTrue(hand.getBoard().getFlop1() != null);
		assertTrue(hand.getBoard().getFlop2() != null);
		assertTrue(hand.getBoard().getFlop3() != null);
	}
	
	@Test
	public void testTurn(){
		Game game = setupGame();
		HandEntity hand = handService.startNewHand(game);
		removeBetValues(hand);
		hand = handService.flop(hand);
		assertNull(hand.getBoard().getTurn());
		assertNotNull(hand.getBoard().getFlop1());
		
		hand = handService.turn(hand);
		assertNotNull(hand.getBoard().getTurn());
		assertNull(hand.getBoard().getRiver());
	}
	
	@Test
	public void testRiver(){
		Game game = setupGame();
		HandEntity hand = handService.startNewHand(game);
		removeBetValues(hand);
		hand = handService.flop(hand);
		hand = handService.turn(hand);
		hand = handService.river(hand);
		
		assertNotNull(hand.getBoard().getFlop3());
		assertNotNull(hand.getBoard().getTurn());
		assertNotNull(hand.getBoard().getRiver());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testDuplicateFlop(){
		Game game = setupGame();
		HandEntity hand = handService.startNewHand(game);
		removeBetValues(hand);
		hand = handService.flop(hand);
		hand = handService.flop(hand);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testFaildedTurn(){
		Game game = setupGame();
		HandEntity hand = handService.startNewHand(game);
		removeBetValues(hand);
		hand = handService.turn(hand);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testDuplicateTurn(){
		Game game = setupGame();
		HandEntity hand = handService.startNewHand(game);
		removeBetValues(hand);
		hand = handService.flop(hand);
		hand = handService.turn(hand);
		hand = handService.turn(hand);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testFailedRiver(){
		Game game = setupGame();
		HandEntity hand = handService.startNewHand(game);
		removeBetValues(hand);
		hand = handService.flop(hand);
		hand = handService.river(hand);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testDuplicateRiver(){
		Game game = setupGame();
		HandEntity hand = handService.startNewHand(game);
		removeBetValues(hand);
		hand = handService.flop(hand);
		hand = handService.turn(hand);
		hand = handService.river(hand);
		hand = handService.river(hand);
	}
	
	@Test
	public void testNextToActAtStart(){
		Game game = setupGame();
		Player btnPlayer = game.getPlayerInBTN();
		assertNotNull(btnPlayer);
		
		HandEntity hand = handService.startNewHand(game);
		Player bbPlayer = handService.getPlayerInBB(hand);
		assertNotNull(bbPlayer);
		
		List<PlayerHand> players = new ArrayList<PlayerHand>();
		players.addAll(hand.getPlayers());
		Collections.sort(players);
		assertEquals(btnPlayer, players.get(0).getPlayer());
		assertEquals(bbPlayer, players.get(2).getPlayer());
		assertEquals("Check Next Player to Act is after BB", players.get(3).getPlayer(), hand.getCurrentToAct());
	}
	
	@Test
	public void testSitOutCurrentPlayer(){
		Game game = setupGame();
		HandEntity hand = handService.startNewHand(game);
		List<PlayerHand> players = new ArrayList<PlayerHand>();
		players.addAll(hand.getPlayers());
		Collections.sort(players);
		assertEquals("Check Next Player to Act is after BB", players.get(3).getPlayer(), hand.getCurrentToAct());
		
		handService.sitOutCurrentPlayer(hand);
		assertTrue(players.get(3).getPlayer().isSittingOut());
		assertEquals(players.get(0).getPlayer(), hand.getCurrentToAct());
	}
	
	@Test
	public void testSitOutCurrentPlayerWithOtherSitters(){
		Game game = setupGame();
		HandEntity hand = handService.startNewHand(game);
		List<PlayerHand> players = new ArrayList<PlayerHand>();
		players.addAll(hand.getPlayers());
		Collections.sort(players);
		assertEquals("Check Next Player to Act is after BB", players.get(3).getPlayer(), hand.getCurrentToAct());
		
		players.get(0).getPlayer().setSittingOut(true);
		handService.sitOutCurrentPlayer(hand);
		assertTrue(players.get(3).getPlayer().isSittingOut());
		//Player 0 is already out, when player 3 is sat out, action moves to 1
		assertEquals(players.get(1).getPlayer(), hand.getCurrentToAct());
	}
	
	@Test
	public void testEndHand(){
		Game game = setupGame();
		HandEntity hand = handService.startNewHand(game);
		removeBetValues(hand);
		Player bbPlayer = handService.getPlayerInBB(hand);
		
		List<PlayerHand> players = new ArrayList<PlayerHand>();
		players.addAll(hand.getPlayers());
		Collections.sort(players);
		assertEquals(bbPlayer, players.get(2).getPlayer());
		
		hand.setBoard(getDummyBoard());
		handService.endHand(hand);
		assertEquals(game.getPlayerInBTN(), players.get(1).getPlayer());
		
		hand = handService.startNewHand(game);
		removeBetValues(hand);
		assertEquals(players.get(3).getPlayer(), handService.getPlayerInBB(hand));
		assertEquals(players.get(2).getPlayer(), handService.getPlayerInSB(hand));
		
		hand.setBoard(getDummyBoard());
		handService.endHand(hand);
		hand = handService.startNewHand(game);
		removeBetValues(hand);
		assertEquals(game.getPlayerInBTN(), players.get(2).getPlayer());
		assertEquals(players.get(0).getPlayer(), handService.getPlayerInBB(hand));
		assertEquals(players.get(3).getPlayer(), handService.getPlayerInSB(hand));
		
		hand.setBoard(getDummyBoard());
		handService.endHand(hand);
		hand = handService.startNewHand(game);
		removeBetValues(hand);
		assertEquals(game.getPlayerInBTN(), players.get(3).getPlayer());
		assertEquals(players.get(1).getPlayer(), handService.getPlayerInBB(hand));
		assertEquals(players.get(0).getPlayer(), handService.getPlayerInSB(hand));
		
		hand.setBoard(getDummyBoard());
		handService.endHand(hand);
		hand = handService.startNewHand(game);
		removeBetValues(hand);
		assertEquals(game.getPlayerInBTN(), players.get(0).getPlayer());
		assertEquals(players.get(2).getPlayer(), handService.getPlayerInBB(hand));
		assertEquals(players.get(1).getPlayer(), handService.getPlayerInSB(hand));
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
		
		game.setPlayerInBTN(p1);
		game = gameDao.save(game);
		
		flushAndClear();
		
		return gameDao.findById(game.getId());
	}
	
	private BoardEntity getDummyBoard(){
		BoardEntity board = new BoardEntity();
		board.setFlop1(Card.ACE_OF_CLUBS);
		board.setFlop2(Card.EIGHT_OF_CLUBS);
		board.setFlop3(Card.FIVE_OF_HEARTS);
		board.setTurn(Card.ACE_OF_SPADES);
		board.setRiver(Card.TWO_OF_DIAMONDS);
		return board;
	}
	
	private void removeBetValues(HandEntity hand){
		for (PlayerHand ph : hand.getPlayers()){
			ph.setRoundBetAmount(0);
		}
		hand.setTotalBetAmount(0);
	}
}
