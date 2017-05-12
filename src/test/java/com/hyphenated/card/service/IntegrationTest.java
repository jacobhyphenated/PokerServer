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

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyphenated.card.AbstractSpringTest;
import com.hyphenated.card.Card;
import com.hyphenated.card.domain.BlindLevel;
import com.hyphenated.card.domain.CommonTournamentFormats;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameStatus;
import com.hyphenated.card.domain.GameType;
import com.hyphenated.card.domain.HandEntity;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.domain.PlayerHand;
import com.hyphenated.card.domain.PlayerStatus;
import com.hyphenated.card.domain.TournamentStructure;
import com.hyphenated.card.util.GameUtil;

public class IntegrationTest extends AbstractSpringTest {

	@Autowired
	private GameService gameService;

	@Autowired
	private PlayerActionService playerActionService;

	@Autowired
	private PokerHandService handService;

	@Test
	public void testGameStart() {
		Game game = new Game();
		game.setName("Test Game");
		game.setGameType(GameType.TOURNAMENT);
		TournamentStructure gs = new TournamentStructure();
		gs.setBlindLength(10);
		gs.setBlindLevels(CommonTournamentFormats.TWO_HR_NINEPPL.getBlindLevels());
		gs.setStartingChips(1000);
		game.setGameStructure(gs);

		game = gameService.saveGame(game);

		flushAndClear();

		game = gameService.getGameById(game.getId(), true);

		Player p1 = new Player();
		p1.setChips(game.getGameStructure().getStartingChips());
		p1.setName("Player 1");
		p1 = gameService.addNewPlayerToGame(game, p1);

		Player p2 = new Player();
		p2.setChips(game.getGameStructure().getStartingChips());
		p2.setName("Player 2");
		p2 = gameService.addNewPlayerToGame(game, p2);

		Player p3 = new Player();
		p3.setChips(game.getGameStructure().getStartingChips());
		p3.setName("Player 3");
		p3 = gameService.addNewPlayerToGame(game, p3);

		flushAndClear();
		assertEquals(PlayerStatus.NOT_STARTED, playerActionService.getPlayerStatus(p1));
		assertEquals(PlayerStatus.NOT_STARTED, playerActionService.getPlayerStatus(p2));
		assertEquals(PlayerStatus.NOT_STARTED, playerActionService.getPlayerStatus(p3));
		assertEquals(GameStatus.NOT_STARTED, GameUtil.getGameStatus(game));

		game = gameService.getGameById(game.getId(), true);

		game = gameService.startGame(game);
		assertTrue(game.isStarted());
		assertEquals(3, game.getPlayersRemaining());
		assertEquals(PlayerStatus.SEATING, playerActionService.getPlayerStatus(p1));
		assertEquals(PlayerStatus.SEATING, playerActionService.getPlayerStatus(p2));
		assertEquals(PlayerStatus.SEATING, playerActionService.getPlayerStatus(p3));
		assertEquals(GameStatus.SEATING, GameUtil.getGameStatus(game));

		HandEntity hand = handService.startNewHand(game);
		TournamentStructure structure = (TournamentStructure) game.getGameStructure();
		assertEquals(structure.getCurrentBlindLevel(), BlindLevel.BLIND_10_20);
		assertEquals(hand.getBlindLevel(), BlindLevel.BLIND_10_20);

		Player btn = hand.getCurrentToAct();
		Player sb = handService.getPlayerInSB(hand);
		Player bb = handService.getPlayerInBB(hand);
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.ACTION_TO_CALL, playerActionService.getPlayerStatus(btn));
		assertEquals(GameStatus.PREFLOP, GameUtil.getGameStatus(game));

