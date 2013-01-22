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
	
	/**
	 * Initialize a deck using a pre existing list of cards
	 * @param cards List of cards, assumed correctly shuffled
	 */
	public Deck(List<Card> cards){
		//this.cards = new LinkedList<Card>();
		//this.cards.addAll(cards);
		this.cards = cards;
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
	
	/**
	 * Get the cards in the deck in the form of a list
	 * @return
	 */
	public List<Card> exportDeck(){
		return cards;
	}
}
