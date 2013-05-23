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
package com.hyphenated.card.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hyphenated.card.Card;

@Entity
@Table(name="board")
public class BoardEntity {
	
	private long id;
	private Card flop1;
	private Card flop2;
	private Card flop3;
	private Card turn;
	private Card river;
	
	/**
	 * Get all of the cards on the board in list form.
	 * The list is ordered with flop first, then turn, then river.
	 * @return
	 */
	@Transient
	public List<Card> getBoardCards(){
		List<Card> cards = new ArrayList<Card>();
		if(flop1 != null){
			cards.add(flop1);
			cards.add(flop2);
			cards.add(flop3);
		}
		if(turn != null){
			cards.add(turn);
		}
		if(river != null){
			cards.add(river);
		}
		return cards;
	}
	
	@Column(name="board_id")
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="flop1")
	@Enumerated(EnumType.STRING)
	public Card getFlop1() {
		return flop1;
	}
	public void setFlop1(Card flop1) {
		this.flop1 = flop1;
	}
	
	@Column(name="flop2")
	@Enumerated(EnumType.STRING)
	public Card getFlop2() {
		return flop2;
	}
	public void setFlop2(Card flop2) {
		this.flop2 = flop2;
	}
	
	@Column(name="flop3")
	@Enumerated(EnumType.STRING)
	public Card getFlop3() {
		return flop3;
	}
	public void setFlop3(Card flop3) {
		this.flop3 = flop3;
	}
	
	@Column(name="turn")
	@Enumerated(EnumType.STRING)
	public Card getTurn() {
		return turn;
	}
	public void setTurn(Card turn) {
		this.turn = turn;
	}
	
	@Column(name="river")
	@Enumerated(EnumType.STRING)
	public Card getRiver() {
		return river;
	}
	public void setRiver(Card river) {
		this.river = river;
	}
}
