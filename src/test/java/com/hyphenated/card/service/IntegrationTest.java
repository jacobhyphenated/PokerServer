package com.hyphenated.card.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyphenated.card.AbstractSpringTest;

public class IntegrationTest extends AbstractSpringTest {
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private PlayerActionService playerActionService;
	
	@Autowired
	private PokerHandService handService;

	@Test
	public void testGameStart(){
		
	}
}