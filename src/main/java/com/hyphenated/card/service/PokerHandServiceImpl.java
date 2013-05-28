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
package com.hyphenated.card.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyphenated.card.Card;
import com.hyphenated.card.Deck;
import com.hyphenated.card.dao.GameDao;
import com.hyphenated.card.dao.HandDao;
import com.hyphenated.card.dao.PlayerDao;
import com.hyphenated.card.domain.BlindLevel;
import com.hyphenated.card.domain.BoardEntity;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.HandEntity;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.domain.PlayerHand;
import com.hyphenated.card.util.PlayerHandBetAmountComparator;
import com.hyphenated.card.util.PlayerUtil;

@Service
public class PokerHandServiceImpl implements PokerHandService {
	
	@Autowired
	private HandDao handDao;
	
	@Autowired
	private GameDao gameDao;
	
	@Autowired
	private PlayerDao playerDao;
	
	@Override
	@Transactional
	public HandEntity startNewHand(Game game) {
		game = gameDao.merge(game);
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
		Player nextToAct = PlayerUtil.getNextPlayerInGameOrderPH(players, this.getPlayerInBB(hand));
		hand.setCurrentToAct(nextToAct);
		
		//Register the Forced Small and Big Blind bets as part of the hand
		Player smallBlind = getPlayerInSB(hand);
		Player bigBlind = getPlayerInBB(hand);
		int sbBet = 0;
		int bbBet = 0;
		for (PlayerHand ph : hand.getPlayers()){
			if(ph.getPlayer().equals(smallBlind)){
				sbBet = Math.min(hand.getBlindLevel().getSmallBlind(), smallBlind.getChips());
				ph.setBetAmount(sbBet);
				ph.setRoundBetAmount(sbBet);
				smallBlind.setChips(smallBlind.getChips() - sbBet);
				
			}
			else if(ph.getPlayer().equals(bigBlind)){
				bbBet = Math.min(hand.getBlindLevel().getBigBlind(), bigBlind.getChips());
				ph.setBetAmount(bbBet);
				ph.setRoundBetAmount(bbBet);
				bigBlind.setChips(bigBlind.getChips() - bbBet);
			}
			
		}
		hand.setTotalBetAmount(hand.getBlindLevel().getBigBlind());
		hand.setLastBetAmount(hand.getBlindLevel().getBigBlind());
		hand.setPot(sbBet + bbBet);
		
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
		if(!isActionResolved(hand)){
			throw new IllegalStateException("There are unresolved betting actions");
		}
		
		Game game = hand.getGame();

		hand.setCurrentToAct(null);
		
		determineWinner(hand);

		//If players were eliminated this hand, set their finished position
		List<PlayerHand> phs = new ArrayList<PlayerHand>();
		phs.addAll(hand.getPlayers());
		//Sort the list of PlayerHands so the one with the smallest chips at risk is first.
		//Use this to determine finish position if multiple players are eliminated on the same hand.
		Collections.sort(phs, new PlayerHandBetAmountComparator());
		for(PlayerHand ph : phs){
			if(ph.getPlayer().getChips() <= 0){
				ph.getPlayer().setFinishPosition(game.getPlayersRemaining());
				game.setPlayersRemaining(game.getPlayersRemaining() - 1);
				playerDao.save(ph.getPlayer());
			}
		}
		
		//For all players in the game, remove any who are out of chips (eliminated)
		List<Player> players = new ArrayList<Player>();
		for(Player p : game.getPlayers()){
			if(p.getChips() != 0){
				players.add(p);
			}
			else if(p.equals(game.getPlayerInBTN())){
				//If the player on the Button has been eliminated, we still need this player
				//in the list so that we calculate next button from its position
				players.add(p);
			}
		}
		if(game.getPlayersRemaining() < 2){
			//TODO end game. Move to start hand?
		}
		
		//Rotate Button.  Use Simplified Moving Button algorithm (for ease of coding)
		//This means we always rotate button.  Blinds will be next two active players.  May skip blinds.
		Player nextButton = PlayerUtil.getNextPlayerInGameOrder(players, game.getPlayerInBTN());
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
		if(!isActionResolved(hand)){
			throw new IllegalStateException("There are unresolved preflop actions");
		}
		
		Deck d = new Deck(hand.getCards());
		d.shuffleDeck();
		BoardEntity board = hand.getBoard();
		board.setFlop1(d.dealCard());
		board.setFlop2(d.dealCard());
		board.setFlop3(d.dealCard());
		hand.setCards(d.exportDeck());
		resetRoundValues(hand);
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
		if(!isActionResolved(hand)){
			throw new IllegalStateException("There are unresolved flop actions");
		}
		
		Deck d = new Deck(hand.getCards());
		d.shuffleDeck();
		BoardEntity board = hand.getBoard();
		board.setTurn(d.dealCard());
		hand.setCards(d.exportDeck());
		resetRoundValues(hand);
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
		if(!isActionResolved(hand)){
			throw new IllegalStateException("There are unresolved turn actions");
		}
		
		Deck d = new Deck(hand.getCards());
		d.shuffleDeck();
		BoardEntity board = hand.getBoard();
		board.setRiver(d.dealCard());
		hand.setCards(d.exportDeck());
		resetRoundValues(hand);
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
		return PlayerUtil.getNextPlayerInGameOrderPH(players, button);
	}
	
