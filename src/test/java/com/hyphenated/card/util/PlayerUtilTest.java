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
package com.hyphenated.card.util;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.hyphenated.card.Card;
import com.hyphenated.card.domain.BoardEntity;
import com.hyphenated.card.domain.HandEntity;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.domain.PlayerHand;

import junit.framework.TestCase;

public class PlayerUtilTest extends TestCase {

	@Test
	public void testCompareTwoHands(){
		Player p1 = new Player();
		p1.setId("1");
		
		Player p2 = new Player();
		p2.setId("2");
		
		PlayerHand ph1 = new PlayerHand();
		ph1.setPlayer(p1);
		ph1.setId(11);
		ph1.setCard1(Card.ACE_OF_CLUBS);
		ph1.setCard2(Card.ACE_OF_HEARTS);
		
		PlayerHand ph2 = new PlayerHand();
		ph2.setPlayer(p2);
		ph2.setId(22);
		ph2.setCard1(Card.KING_OF_CLUBS);
		ph2.setCard2(Card.KING_OF_HEARTS);
		
		Set<PlayerHand> phs = new HashSet<PlayerHand>();
		phs.add(ph1);
		phs.add(ph2);
		
		HandEntity hand = new HandEntity();
		hand.setPlayers(phs);
		
		BoardEntity board = new BoardEntity();
		board.setFlop1(Card.TWO_OF_DIAMONDS);
		board.setFlop2(Card.EIGHT_OF_SPADES);
		board.setFlop3(Card.FOUR_OF_CLUBS);
		board.setTurn(Card.TEN_OF_HEARTS);
		board.setRiver(Card.SIX_OF_SPADES);
		hand.setBoard(board);
		
		List<Player> winners = PlayerUtil.getWinnersOfHand(hand, phs);
		assertEquals(1, winners.size());
		assertEquals(p1, winners.get(0));
	}
	
	@Test
	public void testCompareThreeHandsOneWinner(){
		Player p1 = new Player();
		p1.setId("1");
		
		Player p2 = new Player();
		p2.setId("2");
		
		Player p3 = new Player();
		p3.setId("3");
		
		PlayerHand ph1 = new PlayerHand();
		ph1.setPlayer(p1);
		ph1.setId(11);
		ph1.setCard1(Card.ACE_OF_CLUBS);
		ph1.setCard2(Card.ACE_OF_HEARTS);
		
		PlayerHand ph2 = new PlayerHand();
		ph2.setPlayer(p2);
		ph2.setId(22);
		ph2.setCard1(Card.KING_OF_CLUBS);
		ph2.setCard2(Card.KING_OF_HEARTS);
		
		PlayerHand ph3 = new PlayerHand();
		ph3.setPlayer(p3);
		ph3.setId(33);
		ph3.setCard1(Card.SIX_OF_HEARTS);
		ph3.setCard2(Card.QUEEN_OF_SPADES);
		
		Set<PlayerHand> phs = new HashSet<PlayerHand>();
		phs.add(ph1);
		phs.add(ph2);
		phs.add(ph3);
		
		HandEntity hand = new HandEntity();
		hand.setPlayers(phs);
		
		BoardEntity board = new BoardEntity();
		board.setFlop1(Card.TWO_OF_DIAMONDS);
		board.setFlop2(Card.EIGHT_OF_SPADES);
		board.setFlop3(Card.FOUR_OF_CLUBS);
		board.setTurn(Card.TEN_OF_HEARTS);
		board.setRiver(Card.SIX_OF_SPADES);
		hand.setBoard(board);
		
		List<Player> winners = PlayerUtil.getWinnersOfHand(hand, phs);
		assertEquals(1, winners.size());
		assertEquals(p1, winners.get(0));
	}
	
