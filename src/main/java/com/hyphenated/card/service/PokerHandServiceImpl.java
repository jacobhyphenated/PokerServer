package com.hyphenated.card.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyphenated.card.Deck;
import com.hyphenated.card.dao.BoardDao;
import com.hyphenated.card.dao.GameDao;
import com.hyphenated.card.dao.HandDao;
import com.hyphenated.card.domain.BlindLevel;
import com.hyphenated.card.domain.BoardEntity;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.HandEntity;
import com.hyphenated.card.domain.Player;

@Service
public class PokerHandServiceImpl implements PokerHandService {

	@Autowired
	private BoardDao boardDao;
	
	@Autowired
	private HandDao handDao;
	
	@Autowired
	private GameDao gameDao;
	
	@Override
	@Transactional
	public HandEntity startNewHand(Game game) {
		HandEntity hand = new HandEntity();
		hand.setBlindLevel(BlindLevel.BLIND_10_20); //TODO get blind level from game type
		//hand.setCurrentToAct(null); TODO service call to get current player to act for new hand from game.
		hand.setGame(game);
		Set<Player> participatingPlayers = new HashSet<Player>();
		for(Player p : game.getPlayers()){
			if(p.getChips() > 0){
				participatingPlayers.add(p);
			}
		}
		//TODO deal starting cards to players
		hand.setPlayers(participatingPlayers);
		Deck d = new Deck(true);
		hand.setCards(d.exportDeck());
		
		BoardEntity b = new BoardEntity();
		boardDao.save(b);
		hand.setBoard(b);
		
		hand = handDao.save(hand);
		
		game.setCurrentHand(hand);
		gameDao.save(game);
		return hand;
	}

	@Override
	@Transactional(readOnly=true)
	public HandEntity getHandById(long id) {
		return handDao.findById(id);
	}

	@Override
	@Transactional
	public HandEntity saveHand(HandEntity hand) {
		return handDao.save(hand);
	}
	

	@Override
	@Transactional
	public HandEntity flop(HandEntity hand) throws IllegalStateException {
		if(hand.getBoard().getFlop1() != null){
			throw new IllegalStateException("Unexpected Flop.");
		}
		//Re-attach to persistent context for this transaction (Lazy Loading stuff)
		hand = handDao.merge(hand);
		
		Deck d = new Deck(hand.getCards());
		d.shuffleDeck();
		BoardEntity board = hand.getBoard();
		board.setFlop1(d.dealCard());
		board.setFlop2(d.dealCard());
		board.setFlop3(d.dealCard());
		hand.setCards(d.exportDeck());
		boardDao.save(board);
		return handDao.merge(hand);
	}
	
	@Override
	@Transactional
	public HandEntity turn(HandEntity hand) throws IllegalStateException{
		if(hand.getBoard().getFlop1() == null || hand.getBoard().getTurn()!= null){
			throw new IllegalStateException("Unexpected Turn.");
		}
		//Re-attach to persistent context for this transaction (Lazy Loading stuff)
		hand = handDao.merge(hand);
		Deck d = new Deck(hand.getCards());
		d.shuffleDeck();
		BoardEntity board = hand.getBoard();
		board.setTurn(d.dealCard());
		hand.setCards(d.exportDeck());
		boardDao.save(board);
		return handDao.merge(hand);
	}
	
	@Override
	@Transactional
	public HandEntity river(HandEntity hand) throws IllegalStateException{
		if(hand.getBoard().getFlop1() == null || hand.getBoard().getTurn() == null 
				|| hand.getBoard().getRiver() != null){
			throw new IllegalStateException("Unexpected River.");
		}
		//Re-attach to persistent context for this transaction (Lazy Loading stuff)
		hand = handDao.merge(hand);
		Deck d = new Deck(hand.getCards());
		d.shuffleDeck();
		BoardEntity board = hand.getBoard();
		board.setRiver(d.dealCard());
		hand.setCards(d.exportDeck());
		boardDao.save(board);
		return handDao.merge(hand);
	}
	
	@Override
	@Transactional(readOnly=true)
	public BoardEntity getBoard(long id) {
		return boardDao.findById(id);
	}

	@Override
	@Transactional
	public BoardEntity saveBoard(BoardEntity board) {
		return boardDao.save(board);
	}

	@Override
	@Transactional(readOnly=true)
	public List<BoardEntity> getAllBoards() {
		return boardDao.findAll();
	}

	@Override
	@Transactional
	public void deleteBoard(BoardEntity board) {
		boardDao.remove(board);
	}

}