		assertEquals(1000, btn.getChips());
		assertEquals(990, sb.getChips());
		assertEquals(980, bb.getChips());
		assertEquals(30, hand.getPot());
		assertEquals(20, hand.getLastBetAmount());
		assertEquals(20, hand.getTotalBetAmount());
	}

	@Test
	public void testFoldToBB() {
		Game game = gameSetup();
		HandEntity hand = handService.startNewHand(game);
		Player bb = handService.getPlayerInBB(hand);
		playerActionService.fold(hand.getCurrentToAct(), hand);
		playerActionService.fold(hand.getCurrentToAct(), hand);
		playerActionService.fold(hand.getCurrentToAct(), hand);
		handService.endHand(hand);
		assertEquals(1010, bb.getChips());
	}

	@Test
	public void testFoldOnFlop() {
		Game game = gameSetup();
		HandEntity hand = handService.startNewHand(game);
		Player bb = handService.getPlayerInBB(hand);
		Player sb = handService.getPlayerInSB(hand);
		assertTrue(playerActionService.fold(hand.getCurrentToAct(), hand));
		playerActionService.call(hand.getCurrentToAct(), hand);
		playerActionService.call(sb, hand);
		assertEquals(60, hand.getPot());
		assertEquals(PlayerStatus.ACTION_TO_CHECK, playerActionService.getPlayerStatus(bb));

		handService.flop(hand);
		assertEquals(PlayerStatus.ACTION_TO_CHECK, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(bb));

		playerActionService.bet(sb, hand, 200);
		assertEquals(PlayerStatus.ACTION_TO_CALL, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(sb));

		assertTrue(playerActionService.fold(bb, hand));
		assertTrue(playerActionService.fold(hand.getCurrentToAct(), hand));

		assertEquals(1, hand.getPlayers().size());
		handService.endHand(hand);

		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.WON_HAND, playerActionService.getPlayerStatus(sb));
		assertEquals(980, bb.getChips());
		assertEquals(1040, sb.getChips());
	}

	@Test
	public void testFoldTurnWithMultipleBets() {
		Game game = gameSetup();
		HandEntity hand = handService.startNewHand(game);
		Player bb = handService.getPlayerInBB(hand);
		Player sb = handService.getPlayerInSB(hand);
		Player co = hand.getCurrentToAct();
		playerActionService.call(co, hand);
		Player btn = hand.getCurrentToAct();
		// Raise of 60 on top of 20. 80 total.
		playerActionService.bet(btn, hand, 60);
		playerActionService.call(sb, hand);
		playerActionService.fold(bb, hand);
		playerActionService.call(co, hand);
		// Three callers, one fold. BB out 20 chips, 3 in for 80
		assertEquals(260, hand.getPot());
		assertEquals(80, hand.getTotalBetAmount());
		assertEquals(60, hand.getLastBetAmount());

		hand = handService.flop(hand);
		assertTrue(playerActionService.bet(sb, hand, 200));
		assertTrue(playerActionService.call(co, hand));
		assertFalse(playerActionService.bet(btn, hand, 100));
		assertTrue(playerActionService.bet(btn, hand, 200));
		assertTrue(playerActionService.fold(sb, hand));
		assertTrue(playerActionService.call(co, hand));
		// Raise of 200 on top of 200 bet. 400 total bet.
		// 200 put in by SB, who folds. 400 put in by btn and co.
		assertEquals(1260, hand.getPot());
		assertEquals(PlayerStatus.ACTION_TO_CHECK, playerActionService.getPlayerStatus(btn));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(bb));

		hand = handService.turn(hand);
		assertEquals(PlayerStatus.ACTION_TO_CHECK, playerActionService.getPlayerStatus(co));
		assertTrue(playerActionService.check(co, hand));
		assertTrue(playerActionService.bet(btn, hand, 50));
		assertTrue(playerActionService.fold(co, hand));
		handService.endHand(hand);

		assertEquals(PlayerStatus.WON_HAND, playerActionService.getPlayerStatus(btn));
		assertEquals(980, bb.getChips());
		assertEquals(720, sb.getChips());
		assertEquals(520, co.getChips());
		assertEquals(1780, btn.getChips());
	}

	@Test
	public void testToRiverShowdown() {
		Game game = gameSetup();
		HandEntity hand = handService.startNewHand(game);
		Player bb = handService.getPlayerInBB(hand);
		Player sb = handService.getPlayerInSB(hand);
		Player co = hand.getCurrentToAct();
		playerActionService.call(co, hand);
		Player btn = hand.getCurrentToAct();
		// Raise of 45 on top of 20. 65 total.
		playerActionService.bet(btn, hand, 45);
		playerActionService.call(sb, hand);
		playerActionService.fold(bb, hand);
		playerActionService.call(co, hand);
		// Three callers, one fold. BB out 20 chips, 3 in for 65
		assertEquals(215, hand.getPot());
		assertEquals(65, hand.getTotalBetAmount());
		assertEquals(45, hand.getLastBetAmount());
		for (PlayerHand ph : hand.getPlayers()) {
			assertEquals(65, ph.getBetAmount());
			assertEquals(65, ph.getRoundBetAmount());
		}
		assertEquals(GameStatus.PREFLOP, GameUtil.getGameStatus(game));

		hand = handService.flop(hand);
		assertTrue(playerActionService.check(sb, hand));
		assertTrue(playerActionService.check(co, hand));
		assertTrue(playerActionService.check(btn, hand));
		for (PlayerHand ph : hand.getPlayers()) {
			assertEquals(65, ph.getBetAmount());
			assertEquals(0, ph.getRoundBetAmount());
		}
		assertEquals(GameStatus.FLOP, GameUtil.getGameStatus(game));

		hand = handService.turn(hand);
		assertTrue(playerActionService.check(sb, hand));
		assertTrue(playerActionService.bet(co, hand, 100));
		assertTrue(playerActionService.call(btn, hand));
		assertTrue(playerActionService.fold(sb, hand));
		// two callers, one fold. 2 in for 100 more
		assertEquals(415, hand.getPot());
		for (PlayerHand ph : hand.getPlayers()) {
			assertEquals(165, ph.getBetAmount());
			assertEquals(100, ph.getRoundBetAmount());
		}
		assertEquals(GameStatus.TURN, GameUtil.getGameStatus(game));

		hand = handService.river(hand);
		assertEquals(PlayerStatus.ACTION_TO_CHECK, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(btn));
		assertTrue(playerActionService.check(co, hand));
		assertTrue(playerActionService.bet(btn, hand, 100));
		assertTrue(playerActionService.bet(co, hand, 200));
		assertTrue(playerActionService.call(btn, hand));
		for (PlayerHand ph : hand.getPlayers()) {
			assertEquals(465, ph.getBetAmount());
			assertEquals(300, ph.getRoundBetAmount());
		}
		assertEquals(1015, hand.getPot());
		assertEquals(GameStatus.RIVER, GameUtil.getGameStatus(game));

		for (PlayerHand ph : hand.getPlayers()) {
			if (ph.getPlayer().equals(co)) {
				ph.setCard1(Card.ACE_OF_CLUBS);
				ph.setCard2(Card.ACE_OF_DIAMONDS);
			} else {
				ph.setCard1(Card.KING_OF_CLUBS);
				ph.setCard2(Card.KING_OF_DIAMONDS);
			}
		}
		hand.getBoard().setFlop1(Card.TWO_OF_SPADES);
		hand.getBoard().setFlop2(Card.THREE_OF_SPADES);
		hand.getBoard().setFlop3(Card.SIX_OF_DIAMONDS);
		hand.getBoard().setTurn(Card.QUEEN_OF_CLUBS);
		hand.getBoard().setRiver(Card.NINE_OF_HEARTS);

		handService.endHand(hand);

		assertEquals(PlayerStatus.LOST_HAND, playerActionService.getPlayerStatus(btn));
		assertEquals(PlayerStatus.WON_HAND, playerActionService.getPlayerStatus(co));
		assertEquals(GameStatus.END_HAND, GameUtil.getGameStatus(game));

		assertEquals(980, bb.getChips());
		assertEquals(935, sb.getChips());
		assertEquals(535, btn.getChips());
		assertEquals(1550, co.getChips());
	}

	@Test
	public void testEndHandWithElimination() {
		Game game = gameSetup();
		HandEntity hand = handService.startNewHand(game);
		Player bb = handService.getPlayerInBB(hand);
		Player sb = handService.getPlayerInSB(hand);
		Player co = hand.getCurrentToAct();
		playerActionService.call(co, hand);
		Player btn = hand.getCurrentToAct();
		assertTrue(playerActionService.bet(btn, hand, 1000));
		assertTrue(playerActionService.fold(sb, hand));
		assertTrue(playerActionService.fold(bb, hand));
		assertTrue(playerActionService.call(co, hand));
		assertEquals(2030, hand.getPot());
		assertEquals(1000, hand.getTotalBetAmount());
		assertEquals(980, hand.getLastBetAmount());

		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(btn));

		handService.flop(hand);
		handService.turn(hand);
		handService.river(hand);

		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(btn));

		for (PlayerHand ph : hand.getPlayers()) {
			if (ph.getPlayer().equals(co)) {
				ph.setCard1(Card.ACE_OF_CLUBS);
				ph.setCard2(Card.ACE_OF_DIAMONDS);
			} else {
				ph.setCard1(Card.KING_OF_CLUBS);
				ph.setCard2(Card.KING_OF_DIAMONDS);
			}
		}
		hand.getBoard().setFlop1(Card.TWO_OF_SPADES);
		hand.getBoard().setFlop2(Card.THREE_OF_SPADES);
		hand.getBoard().setFlop3(Card.SIX_OF_DIAMONDS);
		hand.getBoard().setTurn(Card.QUEEN_OF_CLUBS);
		hand.getBoard().setRiver(Card.NINE_OF_HEARTS);

		handService.endHand(hand);
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.WON_HAND, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.LOST_HAND, playerActionService.getPlayerStatus(btn));

		hand = handService.startNewHand(game);
		assertEquals(3, hand.getPlayers().size());

		assertEquals(PlayerStatus.ACTION_TO_CALL, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.ELIMINATED, playerActionService.getPlayerStatus(btn));

		assertEquals(990, sb.getChips());
		assertEquals(970, bb.getChips());
		assertEquals(2010, co.getChips());
	}

	@Test
	public void testEndHandWithEliminationSB() {
		Game game = gameSetup();
		HandEntity hand = handService.startNewHand(game);
		Player bb = handService.getPlayerInBB(hand);
		Player sb = handService.getPlayerInSB(hand);
		Player co = hand.getCurrentToAct();
		playerActionService.fold(co, hand);
		Player btn = hand.getCurrentToAct();
		playerActionService.bet(btn, hand, 1000);
		playerActionService.call(sb, hand);
		playerActionService.fold(bb, hand);

		assertEquals(2020, hand.getPot());
		assertEquals(1000, hand.getTotalBetAmount());
		assertEquals(980, hand.getLastBetAmount());

		handService.flop(hand);
		handService.turn(hand);
		handService.river(hand);

		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(btn));

		for (PlayerHand ph : hand.getPlayers()) {
			if (ph.getPlayer().equals(btn)) {
				ph.setCard1(Card.ACE_OF_CLUBS);
				ph.setCard2(Card.ACE_OF_DIAMONDS);
			} else {
				ph.setCard1(Card.KING_OF_CLUBS);
				ph.setCard2(Card.KING_OF_DIAMONDS);
			}
		}
		hand.getBoard().setFlop1(Card.TWO_OF_SPADES);
		hand.getBoard().setFlop2(Card.THREE_OF_SPADES);
		hand.getBoard().setFlop3(Card.SIX_OF_DIAMONDS);
		hand.getBoard().setTurn(Card.QUEEN_OF_CLUBS);
		hand.getBoard().setRiver(Card.NINE_OF_HEARTS);

		handService.endHand(hand);
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.WON_HAND, playerActionService.getPlayerStatus(btn));
		assertEquals(PlayerStatus.LOST_HAND, playerActionService.getPlayerStatus(sb));

		hand = handService.startNewHand(game);

		// SB eliminated and Button moves to BB. This is simple moving button.
		assertEquals(PlayerStatus.ELIMINATED, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.ACTION_TO_CALL, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(btn));

		assertEquals(4, sb.getFinishPosition());
		assertEquals(3, game.getPlayersRemaining());

		assertEquals(2000, btn.getChips());
		assertEquals(980, bb.getChips());
		assertEquals(990, co.getChips());
	}

	@Test
	public void testEndHandWithEliminationNextBB() {
		Game game = gameSetup();
		HandEntity hand = handService.startNewHand(game);
		Player bb = handService.getPlayerInBB(hand);
		Player sb = handService.getPlayerInSB(hand);
		Player co = hand.getCurrentToAct();
		playerActionService.bet(co, hand, 1000);
		Player btn = hand.getCurrentToAct();
		playerActionService.fold(btn, hand);
		playerActionService.call(sb, hand);
		playerActionService.fold(bb, hand);

		assertEquals(2020, hand.getPot());
		assertEquals(1000, hand.getTotalBetAmount());
		assertEquals(980, hand.getLastBetAmount());

		handService.flop(hand);
		handService.turn(hand);
		handService.river(hand);

		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(btn));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(co));

		for (PlayerHand ph : hand.getPlayers()) {
			if (ph.getPlayer().equals(sb)) {
				ph.setCard1(Card.ACE_OF_CLUBS);
				ph.setCard2(Card.ACE_OF_DIAMONDS);
			} else {
				ph.setCard1(Card.KING_OF_CLUBS);
				ph.setCard2(Card.KING_OF_DIAMONDS);
			}
		}
		hand.getBoard().setFlop1(Card.TWO_OF_SPADES);
		hand.getBoard().setFlop2(Card.THREE_OF_SPADES);
		hand.getBoard().setFlop3(Card.SIX_OF_DIAMONDS);
		hand.getBoard().setTurn(Card.QUEEN_OF_CLUBS);
		hand.getBoard().setRiver(Card.NINE_OF_HEARTS);

		handService.endHand(hand);
		assertEquals(PlayerStatus.WON_HAND, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.LOST_HAND, playerActionService.getPlayerStatus(co));

		hand = handService.startNewHand(game);

		// SB eliminated and Button moves to BB. This is simple moving button.
		assertEquals(PlayerStatus.ELIMINATED, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.ACTION_TO_CALL, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(btn));

		assertEquals(4, co.getFinishPosition());
		assertEquals(0, sb.getFinishPosition());
		assertEquals(0, btn.getFinishPosition());
		assertEquals(0, bb.getFinishPosition());
		assertEquals(3, game.getPlayersRemaining());

		assertEquals(2020, sb.getChips());
		assertEquals(970, bb.getChips());
		assertEquals(980, btn.getChips());
	}

	@Test
	public void testEndHandWithEliminationToHeadsUp() {
		Game game = gameSetup();
		HandEntity hand = handService.startNewHand(game);
		Player bb = handService.getPlayerInBB(hand);
		Player sb = handService.getPlayerInSB(hand);
		Player co = hand.getCurrentToAct();
		playerActionService.bet(co, hand, 1000);
		Player btn = hand.getCurrentToAct();
		playerActionService.call(btn, hand);
		playerActionService.call(sb, hand);
		playerActionService.fold(bb, hand);
		assertEquals(3020, hand.getPot());
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(btn));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(co));

		handService.flop(hand);
		handService.turn(hand);
		handService.river(hand);

		for (PlayerHand ph : hand.getPlayers()) {
			if (ph.getPlayer().equals(sb)) {
				ph.setCard1(Card.ACE_OF_CLUBS);
				ph.setCard2(Card.ACE_OF_DIAMONDS);
			} else if (ph.getPlayer().equals(co)) {
				ph.setCard1(Card.KING_OF_CLUBS);
				ph.setCard2(Card.KING_OF_DIAMONDS);
			} else {
				ph.setCard1(Card.JACK_OF_CLUBS);
				ph.setCard2(Card.JACK_OF_DIAMONDS);
			}
		}
		hand.getBoard().setFlop1(Card.TWO_OF_SPADES);
		hand.getBoard().setFlop2(Card.THREE_OF_SPADES);
		hand.getBoard().setFlop3(Card.SIX_OF_DIAMONDS);
		hand.getBoard().setTurn(Card.QUEEN_OF_CLUBS);
		hand.getBoard().setRiver(Card.NINE_OF_HEARTS);

		handService.endHand(hand);
		assertEquals(PlayerStatus.WON_HAND, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.LOST_HAND, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.LOST_HAND, playerActionService.getPlayerStatus(btn));

		hand = handService.startNewHand(game);
		// Two eliminations, down to heads up. Button moves to SB. SB is now SB
		// again. SB/BTN is first to act
		assertEquals(PlayerStatus.ELIMINATED, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.ACTION_TO_CALL, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.ELIMINATED, playerActionService.getPlayerStatus(btn));

		if (co.getFinishPosition() != 4) {
			assertEquals(3, co.getFinishPosition());
			assertEquals(4, btn.getFinishPosition());
		} else {
			assertEquals(4, co.getFinishPosition());
			assertEquals(3, btn.getFinishPosition());
		}
		assertEquals(2, game.getPlayersRemaining());

		assertEquals(3010, sb.getChips());
		assertEquals(960, bb.getChips());
	}

	@Test
	public void testRiverShowdownWithSidePots() {
		Game game = gameSetup();
		HandEntity hand = handService.startNewHand(game);
		Player bb = handService.getPlayerInBB(hand);
		Player sb = handService.getPlayerInSB(hand);
		Player co = hand.getCurrentToAct();
		playerActionService.call(co, hand);
		Player btn = hand.getCurrentToAct();
		playerActionService.call(btn, hand);
		playerActionService.call(sb, hand);
		playerActionService.check(bb, hand);

		// Set chip amounts for Side pots
		sb.setChips(480);
		bb.setChips(980);
		co.setChips(1780);
		btn.setChips(1880);
		assertEquals(80, hand.getPot());

		handService.flop(hand);

		assertTrue(playerActionService.bet(sb, hand, 100));
		assertTrue(playerActionService.call(bb, hand));
		assertTrue(playerActionService.bet(co, hand, 400));
		assertTrue(playerActionService.call(btn, hand));
		assertTrue(playerActionService.call(sb, hand)); // Call off last 380/480
		assertTrue(playerActionService.call(bb, hand));

		assertEquals(2060, hand.getPot());
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.ACTION_TO_CHECK, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.WAITING, playerActionService.getPlayerStatus(btn));

		handService.turn(hand);
		assertEquals(PlayerStatus.ACTION_TO_CHECK, playerActionService.getPlayerStatus(bb));
		assertTrue(playerActionService.bet(bb, hand, 500));

		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(bb));
		assertEquals(480, hand.getTotalBetAmount());
		assertEquals(480, hand.getLastBetAmount());

		assertTrue(playerActionService.call(co, hand));
		assertTrue(playerActionService.bet(btn, hand, 1200)); // Shove

		assertEquals(900, hand.getLastBetAmount()); // Actual amount shoved
		assertEquals(1380, hand.getTotalBetAmount());
		assertTrue(playerActionService.call(co, hand));

		assertEquals(5200, hand.getPot()); // 80 + 480 + 980 + 1780 + 1780 + 100
											// extra from btn

		handService.river(hand);

		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(btn));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(co));

		for (PlayerHand ph : hand.getPlayers()) {
			if (ph.getPlayer().equals(sb)) {
				ph.setCard1(Card.ACE_OF_CLUBS);
				ph.setCard2(Card.ACE_OF_DIAMONDS);
			} else if (ph.getPlayer().equals(co)) {
				ph.setCard1(Card.KING_OF_CLUBS);
				ph.setCard2(Card.KING_OF_DIAMONDS);
			} else if (ph.getPlayer().equals(bb)) {
				ph.setCard1(Card.JACK_OF_CLUBS);
				ph.setCard2(Card.JACK_OF_DIAMONDS);
			} else {
				ph.setCard1(Card.FIVE_OF_CLUBS);
				ph.setCard2(Card.FIVE_OF_HEARTS);
			}
		}

		hand.getBoard().setFlop1(Card.TWO_OF_SPADES);
		hand.getBoard().setFlop2(Card.THREE_OF_SPADES);
		hand.getBoard().setFlop3(Card.SIX_OF_DIAMONDS);
		hand.getBoard().setTurn(Card.QUEEN_OF_CLUBS);
		hand.getBoard().setRiver(Card.NINE_OF_HEARTS);

		handService.endHand(hand);

		// SB wins first side pot of 500 * 4
		// BB loses second side pot and is eliminated, won by co with 500 * 3
		// BTN gets back overbet of 100
		// CO wins last side pot 800 * 2
		assertEquals(2000, sb.getChips());
		assertEquals(100, btn.getChips());
		assertEquals(3100, co.getChips());

		// BTN loses showdown, but gets 100 chips back from the overbet
		assertEquals(PlayerStatus.LOST_HAND, playerActionService.getPlayerStatus(btn));
		assertEquals(PlayerStatus.LOST_HAND, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.WON_HAND, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.WON_HAND, playerActionService.getPlayerStatus(co));

		assertEquals(4, bb.getFinishPosition());
	}

	@Test
	public void testMultipleEliminationsWithDifferentStacks() {
		Game game = gameSetup();
		HandEntity hand = handService.startNewHand(game);
		Player bb = handService.getPlayerInBB(hand);
		Player sb = handService.getPlayerInSB(hand);
		Player co = hand.getCurrentToAct();
		playerActionService.call(co, hand);
		Player btn = hand.getCurrentToAct();
		playerActionService.call(btn, hand);
		playerActionService.call(sb, hand);
		playerActionService.check(bb, hand);

		// Set chip amounts for Side pots
		sb.setChips(480);
		bb.setChips(980);
		co.setChips(1780);
		btn.setChips(1880);
		assertEquals(80, hand.getPot());

		handService.flop(hand);
		playerActionService.bet(sb, hand, 480);
		playerActionService.bet(bb, hand, 500);
		playerActionService.fold(co, hand);
		playerActionService.call(btn, hand);

		handService.turn(hand);
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(sb));
		assertEquals(PlayerStatus.ALL_IN, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.ACTION_TO_CHECK, playerActionService.getPlayerStatus(btn));
		// 80 + 480 * 3 + 500 * 2
		assertEquals(2520, hand.getPot());

		handService.river(hand);

		// Give BTN winning set
		for (PlayerHand ph : hand.getPlayers()) {
			if (ph.getPlayer().equals(sb)) {
				ph.setCard1(Card.ACE_OF_CLUBS);
				ph.setCard2(Card.ACE_OF_DIAMONDS);
			} else if (ph.getPlayer().equals(bb)) {
				ph.setCard1(Card.JACK_OF_CLUBS);
				ph.setCard2(Card.JACK_OF_DIAMONDS);
			} else {
				ph.setCard1(Card.FIVE_OF_CLUBS);
				ph.setCard2(Card.FIVE_OF_HEARTS);
			}
		}

		hand.getBoard().setFlop1(Card.TWO_OF_SPADES);
		hand.getBoard().setFlop2(Card.THREE_OF_SPADES);
		hand.getBoard().setFlop3(Card.FIVE_OF_DIAMONDS);
		hand.getBoard().setTurn(Card.QUEEN_OF_CLUBS);
		hand.getBoard().setRiver(Card.NINE_OF_HEARTS);

		handService.endHand(hand);

		assertEquals(PlayerStatus.WON_HAND, playerActionService.getPlayerStatus(btn));
		assertEquals(PlayerStatus.LOST_HAND, playerActionService.getPlayerStatus(bb));
		assertEquals(PlayerStatus.SIT_OUT, playerActionService.getPlayerStatus(co));
		assertEquals(PlayerStatus.LOST_HAND, playerActionService.getPlayerStatus(sb));

		// sb had less chips to start, so finishes in 4th place while bb gets
		// 3rd.
		assertEquals(4, sb.getFinishPosition());
		assertEquals(3, bb.getFinishPosition());
		assertEquals(2, game.getPlayersRemaining());
	}

	private Game gameSetup() {
		Game game = new Game();
		game.setName("Test Game");
		game.setGameType(GameType.TOURNAMENT);
		TournamentStructure gs = new TournamentStructure();
		gs.setBlindLength(10);
		gs.setBlindLevels(CommonTournamentFormats.TWO_HR_NINEPPL.getBlindLevels());
		gs.setStartingChips(1000);
		game.setGameStructure(gs);

		game = gameService.saveGame(game);

		flushAndClear();
		game = gameService.getGameById(game.getId(), true);

		Player p1 = new Player();
		p1.setChips(game.getGameStructure().getStartingChips());
		p1.setName("Player 1");
		p1 = gameService.addNewPlayerToGame(game, p1);

		Player p2 = new Player();
		p2.setChips(game.getGameStructure().getStartingChips());
		p2.setName("Player 2");
		p2 = gameService.addNewPlayerToGame(game, p2);

		Player p3 = new Player();
		p3.setChips(game.getGameStructure().getStartingChips());
		p3.setName("Player 3");
		p3 = gameService.addNewPlayerToGame(game, p3);

		Player p4 = new Player();
		p4.setChips(game.getGameStructure().getStartingChips());
		p4.setName("Player 4");
		p4 = gameService.addNewPlayerToGame(game, p4);

		flushAndClear();
		game = gameService.getGameById(game.getId(), true);
		return gameService.startGame(game);
	}
}