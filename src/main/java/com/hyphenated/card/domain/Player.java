package com.hyphenated.card.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="player")
public class Player implements Comparable<Player>{

	private long id;
	private Game game;
	private String name;
	private int chips;
	private int gamePosition;
	private int finishPosition;
	
	@Column(name="player_id")
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name="game_id")
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="chips")
	public int getChips() {
		return chips;
	}
	public void setChips(int chips) {
		this.chips = chips;
	}
	
	@Column(name="game_position")
	public int getGamePosition() {
		return gamePosition;
	}
	public void setGamePosition(int gamePosition) {
		this.gamePosition = gamePosition;
	}
	
	@Column(name="finished_place")
	public int getFinishPosition() {
		return finishPosition;
	}
	public void setFinishPosition(int finishPosition) {
		this.finishPosition = finishPosition;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof Player)){
			return false;
		}
		Player p = (Player) o;
		if(this.getId() <= 0){
			return this.getName().equals(p.getName());
		}
		return this.getId() == p.getId();
	}
	
	@Override
	public int hashCode(){
		if(id <= 0){
			return name.hashCode();
		}
		return (int) id;
	}
	
	@Override
	@Transient
	public int compareTo(Player p){
		return this.getGamePosition() - p.getGamePosition();
	}
}
