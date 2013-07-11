Poker Server
=============

This project is a server designed to run a friendly game of Texas Holdem Poker.  This is the server only, not the front-end or User Interface.  The server exposes a REST API for client side functionality.

What This Project Is
--------------------
This is a server program designed to facilitate a home game of Texas Holdem Poker. Some features:

* Dealing every hand and performing a proper random suffle of the deck
* Tracking the dealer button, small blind, and big blind
* Tracking chip stacks for every player
* Tracking each player's hole cards as well as the community cards
* Tracking which player is currently supposed to act as well as what actions are legal for that player
* Handling the evaluation of the poker hands, determining who wins and loses, handling split pots, and handling side pots when there are multiple all in players
* Handling the blind structure and timers

This software is for use with friendly games of poker, for home games where everyone is in the same room.  This was my thought when I designed it, and my thinking when I wrote it. 

What This Project Is Not
-------------------------
* This is not professional online poker software
* This is not designed to be used in highly competative situations with large amounts of money at risk
* There is no guarantee of security, and I'm not promising anything. I doubt I can stress this enough
* This is not an infrustructre project. This is just the java server. To use it, you can set it up however you like, with apache/tomcat or jboss or whatever, but it does not come pre-configured. 
* This is not designed to replace the normal interaction between players in a poker game, it is designed to augment it.

Some Background
----------------
I created this project for a couple of reasons. For one thing, it sounded fun, and was a good way to keep my Java/Spring skillset up to date and sharp. But also, I felt that it might be a cool and useful tool for running poker games.

Generally when I play a friendly home game of poker, there are a couple of things that can have negative impacts on the experience of the game. Sometimes people are new to the game, and the inexperience can slow down the pace of play. Sometimes people are not paying close attention, so they act out of turn, or take too much time to act. Sometimes people make mistakes, flip their cards up, or misdeal and the hand has to start over. Lastly, dealing takes a lot of time, and not everyone is very good at shuffling.

None of these issues are a big deal. It's a friendly game; no need to be stressed out about the little things. But a program like this might go a long way to making the game run more smoothly by eliminating some of these mistakes and streamlining the gameplay.

I also invision this as a good learning tool. It is easier to learn the game without being distracted by chipstacks, dealing, maintaining the blinds, moving the dealer button, and remembering whose turn it is.

A lot of these ideas were floating around in my mind when I started development on this project, so this is the background info that influenced a lot of my design and architecture decisions.


Technical Things
-------------------
This is a java server project using Java 1.6 with Spring(3.2) for IOC and Hibernate(4.1) for ORM and Maven for dependency management/deployment. I used Spring for unit tests as well.  

The database is MySQL. 

The API was build using Spring MVC and Jackson for JSON marshalling. 

The algorithm used for Texas Holdem hand evaluation uses a precomputed table of values. This is very memory intensive and will overflow some of the default java heap space allocations, specifically eclipse servers and JUnit testing. I find that adding the VM Arguments: _-Xms512m -Xmx1524m_ was more than sufficient to solve the problem.

Setup and Installation
----------------------
Spinning up this server will largely depend on what your setup is. I had originally intended to include a war file in the repository, but it was too big and made pull/push times impractical. To get a working war file, you will have to set up and build the project.

Setting up the development will depend on your IDE and various plugins. I used eclipse, so my instructions will be mostly centered around that.

This project was built on Maven, so you will need maven properly installed to pull the libraries and build the war file.

1. Clone the repository
 * If you have a git eclipse plugin, use that to clone the repository and import the project into eclipse
 * If you do not have a plugin, clone the repository then import the project as an existing project
2. Run maven.  Use maven build or maven install or an eclipse plugin. This will pull all the jar files needed for the project.
* Set up the database. I used mysql and you will have to make some configuration changes if you are wanting to use a different database.
 * There is a mysql dump of the database structure in the scripts folder
 * The file hibernate-context.xml in the WEB-INF/spring/ folder contains the configuration for the database (username,password,etc).
* Deploy the eclipse project to an eclipse server
 * If you don't have an eclipse server set up, just create a new server (ex. Tomcat V7.0) and deploy the project to that.
* Change the server vm args (Overview --> open launch configurations --> arguments --> VM arguments), adding -Xms512m -Xmx1524m
* Publish and start the server.
* There you go, should be working.

I get that this might be a bit much, and I have obviously not spent a lot of time on the configuraion side of things, so I welcome pull requests that make the initial set up easier.


Outstanding issues, Quality, Polish
--------------------------------
This is still a work in progress. What is currently here on github should work successfully with no major bugs, but there are some known issues that need to be worked on, or issues that have simple work-arounds and do not detract too much from the overall process.

The issues that I know about I have added to the Issues Tab. You can probably find a few //TODO messages throughout my code pointing out things that need additional work as well.

Because this is still a work in progress, I will likely be making periodic changes. The plan is to make sure any functionality change is backward compatible with the API, but I cannot guarantee that will happen with 100% certainty.  However, I do plan on tagging all major releases of functionality, so it should be easy to switch back to a working version if new changes create new issues.


