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
package com.hyphenated.card.domain;

/**
 * 
 * Data structure for Blind Level.  This enum has all possible Blind Levels
 * that can occur for any game type.
 * 
 * @author jacobhyphenated
 */
public enum BlindLevel {

	BLIND_10_20(10,20),
	BLIND_15_30(15,30),
	BLIND_20_40(20,40),
	BLIND_25_50(25,50),
	BLIND_30_60(30,60),
	BLIND_40_80(40,80),
	BLIND_50_100(50,100),
	BLIND_60_120(60,120),
	BLIND_75_150(75,150),
	BLIND_80_160(80,160),
	BLIND_100_200(100,200),
	BLIND_120_240(120,240),
	BLIND_150_300(150,300),
	BLIND_200_400(200,400),
	BLIND_250_500(250,500),
	BLIND_300_600(300,600),
	BLIND_400_800(400,800),
	BLIND_500_1000(500,1000),
	BLIND_600_1200(600,1200),
	BLIND_700_1400(700,1400),
	BLIND_800_1600(800,1600),
	BLIND_900_1800(900,1800),
	BLIND_1000_2000(1000,2000),
	BLIND_1200_2400(1200,2400),
	BLIND_1500_3000(1500,3000),
	BLIND_1600_3200(1600,3200),
	BLIND_1800_3600(1800,3600),
	BLIND_2000_4000(2000,4000),
	BLIND_2500_5000(2500,5000),
	BLIND_3000_6000(3000,6000),
	BLIND_4000_8000(4000,8000),
	BLIND_5000_10K(5000,10000),
	BLIND_6000_12K(6000,12000),
	BLIND_7000_14K(7000,14000),
	BLIND_8000_16K(8000,16000),
	BLIND_9000_18K(9000,18000),
	BLIND_10K_20K(10000,20000),
	BLIND_12K_24K(12000,24000),
	BLIND_14K_28K(14000,28000),
	BLIND_16K_32K(16000,32000),
	BLIND_18K_36K(18000,36000),
	BLIND_20K_40K(20000,40000),
	BLIND_25K_50K(25000,50000);
	
	private int smallBlind;
	private int bigBlind;
	
	private BlindLevel(int smallBlind, int bigBlind){
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
	}
	
	public int getSmallBlind(){
		return smallBlind;
	}
	
	public int getBigBlind(){
		return bigBlind;
	}
}
