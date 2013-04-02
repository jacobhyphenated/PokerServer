package com.hyphenated.card.domain;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape=Shape.OBJECT)
public enum CommonTournamentFormats {
	
	TWO_HR_SEVENPPL("7 to 8 People, 2 hour length", 15, 1500,
			BlindLevel.BLIND_10_20,
			BlindLevel.BLIND_15_30,
			BlindLevel.BLIND_25_50,
			BlindLevel.BLIND_50_100,
			BlindLevel.BLIND_75_150,
			BlindLevel.BLIND_100_200,
			BlindLevel.BLIND_200_400,
			BlindLevel.BLIND_300_600,
			BlindLevel.BLIND_500_1000,
			BlindLevel.BLIND_1000_2000,
			BlindLevel.BLIND_2000_4000),
	
	TWO_HR_NINEPPL("9 Players, 2 hour length", 10, 1500,
			BlindLevel.BLIND_10_20,
			BlindLevel.BLIND_15_30,
			BlindLevel.BLIND_20_40,
			BlindLevel.BLIND_30_60,
			BlindLevel.BLIND_40_80,
			BlindLevel.BLIND_50_100,
			BlindLevel.BLIND_75_150,
			BlindLevel.BLIND_100_200,
			BlindLevel.BLIND_150_300,
			BlindLevel.BLIND_200_400,
			BlindLevel.BLIND_300_600,
			BlindLevel.BLIND_400_800,
			BlindLevel.BLIND_500_1000,
			BlindLevel.BLIND_1000_2000,
			BlindLevel.BLIND_2000_4000,
			BlindLevel.BLIND_4000_8000),
			
	TWO_HR_SIXPPL("6 Players, 2 hour length", 15, 2000,
			BlindLevel.BLIND_10_20,
			BlindLevel.BLIND_15_30,
			BlindLevel.BLIND_25_50,
			BlindLevel.BLIND_50_100,
			BlindLevel.BLIND_75_150,
			BlindLevel.BLIND_100_200,
			BlindLevel.BLIND_200_400,
			BlindLevel.BLIND_300_600,
			BlindLevel.BLIND_500_1000,
			BlindLevel.BLIND_1000_2000,
			BlindLevel.BLIND_2000_4000);
	
	
	private String description;
	private BlindLevel[] levels;
	private int timeInMins;
	private int startingChips;
	
	private CommonTournamentFormats(String description, int timeInMins, int startingChips, BlindLevel... levels){
		this.description = description;
		this.timeInMins = timeInMins;
		this.levels = levels;
		this.startingChips = startingChips;
	}
	
	public String getDescription(){
		return description;
	}
	
	@JsonProperty("blindLength")
	public int getTimeInMinutes(){
		return timeInMins;
	}
	
	public List<BlindLevel> getBlindLevels(){
		return Arrays.asList(levels);
	}
	
	public int getStartingChips(){
		return this.startingChips;
	}
	
	/**
	 * Override the super Enum method for JSON Serialization purposes.
	 * @return Exactly the same as if you called the Enum getName().  Returns
	 * the string value of the Enum identifier.
	 */
	public String getName(){
		return super.name();
	}
}
