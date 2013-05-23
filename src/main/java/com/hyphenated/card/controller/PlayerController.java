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
package com.hyphenated.card.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.HandEntity;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.domain.PlayerHand;
import com.hyphenated.card.domain.PlayerStatus;
import com.hyphenated.card.service.GameService;
import com.hyphenated.card.service.PlayerActionService;

/**
 * Controller class that will handle the front-end API interactions regarding
 * individual players involved with a game.
 * 
 * @author jacobhyphenated
 * Copyright (c) 2013
 */
@Controller
public class PlayerController {
	
	@Autowired
	private PlayerActionService playerActionService;
	
	@Autowired
	private GameService gameService;
	
	@RequestMapping("/games")
	public ModelAndView getGames(){
		//TODO - Service method not yet written.  For now, use gameId from game controller
		return null;
	}
	
	/**
	 * Have a new player join a game.
	 * @param gameId unique ID for the game to be joined
	 * @param playerName player name
	 * @return Map representing the JSON String of the new player's unique id.  The playerId will
	 * be used for that player's actions in future requests.  Example: {"playerId":xxx}
	 */
	@RequestMapping("/join")
	public @ResponseBody Map<String,Long> joinGame(@RequestParam long gameId, @RequestParam String playerName){
		Game game = gameService.getGameById(gameId, false);
		Player player = new Player();
		player.setName(playerName);
		player = gameService.addNewPlayerToGame(game, player);
		return Collections.singletonMap("playerId", player.getId());
	}
	
	/**
	 * Player status in the current game and hand.
	 * 
	 * @param gameId unique ID of the game the player is playing in
	 * @param playerId unique ID of the player
	 * @return a Map represented as a JSON String with values for status ({@link PlayerStatus}),
	 * chips left, current hole cards, amount bet this betting round, and amount to call if there is
	 * a bet this round. Example: {"status":"ACTION_TO_CALL","chips":1000,"card1":"Xx","card2":"Xx",
	 * "amountBetRound":xx,"amountToCall":100}
	 */
	@RequestMapping("/status")
	public @ResponseBody Map<String,? extends Object> getPlayerStatus(@RequestParam long gameId, @RequestParam long playerId){
		Game game = gameService.getGameById(gameId, false);
		Player player = playerActionService.getPlayerById(playerId);
		Map<String,Object> results = new HashMap<String, Object>();
		results.put("status", playerActionService.getPlayerStatus(player));
		results.put("chips", player.getChips());
		if(game.getCurrentHand() != null){
			HandEntity hand = game.getCurrentHand();
			PlayerHand playerHand = null;
			for(PlayerHand ph : hand.getPlayers()){
				if(ph.getPlayer().equals(player)){
					playerHand = ph;
					break;
				}
			}
			if(playerHand != null){
				results.put("card1", playerHand.getHand().getCard(0));
				results.put("card2", playerHand.getHand().getCard(1));
				results.put("amountBetRound",playerHand.getRoundBetAmount());
				
				int toCall = hand.getTotalBetAmount() - playerHand.getRoundBetAmount();
				toCall = Math.min(toCall, player.getChips());
				if(toCall > 0){
					results.put("amountToCall", toCall);					
				}
			}
		}
		return results;
	}
	
	/**
	 * Fold the hand.
	 * @param gameId Unique ID of the game being played
	 * @param playerId Unique ID of the player taking the action
	 * @return Map with a single field for success, if the player successfully folded or not.
	 * If fold is not a legal action, or the it is not this players turn to act, success will be false.
	 * Example: {"success":true}
	 */
	@RequestMapping("/fold")
	public @ResponseBody Map<String,Boolean> fold(@RequestParam long gameId, @RequestParam long playerId){
		Game game = gameService.getGameById(gameId, false);
		Player player = playerActionService.getPlayerById(playerId);
		boolean folded = playerActionService.fold(player, game.getCurrentHand());
		return Collections.singletonMap("success", folded);
	}
	
	/**
	 * Call a bet.
	 * @param gameId Unique ID of the game being played
	 * @param playerId unique ID of the player calling the action
	 * @return Map represented as a JSON String with two fields, success and chips.  If calling is
	 * not a legal action, or it is not this player's turn, success will be false. The Chips field
	 * represents the current amount of chips the player has left after taking this action.
	 * Example: {"success":true,"chips":xxx}
	 */
	@RequestMapping("/call")
	public @ResponseBody Map<String,? extends Object> call(@RequestParam long gameId, @RequestParam long playerId){
		Game game = gameService.getGameById(gameId, false);
		Player player = playerActionService.getPlayerById(playerId);
		boolean called = playerActionService.call(player, game.getCurrentHand());
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", called);
		resultMap.put("chips", player.getChips());
		return resultMap;
	}
	
	/**
	 * Check the action in this hand.
	 * @param gameId Unique ID of the game being played
	 * @param playerId Unique ID of the player making the action
	 * @return Map represented as a JSON String with a single field: success.  If checking is not
	 * a legal action, or it is not this player's turn, success will be false.
	 * Example: {"success":false}
	 */
	@RequestMapping("/check")
	public @ResponseBody Map<String,Boolean> check(@RequestParam long gameId, @RequestParam long playerId){
		Game game = gameService.getGameById(gameId, false);
		Player player = playerActionService.getPlayerById(playerId);
		boolean checked = playerActionService.check(player, game.getCurrentHand());
		return Collections.singletonMap("success", checked);
	}
	
	/**
	 * Bet or Raise.
	 * 
	 * @param gameId unique ID of the game being played
	 * @param playerId Unique ID of the player taking this action
	 * @param betAmount amount of the bet.  This is the total amount, so if there was a bet
	 * before, and this is the raise, this value represents the amount bet for only this raise from the player.
	 * To put in another way, this betAmount value is the amount is the amount bet <em>in addition to</em>
	 * the amount it would take to call.
	 * <br />For example: Player 1 bets 100.  Player 2 raises to 300.  Player 2 calls this method with the
	 * betAmount parameter of 200.  Player 1 re-raises to 900 total (100 + 300 + 500 more).  The betAmount
	 * parameter is passed as 500.
	 * @return Map representing a JSON String with two values: success and chips.  If a bet is not a legal
	 * action, or if it is not this player's turn, success will be false. The chips value represents the
	 * amount of chips the player has after completing this action.
	 * Example: {"success":true,"chips":xxx}
	 */
	@RequestMapping("/bet")
	public @ResponseBody Map<String,? extends Object> bet(@RequestParam long gameId, @RequestParam long playerId, @RequestParam int betAmount){
		Game game = gameService.getGameById(gameId, false);
		Player player = playerActionService.getPlayerById(playerId);
		boolean bet = playerActionService.bet(player, game.getCurrentHand(),betAmount);
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", bet);
		resultMap.put("chips", player.getChips());
		return resultMap;
	}

}
