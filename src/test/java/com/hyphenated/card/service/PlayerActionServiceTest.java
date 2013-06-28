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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyphenated.card.AbstractSpringTest;
import com.hyphenated.card.Deck;
import com.hyphenated.card.dao.GameDao;
import com.hyphenated.card.dao.HandDao;
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
import com.hyphenated.card.domain.PlayerStatus;

public class PlayerActionServiceTest extends AbstractSpringTest {

	@Autowired
	private PlayerActionService playerActionService;
	
	@Autowired
	private PlayerDao playerDao;
	@Autowired
	private HandDao handDao;
	@Autowired 
	private GameDao gameDao;
	
	@Test
	public void testPlayerStatusStart(){
		Game start = setupBasicGame(false);
		int playerCount = 0;
		for(Player p : start.getPlayers()){
			assertEquals(PlayerStatus.NOT_STARTED, playerActionService.getPlayerStatus(p));
			playerCount++;
		}
		assertEquals(5, playerCount);
	}
	
	@Test
	public void testPlayerStatusSeating(){
		Game start = setupBasicGame(true);
		int playerCount = 0;
		for(Player p : start.getPlayers()){
			assertEquals(PlayerStatus.SEATING, playerActionService.getPlayerStatus(p));
			playerCount++;
		}
		assertEquals(5, playerCount);
	}
	
