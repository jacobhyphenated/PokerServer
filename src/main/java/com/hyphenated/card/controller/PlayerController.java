package com.hyphenated.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hyphenated.card.service.GameService;
import com.hyphenated.card.service.PlayerActionService;

@Controller
public class PlayerController {
	
	@Autowired
	private PlayerActionService playerActionService;
	
	@Autowired
	private GameService gameService;
	
	@RequestMapping("/games")
	public ModelAndView getGames(){
		//TODO
		return null;
	}
	
	@RequestMapping("/join")
	public ModelAndView joinGame(@RequestParam long gameId, @RequestParam String playerName){
		//TODO
		return null;
	}
	
	@RequestMapping("/status")
	public ModelAndView getPlayerStatus(@RequestParam long gameId, @RequestParam long playerId){
		//TODO
		return null;
	}
	
	@RequestMapping("/fold")
	public ModelAndView fold(@RequestParam long gameId, @RequestParam long playerId){
		//TODO
		return null;
	}
	
	@RequestMapping("/call")
	public ModelAndView call(@RequestParam long gameId, @RequestParam long playerId){
		//TODO
		return null;
	}
	
	@RequestMapping("/check")
	public ModelAndView check(@RequestParam long gameId, @RequestParam long playerId){
		//TODO
		return null;
	}
	
	@RequestMapping("/bet")
	public ModelAndView bet(@RequestParam long gameId, @RequestParam long playerId, @RequestParam int betAmount){
		//TODO
		return null;
	}

}
