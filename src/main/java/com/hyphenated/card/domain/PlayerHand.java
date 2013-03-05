package com.hyphenated.card.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hyphenated.card.Card;
import com.hyphenated.card.holder.Hand;

@Entity
@Table(name="player_hand")
public class PlayerHand implements Comparable<PlayerHand>{

	private long id;
	private Player player;
	private HandEntity handEntity;
	private Card card1;
	private Card card2;
	private int betAmount;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name="player_hand_id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="player_id")
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	@ManyToOne
	@JoinColumn(name="hand_id")
	public HandEntity getHandEntity() {
		return handEntity;
	}
	public void setHandEntity(HandEntity hand) {
		this.handEntity = hand;
	}
	
	@Column(name="card1")
	@Enumerated(EnumType.STRING)
	protected Card getCard1() {
		return card1;
	}
	public void setCard1(Card card1) {
		this.card1 = card1;
	}
	
	@Column(name="card2")
	@Enumerated(EnumType.STRING)
	protected Card getCard2() {
		return card2;
	}
	public void setCard2(Card card2) {
		this.card2 = card2;
	}
	
	@Column(name="bet_amount")
	public int getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(int betAmount) {
		this.betAmount = betAmount;
	}
	
	/**
	 * Get the hole cards for the player
	 * @return {@link Hand} container for the two hole cards. Null if both hole cards not specified
	 */
	@Transient
	public Hand getHand(){
		if(card1 == null || card2 == null){
			return null;
		}
		return new Hand(card1, card2);
	}
	
	@Transient
	@Override
	public int compareTo(PlayerHand o) {
		return this.getPlayer().compareTo(o.getPlayer());
	}
}
