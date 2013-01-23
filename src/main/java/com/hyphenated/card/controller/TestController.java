package com.hyphenated.card.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hyphenated.card.domain.BoardEntity;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameType;
import com.hyphenated.card.domain.HandEntity;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.domain.PlayerHand;
import com.hyphenated.card.holder.Board;
import com.hyphenated.card.service.GameService;
import com.hyphenated.card.service.PokerHandService;

@Controller
public class TestController {
	
	@Autowired
	private PokerHandService pokerHandService;
	
	@Autowired
	private GameService gameService;
	
	@RequestMapping("/pokergame")
	public ModelAndView createGame(){
		Game game = new Game();
		game.setGameType(GameType.TOURNAMENT);
		game.setPlayersRemaining(0);
		game.setName("Test Game 1");
		game.setStarted(false);
		game = gameService.saveGame(game);
		
		Player p1 = new Player();
		p1.setName("Test 1");
		p1.setChips(1500);
		p1.setGamePosition(1);
		p1 = gameService.addNewPlayerToGame(game, p1);
		game = p1.getGame();
		
		Player p2 = new Player();
		p2.setName("Test 2");
		p2.setChips(1500);
		p2.setGamePosition(2);
		p2 = gameService.addNewPlayerToGame(game, p2);
		
		return new ModelAndView("board", "board", "Created game id: " + game.getId() + " and 2 players: " + p1.getId() + ", " + p2.getId() );
	}
	
	@RequestMapping("/pokergame/get")
	public ModelAndView getGame(@RequestParam long gameId){
		Game game = gameService.getGameById(gameId, true);
		String out = "Game id = " + game.getId();
		for(Player p : game.getPlayers()){
			out += "<br /><br />Player: " + p.getName() + "(" + p.getId() + ")";
		}
		if(game.getCurrentHand() != null){
			out += "<br /><br />current hand: " + game.getCurrentHand().getId();
		}
		return new ModelAndView("board", "board", out);
	}
	
	@RequestMapping("/pokergame/hand")
	public ModelAndView startHand(@RequestParam long gameId){
		Game game = gameService.getGameById(gameId, true);
		HandEntity hand = pokerHandService.startNewHand(game);
		ModelAndView mv = new ModelAndView("board");
		mv.addObject("board", "Hand Created: " + hand.getId() + "<br /><br /> Number of players: " 
				+hand.getPlayers().size());
		String boardCards = Arrays.toString(hand.getBoard().getBoardCards().toArray());
		mv.addObject("boardid", "Board:" + boardCards);
		return mv;
	}
	
	@RequestMapping("pokergame/hand/get")
	public ModelAndView getHand(@RequestParam long handId){
		HandEntity hand = pokerHandService.getHandById(handId);
		ModelAndView mv = new ModelAndView("board");
		mv.addObject("board", "Hand: " + hand.getId() + "<br /><br /> Number of players: " 
				+hand.getPlayers().size());
		String boardCards = Arrays.toString(hand.getBoard().getBoardCards().toArray());
		mv.addObject("boardid", "Board:" + boardCards);
		return mv;
	}
	
	@RequestMapping("pokergame/hand/flop")
	public ModelAndView handFlop(@RequestParam long handId){
		HandEntity hand = pokerHandService.getHandById(handId);
		hand = pokerHandService.flop(hand);
		ModelAndView mv = new ModelAndView("board");
		mv.addObject("board", "Hand: " + hand.getId() + "<br /><br /> Number of players: " 
				+hand.getPlayers().size());
		String boardCards = Arrays.toString(hand.getBoard().getBoardCards().toArray());
		mv.addObject("boardid", "Board:" + boardCards);
		return mv;
	}
	
	@RequestMapping("pokergame/hand/turn")
	public ModelAndView handTurn(@RequestParam long handId){
		HandEntity hand = pokerHandService.getHandById(handId);
		hand = pokerHandService.turn(hand);
		ModelAndView mv = new ModelAndView("board");
		mv.addObject("board", "Hand: " + hand.getId() + "<br /><br /> Number of players: " 
				+hand.getPlayers().size());
		String boardCards = Arrays.toString(hand.getBoard().getBoardCards().toArray());
		mv.addObject("boardid", "Board:" + boardCards);
		return mv;
	}
	
	@RequestMapping("pokergame/hand/river")
	public ModelAndView handRiver(@RequestParam long handId){
		HandEntity hand = pokerHandService.getHandById(handId);
		hand = pokerHandService.river(hand);
		ModelAndView mv = new ModelAndView("board");
		mv.addObject("board", "Hand: " + hand.getId() + "<br /><br /> Number of players: " 
				+hand.getPlayers().size());
		String boardCards = Arrays.toString(hand.getBoard().getBoardCards().toArray());
		mv.addObject("boardid", "Board:" + boardCards);
		return mv;
	}
	
	@RequestMapping("pokergame/hand/showdown")
	public ModelAndView handShowdown(@RequestParam long handId){
		HandEntity hand = pokerHandService.getHandById(handId);
		String out = "";
		BoardEntity b = hand.getBoard();
		Board board  = new Board(b.getFlop1(),b.getFlop2(),b.getFlop3(),b.getTurn(),b.getRiver());
		for(PlayerHand ph : hand.getPlayers()){
			out += ph.getPlayer().getName() + ": " + ph.getHand().toString() + "<br /><br />";
		}
		
		out += board.toString();
		ModelAndView mv = new ModelAndView("board");
		mv.addObject("board", "Hand: " + hand.getId());
		mv.addObject("boardid", out);
		return mv;
	}
}
