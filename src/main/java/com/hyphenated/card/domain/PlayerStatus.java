package com.hyphenated.card.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Enum type to track the player's current status in the game/hand
 * 
 * @author jacobhyphenated
 */
@JsonFormat(shape=Shape.STRING)
public enum PlayerStatus {
	/** The game has not been started */
	NOT_STARTED,
	/** Players are taking there seats */
	SEATING,
	/** Waiting for another player to act */
	WAITING,
	/** Player is still in the hand, but has committed all of his/her chips and will take no further action */
	ALL_IN,
	/** The hand went to showdown, but the player lost */
	LOST_HAND,
	/** Won chips in the previous hand.  The old English for won is winnan. */
	WON_HAND,
	/** Post the Small Blind for this hand */
	POST_SB,
	/** Post the Big Blind for this hand */
	POST_BB,
	/** Player is ready to act.  Player must call or raise to continue. */
	ACTION_TO_CALL,
	/** Player is ready to act. There is no bet, so the player may check. */
	ACTION_TO_CHECK,
	/** The Player is still in the game but not in the hand. */
	SIT_OUT,
	/** The Player is no longer in the game.  */
	ELIMINATED;
}
