package com.hyphenated.card.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyphenated.card.domain.CommonTournamentFormats;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameStatus;
import com.hyphenated.card.domain.GameStructure;
import com.hyphenated.card.domain.GameType;
import com.hyphenated.card.domain.HandEntity;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.service.GameService;
import com.hyphenated.card.service.PokerHandService;
import com.hyphenated.card.util.GameUtil;

/**
 * Controller class that will handle the API interactions with the front-end for the GameController.
 * The game controller is the device that handles the community cards, setting up the game, dealing, etc.
 * This will not be the controller for specific player actions, but for actions that effect the
 * game at a higher level.
 * 
 * @author jacobhyphenated
 * Copyright (c) 2013
 */
@Controller
public class GameController {

	@Autowired
	private GameService gameService;
	
	@Autowired
	private PokerHandService handService;
	
	/**
	 * Get a list of currently available game structures
	 * <br /><br />
	 * The standard URL Request to the path /structures with no parameters.
	 * @return The response is a JSON array of {@link CommonTournamentFormats} objects in JSON Object form. 
	 * Each object will contain a "name" that is the unique identifier for that format type.
	 */
	@RequestMapping("/structures")
	public @ResponseBody List<CommonTournamentFormats> getGameStructures(){
		List<CommonTournamentFormats> structures = Arrays.asList(CommonTournamentFormats.values());
		return structures;
	}
	
	/**
	 * Create a new game based on the parameters from the URL Request
	 * <br /><br />
	 * The standard URL Request to the path /create with two parameters, like:
	 * pokerserverurl.com/create?gameName=MyPokerGame&gameStructure=TWO_HR_SEVENPPL
	 * <br /><br />
	 * Use the Spring to leverage the Enum type conversions. Return JSON response
	 * with one value, gameId.
	 * @param gameName Name to identify this game
	 * @param gameStructure Type of the game that will be played
	 * @return {"gameId":xxxx}.  The Java Method returns the Map<String,Long> which is converted
	 * by Spring to the JSON object.
	 */
	@RequestMapping("/create")
	public @ResponseBody Map<String,Long> createGame(@RequestParam String gameName, 
			@RequestParam CommonTournamentFormats gameStructure){
		Game game = new Game();
		game.setName(gameName);
		game.setGameType(GameType.TOURNAMENT); //Until Cash games are supported
		GameStructure gs = new GameStructure();
		gs.setBlindLength(gameStructure.getTimeInMinutes());
		gs.setBlindLevels(gameStructure.getBlindLevels());
		gs.setStartingChips(gameStructure.getStartingChips());
		game.setGameStructure(gs);
		game = gameService.saveGame(game);
		
		return Collections.singletonMap("gameId", game.getId());
	}
	
	/**
	 * Get the status of the game.  List the status code as well as a list of all players in the game.
	 * <br /><br />
	 * The response will be a JSON Object containing the status, a list of player JSON objects, the
	 * big blind, the small blind, the pot size for the hand and the board cards (if a hand is in progress), 
	 * and the number of milliseconds left for the current blind level.
	 * @param gameId unique identifier for the game
	 * @return JSON Object of the format: {gameStatus:xxx,smallBlind:xx,bigBlind:xx,blindTime:xxx,pot:xxx,
	 * players:[{name:xxx,chips:xxx,finishPosition:xxx},...],cards:[Xx,Xx...]}
	 */
	@RequestMapping("/gamestatus")
	public @ResponseBody Map<String, ? extends Object> getGameStatus(@RequestParam long gameId){
		Game game = gameService.getGameById(gameId, true);
		GameStatus gs = GameUtil.getGameStatus(game);
		Collection<Player> players = game.getPlayers();
		
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("gameStatus", gs);
		results.put("players", players);
		//Before the game is started, there is no current blind level set.
		if(game.getGameStructure().getCurrentBlindLevel() != null){
			results.put("smallBlind", game.getGameStructure().getCurrentBlindLevel().getSmallBlind());
			results.put("bigBlind", game.getGameStructure().getCurrentBlindLevel().getBigBlind());
		}
		if(game.getGameStructure().getCurrentBlindEndTime() != null){
			long timeLeft = game.getGameStructure().getCurrentBlindEndTime().getTime() - new Date().getTime();
			timeLeft = Math.max(0, timeLeft);
			results.put("blindTime", timeLeft);
		}
		if(game.getCurrentHand() != null){
			results.put("pot", game.getCurrentHand().getPot());
			results.put("cards", game.getCurrentHand().getBoard().getBoardCards());
		}
		return results;
	}
	
