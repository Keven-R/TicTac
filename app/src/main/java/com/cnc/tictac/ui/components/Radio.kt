package com.cnc.tictac.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cnc.tictac.viewmodel.TicTacViewModel

/* Radio
 * Used to enable a user to select 1 out of up to 3 options.
 *
 * Knows uses:
 *  - Game settings screen
 *
 * @param[title] Describes what the user is choosing
 * @param[numOptions] Number of options to choose from, 2-3
 * @param[labels] Labels for each option item
 * @param[selectedIndex] Index of the current selected item
 * @param[isDisabled] Array with length == numOptions. Each item = true if disabled, false if enabled.
 */
@Composable
fun Radio(
    modifier: Modifier = Modifier,
    title: String = "Select one: ",
    viewModel: TicTacViewModel, // Needed to process onClick
    onClick: () -> Unit,
    labels: Array<String> = arrayOf("1", "2"),
    isDisabled: Array<Boolean> = arrayOf(false, false, false),
    selectedIndex: Int = 0
) {
    // CONTAINER: for both title and radio choices
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TitleMedium(title)

        // CONTAINER: for all radio choices, 2-3
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterHorizontally
            )) {

            // Style radio button depending on if it's selected, disabled, or default
            for ((ind, currIndex) in labels.indices.withIndex()) {
                if (currIndex == selectedIndex) {
                    PrimaryButton(
                        label = labels[currIndex],
                        viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onClick = { /*DO NOTHING ON CLICK*/ }
                    )
                } else if (isDisabled[ind]) {
                    SecondaryButtonDisabled(
                        label = labels[currIndex],
                        viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onClick = { /*DO NOTHING ON CLICK*/ }
                    )
                } else {
                    SecondaryButton(
                        label = labels[currIndex],
                        viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
fun ImageGridSingleSelect (
    modifier: Modifier = Modifier,
    viewModel: TicTacViewModel,
    gridModifier: Modifier = Modifier,
    label: String = "Select one",
    imageIds: Array<Int>, // List of resource id's containing all images in the selection
    selectedIndex: Int = 0, // Index of currently selected image, defaults to 0 if none,
    isVerticalScroll: Boolean = true, // True for vertical scroll, false otherwise
    minItemSize: Dp = 140.dp, // 3 by default, use 2 for phones
    imagePadding: Int = 24, // Default value, make larger for bigger screens
    colSpacing: Dp = 8.dp,
    rowSpacing: Dp = 8.dp,
) {
    // CONTAINER: Contains both text field and label
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // ELEMENT: Label
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )

        // CONTAINER: Images to select from
        if (isVerticalScroll) {
            LazyVerticalGrid(
                modifier = gridModifier.fillMaxWidth(),
                columns = GridCells.Adaptive(minSize = minItemSize),
                horizontalArrangement = Arrangement.spacedBy(colSpacing),
                verticalArrangement = Arrangement.spacedBy(rowSpacing),
                userScrollEnabled = true,
                content = {
                    items(imageIds.count()) { i ->
                        ImageGridItem(
                            modifier = Modifier.fillMaxWidth(),
                            viewModel = viewModel,
                            avatarResourceId = imageIds[i],
                            isSelected = i == selectedIndex,
                            padding = imagePadding,
                            position = i
                        )
                    }
                }
            )
        } else {
            LazyHorizontalGrid(
                modifier = gridModifier.fillMaxWidth(),
                rows = GridCells.Adaptive(minSize = minItemSize),
                horizontalArrangement = Arrangement.spacedBy(colSpacing, Alignment.Start),
                verticalArrangement = Arrangement.spacedBy(rowSpacing, Alignment.Top),
                userScrollEnabled = true,
                content =  {
                    items(imageIds.count()) { i ->
                        ImageGridItem(
                            modifier = Modifier.aspectRatio(1f).fillMaxSize(),
                            viewModel = viewModel,
                            avatarResourceId = imageIds[i],
                            isSelected = i == selectedIndex,
                            padding = imagePadding,
                            position = i
                        )
                    }
                }
            )

        }
    }
}

@Composable
fun ImageGridItem (
    modifier: Modifier = Modifier,
    viewModel: TicTacViewModel,
    avatarResourceId: Int,
    isSelected: Boolean,
    padding: Int = 24,
    position: Int
) {
    AvatarBlock(
        avatarResourceId = avatarResourceId,
        isCircle = false,
        isFilled = isSelected,
        isBorderTransparent = !isSelected,
        padding = padding,
        boxModifier = modifier.clickable {
            viewModel.selectedAvatar = position
        }
    )
}