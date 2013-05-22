package com.hyphenated.card.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hyphenated.card.Card;

@Entity
@Table(name="hand")
public class HandEntity {
	
	private long id;
	private Game game;
	private BoardEntity board;
	private Set<PlayerHand> players;
	private Player currentToAct;
	private BlindLevel blindLevel;
	private List<Card> cardList;
	private int pot;
	private int totalBetAmount;
	private int lastBetAmount;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name="hand_id")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="game_id")
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	@OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="board_id")
	public BoardEntity getBoard() {
		return board;
	}
	public void setBoard(BoardEntity board) {
		this.board = board;
	}
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="handEntity",cascade={CascadeType.ALL},orphanRemoval=true)
	public Set<PlayerHand> getPlayers() {
		return players;
	}
	public void setPlayers(Set<PlayerHand> players) {
		this.players = players;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="player_to_act_id")
	public Player getCurrentToAct() {
		return currentToAct;
	}
	public void setCurrentToAct(Player currentToAct) {
		this.currentToAct = currentToAct;
	}
	
	@Column(name="blind_level")
	@Enumerated(EnumType.STRING)
	public BlindLevel getBlindLevel() {
		return blindLevel;
	}
	public void setBlindLevel(BlindLevel blindLevel) {
		this.blindLevel = blindLevel;
	}
	
	@ElementCollection(targetClass=Card.class)
	@JoinTable(name="hand_deck", joinColumns=@JoinColumn(name="hand_id"))
	@Column(name="card",nullable=false)
	@Enumerated(EnumType.STRING)
	public List<Card> getCards(){
		return cardList;
	}
	public void setCards(List<Card> cards){
		cardList = cards;
	}
	
	@Column(name="pot")
	public int getPot() {
		return pot;
	}
	public void setPot(int pot) {
		this.pot = pot;
	}
	
	@Column(name="total_bet_amount")
	public int getTotalBetAmount() {
		return totalBetAmount;
	}
	public void setTotalBetAmount(int betAmount) {
		this.totalBetAmount = betAmount;
	}
	
	
	/**
	 * In No Limit poker, the minimum bet size is twice the previous bet.  Use this field to determine
	 * what that amount would be.
	 * @return The last bet/raise amount
	 */
	@Column(name="bet_amount")
	public int getLastBetAmount() {
		return lastBetAmount;
	}
	public void setLastBetAmount(int lastBetAmount) {
		this.lastBetAmount = lastBetAmount;
	}
	
	@Transient
	@Override
	public boolean equals(Object o){
		if (o == null || !(o instanceof HandEntity)){
			return false;
		}
		return ((HandEntity) o).getId() == this.getId();
	}
	
	@Transient
	@Override
	public int hashCode(){
		return (int) this.getId();
	}
}
