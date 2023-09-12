package com.cnc.tictac.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
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
    isContainerNarrow: Boolean = true, // Narrow means height > width
    isGameActive: Boolean = true,
    boardSize: Int,
    board: Array<String>, // 3, 4, 5 only
    winIndices: Array<Boolean>? = null,
) {
    if (isContainerNarrow) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(count = boardSize),
            content = {
                items(boardSize * boardSize) { i ->
                    BoardCell(
                        modifier = cellModifier.fillMaxSize(),
                        isGameActive = isGameActive,
                        win = if (winIndices != null) winIndices[i] else false,
                        content = board[i]
                    )
                }
            }
        )
    } else {
        // Set content to display (needs transformation depending on layout
        var boardContent = Array(boardSize*boardSize) {_ -> ""}

        for (row in 0..boardSize-1) {
            for (col in 0..boardSize-1) {
                boardContent[col*boardSize+row] = "${board[row*boardSize + col]}"
            }
        }

        LazyHorizontalGrid(
            modifier = modifier,
            rows = GridCells.Fixed(count = boardSize),
            content = {
                items(boardSize * boardSize) { i ->
                    val row = 1
                    val col = 1

                    BoardCell(
                        modifier = cellModifier.fillMaxSize(),
                        isGameActive = isGameActive,
                        win = if (winIndices != null) winIndices[i] else false,
                        content = boardContent[i]
                    )
                }
            }
        )
    }
}

@Composable
fun BoardCell (
    modifier: Modifier = Modifier,
    isGameActive: Boolean = true,
    win: Boolean = false,
    content: String,
) {
    var borderColor : Color
    var bgColor : Color
    var contentColor : Color

    if (isGameActive) {
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
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        if (content != "") {
            if (content == "o") {
//                AvatarBlock(
//                    avatarResourceId = if (win) images.marker_x_win else images.marker_x_default,
//                    isCircle = true,
//                    isFilled = win,
//                    boxModifier = modifier
//                        .clip(CircleShape)
//                        .border(1.dp, borderColor, CircleShape)
//                        .background(bgColor)
//                        .aspectRatio(1f),
//                )
                Avatar(
                    avatarResourceId = if (win) images.marker_o_win else images.marker_o_default,
                    onPrimaryColour = !win,
                    color = contentColor,
                )
            } else {
//                AvatarBlock(
//                    avatarResourceId = if (win) images.marker_x_win else images.marker_x_default,
//                    isCircle = true,
//                    isFilled = win,
//                    boxModifier = modifier
//                        .clip(CircleShape)
//                        .border(1.dp, borderColor, CircleShape)
//                        .background(bgColor)
//                        .aspectRatio(1f),
//                )
                Avatar(
                    avatarResourceId = if (win) images.marker_x_win else images.marker_x_default,
                    onPrimaryColour = !win,
                    color = contentColor,
                )
            }
        } else {
            Text(text = content)
        }
    }
}