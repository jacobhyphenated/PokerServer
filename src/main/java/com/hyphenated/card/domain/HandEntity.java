package com.hyphenated.card.domain;

import java.util.List;
import java.util.Set;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.hyphenated.card.Card;

@Entity
@Table(name="hand")
public class HandEntity {
	
	private long id;
	private Game game;
	private BoardEntity board;
	private Set<Player> players;
	private Player currentToAct;
	private BlindLevel blindLevel;
	private List<Card> cardList;
	
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
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="board_id")
	public BoardEntity getBoard() {
		return board;
	}
	public void setBoard(BoardEntity board) {
		this.board = board;
	}
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="player_hand",
		joinColumns=@JoinColumn(name="hand_id"),
		inverseJoinColumns=@JoinColumn(name="player_id"))
	public Set<Player> getPlayers() {
		return players;
	}
	public void setPlayers(Set<Player> players) {
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
}