	@Test
	public void testCompareThreeHandsOneWinnerFromMiddle(){
		Player p1 = new Player();
		p1.setId("1");
		
		Player p2 = new Player();
		p2.setId("2");
		
		Player p3 = new Player();
		p3.setId("3");
		
		PlayerHand ph1 = new PlayerHand();
		ph1.setPlayer(p1);
		ph1.setId(11);
		ph1.setCard1(Card.ACE_OF_CLUBS);
		ph1.setCard2(Card.ACE_OF_HEARTS);
		
		PlayerHand ph2 = new PlayerHand();
		ph2.setPlayer(p2);
		ph2.setId(22);
		ph2.setCard1(Card.EIGHT_OF_CLUBS);
		ph2.setCard2(Card.EIGHT_OF_DIAMONDS);
		
		PlayerHand ph3 = new PlayerHand();
		ph3.setPlayer(p3);
		ph3.setId(33);
		ph3.setCard1(Card.SIX_OF_HEARTS);
		ph3.setCard2(Card.QUEEN_OF_SPADES);
		
		Set<PlayerHand> phs = new HashSet<PlayerHand>();
		phs.add(ph1);
		phs.add(ph2);
		phs.add(ph3);
		
		HandEntity hand = new HandEntity();
		hand.setPlayers(phs);
		
		BoardEntity board = new BoardEntity();
		board.setFlop1(Card.TWO_OF_DIAMONDS);
		board.setFlop2(Card.EIGHT_OF_SPADES);
		board.setFlop3(Card.FOUR_OF_CLUBS);
		board.setTurn(Card.TEN_OF_HEARTS);
		board.setRiver(Card.SIX_OF_SPADES);
		hand.setBoard(board);
		
		List<Player> winners = PlayerUtil.getWinnersOfHand(hand, phs);
		assertEquals(1, winners.size());
		assertEquals(p2, winners.get(0));
	}
	
	@Test
	public void testCompareThreeHandsTwoWinners(){
		Player p1 = new Player();
		p1.setId("1");
		
		Player p2 = new Player();
		p2.setId("2");
		
		Player p3 = new Player();
		p3.setId("3");
		
		PlayerHand ph1 = new PlayerHand();
		ph1.setPlayer(p1);
		ph1.setId(11);
		ph1.setCard1(Card.TEN_OF_CLUBS);
		ph1.setCard2(Card.EIGHT_OF_CLUBS);
		
		PlayerHand ph2 = new PlayerHand();
		ph2.setPlayer(p2);
		ph2.setId(22);
		ph2.setCard1(Card.TEN_OF_DIAMONDS);
		ph2.setCard2(Card.NINE_OF_SPADES);
		
		PlayerHand ph3 = new PlayerHand();
		ph3.setPlayer(p3);
		ph3.setId(33);
		ph3.setCard1(Card.SIX_OF_HEARTS);
		ph3.setCard2(Card.QUEEN_OF_SPADES);
		
		Set<PlayerHand> phs = new HashSet<PlayerHand>();
		phs.add(ph1);
		phs.add(ph2);
		phs.add(ph3);
		
		HandEntity hand = new HandEntity();
		hand.setPlayers(phs);
		
		BoardEntity board = new BoardEntity();
		board.setFlop1(Card.TWO_OF_DIAMONDS);
		board.setFlop2(Card.TWO_OF_SPADES);
		board.setFlop3(Card.ACE_OF_SPADES);
		board.setTurn(Card.TEN_OF_HEARTS);
		board.setRiver(Card.KING_OF_SPADES);
		hand.setBoard(board);
		
		List<Player> winners = PlayerUtil.getWinnersOfHand(hand, phs);
		assertEquals(2, winners.size());
		assertTrue(winners.contains(p1));
		assertTrue(winners.contains(p2));
		assertTrue(!winners.contains(p3));
	}
	
	@Test
	public void testSingleWinnerAtShowdown(){
		Player p1 = new Player();
		p1.setId("1");
		p1.setChips(1000);
		
		Player p2 = new Player();
		p2.setId("2");
		p2.setChips(1000);
		
		PlayerHand ph1 = new PlayerHand();
		ph1.setPlayer(p1);
		ph1.setId(11);
		ph1.setCard1(Card.ACE_OF_CLUBS);
		ph1.setCard2(Card.ACE_OF_HEARTS);
		ph1.setBetAmount(300);
		
		PlayerHand ph2 = new PlayerHand();
		ph2.setPlayer(p2);
		ph2.setId(22);
		ph2.setCard1(Card.KING_OF_CLUBS);
		ph2.setCard2(Card.KING_OF_HEARTS);
		ph2.setBetAmount(300);
		
		Set<PlayerHand> phs = new HashSet<PlayerHand>();
		phs.add(ph1);
		phs.add(ph2);
		
		HandEntity hand = new HandEntity();
		hand.setPlayers(phs);
		
		BoardEntity board = new BoardEntity();
		board.setFlop1(Card.TWO_OF_DIAMONDS);
		board.setFlop2(Card.EIGHT_OF_SPADES);
		board.setFlop3(Card.FOUR_OF_CLUBS);
		board.setTurn(Card.TEN_OF_HEARTS);
		board.setRiver(Card.SIX_OF_SPADES);
		hand.setBoard(board);
		hand.setPot(600);
		
		Map<Player, Integer> winners = PlayerUtil.getAmountWonInHandForAllPlayers(hand);
		assertEquals(1, winners.size());
		assertEquals(new Integer(600), winners.get(p1));
	}
	
