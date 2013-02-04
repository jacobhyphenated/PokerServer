package com.hyphenated.card.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="game")
public class Game {

	private long id;
	private int playersRemaining;
	private Player playerInBB;
	private Player playerInBTN;
	private GameType gameType;
	private String name;
	private boolean isStarted;
	private Set<Player> players;
	private HandEntity currentHand;
	private GameStructure gameStructure;
	
	@Column(name="game_id")
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="players_left")
	public int getPlayersRemaining() {
		return playersRemaining;
	}
	public void setPlayersRemaining(int playersRemaining) {
		this.playersRemaining = playersRemaining;
	}
	
	@OneToOne
	@JoinColumn(name="bb_player_id")
	public Player getPlayerInBB() {
		return playerInBB;
	}
	public void setPlayerInBB(Player playerInBB) {
		this.playerInBB = playerInBB;
	}
	
	@OneToOne
	@JoinColumn(name="btn_player_id")
	public Player getPlayerInBTN(){
		return playerInBTN;
	}
	public void setPlayerInBTN(Player playerInBTN){
		this.playerInBTN = playerInBTN;
	}
	
	@Column(name="game_type")
	@Enumerated(EnumType.STRING)
	public GameType getGameType() {
		return gameType;
	}
	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}
	
	@OneToMany(mappedBy="game", fetch=FetchType.LAZY)
	public Set<Player> getPlayers() {
		return players;
	}
	public void setPlayers(Set<Player> players) {
		this.players = players;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="is_started")
	public boolean isStarted() {
		return isStarted;
	}
	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="current_hand_id")
	public HandEntity getCurrentHand() {
		return currentHand;
	}
	public void setCurrentHand(HandEntity currentHand) {
		this.currentHand = currentHand;
	}
	
	@OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@JoinColumn(name="game_structure_id")
	public GameStructure getGameStructure() {
		return gameStructure;
	}
	public void setGameStructure(GameStructure gameStructure) {
		this.gameStructure = gameStructure;
	}
}
