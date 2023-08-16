# TicTac Backend
Tic Tac Toe Game Backend Documentation

## Player.kt Class
This is an abstract class that defines the general properties of a player. 
### Variables
```KOTLIN
playerName     : String
playerID       : Long
playerIcon     : Pair<Int, String> // resource and description
playerAvatar   : Pair<Int, String>
```
### Abstract methods
```KOTLIN
toString() : String
copy() : Player
```
## HumanPlayer.kt
Implementation of abstract class Player

*Note: There will eventually be an AIPlayer.kt that is an implementation of
Player with extended methods for deciding where to place the puck*

## Board.kt Class
This class handles the creation and modification of a game board, and
testing the board for win conditions.

### Constructor
```KOTLIN
Board(width = 3, height = 3, minimumWin = 3)
```
Default inputs: `width = 3`, `height = 3`, `minimumWin = 3`.
boardState and boardHistory can be set in the constructor, however, they are
initialised to blank (null) arrays, and do not need to be explicitly set. 

### Boardstate typealias
The BoardState type is a nullable 2D array of players.
```KOTLIN
typealias BoardState = Array<Array<Player?>>
```

### Accessors
```KOTLIN
getBoardHistory() : Stack<BoardState>
```
```KOTLIN
getBoardState() : BoardState
```

### Placing a Puck
```KOTLIN
placePuck(player : Player, x : Int, y : Int) : Boolean
```
Places a player object in the 2D `boardState` array, and adds the new `boardState`
to the `boardHistory` stack. 
If the puck position is already occupied, it returns `false`, if the position
is out of bounds, it returns `false`.  Otherwise it returns `true`

### Undo Previous Move
```KOTLIN
undoPreviousMove()
```
This methods removes the latest addition from the `boardHistory` stack, and sets
the `boardState` to the top of the `boardHistory` stack.

### Clearning the game board
```KOTLIN
clearGameBoard()
```
This method sets `boardState` to `null`, and empties the `boardHistory` stack.

### Searching for win condition
```KOTLIN
searchWinCondition(currentPlayer : Player) : Pair<String, Boolean>
```
This method is longer and more complex as it must find all "strings" of pucks 
from the player `currentPlayer` that constitute a win. 
To do this, the algorithm creates three matricies: in this example, we will 
assume the boardsize is 3 by 3 and the win condition is length 3.
The `convolution matirices` are as follows. 
```
1 , 1 1 1 , 1 0 0 , 0 0 1
1           0 1 0   0 1 0
1           0 0 1   1 0 0
```
The algorithm then creates a new blank board of Integer `0`s, and places a `1` in 
all squares that contain 'currentPlayer' on the board.  A 2D convolution is performed
using all four matricies. 
If there exists a `result[any y][any x] == minimumWin` in the convolution output, 
a win condition has been found. 

The output pair contains the win configuration: `"Horisontal", "Veritcal", "Diagonal"`,
and a boolean indicating that a win has occured. 

### Private methods
#### 2D convolution
```KOTLIN
Array<Array<Int>>.convolution2D(operand: Array<Array<Int>>) : Array<Array<Int>>
```
#### Deep Copy
```KOTLIN
Array<Array<Player?>>.deepCopy() : Array<Array<Player?>>
```