	@Test
	public void testSplitPotAtShowdown(){
		Player p1 = new Player();
		p1.setId("1");
		p1.setChips(1000);
		
		Player p2 = new Player();
		p2.setId("2");
		p2.setChips(1000);
		
		Player p3 = new Player();
		p3.setId("3");
		p3.setChips(1000);
		
		PlayerHand ph1 = new PlayerHand();
		ph1.setPlayer(p1);
		ph1.setId(11);
		ph1.setCard1(Card.TEN_OF_CLUBS);
		ph1.setCard2(Card.EIGHT_OF_CLUBS);
		ph1.setBetAmount(400);
		
		PlayerHand ph2 = new PlayerHand();
		ph2.setPlayer(p2);
		ph2.setId(22);
		ph2.setCard1(Card.TEN_OF_DIAMONDS);
		ph2.setCard2(Card.NINE_OF_SPADES);
		ph2.setBetAmount(400);
		
		PlayerHand ph3 = new PlayerHand();
		ph3.setPlayer(p3);
		ph3.setId(33);
		ph3.setCard1(Card.SIX_OF_HEARTS);
		ph3.setCard2(Card.QUEEN_OF_SPADES);
		ph3.setBetAmount(400);
		
		Set<PlayerHand> phs = new HashSet<PlayerHand>();
		phs.add(ph1);
		phs.add(ph2);
		phs.add(ph3);
		
		HandEntity hand = new HandEntity();
		hand.setPlayers(phs);
		
		BoardEntity board = new BoardEntity();
		board.setFlop1(Card.TWO_OF_DIAMONDS);
		board.setFlop2(Card.TWO_OF_SPADES);
		board.setFlop3(Card.ACE_OF_SPADES);
		board.setTurn(Card.TEN_OF_HEARTS);
		board.setRiver(Card.KING_OF_SPADES);
		hand.setBoard(board);
		hand.setPot(1200);
		
		Map<Player, Integer> winners = PlayerUtil.getAmountWonInHandForAllPlayers(hand);
		assertEquals(2, winners.size());
		assertEquals(new Integer(600), winners.get(p1));
		assertEquals(new Integer(600), winners.get(p2));
	}
	
	@Test
	public void testAllInCallForTwoPlayers(){
		Player p1 = new Player();
		p1.setId("1");
		p1.setChips(0);
		
		Player p2 = new Player();
		p2.setId("2");
		p2.setChips(1000);
		
		PlayerHand ph1 = new PlayerHand();
		ph1.setPlayer(p1);
		ph1.setId(11);
		ph1.setCard1(Card.ACE_OF_CLUBS);
		ph1.setCard2(Card.ACE_OF_HEARTS);
		ph1.setBetAmount(300);
		
		PlayerHand ph2 = new PlayerHand();
		ph2.setPlayer(p2);
		ph2.setId(22);
		ph2.setCard1(Card.KING_OF_CLUBS);
		ph2.setCard2(Card.KING_OF_HEARTS);
		ph2.setBetAmount(300);
		
		Set<PlayerHand> phs = new HashSet<PlayerHand>();
		phs.add(ph1);
		phs.add(ph2);
		
		HandEntity hand = new HandEntity();
		hand.setPlayers(phs);
		
		BoardEntity board = new BoardEntity();
		board.setFlop1(Card.TWO_OF_DIAMONDS);
		board.setFlop2(Card.EIGHT_OF_SPADES);
		board.setFlop3(Card.FOUR_OF_CLUBS);
		board.setTurn(Card.TEN_OF_HEARTS);
		board.setRiver(Card.SIX_OF_SPADES);
		hand.setBoard(board);
		hand.setPot(600);
		
		Map<Player, Integer> winners = PlayerUtil.getAmountWonInHandForAllPlayers(hand);
		assertEquals(1, winners.size());
		assertEquals(new Integer(600), winners.get(p1));
	}
	
