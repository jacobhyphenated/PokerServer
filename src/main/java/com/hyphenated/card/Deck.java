package com.hyphenated.card;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Standard deck of cards for poker. 52 Cards. 13 Clubs, Diamonds, Spades, and Hearts.
 * 
 * @author jacobhyphenated
 */
public class Deck {
	private List<Card> cards;
	
	/**
	 * Construct a standard shuffled playing card deck.
	 */
	public Deck(){
		this(true);
	}
	
	/**
	 * Create a standard deck of playing cards
	 * @param shuffle true for shuffled deck.  False for not shuffled.
	 */
	public Deck(boolean shuffle){
		initDeck();
		if(shuffle){
			shuffleDeck();
		}
	}
	
	public void initDeck(){
		cards = new LinkedList<Card>();
		cards.addAll(Arrays.asList(Card.values()));
	}
	
	public void shuffleDeck(){
		Collections.shuffle(cards);
	}
	
	/**
	 * Returns the top card from the deck.  Removes the card from the deck.
	 * @return {@link Card}
	 */
	public Card dealCard(){
		return cards.remove(0);
	}
}
