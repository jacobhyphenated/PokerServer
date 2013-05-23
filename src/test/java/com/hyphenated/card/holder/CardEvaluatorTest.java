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
package com.hyphenated.card.holder;

import junit.framework.TestCase;

import org.junit.Test;

import com.hyphenated.card.Card;
import com.hyphenated.card.HandType;
import com.hyphenated.card.eval.HandRank;
import com.hyphenated.card.eval.HandRankEvaluator;
import com.hyphenated.card.eval.TwoPlusTwoHandEvaluator;

/**
 * JUnit tests for the Seven Card Poker Hand Evaluation.
 * The Evaluation algorithm ({@link TwoPlusTwoHandEvaluator}) needs to always determine
 * the winner between two (or more) hands.  It should also correctly determine
 * the type of the hand (pair, flush, etc.)
 * 
 * @author jacobhyphenated
 */
public class CardEvaluatorTest extends TestCase {

	@Test
	//test pair over pair
	public void testPair1(){
		Hand h1 = new Hand(Card.ACE_OF_CLUBS, Card.ACE_OF_DIAMONDS);
		Hand h2 = new Hand(Card.KING_OF_CLUBS, Card.KING_OF_DIAMONDS);
		Board b = new Board(Card.TWO_OF_DIAMONDS, Card.EIGHT_OF_SPADES, Card.FOUR_OF_CLUBS, 
				Card.TEN_OF_HEARTS, Card.SIX_OF_SPADES);
		int comp = compare(h1,h2,b);
		assertTrue(comp > 0);
	}
	
	@Test
	//test pair over pair in reverse
	public void testPair2(){
		Hand h2 = new Hand(Card.ACE_OF_CLUBS, Card.ACE_OF_DIAMONDS);
		Hand h1 = new Hand(Card.KING_OF_CLUBS, Card.KING_OF_DIAMONDS);
		Board b = new Board(Card.TWO_OF_DIAMONDS, Card.EIGHT_OF_SPADES, Card.FOUR_OF_CLUBS, 
				Card.TEN_OF_HEARTS, Card.SIX_OF_SPADES);
		int comp = compare(h1,h2,b);
		assertTrue(comp < 0);
	}
	
	@Test
	//Test pair over not pair
	public void testPairvsNotPair(){
		Hand h1 = new Hand(Card.NINE_OF_DIAMONDS, Card.SEVEN_OF_CLUBS);
		Hand h2 = new Hand(Card.KING_OF_CLUBS, Card.NINE_OF_SPADES);
		Board b = new Board(Card.TWO_OF_DIAMONDS, Card.EIGHT_OF_SPADES, Card.FOUR_OF_CLUBS, 
				Card.TEN_OF_HEARTS, Card.NINE_OF_HEARTS);
		int comp = compare(h1,h2,b);
		assertTrue(comp < 0);
	}
	
	@Test
	//test two pair vs counterfeit two pair
	public void testTwoPair(){
		Hand h1 = new Hand(Card.NINE_OF_CLUBS, Card.THREE_OF_SPADES);
		Hand h2 = new Hand(Card.EIGHT_OF_CLUBS, Card.KING_OF_CLUBS);
		Board b = new Board(Card.NINE_OF_HEARTS, Card.THREE_OF_HEARTS, Card.TEN_OF_CLUBS, 
				Card.TEN_OF_HEARTS, Card.KING_OF_HEARTS);
		int comp = compare(h1,h2,b);
		assertTrue(comp < 0);
	}
	
	@Test
	//test flush over 3 of a kind
	public void testFlushThreeOfKind(){
		Hand h1 = new Hand(Card.TEN_OF_CLUBS, Card.EIGHT_OF_CLUBS);
		Hand h2 = new Hand(Card.KING_OF_SPADES, Card.KING_OF_DIAMONDS);
		Board b = new Board(Card.TWO_OF_CLUBS, Card.THREE_OF_CLUBS, Card.FOUR_OF_CLUBS, 
				Card.KING_OF_HEARTS, Card.SEVEN_OF_HEARTS);
		int comp = compare(h1,h2,b);
		assertTrue(comp > 0);
	}
	
	@Test
	//test flush over straight
	public void testFlushStraight(){
		Hand h1 = new Hand(Card.TEN_OF_CLUBS, Card.EIGHT_OF_CLUBS);
		Hand h2 = new Hand(Card.FIVE_OF_SPADES, Card.SIX_OF_DIAMONDS);
		Board b = new Board(Card.TWO_OF_CLUBS, Card.THREE_OF_CLUBS, Card.FOUR_OF_CLUBS, 
				Card.KING_OF_HEARTS, Card.ACE_OF_HEARTS);
		int comp = compare(h1,h2,b);
		assertTrue(comp > 0);
	}
	
