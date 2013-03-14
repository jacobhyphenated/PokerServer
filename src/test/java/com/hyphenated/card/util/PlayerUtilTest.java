package com.hyphenated.card.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		p1.setId(1);
		
		Player p2 = new Player();
		p2.setId(2);
		
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
		p1.setId(1);
		
		Player p2 = new Player();
		p2.setId(2);
		
		Player p3 = new Player();
		p3.setId(3);
		
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
		p1.setId(1);
		
		Player p2 = new Player();
		p2.setId(2);
		
		Player p3 = new Player();
		p3.setId(3);
		
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
		p1.setId(1);
		
		Player p2 = new Player();
		p2.setId(2);
		
		Player p3 = new Player();
		p3.setId(3);
		
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
}
