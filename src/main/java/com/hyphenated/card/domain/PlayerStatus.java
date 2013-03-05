package com.hyphenated.card.domain;

/**
 * Enum type to track the player's current status in the game/hand
 * 
 * @author jacobhyphenated
 */
public enum PlayerStatus {
	/** The game has not been started */
	NOT_STARTED,
	/** Players are taking there seats */
	SEATING,
	/** Waiting for another player to act */
	WAITING,
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