	@Test
	//test straight over pair
	public void testStraight(){
		Hand h1 = new Hand(Card.KING_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		Hand h2 = new Hand(Card.FIVE_OF_SPADES, Card.SIX_OF_DIAMONDS);
		Board b = new Board(Card.TWO_OF_CLUBS, Card.THREE_OF_CLUBS, Card.FOUR_OF_CLUBS, 
				Card.KING_OF_HEARTS, Card.ACE_OF_HEARTS);
		int comp = compare(h1,h2,b);
		assertTrue(comp < 0);
	}
	
	@Test
	//test split
	public void testSplit(){
		Hand h1 = new Hand(Card.TEN_OF_CLUBS, Card.EIGHT_OF_CLUBS);
		Hand h2 = new Hand(Card.TEN_OF_DIAMONDS, Card.NINE_OF_HEARTS);
		Board b = new Board(Card.TWO_OF_CLUBS, Card.TWO_OF_DIAMONDS, Card.TEN_OF_SPADES, 
				Card.KING_OF_HEARTS, Card.ACE_OF_HEARTS);
		int comp = compare(h1,h2,b);
		assertTrue(comp == 0);
	}
	
	@Test
	//test 3 pair vs. 2 pair
	public void testThreePair(){
		Hand h1 = new Hand(Card.TEN_OF_DIAMONDS, Card.TEN_OF_CLUBS);
		Hand h2 = new Hand(Card.ACE_OF_DIAMONDS, Card.QUEEN_OF_SPADES);
		Board b = new Board(Card.JACK_OF_CLUBS, Card.THREE_OF_HEARTS, Card.EIGHT_OF_DIAMONDS,
				Card.JACK_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		int comp = compare(h1,h2,b);
		assertTrue("Comp is " + comp,comp > 0);
	}
	
	@Test
	public void testOnePairType(){
		Hand h1 = new Hand(Card.TEN_OF_DIAMONDS, Card.NINE_OF_CLUBS);
		Board b = new Board(Card.JACK_OF_CLUBS, Card.THREE_OF_HEARTS, Card.NINE_OF_DIAMONDS,
				Card.TWO_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		HandRankEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.PAIR, rank.getHandType());
	}
	
	@Test 
	public void testTwoPairType(){
		Hand h1 = new Hand(Card.TEN_OF_DIAMONDS, Card.NINE_OF_CLUBS);
		Board b = new Board(Card.JACK_OF_CLUBS, Card.THREE_OF_HEARTS, Card.NINE_OF_DIAMONDS,
				Card.JACK_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		HandRankEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.TWO_PAIR, rank.getHandType());
	}
	
	@Test
	public void testThreeOfAKindType(){
		Hand h1 = new Hand(Card.JACK_OF_DIAMONDS, Card.FIVE_OF_CLUBS);
		Board b = new Board(Card.JACK_OF_CLUBS, Card.THREE_OF_HEARTS, Card.NINE_OF_DIAMONDS,
				Card.JACK_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		HandRankEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.THREE_OF_A_KIND, rank.getHandType());
	}
	
	@Test
	public void testFullHouseType(){
		Hand h1 = new Hand(Card.JACK_OF_DIAMONDS, Card.NINE_OF_CLUBS);
		Board b = new Board(Card.JACK_OF_CLUBS, Card.THREE_OF_HEARTS, Card.NINE_OF_DIAMONDS,
				Card.JACK_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		HandRankEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.FULL_HOUSE, rank.getHandType());
	}
	
	@Test
	public void testStraightType(){
		Hand h1 = new Hand(Card.JACK_OF_DIAMONDS, Card.NINE_OF_CLUBS);
		Board b = new Board(Card.TEN_OF_CLUBS, Card.THREE_OF_HEARTS, Card.NINE_OF_DIAMONDS,
				Card.QUEEN_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		HandRankEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.STRAIGHT, rank.getHandType());
	}
	
	@Test
	public void testFlushType(){
		Hand h1 = new Hand(Card.JACK_OF_DIAMONDS, Card.NINE_OF_DIAMONDS);
		Board b = new Board(Card.JACK_OF_CLUBS, Card.THREE_OF_HEARTS, Card.TWO_OF_DIAMONDS,
				Card.FIVE_OF_DIAMONDS, Card.EIGHT_OF_DIAMONDS);
		HandRankEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.FLUSH, rank.getHandType());
	}
	
	@Test
	public void testFourOfAKindType(){
		Hand h1 = new Hand(Card.JACK_OF_DIAMONDS, Card.JACK_OF_CLUBS);
		Board b = new Board(Card.JACK_OF_HEARTS, Card.THREE_OF_HEARTS, Card.NINE_OF_DIAMONDS,
				Card.JACK_OF_SPADES, Card.EIGHT_OF_DIAMONDS);
		HandRankEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.FOUR_OF_A_KIND, rank.getHandType());
	}
	
	@Test
	public void testStraightFlushType(){
		Hand h1 = new Hand(Card.JACK_OF_DIAMONDS, Card.NINE_OF_CLUBS);
		Board b = new Board(Card.TEN_OF_DIAMONDS, Card.THREE_OF_HEARTS, Card.NINE_OF_DIAMONDS,
				Card.QUEEN_OF_DIAMONDS, Card.EIGHT_OF_DIAMONDS);
		HandRankEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank = evaluator.evaluate(b, h1);
		assertEquals(HandType.STRAIGHT_FLUSH, rank.getHandType());
	}
	
	//Compareto equivalent to h1.compareTo(h2) using Two Plus Two Algorithm
	private int compare(Hand h1, Hand h2, Board b){
		HandRankEvaluator evaluator =  TwoPlusTwoHandEvaluator.getInstance();
		HandRank rank1 = evaluator.evaluate(b, h1);
		HandRank rank2 = evaluator.evaluate(b, h2);
		return rank1.compareTo(rank2);
	}
}
