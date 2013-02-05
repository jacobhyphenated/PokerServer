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
		
		//Sort and get the next player to act (immediately after the big blind)
		List<PlayerHand> players = new ArrayList<PlayerHand>();
		players.addAll(participatingPlayers);
		Player nextToAct = getNextPlayerInGameOrder(players, this.getPlayerInBB(hand));
		hand.setCurrentToAct(nextToAct);
		
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

		List<PlayerHand> players = new ArrayList<PlayerHand>();
		//For all players in the hand, remove any who are out of chips (eliminated)
		int count = 0;
		for(PlayerHand p : hand.getPlayers()){
			if(p.getPlayer().getChips() != 0){
				players.add(p);
				count++;
			}
			else if(p.getPlayer().equals(game.getPlayerInBTN())){
				//If the player on the Button has been eliminated, we still need this player
				//in the list so that we calculate next button from its position
				players.add(p);
			}
		}
		game.setPlayersRemaining(count);
		if(count < 2){
			//TODO end game
		}
		
		//Rotate Button.  Use Simplified Moving Button algorithm (for ease of coding)
		//This means we always rotate button.  Blinds will be next two active players.  May skip blinds.
		Player nextButton = this.getNextPlayerInGameOrder(players, game.getPlayerInBTN());
		game.setPlayerInBTN(nextButton);
		
		gameDao.merge(game);
		
		//Remove Deck from database. No need to keep that around anymore
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
	
	@Override
	public Player getPlayerInSB(HandEntity hand){
		Player button = hand.getGame().getPlayerInBTN();
		//Heads up the Button is the Small Blind
		if(hand.getPlayers().size() == 2){
			return button;
		}
		List<PlayerHand> players = new ArrayList<PlayerHand>();
		players.addAll(hand.getPlayers());
		return getNextPlayerInGameOrder(players, button);
	}
	
	@Override
	public Player getPlayerInBB(HandEntity hand){
		Player button = hand.getGame().getPlayerInBTN();
		List<PlayerHand> players = new ArrayList<PlayerHand>();
		players.addAll(hand.getPlayers());
		Player leftOfButton = getNextPlayerInGameOrder(players, button);
		//Heads up, the player who is not the Button is the Big blind
		if(hand.getPlayers().size() == 2){
			return leftOfButton;
		}
		return getNextPlayerInGameOrder(players, leftOfButton);
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
	
	private Player getNextPlayerInGameOrder(List<PlayerHand> players, Player startPlayer){
		//Sorted list by game order
		Collections.sort(players);
		for(int i = 0; i < players.size(); i++){
			//Find the player we are starting at
			if(players.get(i).getPlayer().equals(startPlayer)){
				//The next player is either the next in the list, or the first in the list if startPlayer is at the end
				return (i == players.size() - 1) ? players.get(0).getPlayer() : players.get(i+1).getPlayer();
			}
		}
		return null;
	}

}