	@Test
	@Ignore
	public void testSidePotOneWinner(){
		Player p1 = new Player();
		p1.setId("1");
		p1.setChips(0);
		
		Player p2 = new Player();
		p2.setId("2");
		p2.setChips(1000);
		
		Player p3 = new Player();
		p3.setId("3");
		p3.setChips(1000);
		
		PlayerHand ph1 = new PlayerHand();
		ph1.setPlayer(p1);
		ph1.setId(11);
		ph1.setCard1(Card.JACK_OF_CLUBS);
		ph1.setCard2(Card.EIGHT_OF_CLUBS);
		ph1.setBetAmount(400);
		
		PlayerHand ph2 = new PlayerHand();
		ph2.setPlayer(p2);
		ph2.setId(22);
		ph2.setCard1(Card.TEN_OF_DIAMONDS);
		ph2.setCard2(Card.NINE_OF_SPADES);
		ph2.setBetAmount(600);
		
		PlayerHand ph3 = new PlayerHand();
		ph3.setPlayer(p3);
		ph3.setId(33);
		ph3.setCard1(Card.SIX_OF_HEARTS);
		ph3.setCard2(Card.QUEEN_OF_SPADES);
		ph3.setBetAmount(600);
		
		Set<PlayerHand> phs = new HashSet<PlayerHand>();
		phs.add(ph1);
		phs.add(ph2);
		phs.add(ph3);
		
		HandEntity hand = new HandEntity();
		hand.setPlayers(phs);
		
		BoardEntity board = new BoardEntity();
		board.setFlop1(Card.TWO_OF_DIAMONDS);
		board.setFlop2(Card.TWO_OF_SPADES);
		board.setFlop3(Card.ACE_OF_SPADES);
		board.setTurn(Card.TEN_OF_HEARTS);
		board.setRiver(Card.KING_OF_SPADES);
		hand.setBoard(board);
		hand.setPot(1600);
		
		Map<Player, Integer> winners = PlayerUtil.getAmountWonInHandForAllPlayers(hand);
		assertEquals(1, winners.size());
		assertEquals(new Integer(1600), winners.get(p2));
	}
	
	@Test
	public void testSidePotTwoWinners(){
		Player p1 = new Player();
		p1.setId("1");
		p1.setChips(0);
		
		Player p2 = new Player();
		p2.setId("2");
		p2.setChips(1000);
		
		Player p3 = new Player();
		p3.setId("3");
		p3.setChips(1000);
		
		PlayerHand ph1 = new PlayerHand();
		ph1.setPlayer(p1);
		ph1.setId(11);
		ph1.setCard1(Card.ACE_OF_CLUBS);
		ph1.setCard2(Card.ACE_OF_HEARTS);
		ph1.setBetAmount(400);
		
		PlayerHand ph2 = new PlayerHand();
		ph2.setPlayer(p2);
		ph2.setId(22);
		ph2.setCard1(Card.TEN_OF_DIAMONDS);
		ph2.setCard2(Card.NINE_OF_SPADES);
		ph2.setBetAmount(600);
		
		PlayerHand ph3 = new PlayerHand();
		ph3.setPlayer(p3);
		ph3.setId(33);
		ph3.setCard1(Card.SIX_OF_HEARTS);
		ph3.setCard2(Card.QUEEN_OF_SPADES);
		ph3.setBetAmount(600);
		
		Set<PlayerHand> phs = new HashSet<PlayerHand>();
		phs.add(ph1);
		phs.add(ph2);
		phs.add(ph3);
		
		HandEntity hand = new HandEntity();
		hand.setPlayers(phs);
		
		BoardEntity board = new BoardEntity();
		board.setFlop1(Card.TWO_OF_DIAMONDS);
		board.setFlop2(Card.TWO_OF_SPADES);
		board.setFlop3(Card.ACE_OF_SPADES);
		board.setTurn(Card.TEN_OF_HEARTS);
		board.setRiver(Card.KING_OF_SPADES);
		hand.setBoard(board);
		hand.setPot(1600);
		
		Map<Player, Integer> winners = PlayerUtil.getAmountWonInHandForAllPlayers(hand);
		assertEquals(2, winners.size());
		assertEquals(new Integer(1200), winners.get(p1));
		assertEquals(new Integer(400), winners.get(p2));
	}
	
