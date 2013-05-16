package com.hyphenated.card.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Enum to track the status of the Game from the perspective of the game organizer/controller.
 * 
 * @author jacobhyphenated
 * Copyright (c) 2013
 */
@JsonFormat(shape=Shape.STRING)
public enum GameStatus {
	NOT_STARTED,
	SEATING,
	PREFLOP,
	FLOP,
	TURN,
	RIVER,
	END_HAND
}
