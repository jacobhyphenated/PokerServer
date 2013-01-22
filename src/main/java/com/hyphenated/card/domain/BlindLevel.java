package com.hyphenated.card.domain;

/**
 * 
 * Data structure for Blind Level.  This enum has all possible Blind Levels
 * that can occur for any game type.
 * 
 * @author jacobhyphenated
 */
public enum BlindLevel {

	BLIND_10_20(10,20);
	
	private int smallBlind;
	private int bigBlind;
	
	private BlindLevel(int smallBlind, int bigBlind){
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
	}
	
	public int getSmallBlind(){
		return smallBlind;
	}
	
	public int getBigBlind(){
		return bigBlind;
	}
}
