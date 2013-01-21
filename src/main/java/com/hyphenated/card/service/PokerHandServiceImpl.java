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
		Deck d = new Deck(true);
		hand.setCards(d.exportDeck());
		hand.setGame(game);
		Set<Player> participatingPlayers = new HashSet<Player>();
		for(Player p : game.getPlayers()){
			if(p.getChips() > 0){
				participatingPlayers.add(p);
			}
		}
		hand.setPlayers(participatingPlayers);
		
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
