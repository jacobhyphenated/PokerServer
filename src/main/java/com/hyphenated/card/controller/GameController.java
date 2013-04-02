package com.hyphenated.card.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyphenated.card.domain.CommonTournamentFormats;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameStructure;
import com.hyphenated.card.domain.GameType;
import com.hyphenated.card.service.GameService;
import com.hyphenated.card.service.PokerHandService;

@Controller
public class GameController {

	@Autowired
	private GameService gameService;
	
	@Autowired
	private PokerHandService handService;
	
	@RequestMapping("/structures")
	public ModelAndView getGameStructures(){
		//TODO
		return null;
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
	 * @return {"gameId":xxxx}
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
	
	@RequestMapping("/gamestatus")
	public ModelAndView getGameStatus(@RequestParam long gameId){
		//TODO
		return null;
	}
	
	@RequestMapping("/start/game")
	public ModelAndView startGame(@RequestParam long gameId){
		//TODO
		return null;
	}
	
	@RequestMapping("/start/hand")
	public ModelAndView getGameStatus(@RequestParam long gameId, @RequestParam long handId){
		//TODO
		return null;
	}
	
	@RequestMapping("/flop")
	public ModelAndView flop(@RequestParam long gameId, @RequestParam long handId){
		//TODO
		return null;
	}
	
	@RequestMapping("/turn")
	public ModelAndView turn(@RequestParam long gameId, @RequestParam long handId){
		//TODO
		return null;
	}
	
	@RequestMapping("/river")
	public ModelAndView river(@RequestParam long gameId, @RequestParam long handId){
		//TODO
		return null;
	}
	
	@RequestMapping("/endhand")
	public ModelAndView endHand(@RequestParam long gameId, @RequestParam long handId){
		//TODO
		return null;
	}
}
