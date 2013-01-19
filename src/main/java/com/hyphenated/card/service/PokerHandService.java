package com.hyphenated.card.service;

import java.util.List;

import com.hyphenated.card.domain.BoardEntity;

/**
 * Service to handle operations related to a poker hand.  Tracks the board state and players.
 * 
 * @author jacobhyphenated
 */
public interface PokerHandService {

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
