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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="player")
public class Player implements Comparable<Player>, Serializable{

	private static final long serialVersionUID = -1384636077333014255L;
	private String id;
	private Game game;
	private String name;
	private int chips;
	private int gamePosition;
	private int finishPosition;
	private boolean sittingOut;
	
	@JsonIgnore
	@Column(name="player_id")
	@Id
	@GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonIgnore
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
	
	@Column(name="sitting_out")
	public boolean isSittingOut() {
		return sittingOut;
	}
	public void setSittingOut(boolean sittingOut) {
		this.sittingOut = sittingOut;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof Player)){
			return false;
		}
		Player p = (Player) o;
		if(this.getId() == null){
			return this.getName().equals(p.getName());
		}
		return this.getId().equals(p.getId());
	}
	
	@Override
	public int hashCode(){
		if(id == null){
			return name.hashCode();
		}
		return id.hashCode();
	}
	
	@Override
	@Transient
	public int compareTo(Player p){
		return this.getGamePosition() - p.getGamePosition();
	}
}