	@Override
	public Player getPlayerInBB(HandEntity hand){
		Player button = hand.getGame().getPlayerInBTN();
		List<PlayerHand> players = new ArrayList<PlayerHand>();
		players.addAll(hand.getPlayers());
		Player leftOfButton = PlayerUtil.getNextPlayerInGameOrderPH(players, button);
		//Heads up, the player who is not the Button is the Big blind
		if(hand.getPlayers().size() == 2){
			return leftOfButton;
		}
		return PlayerUtil.getNextPlayerInGameOrderPH(players, leftOfButton);
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
	
	private void resetRoundValues(HandEntity hand){
		hand.setTotalBetAmount(0);
		hand.setLastBetAmount(0);
		
		List<Player> playersInHand = new ArrayList<Player>();
		for(PlayerHand ph : hand.getPlayers()){
			ph.setRoundBetAmount(0);
			playersInHand.add(ph.getPlayer());
		}
		//Next player is to the left of the button.  Given that the button may have been eliminated
		//In a round of betting, we need to put the button back to determine relative position.
		Player btn = hand.getGame().getPlayerInBTN();
		if(!playersInHand.contains(btn)){
			playersInHand.add(btn);
		}
		
		Player next = PlayerUtil.getNextPlayerInGameOrder(playersInHand, btn);
		Player firstNext = next;
		//Skip all in players
		while(next.getChips() <= 0 ){
			next = PlayerUtil.getNextPlayerInGameOrder(playersInHand, next);
			if(next.equals(firstNext)){
				//Exit condition if all players are all in.
				break;
			}
		}
		hand.setCurrentToAct(next);
	}
	
	private void determineWinner(HandEntity hand){
		//if only one PH left, everyone else folded
		if(hand.getPlayers().size() == 1){
			Player winner = hand.getPlayers().iterator().next().getPlayer();
			winner.setChips(winner.getChips() + hand.getPot());
			playerDao.merge(winner);
		}
		else{
			//Refund all in overbet player if applicable before determining winner
			refundOverbet(hand);
			
			//Iterate through map of players to there amount won.  Persist.
			Map<Player, Integer> winners = PlayerUtil.getAmountWonInHandForAllPlayers(hand);
			if(winners == null){
				return;
			}
			for(Map.Entry<Player, Integer> entry : winners.entrySet()){
				Player player = entry.getKey();
				player.setChips(player.getChips() + entry.getValue());
				playerDao.merge(player);
			}
		}
	}
	
	private void refundOverbet(HandEntity hand){
		List<PlayerHand> phs = new ArrayList<PlayerHand>();
		phs.addAll(hand.getPlayers());
		//Sort from most money contributed, to the least
		Collections.sort(phs, new PlayerHandBetAmountComparator());
		Collections.reverse(phs);
		//If there are at least 2 players, and the top player contributed more to the pot than the next player
		if(phs.size() >= 2 && (phs.get(0).getBetAmount() > phs.get(1).getBetAmount())){
			//Refund that extra amount contributed. Remove from pot, add back to player
			int diff = phs.get(0).getBetAmount() - phs.get(1).getBetAmount();
			phs.get(0).setBetAmount(phs.get(1).getBetAmount());
			phs.get(0).getPlayer().setChips(phs.get(0).getPlayer().getChips() + diff);
			hand.setPot(hand.getPot() - diff);
		}
	}
	
	//Helper method to see if there are any outstanding actions left in a betting round
	private boolean isActionResolved(HandEntity hand){
		int roundBetAmount = hand.getTotalBetAmount();
		for(PlayerHand ph : hand.getPlayers()){
			//All players should have paid the roundBetAmount or should be all in
			if(ph.getRoundBetAmount() != roundBetAmount && ph.getPlayer().getChips() > 0){
				return false;
			}
		}
		return true;
	}

}
