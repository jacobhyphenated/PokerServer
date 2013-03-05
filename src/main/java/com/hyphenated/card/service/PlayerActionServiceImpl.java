package com.hyphenated.card.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hyphenated.card.dao.PlayerDao;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.HandEntity;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.domain.PlayerHand;
import com.hyphenated.card.domain.PlayerStatus;

@Service
public class PlayerActionServiceImpl implements PlayerActionService {

	@Autowired
	private PlayerDao playerDao;
	
	@Override
	public boolean fold(Player player, HandEntity hand) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean check(Player player, HandEntity hand) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean bet(Player player, HandEntity hand, int betAmount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean call(Player player, HandEntity hand) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transactional
	public PlayerStatus getPlayerStatus(Player player) {
		player = playerDao.merge(player);
		if(player == null || player.getChips() <= 0){
			return PlayerStatus.ELIMINATED;
		}
		
		Game game = player.getGame();
		if(!game.isStarted()){
			return PlayerStatus.NOT_STARTED;
		}
		
		HandEntity hand = game.getCurrentHand();
		if(hand == null){
			return PlayerStatus.SEATING;
		}
		PlayerHand playerHand = null;
		for(PlayerHand ph : hand.getPlayers()){
			if(ph.getPlayer().equals(player)){
				playerHand = ph;
				break;
			}
		}
		
		if(!hand.getPlayers().contains(playerHand)){
			return PlayerStatus.SIT_OUT;
		}
		
		if(hand.getCurrentToAct() == null){
			//TODO is winner?
		}
		
		if(!player.equals(hand.getCurrentToAct())){
			return PlayerStatus.WAITING;
		}
		
		if(hand.getTotalBetAmount() > playerHand.getBetAmount()){
			return PlayerStatus.ACTION_TO_CALL;
		}
		else{
			return PlayerStatus.ACTION_TO_CHECK;
		}

	}

	
}
