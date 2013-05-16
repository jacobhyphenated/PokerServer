package com.hyphenated.card.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyphenated.card.domain.CommonTournamentFormats;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameStatus;
import com.hyphenated.card.domain.GameStructure;
import com.hyphenated.card.domain.GameType;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.service.GameService;
import com.hyphenated.card.service.PokerHandService;
import com.hyphenated.card.util.GameUtil;

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
	 * The response will be a JSON Object containing two values, the status and a list of player JSON objects.
	 * @param gameId unique identifier for the game
	 * @return JSON Object of the format: {gameStatus:xxx,players:[{name:xxx,chips:xxx,finishPosition:xxx},...]}
	 */
	@RequestMapping("/gamestatus")
	public @ResponseBody Map<String, ? extends Object> getGameStatus(@RequestParam long gameId){
		Game game = gameService.getGameById(gameId, true);
		GameStatus gs = GameUtil.getGameStatus(game);
		Collection<Player> players = game.getPlayers();
		
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("gameStatus", gs);
		results.put("players", players);
		return results;
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
