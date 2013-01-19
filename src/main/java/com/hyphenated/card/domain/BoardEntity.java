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