	/**
	 * Start the game.  This should be called when the players have joined and everyone is ready to begin.
	 * @param gameId unique ID for the game that is to be started
	 * @return Map representing a JSON string with a single field for "success" which is either true or false.
	 * example: {"success":true}
	 */
	@RequestMapping("/startgame")
	public @ResponseBody Map<String,Boolean> startGame(@RequestParam long gameId){
		Game game = gameService.getGameById(gameId, false);
		if(!game.isStarted()){
			try{
				game = gameService.startGame(game);
				return Collections.singletonMap("success", true);
			}catch(Exception e){
				//Failure of some sort starting the game. Probably IllegalStateException
			}
		}
		return Collections.singletonMap("success", false);
	}
	
	/**
	 * Start a new hand. This method should be called at the start of the game, or when
	 * a hand is finished and a new hand needs to be dealt.
	 * @param gameId unique Id for the game with the hand to be dealt
	 * @return Map translated to a JSON string with a single field for handId of the new hand.
	 * Example: {"handId":xxx}
	 */
	@RequestMapping("/starthand")
	public @ResponseBody Map<String,Long> startHand(@RequestParam long gameId){
		Game game = gameService.getGameById(gameId, false);
		HandEntity hand = handService.startNewHand(game);
		return Collections.singletonMap("handId", hand.getId());
	}
	
	/**
	 * Deal the flop for the hand. This should be called when preflop actions are complete
	 * and the the players are ready to deal the flop cards
	 * @param handId unique ID for the hand where the flop is being dealt
	 * @return A map represented as a JSON String for the three cards dealt on the flop.
	 * The cards are denoted by the rank and the suit, with <em>2-9,T,J,Q,K,A</em> as the rank and 
	 * <em>c,s,d,h</em> as the suit.  For example: Ace of clubs is <em>Ac</em> and Nine of Diamonds
	 * is <em>9d</em>
	 * <br /><br />
	 * The json field values are card1, card2, card3.  Example: {"card1":"Xx","card2":"Xx","card3":"Xx"}
	 */
	@RequestMapping("/flop")
	public @ResponseBody Map<String,String> flop(@RequestParam long handId){
		HandEntity hand = handService.getHandById(handId);
		hand = handService.flop(hand);
		Map<String,String> result = new HashMap<String,String>();
		result.put("card1", hand.getBoard().getFlop1().toString());
		result.put("card2", hand.getBoard().getFlop2().toString());
		result.put("card3", hand.getBoard().getFlop3().toString());
		return result;
	}
	
	/**
	 * Deal the turn  for the hand. This should be called when the flop actions are complete
	 * and the players are ready for the turn card to be dealt.
	 * @param handId unique ID for the hand to receive the turn card.
	 * @return Map represented as a JSON String for the turn card, labeled as card4.
	 * Example: {"card4":"Xx"}
	 */
	@RequestMapping("/turn")
	public @ResponseBody Map<String,String> turn(@RequestParam long handId){
		HandEntity hand = handService.getHandById(handId);
		hand = handService.turn(hand);
		return Collections.singletonMap("card4", hand.getBoard().getTurn().toString());
	}
	
	/**
	 * Deal the river card for the hand. This should be called when the turn action is complete
	 * and the players are ready for the river card to be dealt.
	 * @param handId Unique ID for the hand to receive the river card
	 * @return Map represented as a JSON String for the river card, labeled as card5. 
	 * Example: {"card5":"Xx"}
	 */
	@RequestMapping("/river")
	public @ResponseBody Map<String,String> river(@RequestParam long handId){
		HandEntity hand = handService.getHandById(handId);
		hand = handService.river(hand);
		return Collections.singletonMap("card5", hand.getBoard().getRiver().toString());
	}
	
	/**
	 * End the hand. This completes all actions that can be done on the hand.  The winners
	 * are determined and the chips given to the appropriate players.  This will detach
	 * the hand from the game, and no more actions may be taken on this hand.
	 * @param handId Unique ID for the hand to be ended
	 * @return Map represented as a JSON String determining if the action was successful. 
	 * Example: {"success":true}
	 */
	@RequestMapping("/endhand")
	public @ResponseBody Map<String,Boolean> endHand(@RequestParam long handId){
		HandEntity hand = handService.getHandById(handId);
		handService.endHand(hand);
		return Collections.singletonMap("success", true);
	}
	
	/**
	 * Sometimes it is nice to know that everything is working
	 * @return {"success":true}
	 */
	@RequestMapping("/ping")
	public @ResponseBody Map<String,Boolean> pingServer(){
		return Collections.singletonMap("success", true);
	}
}
