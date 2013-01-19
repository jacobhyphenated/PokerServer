package com.hyphenated.card.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hyphenated.card.Card;
import com.hyphenated.card.Deck;
import com.hyphenated.card.domain.BoardEntity;
import com.hyphenated.card.domain.Game;
import com.hyphenated.card.domain.GameType;
import com.hyphenated.card.domain.Player;
import com.hyphenated.card.eval.FSMHandRankEvaluatorFactory;
import com.hyphenated.card.eval.HandRank;
import com.hyphenated.card.eval.HandRankEvaluator;
import com.hyphenated.card.holder.Board;
import com.hyphenated.card.holder.Hand;
import com.hyphenated.card.service.GameService;
import com.hyphenated.card.service.PokerHandService;

@Controller
public class TestController {
	
	@Autowired
	private PokerHandService pokerHandService;
	
	@Autowired
	private GameService gameService;

	@RequestMapping("/test")
	public ModelAndView helloWorld(){
		String testMsg = "Hello World";
		return new ModelAndView("hello", "hand1", testMsg);
	}
	
	@RequestMapping("/testvar")
	public ModelAndView testies(@RequestParam("test") String testParam, 
			@RequestParam(value="test2",required=false) String test2){
		if(test2 != null){
			testParam = testParam + test2;
		}
		return new ModelAndView("hello", "hand1", testParam);
	}
	
	@RequestMapping("/pokertest")
	public ModelAndView testPoker(){
		Deck d = new Deck();
		Card card1 = d.dealCard();
		Card card2 = d.dealCard();
		Hand hand1 = new Hand(card1, card2);
		Hand hand2 = new Hand(d.dealCard(), d.dealCard());
		Board board = new Board(d.dealCard(), d.dealCard(), d.dealCard(), d.dealCard(), d.dealCard());
		HandRankEvaluator evaluator =  FSMHandRankEvaluatorFactory.create();
		HandRank rank1 = evaluator.evaluate(board, hand1);
		HandRank rank2 = evaluator.evaluate(board, hand2);
		
		ModelAndView out =  new ModelAndView("hello");
		out.addObject("hand1", hand1.toString());
		out.addObject("hand2", hand2.toString());
		out.addObject("board", board.toString());
		String result = null;
		int compare = rank1.compareTo(rank2);
		if(compare == 0){
			result = "Split!";
		}
		else if(compare > 0){
			result = "Hand 1 Wins";
		}
		else{
			result = "Hand 2 Wins";
		}
		out.addObject("result", result);
		return out;
	}
	
	@RequestMapping("/pokerboard")
	public ModelAndView boardTest(){
		BoardEntity board = new BoardEntity();
		Deck d = new Deck();
		board.setFlop1(d.dealCard());
		board.setFlop2(d.dealCard());
		board.setFlop3(d.dealCard());
		board = pokerHandService.saveBoard(board);
		String boardCards = Arrays.toString(board.getBoardCards().toArray());
		
		ModelAndView out =  new ModelAndView("board");
		out.addObject("board", boardCards);
		out.addObject("boardid", board.getId());
		return out;
	}
	
	@RequestMapping("/pokerboard/get")
	public ModelAndView boardTestFetch(@RequestParam(required=false) Long boardId){
		ModelAndView out =  new ModelAndView("board");
		if(boardId != null){
			BoardEntity board = pokerHandService.getBoard(boardId);
			if(board == null){
				out.addObject("board", "Epic fail");
				return out;
			}
			String boardCards = Arrays.toString(board.getBoardCards().toArray());
			
			out.addObject("board", boardCards);
			out.addObject("boardid", "Fetched board " + board.getId());			
		}else{
			List<BoardEntity> boards = pokerHandService.getAllBoards();
			out.addObject("board", "Number of Boards in DB");
			out.addObject("boardid", boards.size());
		}
		return out;
	}
	
	@RequestMapping("/pokerboard/turn")
	public ModelAndView turnCard(@RequestParam long boardId){
		BoardEntity board = pokerHandService.getBoard(boardId);
		board.setTurn(Card.ACE_OF_HEARTS);
		pokerHandService.saveBoard(board);
		String boardCards = Arrays.toString(board.getBoardCards().toArray());
		ModelAndView out = new ModelAndView("board");
		out.addObject("board", boardCards);
		out.addObject("boardid", board.getId());
		
		return out;
	}
	
	@RequestMapping("/pokerboard/delete")
	public ModelAndView deleteBoard(@RequestParam long boardId){
		BoardEntity board = pokerHandService.getBoard(boardId);
		pokerHandService.deleteBoard(board);
		String boardCards = Arrays.toString(board.getBoardCards().toArray());
		return new ModelAndView("board", "board", "Deleted board: " + boardCards);
	}
	
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
		return new ModelAndView("board", "board", out);
	}
}
