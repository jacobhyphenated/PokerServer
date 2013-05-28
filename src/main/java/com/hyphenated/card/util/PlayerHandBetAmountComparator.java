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
package com.hyphenated.card.util;

import java.util.Comparator;

import com.hyphenated.card.domain.PlayerHand;

/**
 * Custom Comparator function to order players based on how much they have bet in a given hand.
 * 
 * The player who has bet the least should be first in the list, last should be the player who bet most.
 * 
 * @author jacobhyphenated
 */
public class PlayerHandBetAmountComparator implements Comparator<PlayerHand> {

	@Override
	public int compare(PlayerHand o1, PlayerHand o2) {
		if(o1.getBetAmount() < o2.getBetAmount()){
			return -1;
		}
		else if(o1.getBetAmount() == o2.getBetAmount()){
			return 0;
		}
		else{
			return 1;
		}
	}

}
