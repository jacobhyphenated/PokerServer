package com.hyphenated.card.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyphenated.card.Card;
import com.hyphenated.card.Deck;
import com.hyphenated.card.dao.GameDao;
import com.hyphenated.card.dao.HandDao;
import com.hyphenated.card.domain.BlindLevel;
import com.hyphenated.card.domain.BoardEntity;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.HandEntity;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.domain.PlayerHand;

@Service
public class PokerHandServiceImpl implements PokerHandService {
	
	@Autowired
	private HandDao handDao;
	
	@Autowired
	private GameDao gameDao;
	
	@Override
	@Transactional
	public HandEntity startNewHand(Game game) {
		HandEntity hand = new HandEntity();
		updateBlindLevel(game);
		hand.setBlindLevel(game.getGameStructure().getCurrentBlindLevel()); 
		
		hand.setGame(game);
		
		Deck d = new Deck(true);
		
		Set<PlayerHand> participatingPlayers = new HashSet<PlayerHand>();
		for(Player p : game.getPlayers()){
			if(p.getChips() > 0){
				PlayerHand ph = new PlayerHand();
				ph.setHandEntity(hand);
				ph.setPlayer(p);
				ph.setCard1(d.dealCard());
				ph.setCard2(d.dealCard());
				participatingPlayers.add(ph);
			}
		}
		hand.setPlayers(participatingPlayers);
		//hand.setCurrentToAct(null); TODO service call to get current player to act for new hand from game.
		
		BoardEntity b = new BoardEntity();
		hand.setBoard(b);
		hand.setCards(d.exportDeck());
		hand = handDao.save(hand);
		
		game.setCurrentHand(hand);
		gameDao.save(game);
		return hand;
	}
	
	@Override
	@Transactional
	public void endHand(HandEntity hand){
		hand = handDao.merge(hand);
		Game game = hand.getGame();
		game.setCurrentHand(null);
		//TODO move dealer button/bb
		gameDao.merge(game);
		
		//Remove Deck from database.  No need to keep that around anymore
		hand.setCards(new ArrayList<Card>());
		handDao.merge(hand);
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
		return handDao.merge(hand);
	}
	
	
	private void updateBlindLevel(Game game){
		if(game.getGameStructure().getCurrentBlindEndTime() == null){
			//Start the blind
			setNewBlindEndTime(game);
		}
		else if(game.getGameStructure().getCurrentBlindEndTime().before(new Date())){
			//Time has expired, next blind level
			List<BlindLevel> blinds = game.getGameStructure().getBlindLevels();
			Collections.sort(blinds);
			boolean nextBlind = false;
			for(BlindLevel blind : blinds){
				if(nextBlind){
					game.getGameStructure().setCurrentBlindLevel(blind);
					setNewBlindEndTime(game);
					break;
				}
				if(blind == game.getGameStructure().getCurrentBlindLevel()){
					nextBlind = true;
				}
			}
		}
	}
	
	private void setNewBlindEndTime(Game game){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, game.getGameStructure().getBlindLength());
		game.getGameStructure().setCurrentBlindEndTime(c.getTime());
	}

}
