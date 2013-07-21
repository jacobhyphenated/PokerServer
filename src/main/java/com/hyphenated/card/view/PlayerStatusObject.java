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
package com.hyphenated.card.view;

import java.io.Serializable;

import com.hyphenated.card.Card;
import com.hyphenated.card.domain.PlayerStatus;

/**
 * Data Transfer Object Used specifically to store the information relevant to 
 * a Player Status API Request.
 * 
 * @author jacobhyphenated
 */
public class PlayerStatusObject implements Serializable {

	private static final long serialVersionUID = -3374032023771451021L;
	
	private PlayerStatus status;
	private int chips;
	private int smallBlind;
	private int bigBlind;
	private Card card1;
	private Card card2;
	private int amountBetRound;
	private int amountToCall;
	
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof PlayerStatusObject)){
			return false;
		}
		PlayerStatusObject p = (PlayerStatusObject) o;
		return this.status == p.getStatus() && this.chips == p.getChips()
				&&this.card1.equals(p.getCard1()) && this.card2.equals(p.getCard2());
	}
	
	@Override
	public int hashCode(){
		return status.hashCode() + this.chips +this.card1.hashCode() + this.card2.hashCode();
	}
	
	public PlayerStatus getStatus() {
		return status;
	}
	public void setStatus(PlayerStatus status) {
		this.status = status;
	}
	public int getChips() {
		return chips;
	}
	public void setChips(int chips) {
		this.chips = chips;
	}
	public int getSmallBlind() {
		return smallBlind;
	}
	public void setSmallBlind(int smallBlind) {
		this.smallBlind = smallBlind;
	}
	public int getBigBlind() {
		return bigBlind;
	}
	public void setBigBlind(int bigBlind) {
		this.bigBlind = bigBlind;
	}
	public Card getCard1() {
		return card1;
	}
	public void setCard1(Card card1) {
		this.card1 = card1;
	}
	public Card getCard2() {
		return card2;
	}
	public void setCard2(Card card2) {
		this.card2 = card2;
	}
	public int getAmountBetRound() {
		return amountBetRound;
	}
	public void setAmountBetRound(int amountBetRound) {
		this.amountBetRound = amountBetRound;
	}
	public int getAmountToCall() {
		return amountToCall;
	}
	public void setAmountToCall(int amountToCall) {
		this.amountToCall = amountToCall;
	}
}
