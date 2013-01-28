package com.hyphenated.card.controller;

import java.util.Arrays;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hyphenated.card.domain.BoardEntity;
import com.hyphenated.card.domain.CommonTournamentFormats;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameStructure;
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
		game.setName("Test Game and Structure");
		game.setStarted(false);
		GameStructure gs = new GameStructure();
		CommonTournamentFormats format = CommonTournamentFormats.TWO_HR_NINEPPL;
		gs.setBlindLength(format.getTimeInMinutes());
		gs.setBlindLevels(format.getBlindLevels());
		gs.setCurrentBlindLevel(format.getBlindLevels().get(0));
		gs.setStartingChips(format.getStartingChips());
		game.setGameStructure(gs);
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
		
		Player p3 = new Player();
		p3.setName("Test 3 - struc");
		p3.setChips(game.getGameStructure().getStartingChips());
		p3.setGamePosition(3);
		p3 = gameService.addNewPlayerToGame(game, p3);
		
		return new ModelAndView("board", "board", "Created game id: " + game.getId() + " and 3 players: " + p1.getId() + ", " + p2.getId() + ", " + p3.getId() );
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
	
	@RequestMapping("/pokergame/start")
	public ModelAndView startGame(@RequestParam long gameId){
		Game game = gameService.getGameById(gameId, false);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, game.getGameStructure().getBlindLength());
		game.getGameStructure().setCurrentBlindEndTime(cal.getTime());
		game = gameService.saveGame(game);
		return new ModelAndView("board", "board", "Blind level: " + game.getGameStructure().getCurrentBlindLevel().toString() + 
				", Ends: "+ game.getGameStructure().getCurrentBlindEndTime().toString());
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
	
	@RequestMapping("pokergame/hand/end")
	public ModelAndView endHand(@RequestParam long handId){
		HandEntity hand = pokerHandService.getHandById(handId);
		pokerHandService.endHand(hand);
		return new ModelAndView("board", "board", "ended hand " + hand.getId());
	}
}
