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
	/** The Player has been sat out of the game. The player still has a seat at the table, but is not in the seat. */
	SIT_OUT_GAME,
	/** The Player is no longer in the game.  */
	ELIMINATED;
}