	@Test
	public void testSidePotThreeWinners(){
		Player p1 = new Player();
		p1.setId("1");
		p1.setChips(0);
		
		Player p2 = new Player();
		p2.setId("2");
		p2.setChips(0);
		
		Player p3 = new Player();
		p3.setId("3");
		p3.setChips(1000);
		
		Player p4 = new Player();
		p4.setId("4");
		p4.setChips(1000);
		
		PlayerHand ph1 = new PlayerHand();
		ph1.setPlayer(p1);
		ph1.setId(11);
		ph1.setCard1(Card.ACE_OF_CLUBS);
		ph1.setCard2(Card.ACE_OF_HEARTS);
		ph1.setBetAmount(400);
		
		PlayerHand ph2 = new PlayerHand();
		ph2.setPlayer(p2);
		ph2.setId(22);
		ph2.setCard1(Card.TEN_OF_DIAMONDS);
		ph2.setCard2(Card.NINE_OF_SPADES);
		ph2.setBetAmount(600);
		
		PlayerHand ph3 = new PlayerHand();
		ph3.setPlayer(p3);
		ph3.setId(33);
		ph3.setCard1(Card.SIX_OF_HEARTS);
		ph3.setCard2(Card.QUEEN_OF_SPADES);
		ph3.setBetAmount(1000);
		
		PlayerHand ph4 = new PlayerHand();
		ph4.setPlayer(p4);
		ph3.setId(44);
		ph4.setCard1(Card.EIGHT_OF_CLUBS);
		ph4.setCard2(Card.EIGHT_OF_HEARTS);
		ph4.setBetAmount(1000);
		
		Set<PlayerHand> phs = new HashSet<PlayerHand>();
		phs.add(ph1);
		phs.add(ph2);
		phs.add(ph3);
		phs.add(ph4);
		
		HandEntity hand = new HandEntity();
		hand.setPlayers(phs);
		
		BoardEntity board = new BoardEntity();
		board.setFlop1(Card.TWO_OF_DIAMONDS);
		board.setFlop2(Card.TWO_OF_SPADES);
		board.setFlop3(Card.ACE_OF_SPADES);
		board.setTurn(Card.TEN_OF_HEARTS);
		board.setRiver(Card.KING_OF_SPADES);
		hand.setBoard(board);
		hand.setPot(3000);
		
		Map<Player, Integer> winners = PlayerUtil.getAmountWonInHandForAllPlayers(hand);
		assertEquals(3, winners.size());
		assertEquals(new Integer(1600), winners.get(p1));
		assertEquals(new Integer(600), winners.get(p2));
		assertEquals(new Integer(800), winners.get(p4));
	}
	
	@Test
	public void testSidePotFourWithTwoWinners(){
		Player p1 = new Player();
		p1.setId("1");
		p1.setChips(0);
		
		Player p2 = new Player();
		p2.setId("2");
		p2.setChips(0);
		
		Player p3 = new Player();
		p3.setId("3");
		p3.setChips(1000);
		
		Player p4 = new Player();
		p4.setId("4");
		p4.setChips(1000);
		
		PlayerHand ph1 = new PlayerHand();
		ph1.setPlayer(p1);
		ph1.setId(11);
		ph1.setCard1(Card.ACE_OF_CLUBS);
		ph1.setCard2(Card.ACE_OF_HEARTS);
		ph1.setBetAmount(400);
		
		PlayerHand ph2 = new PlayerHand();
		ph2.setPlayer(p2);
		ph2.setId(22);
		ph2.setCard1(Card.QUEEN_OF_DIAMONDS);
		ph2.setCard2(Card.NINE_OF_SPADES);
		ph2.setBetAmount(601);
		
		PlayerHand ph3 = new PlayerHand();
		ph3.setPlayer(p3);
		ph3.setId(33);
		ph3.setCard1(Card.SIX_OF_HEARTS);
		ph3.setCard2(Card.QUEEN_OF_SPADES);
		ph3.setBetAmount(1000);
		
		PlayerHand ph4 = new PlayerHand();
		ph4.setPlayer(p4);
		ph3.setId(44);
		ph4.setCard1(Card.EIGHT_OF_CLUBS);
		ph4.setCard2(Card.EIGHT_OF_HEARTS);
		ph4.setBetAmount(1000);
		
		Set<PlayerHand> phs = new HashSet<PlayerHand>();
		phs.add(ph1);
		phs.add(ph2);
		phs.add(ph3);
		phs.add(ph4);
		
		HandEntity hand = new HandEntity();
		hand.setPlayers(phs);
		
		BoardEntity board = new BoardEntity();
		board.setFlop1(Card.TWO_OF_DIAMONDS);
		board.setFlop2(Card.TWO_OF_SPADES);
		board.setFlop3(Card.ACE_OF_SPADES);
		board.setTurn(Card.TEN_OF_HEARTS);
		board.setRiver(Card.KING_OF_SPADES);
		hand.setBoard(board);
		hand.setPot(3001);
		
		Map<Player, Integer> winners = PlayerUtil.getAmountWonInHandForAllPlayers(hand);
		assertEquals(2, winners.size());
		assertEquals(new Integer(1600), winners.get(p1));
		assertEquals(new Integer(1401), winners.get(p4));
	}
}
