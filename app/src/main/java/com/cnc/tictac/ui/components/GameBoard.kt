package com.cnc.tictac.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cnc.tictac.viewmodel.TicTacEvent
import com.cnc.tictac.viewmodel.TicTacViewModel
import com.cnc.tictac.R.drawable as images

/* GameBoard
 *
 * Knows uses:
 *  - GameScreen: Displays game board
 *
 * REQUIRED PARAMS
 * @param[modifier] LazyVerticalGrid modifier
 * @param[isGameActive] true if game is ongoing (default = true)
 * @param[board] 1d array of strings containing content for each cell of the board
 * @param[boardSize] number of cells in 1 row of the board i.e. 3/4/5
 * @param[winIndices] 1d array of Boolean where true = part of the win condition
 *
 * SAMPLE board (3x3)
 * [row 1       ][row 2     ][row 3      ]
 * ["", "o", "x", "", "", "", "", "", "x"]
 *
 * SAMPLE winIndices (diagonal win)
 * [true, false, false, false, true, false, false, false, true]
 *
 * Matrix view of winIndices above:
 * [    [true, false, false,],
 *      [false, true, false],
 *      [false, false, true]]
 */
@Composable
fun GameBoard (
    modifier: Modifier = Modifier,
    cellModifier: Modifier = Modifier,
    viewModel: TicTacViewModel,
    isContainerNarrow: Boolean = true, // Narrow means height > width
) {
    var boardSize: Int = viewModel.getBoardSize()
//    var board: Array<String> = viewModel.boardState
    var winIndices: Array<Boolean> = viewModel.winIndices

    if (isContainerNarrow) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(count = boardSize),
            content = {
                items(boardSize * boardSize) { i ->
                    BoardCell(
                        modifier = cellModifier.fillMaxSize(),
                        viewModel = viewModel,
                        win = if (winIndices.isNotEmpty()) winIndices[i] else false,
                        content = viewModel.boardState[i],
                        position = i
                    )
                }
            }
        )
    } else {

        Column(modifier = modifier) {
            for (row in 0..boardSize-1) {
                // Generate row same way as lazy grid.
                Row(modifier = Modifier.fillMaxHeight().weight(1f)) {
                    for (col in 0..boardSize-1) {
                        var i = boardSize*row+col
                        BoardCell(
                            modifier = cellModifier.fillMaxHeight(),
                            viewModel = viewModel,
                            win = if (winIndices.isNotEmpty()) winIndices[i] else false,
                            content = viewModel.boardState[i],
                            position = i,
                            )
                    }
                }
            }
        }
    }
}

@Composable
fun BoardCell (
    modifier: Modifier = Modifier,
    viewModel: TicTacViewModel,
    win: Boolean = false,
    content: String,
    position: Int
) {
    var borderColor : Color
    var bgColor : Color
    var contentColor : Color

    if (viewModel.gameActive) {
        borderColor = if (content != "") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outlineVariant
        bgColor = MaterialTheme.colorScheme.primary
        contentColor = if (content != "") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
    } else {
        borderColor = if (win) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline
        bgColor = if (win) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
        contentColor = if (win) MaterialTheme.colorScheme.onSecondary else borderColor
    }

    Box (
        modifier = modifier
            .clip(CircleShape)
            .border(1.dp, borderColor, CircleShape)
            .background(bgColor)
            .aspectRatio(1f)
            .padding(8.dp)
            .clickable { viewModel.onEvent(TicTacEvent.MarkerPlaced(position)) },
        contentAlignment = Alignment.Center
    ) {
        if (content != " ") {
            if (content == "o") {
                Avatar(
                    avatarResourceId = if (win) images.marker_o_win else images.marker_o_default,
                    onPrimaryColour = win,
                    color = contentColor,
                    imageModifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                )
            } else {
                Avatar(
                    avatarResourceId = if (win) images.marker_x_win else images.marker_x_default,
                    onPrimaryColour = win,
                    imageModifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    color = contentColor,
                )
            }
        } else {
            Text(text = content)
        }
    }
}