package com.hyphenated.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@RequestMapping("/create")
	public ModelAndView createGame(@RequestParam String gameName, @RequestParam String gameStucture){
		CommonTournamentFormats structure = CommonTournamentFormats.valueOf(gameStucture);
		Game game = new Game();
		game.setName(gameName);
		game.setGameType(GameType.TOURNAMENT); //Until Cash games are supported
		GameStructure gs = new GameStructure();
		gs.setBlindLength(structure.getTimeInMinutes());
		gs.setBlindLevels(structure.getBlindLevels());
		gs.setStartingChips(structure.getStartingChips());
		game.setGameStructure(gs);
		game = gameService.saveGame(game);
		//TODO
		return null;
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