	@Test
	public void testPlayerHandStarted(){
		HandEntity hand = getBasicHand(this.setupBasicGame(true));
		List<Player> players = new ArrayList<Player>();
		players.addAll(hand.getGame().getPlayers());
		assertEquals(hand.getGame().getPlayers().size(), hand.getPlayers().size());
		Collections.sort(players);
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(players.get(0)));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(players.get(1)));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(players.get(2)));
		assertEquals(PlayerStatus.ACTION_TO_CALL, playerActionService.getPlayerStatus(players.get(3)));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(players.get(4)));
		assertEquals(2000, players.get(0).getChips());
		assertEquals(1000, players.get(1).getChips());
		assertEquals(1500, players.get(2).getChips());
		assertEquals(2200, players.get(3).getChips());
		assertEquals(500, players.get(4).getChips());
	}
	
	@Test
	public void testPreflopPlay(){
		HandEntity hand = getBasicHand(this.setupBasicGame(true));
		List<Player> players = new ArrayList<Player>();
		players.addAll(hand.getGame().getPlayers());
		assertEquals(hand.getGame().getPlayers().size(), hand.getPlayers().size());
		Collections.sort(players);
		assertEquals(30, hand.getPot());
		assertEquals(20, hand.getLastBetAmount());
		assertEquals(20, hand.getTotalBetAmount());
		//Cannot check out of order
		assertFalse(playerActionService.check(players.get(0), hand));
		assertFalse(playerActionService.check(players.get(1), hand));
		assertFalse(playerActionService.check(players.get(2), hand));
		assertFalse(playerActionService.check(players.get(4), hand));
		//Cannot bet out of order
		assertFalse(playerActionService.bet(players.get(0), hand, 100));
		assertFalse(playerActionService.bet(players.get(1), hand, 100));
		assertFalse(playerActionService.bet(players.get(2), hand, 100));
		assertFalse(playerActionService.bet(players.get(4), hand, 100));
		//Cannot call out of order
		assertFalse(playerActionService.call(players.get(0), hand));
		assertFalse(playerActionService.call(players.get(1), hand));
		assertFalse(playerActionService.call(players.get(2), hand));
		assertFalse(playerActionService.call(players.get(4), hand));
		//Cannot fold out of order
		assertFalse(playerActionService.fold(players.get(0), hand));
		assertFalse(playerActionService.fold(players.get(1), hand));
		assertFalse(playerActionService.fold(players.get(2), hand));
		assertFalse(playerActionService.fold(players.get(4), hand));
		
		//Make sure all is still well
		assertEquals(30, hand.getPot());
		assertEquals(20, hand.getLastBetAmount());
		assertEquals(20, hand.getTotalBetAmount());
		assertEquals(players.get(3), hand.getCurrentToAct());
		
		assertTrue(playerActionService.call(players.get(3), hand));
		//2200 - 20 to call
		assertEquals(2180, players.get(3).getChips());
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(players.get(3)));
		assertEquals(PlayerStatus.ACTION_TO_CALL, playerActionService.getPlayerStatus(players.get(4)));
		
		assertFalse(playerActionService.bet(players.get(4), hand,10));
		assertTrue(playerActionService.bet(players.get(4), hand,20));
		assertEquals(460, players.get(4).getChips());
		assertEquals(40, hand.getTotalBetAmount());
		assertEquals(20, hand.getLastBetAmount());
		assertEquals(90, hand.getPot());
		
		assertTrue(playerActionService.call(players.get(0), hand));
		assertEquals(1960, players.get(0).getChips());
		assertEquals(40, hand.getTotalBetAmount());
		assertEquals(20, hand.getLastBetAmount());
		assertEquals(130, hand.getPot());
		
		assertTrue(playerActionService.fold(players.get(1), hand));
		assertEquals(4,hand.getPlayers().size());
		assertEquals(130, hand.getPot());
		assertEquals(PlayerStatus.ACTION_TO_CALL, playerActionService.getPlayerStatus(players.get(2)));
		
		assertTrue(playerActionService.call(players.get(2), hand));
		assertEquals(1460, players.get(2).getChips());
		assertEquals(40, hand.getTotalBetAmount());
		assertEquals(20, hand.getLastBetAmount());
		assertEquals(170, hand.getPot());
		assertEquals(PlayerStatus.ACTION_TO_CALL, playerActionService.getPlayerStatus(players.get(3)));
		
		assertTrue(playerActionService.call(players.get(3), hand));
		assertEquals(2160, players.get(3).getChips());
		assertEquals(40, hand.getTotalBetAmount());
		assertEquals(20, hand.getLastBetAmount());
		assertEquals(190, hand.getPot());	
	}
	
	@Test
	public void testPostFlopPlay(){
		HandEntity hand = getBasicHand(this.setupBasicGame(true));
		List<Player> players = new ArrayList<Player>();
		players.addAll(hand.getGame().getPlayers());
		Collections.sort(players);
		assertTrue(playerActionService.call(players.get(3), hand));
		assertTrue(playerActionService.bet(players.get(4), hand,20));
		assertTrue(playerActionService.call(players.get(0), hand));
		assertTrue(playerActionService.fold(players.get(1), hand));
		assertTrue(playerActionService.call(players.get(2), hand));
		assertTrue(playerActionService.call(players.get(3), hand));
		assertEquals(40, hand.getTotalBetAmount());
		assertEquals(20, hand.getLastBetAmount());
		assertEquals(190, hand.getPot());	
		
		assertEquals(2160, players.get(3).getChips());
		assertEquals(1460, players.get(2).getChips());
		assertEquals(1960, players.get(0).getChips());
		assertEquals(460, players.get(4).getChips());
		
		//caught up on preflop action.  Now deal the flop
		hand.setTotalBetAmount(0);
		hand.setLastBetAmount(0);
		for(PlayerHand ph : hand.getPlayers()){
			ph.setRoundBetAmount(0);
		}
		hand.setCurrentToAct(players.get(2));
		
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(players.get(0)));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(players.get(1)));
		assertEquals(PlayerStatus.ACTION_TO_CHECK, playerActionService.getPlayerStatus(players.get(2)));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(players.get(3)));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(players.get(4)));
		
		assertTrue(playerActionService.check(players.get(2), hand));
		assertTrue(playerActionService.check(players.get(3), hand));
		
		assertEquals(PlayerStatus.ACTION_TO_CHECK, playerActionService.getPlayerStatus(players.get(4)));
		assertTrue(playerActionService.bet(players.get(4), hand, 100));
		assertEquals(100, hand.getTotalBetAmount());
		assertEquals(100, hand.getLastBetAmount());
		assertEquals(290, hand.getPot());
		assertEquals(360, players.get(4).getChips());
		
		assertEquals(PlayerStatus.ACTION_TO_CALL, playerActionService.getPlayerStatus(players.get(0)));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(players.get(3)));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(players.get(4)));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(players.get(2)));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(players.get(1)));
		
		assertTrue(playerActionService.call(players.get(0), hand));
		assertEquals(1860, players.get(0).getChips());
		
		assertTrue(playerActionService.fold(players.get(2), hand));
		assertEquals(3, hand.getPlayers().size());
		
		assertTrue(playerActionService.bet(players.get(3), hand, 400));
		assertEquals(1660, players.get(3).getChips());
		assertEquals(500, hand.getTotalBetAmount());
		assertEquals(400, hand.getLastBetAmount());
		assertEquals(890, hand.getPot());
		
		assertTrue(playerActionService.call(players.get(4), hand));
		assertEquals(0, players.get(4).getChips());
		assertEquals(500, hand.getTotalBetAmount());
		assertEquals(400, hand.getLastBetAmount());
		//890 + 360 all in
		assertEquals(1250, hand.getPot());
		
		assertEquals(PlayerStatus.ACTION_TO_CALL, playerActionService.getPlayerStatus(players.get(0)));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(players.get(3)));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(players.get(4)));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(players.get(2)));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(players.get(1)));
		
		assertTrue(playerActionService.call(players.get(0), hand));
		assertEquals(1460, players.get(0).getChips());
		assertEquals(500, hand.getTotalBetAmount());
		assertEquals(400, hand.getLastBetAmount());
		assertEquals(1650, hand.getPot());
		
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(players.get(0)));
		assertEquals(PlayerStatus.ACTION_TO_CHECK, playerActionService.getPlayerStatus(players.get(3)));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(players.get(4)));
	}
	
	@Test
	public void testSittingOutFold(){
		HandEntity hand = getBasicHand(this.setupBasicGame(true));
		List<Player> players = new ArrayList<Player>();
		players.addAll(hand.getGame().getPlayers());
		assertEquals(hand.getGame().getPlayers().size(), hand.getPlayers().size());
		Collections.sort(players);
		players.get(4).setSittingOut(true);
		assertEquals(30, hand.getPot());
		assertEquals(20, hand.getLastBetAmount());
		assertEquals(20, hand.getTotalBetAmount());
		
		//3 is the next player to act
		assertEquals(players.get(3), hand.getCurrentToAct());
		assertEquals(5, hand.getPlayers().size());
		
		assertTrue(playerActionService.call(players.get(3), hand));
		assertEquals(PlayerStatus.ACTION_TO_CALL, playerActionService.getPlayerStatus(players.get(0)));
		flushAndClear();
		hand = handDao.findById(hand.getId());
		assertEquals(4, hand.getPlayers().size());
	}
	
	@Test
	public void testSittingOutCall(){
		HandEntity hand = getBasicHand(this.setupBasicGame(true));
		List<Player> players = new ArrayList<Player>();
		players.addAll(hand.getGame().getPlayers());
		Collections.sort(players);
		assertTrue(playerActionService.call(players.get(3), hand));
		assertTrue(playerActionService.call(players.get(4), hand));
		assertTrue(playerActionService.call(players.get(0), hand));
		assertTrue(playerActionService.call(players.get(1), hand));
		assertTrue(playerActionService.call(players.get(2), hand));
		
		assertEquals(130, hand.getPot());
		players.get(2).setSittingOut(true);
		players.get(3).setSittingOut(true);
		
		//caught up on preflop action.  Now deal the flop
		hand.setTotalBetAmount(0);
		hand.setLastBetAmount(0);
		for(PlayerHand ph : hand.getPlayers()){
			ph.setRoundBetAmount(0);
		}
		hand.setCurrentToAct(players.get(1));
		
		assertTrue(playerActionService.check(players.get(1), hand));
		
		assertEquals(players.get(4), hand.getCurrentToAct());
		
	}
	
	private HandEntity getBasicHand(Game game){
		HandEntity hand = new HandEntity();
		hand.setGame(game);
		Deck d = new Deck(true);
		Set<PlayerHand> participatingPlayers = new HashSet<PlayerHand>();
		Player start = new Player();
		for(Player p : game.getPlayers()){
			PlayerHand ph = new PlayerHand();
			ph.setHandEntity(hand);
			ph.setPlayer(p);
			ph.setCard1(d.dealCard());
			ph.setCard2(d.dealCard());
			participatingPlayers.add(ph);
			if(p.getName().equals("Player 4")){
				start = p;
			}
		}
		hand.setCurrentToAct(start);
		hand.setPlayers(participatingPlayers);
		hand.setBlindLevel(game.getGameStructure().getCurrentBlindLevel());
		hand.setTotalBetAmount(hand.getBlindLevel().getBigBlind());
		hand.setLastBetAmount(hand.getBlindLevel().getBigBlind());
		hand.setPot(hand.getBlindLevel().getBigBlind() + hand.getBlindLevel().getSmallBlind());
		
		BoardEntity b = new BoardEntity();
		hand.setBoard(b);
		hand.setCards(d.exportDeck());
		hand = handDao.save(hand);
		
		game.setCurrentHand(hand);
		gameDao.save(game);
		flushAndClear();
		return handDao.findById(hand.getId());
	}
	
	private Game setupBasicGame(boolean isStarted){
		Game game = new Game();
		game.setName("Test Game");
		game.setPlayersRemaining(5);
		game.setStarted(isStarted);
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
		p1.setChips(2000);
		p1.setGamePosition(1);
		playerDao.save(p1);
		
		Player p2 = new Player();
		p2.setName("Player 2");
		p2.setGame(game);
		p2.setChips(1000);
		p2.setGamePosition(2);
		playerDao.save(p2);
		
		Player p3 = new Player();
		p3.setName("Player 3");
		p3.setGame(game);
		p3.setChips(1500);
		p3.setGamePosition(3);
		playerDao.save(p3);
		
		Player p4 = new Player();
		p4.setName("Player 4");
		p4.setGame(game);
		p4.setChips(2200);
		p4.setGamePosition(4);
		playerDao.save(p4);
		
		Player p5 = new Player();
		p5.setName("Player 5");
		p5.setGame(game);
		p5.setChips(500);
		p5.setGamePosition(5);
		playerDao.save(p5);
		
		game.setPlayerInBTN(p1);
		game = gameDao.save(game);
		
		flushAndClear();
		
		return gameDao.findById(game.getId());
	}
}
