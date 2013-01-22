package com.hyphenated.card.service;

import java.util.List;

import com.hyphenated.card.domain.BoardEntity;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.HandEntity;

/**
 * Service to handle operations related to a poker hand.  Tracks the board state and players.
 * 
 * @author jacobhyphenated
 */
public interface PokerHandService {
	
	/**
	 * Start a new hand on the given game
	 * @param game {@link Game} to start the hand with
	 * @return {@link HandEntity} for the hand
	 */
	public HandEntity startNewHand(Game game);
	
	/**
	 * Get the hand from the persistence context based on the unique id
	 * @param id unique id for the hand
	 * @return {@link HandEntity}
	 */
	public HandEntity getHandById(long id);
	
	/**
	 * Save the hand to the persistence context
	 * @param hand Detached {@link HandEntity}
	 * @return HandEntity with attached persistence context
	 */
	public HandEntity saveHand(HandEntity hand);
	
	/**
	 * Handle the flop for the hand
	 * @param hand {@link HandEntity} that owns the board the flop will be on
	 * @return updated hand with {@link BoardEntity} containing the flop
	 * @throws IllegalStateException if the hand is not in a state to expect the flop.
	 */
	public HandEntity flop(HandEntity hand) throws IllegalStateException;
	
	/**
	 * Handle the turn for the hand
	 * @param hand {@link HandEntity} that owns the board the turn will be on
	 * @return updated hand with {@link BoardEntity} containing the turn
	 * @throws IllegalStateException if the hand is not in a state to expect a turn card.
	 * This may be because there is already a turn card, or there is no flop yet.
	 */
	public HandEntity turn(HandEntity hand) throws IllegalStateException;
	
	/**
	 * Handle the river card for the hand
	 * @param hand {@link HandEntity} that owns the board the river will be on
	 * @return updated hand with {@link BoardEntity} containing the river card
	 * @throws IllegalStateException if the hand is not in a state to expect a river card.
	 */
	public HandEntity river(HandEntity hand) throws IllegalStateException;

	/**
	 * Get the board - all community cards (flop, turn, river)
	 * @param id unique ID of the board
	 * @return
	 */
	public BoardEntity getBoard(long id);
	
	/**
	 * Persist the board.  This can be a new board, or a board where changes have been made
	 * @param board {@link BoardEntity} to be saved
	 * @return updated board object now attached to a persistent context
	 */
	public BoardEntity saveBoard(BoardEntity board);
	
	/**
	 * Get all boards.  This is a test / debug method and should probably be removed
	 * @return
	 */
	public List<BoardEntity> getAllBoards();
	
	/**
	 * Remove a board from the persistent context.
	 * @param board attached {@link BoardEntity} object to be removed.
	 */
	public void deleteBoard(BoardEntity board);
}
