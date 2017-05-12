package com.hyphenated.card.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TournamentStructure extends GameStructure {
	private static final long serialVersionUID = 420779010922553220L;
	private BlindLevel currentBlindLevel;
	private int blindLength;
	private Date currentBlindEndTime;
	private List<BlindLevel> blindLevels;

	@Column(name = "current_blind_level")
	@Enumerated(EnumType.STRING)
	public BlindLevel getCurrentBlindLevel() {
		return currentBlindLevel;
	}

	public void setCurrentBlindLevel(BlindLevel currentBlindLevel) {
		this.currentBlindLevel = currentBlindLevel;
	}

	@Column(name = "blind_length")
	public int getBlindLength() {
		return blindLength;
	}

	public void setBlindLength(int blindLength) {
		this.blindLength = blindLength;
	}

	@Column(name = "current_blind_ends")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCurrentBlindEndTime() {
		return currentBlindEndTime;
	}

	public void setCurrentBlindEndTime(Date currentBlindEndTime) {
		this.currentBlindEndTime = currentBlindEndTime;
	}

	@ElementCollection(targetClass = BlindLevel.class)
	@JoinTable(name = "game_blind", joinColumns = @JoinColumn(name = "game_structure_id"))
	@Column(name = "blind", nullable = false)
	@Enumerated(EnumType.STRING)
	public List<BlindLevel> getBlindLevels() {
		return blindLevels;
	}

	public void setBlindLevels(List<BlindLevel> blindLevels) {
		this.blindLevels = blindLevels;
	}

}