API
======
The API consists of two components. The game component, and the player compomant. The way I envision it, there will be a device (say for example, an iPad) that sits in the center of the table and displays the blind time, the community cards, and other game information, that is the Game Controller.  Each player will have their own device, and will use the player section of the API.

Requests to the server are made using http request parameters. The response is properly formatted JSON.

###Current Version: 0.5

**New in Version 0.5**: The Player Status API call now returns the small and big blind, giving the player client easier access to the blind information. Version 0.5 is completely backwards compatible with Version 0.4.

**New in Version 0.4**: Sitting out of the game, and sitting back in.  There are new API calls that allow the game controller to "sit out" an idle player.  This player will be skipped in the game action order. In a tournament, the player will still post blinds, but will fold to any bet or raise.  A second API call is added to the player controller, allowing that player to sit back in.

Version 0.4 is backwards compatible with 0.1 as only new API calls were added, and no other API calls changed. The exception is Player Status. There is now a new status called ```SIT_OUT_GAME``` that represents a player who has been sat out.  If a client targeting an earlier version of the API cannot handle the new status, then it will not be able to use a Server running 0.4.

**New in Version 0.3**: UUID identifiers for player ID.  The playerId API parameter is now a longer more unique string, making the id next to impossible to guess. 

Version 0.3 is backward compatible through 0.1. The catch is that the playerId is now a guaranteed String type, so any implementation that used an integer for playerId may no longer work.  The type was never specified as an integer in the previous API specifications, but an integer would have worked in previous versions; only String will work moving forward. 

**New in Version 0.2**: JSONP.  Simply add a parameter named callback to the query, with the value of the javascript function.

```http://your-url.com/ping?callback=test``` Results in ```test({"success":true"});```

Version 0.2 is completely backwards compatible with Version 0.1. All that was added was JSONP support.

Game Controller
---------------------

###List Structure (/structures)

Get a list of currently available structures for the poker game. These game structures represent the blind structure, the blind length, starting chip stacks, etc.

####_request_

No parameters

####_response_

* Array of Game Structures:
 * *name* - A unique identifier for the game structure type
 * *description* - a short description of structure and the game situation it was designed for
 * *startingChips* - the number of chips each player will start with
 * *blindLevels* - an array of blind levels, each value a string with the format BLIND_SB_BB
 * *blindLength* - integer representing the length of each blind level in minutes

###Create Game (/create)

Create a new game. The game will be put in an unstarted state until players join.

####_request_

* *gameName* - Name for the game.  There are no major restrictions on this value. It does not have to be unique
* *gameStructure* - The type of structure your game will follow.  This is a string that corresponsds to the _name_ field of the Game Structure object, like the type you get from the List Structures request

####_response_

* *gameId* - unique identifier for the game you created. Use this value to reference this particular game for future API calls.

###Game Status (/gamestatus)

Get the status of an existing game. Contains information about the current game state and the players involved in the game.

####_request_

* *gameId* - unique ID for the game

####_response_

* *gameStatus* - status representing the current state of the game. Possible values are:
 * NOT\_STARTED
 * SEATING
 * PREFLOP
 * FLOP
 * TURN
 * RIVER
 * END\_HAND
* *smallBlind* - current small blind (optional)
* *bigBlind* - current big blind (optional)
* *blindTime* - amount of time remaining in the current blind level, in milliseconds (optional)
* *pot* - number of chips in the pot for the current hand (optional)
* *board* - Array of cards, representing the community cards in the hand (optional)
 * Cards are represented by a 2 character string. The first character is value, the second is suit. [2-9,T,J,Q,K,A][c,d,h,s]
* *players* - Array of player objects
 * *name* - name of the player
 * *chips* - number of chips the player has remaining
 * *finishPosition* - if the player is out of the game, this is the position they finished in. This will be zero for players still in the game. 
 * *gamePosition* - the seating position.  This determines the order of action for every hand, and should determine the seating.  This is randomized at the start of a tournament.

###Start Game (/startgame)

Start the game. For tournaments, all players have joined and it is time to begin the game. Players should not be able to join tournaments after the game is started.

####_request_

* *gameId* - unique ID for the game to be started

####_response_

* *success* - true if the game was successfully placed in a started state. False otherwise.

###Start Hand (/starthand)

Starts a new hand.  This should be called to trigger the dealing of the hand.  After this, players will be assigned cards and the hand will go to preflop actions.

####_request_

* *gameId* - unique ID of the game where the hand is being dealt

####_response_

* *handId* - unique ID for the hand that was dealt.  Use this id to reference this particular hand.

###Deal Flop (/flop)

Deal the three flop community cards.

####_request_

* *handId* - unique ID for the hand where the flop is dealt

####_response_

* *card1* - first flop card
* *card2* - second flop card
* *card3* - third flop card

###Deal Turn (/turn)

Deal the turn card for the hand

####_request_

* *handId* - unique ID for the hand where the turn card is dealt

####_response_

* *card4* - turn card

###Deal Rivier (/river)

Deal the river card for the hand

####_request_

* *handId* - unique ID for the hand where the river card is dealt

####_response_

* *card5* - river card

###End Hand (/endhand)

