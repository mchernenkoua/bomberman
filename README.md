#Bomberman
==========

Working (but not completely finished) example of my code
The last stable version is available by the link http://bomber.herokuapp.com/

The tasks description:
----------------------

	Create a game like the Bomberman. 
	Games simulation have to be on server side totally.
	The Game is going with a player and three bots.

    Rules:
	1. The Map

		After the game start it have to generate the classical games field with 13x11 size according to the Dynablasters principle - 
			cells with both non-pair indexes have to be filled with non-destroyable blocks, players placed on corners ( 
			one real and three bots). All others cells have to be randomly filled with destroyable blocks.

	2. The Player (and Bots) can:
		
		leabe a bomb
		move on free cells
		
	3. Maximum players speed - 4 cells/second
	4. Bombs detonation radius - 3 cells on vertical and horisontal
	5. Bomd have to detonate in 3 seconds after setting up
	6. One player can set only one bomb at one time, but every 30 seconds it count have to increased on 1
		
Realization description
-----------------------
	
	The way of exchange data between a client and a server - is WebSockets
	A Server expects from a clien commands as such json object

{
  "gameId":"1",        - game ID (it is not planning to realize a multiplayer mode for now, so a server uses the playerId as the gameId)
  "playerId":"1",      - player ID (now the ID of opened session uses as the player ID, but it is understandable that it is not very good idea, 
							so it is need to rewrite it - make a generation separate ID when connect is open)
					   - it is allowed to ignore this fields, they are filling automatily from session params
					   
  "command":"START",   - The command to a server. There is description below.
  "message":"msg"      - Some message... Does not use now...
}
    
	Commands to the game server

		START
		QUIT
		GETMAP
		ACT_LEFT
		ACT_RIGHT
		ACT_UP
		ACT_DOWN
		ACT_BOMB

	After every game states changing (but not often then 250 ms), the server sends to a client json with "description" of changed points
	In answer to the GETMAP command, the server sends to a client json with description of all poins on the Map.

	The description of the "point description" - is is one json-object with field "objects". 
	The value of this field - is an array of json-object with two fields:
		“point”- poin of the games Map (object with two field "X" and "Y" - maps coordinates. The top left corner - is point (0, 0))
		“objects” - an array of "games objects", that plased on the poin
	The Game object has such fields:
    
		type  - there are a description below
		state - there are a description below

    Objects with type "Bomb" and "Fire" have addiditional field
 
		owner - (players (or bots) id, wich setted up it)

	Objects with type "Player" and "AIPlayer" have addiditional field
 
		id
		score
		bombCount

	An example of json point description

		{
		  "objects": [
			{
			  "point": {
				"x": 0,
				"y": 0
			  },
			  "objects": [
				{
				  "type": "Player",
				  "state": "LIVE",
				  "id": "1",
				  "bombCount": 2,
				  "score": 0
				}
			  ]
		},
			{
			  "point": {
				"x": 0,
				"y": 2
			  },
			  "objects": [
				{
				  "type": "Barrier",
				  "state": "LIVE”
				}
			  ]
			},
			
			{
			  "point": {
				"x": 9,
				"y": 1
				  },
			  "objects": [
				{
				  "type": "Wall",
				  "state": "LIVE"
				}
			  ]
			}
		]
		}




    Games objects (field "type"):

		Barrier 	destroyable wall
		Bomb    	bomb
		Fire       	detonated bomb
		Player 		player
		AIPlayer 	bot
		Wall        non-destroyable wall

	All objects gave "state" with possible values "LIVE", "DEAD" or "REMOVE"	
		Value "REMOVE" - is services value, so objects with that state have not to get on a client-side
			
		Objects, that could have the “DEAD” state:
			Bomb - in this state it exist only 0.5 second, after that it destroing and File objects are upspringing on the bombs point 
					(on the bombs point and on "bombs damage" points - 3 cells on vertical and horisontal)
			Player (or AIPlayer) - in this players state all move and bomb setting commands does not processed and after 3 seconds
					state changing to the "LIVE" value with re-initialization Players params.

