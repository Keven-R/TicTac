# Tic Tac Game Driver Documentation
The game driver basically handles how Player objects interact with the game Board, and is the point of integration between 
the front and back end. 

## GameConfig class
```KOTLIN
data class GameConfig(
  val boardHeight     : Int = 3
  val boardWidth      : Int = 3
  val boardMinimumWin : Int = 3
)
```

## GameDriver class
### Constructor
```KITLIN
class GameDriver( config : GameConfig = GameConfig() ) { ... }
```

### Adding a player to the game
A new player is added to the player queue, player count is incremented. 
If the player input is null, false is returned. 
```KOTLIN
addPlayer( newPlayer : Player?) : Boolean
```

### Playing a move in the game driver. 
In this rather long function declaration, some logic is performed on the input. 
* If `x, y` are given, then the player is assumed not to be AI, and a move is made. 
* If no coorindates are given, then the function first checks to see if the next player 
in the queue is of the class type `AIPlayer : Player`.  
* if it is, the `generateRandomPlay`method is called to obtain the AI generated play coorindates. 
* this method then checks the win condition, and returns Pair<String, Boolean> much like the Board class. 
```KOTLIN
playMove( x : Int = if(this.playerQueue.peek()!! is AIPlayer) (this.playerQueue.peek()!! as AIPlayer).generateRandomPlay(this.board.getConstraints()).first else 0 ,
          y : Int = if(this.playerQueue.peek()!! is AIPlayer) (this.playerQueue.peek()!! as AIPlayer).generateRandomPlay(this.board.getConstraints()).second else 0 ,
        ) : Pair<String, Boolean>
```

### Who is playing?
This method simply returns the current player at the time. 
```KOTLIN
whoIsPlaying() : Player?
```