Ends the current hand.  This method should be called after the river card and when all players have taken actions. This should also be called when all players except one have folded.

This ends the current hand, making further operations on the hand impossible. Chips are distributed from the pot to the winning player(s).

####_request_

* *handId* - unique ID for the hand to be ended

####_response_

* *success* - true/false

###Sit Out Current Player (/sitoutcurrent) [new in 0.4]

Sits the current player to act out of the game. This player's action will be skipped until the player is sat back into the game. The player's blinds will still be posted in a tournament, and the player will fold to any bet or raise.

####_request_

* *handId* - unique ID for the hand with the player that needs to be sat out.

####_response_

* *success* - true/false

###Ping (/ping)

Sometimes it is nice to know that your configuration is correct and everything is working.  This is a simple convenience method to see if you can hit the server.

####_request_

no request parameters

####_response_

* *success* - true

Player Controller
------------------

###Join Game (/join)

Join a game. If the game is a tournament, you can only join before the game is started.

####_request_

* *gameId* - unique ID of the game to join
* *playerName* - your name. This name will be the display name for your player.

####_response_

* *playerId* - unique ID of the player that was created.  Use this to reference the player in the future.

###Player Status (/status)

####_request_

* *gameId* - unique ID of the game
* *playerId* - unique ID of the player

####_response_

* *status* - current status of the player
 * NOT_STARTED - game is not started
 * SEATING - game is started but no hand is dealt.  Find a seat based on the player's gamePosition (see game status)
 * WAITING - waiting on other players to act
 * ALL_IN - player is all in and can take no more actions this hand
 * LOST_HAND - at the end of the hand, the player has lost at showdown
 * WON_HAND - at the end of the hand, the player has won at least part of the pot
 * POST_SB - the player has posted the small blind for this hand. This status will only be shown preflop
 * POST_BB - the player has posted the big blind for this hand. This status will only be shown preflop
 * ACTION_TO_CALL - The player is the current player to act and must call a bet to continue
 * ACTION_TO_CHECK - The player is the current player to act and has the option to check the action
 * SIT_OUT - The player is out of the current hand
 * SIT_OUT_GAME - The player has left the table and is sitting out of the game. The player could come back and resume later. [new in 0.4]
 * ELIMINATED - the player has been eliminated from the game
* *chips* - number of chips the player has remaining. This does not include chips already bet in the current hand.
* *card1* - first hole card (optional)
* *card2* - second hole card (optional)
* *amountBetRound* - the amount of chips the player has contributed to the pot in the current betting round (optional)
* *amountToCall* - If there is an outstanding bet, this is the number of chips the player must call to continue (optional)
* *smallBlind* - the current small blind (optional). [new in 0.5]
* *bigBlind* - the current big blind (optional). [new in 0.5]

###Fold (/fold)

The player folds their hand

####_request_

* *gameId* - unique ID for the game
* *playerId* - unique ID for the player

####_response_

* *success* - true if the player folds, false if the action is not allowed.


###Call (/call)

The player calls the current bet

####_request_

* *gameId* - unique ID for the game
* *playerId* - unique ID for the player

####_response_

* *success* - true if the player called the bet, false if the action is not allowed
* *chips* - the amount of chips the player has remaining after this action

###Check (/check)

The player checks their action

####_request_

* *gameId* - unique ID for the game
* *playerId* - unique ID for the player

####_response_

* *success* - true if the player checks, false if the action is not allowed

###Bet (/bet)

The player places a bet or makes a raise

####_request_

* *gameId* - unique ID for the game
* *playerId* - uniqueID for the player
* *betAmount* - the amount the player bets/raises
 * This value represents the amount bet in addition to what has already been bet in this round of action
 * For example: Player 1 bets 100, then Player 2 raises to 300 total. The betAmount field would be 200.
 * Example 2: in the previous example, Player 1 re-raises to 900 total, the betAmount is 600 (100 + 200 to call + 600 = 900)

####_response_
* *success* - true if the bet was placed, false if the action is not allowed
* *chips* - the amount of chips the player has remaining after this action

###Sit In (/sitin) [new in 0.4]

The player sits back into the game. If the player has been sat out for inactivity or because they were away from the table, this call will allow them to re-join the game.

####_request_

* *playerId* - unique ID for the player

####_response_

* *success* - true/false


Error Handling
-------------
If the URL is not a valid API endpoint, expect a 404.

If there is an error on the server side, you will get back JSON that describes the error. The 2 fields will be:

* *error* - A quick message about the error
* *errorDetails* - A more detailed message about what caused the error.

The status code will be a version of 400 error code that most correctly describes the error that took place (such as 400 or 403)

One example of a situation where you might expect the error json response would be if you tried to call the Turn method before the flop was dealt. This would give you an error message telling you that the opporation is not allowed given the current state of the hand. The HTTP Response code would be 403.


Security
==========
This app does not currently have any industry grade security and is not intended for use with any significant amount of real money at stake.

There are some obvious steps that need to be taken to make the app more secure, such as putting the poker server behind an apache server with SSL, securing the database, etc.

I point this out because I do not want anyone to think that they can just pull down this project and start putting real money at stake.

License
===========

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
