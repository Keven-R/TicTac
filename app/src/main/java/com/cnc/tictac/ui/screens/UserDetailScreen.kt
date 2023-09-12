package com.cnc.tictac.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.Destination
import com.cnc.tictac.R
import com.cnc.tictac.ui.components.BackButton
import com.cnc.tictac.ui.components.ImageGridSingleSelect
import com.cnc.tictac.ui.components.PrimaryButton
import com.cnc.tictac.ui.components.SingleLineTextField
import com.cnc.tictac.viewmodel.TicTacEvent
import com.cnc.tictac.viewmodel.TicTacViewModel

@Composable
fun UserDetailScreen(navController: NavHostController, viewModel: TicTacViewModel) {
    // Determine device UI layout.
    val configuration = LocalConfiguration.current

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        DisplayCompactUserDetailScreen(navController, viewModel)
    } else {
        DisplayDefaultUserDetailScreen(navController, viewModel)
    }
}

@Composable
fun DisplayCompactUserDetailScreen (navController: NavHostController, viewModel: TicTacViewModel) {
    // CONTAINER: Set bg colour
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.primary)) {
        // CONTAINER: All content on screen

        val avatarArray = arrayOf(R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3, R.drawable.avatar_4, R.drawable.avatar_5, R.drawable.avatar_6, R.drawable.avatar_7, R.drawable.avatar_8, R.drawable.avatar_9, R.drawable.avatar_10)

        // Should select users current avatar
        fun getSelectedAvatar(): Int{
            if(viewModel.newUser){
                return 0
            }
            var currentAvatar = 0
            if(viewModel.player1Edit){ currentAvatar = viewModel.player1Avatar }else{ viewModel.player2Avatar }

            for((index, avatar) in avatarArray.withIndex()){
                if(avatar == currentAvatar){
                    return index
                }
            }
            return 0
        }

        val selectedAvatar by remember { mutableIntStateOf(getSelectedAvatar()) }
        var playerTextFieldValue by remember { mutableStateOf(when(viewModel.player1Edit){
            true -> {if(viewModel.newUser){""}else{viewModel.player1}}
            false -> {if(viewModel.newUser){""}else{viewModel.player2}}
        }) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // CONTAINER: Top nav
            Row(modifier = Modifier.fillMaxWidth()) {
                // ELEMENT: Back button showing current page title)
                BackButton(
                    stringResource(
                        id = when(viewModel.newUser){
                                true -> R.string.page_title_new_user
                                false -> R.string.page_title_edit_profile }),
                        destination = when(viewModel.newUser){
                                        true -> Destination.UserSelectScreen
                                        false -> Destination.ProfileScreen },
                    navController
                )
            }

            // CONTAINER: Editable user details
            Row(
                modifier = Modifier
                    .weight(1f).fillMaxHeight()
                    .fillMaxWidth().padding(bottom = 16.dp),
//                    .verticalScroll(rememberScrollState())
                horizontalArrangement = Arrangement.spacedBy(space = 32.dp),
            ) {
                // ELEMENT: Text box
                SingleLineTextField(
                    modifier = Modifier.width(160.dp),
                    label = stringResource(id = R.string.user_name_field_label),
                    value = playerTextFieldValue,
                    placeholder = stringResource(id = R.string.user_name_placeholder)
                ) {
                    playerTextFieldValue = it
                }

                // ELEMENT: Avatar select
                // TODO: Make avatar selectable
                ImageGridSingleSelect(
                    modifier = Modifier.weight(1f).fillMaxWidth().fillMaxHeight(),
                    gridModifier = Modifier.weight(1f).fillMaxHeight(),
//                    gridModifier = Modifier.nestedScroll(
//                        connection = object : NestedScrollConnection {
//                            override fun onPreScroll(available: Offset, source: NestedScrollSource) = available.copy(x = 0f)
//                        }
//                    ),
                    label = stringResource(id = R.string.user_avatar_select_label),
                    imageIds = avatarArray,
                    selectedIndex = selectedAvatar,
                    isVerticalScroll = false
                )
            }

            // CONTAINER: Primary action
            Row(modifier = Modifier.fillMaxWidth()) {
                // ELEMENT: Button to go to game settings screen
                PrimaryButton(
                    stringResource(id = when(viewModel.newUser){
                        true -> R.string.user_create_action
                        false -> R.string.user_save_action
                    }),
                    navController = navController,
                    destination = when(viewModel.newUser){
                        true -> Destination.UserSelectScreen
                        false -> Destination.ProfileScreen },
                    viewModel = viewModel,
                    event = TicTacEvent.SaveUser(playerTextFieldValue,avatarArray[selectedAvatar]),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun DisplayDefaultUserDetailScreen (navController: NavHostController, viewModel: TicTacViewModel) {
    // CONTAINER: Set bg colour
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.primary)) {
        // CONTAINER: All content on screen

        val avatarArray = arrayOf(R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3, R.drawable.avatar_4, R.drawable.avatar_5, R.drawable.avatar_6, R.drawable.avatar_7, R.drawable.avatar_8, R.drawable.avatar_9, R.drawable.avatar_10)

        // Should select users current avatar
        fun getSelectedAvatar(): Int{
            if(viewModel.newUser){
                return 0
            }
            var currentAvatar = 0
            if(viewModel.player1Edit){ currentAvatar = viewModel.player1Avatar }else{ viewModel.player2Avatar }

            for((index, avatar) in avatarArray.withIndex()){
                if(avatar == currentAvatar){
                    return index
                }
            }
            return 0
        }

        val selectedAvatar by remember { mutableIntStateOf(getSelectedAvatar()) }
        var playerTextFieldValue by remember { mutableStateOf(when(viewModel.player1Edit){
            true -> {if(viewModel.newUser){""}else{viewModel.player1}}
            false -> {if(viewModel.newUser){""}else{viewModel.player2}}
        }) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CONTAINER: Top nav
            Row(modifier = Modifier.fillMaxWidth()) {
                // ELEMENT: Back button showing current page title)
                BackButton(
                    stringResource(
                        id = when(viewModel.newUser){
                            true -> R.string.page_title_new_user
                            false -> R.string.page_title_edit_profile }),
                    destination = when(viewModel.newUser){
                        true -> Destination.UserSelectScreen
                        false -> Destination.ProfileScreen },
                    navController
                )
            }

            // CONTAINER: Editable user details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .fillMaxWidth()
//                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(space = 40.dp),
            ) {
                // ELEMENT: Text box
                SingleLineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(id = R.string.user_name_field_label),
                    value = playerTextFieldValue,
                    placeholder = stringResource(id = R.string.user_name_placeholder)
                ) {
                    playerTextFieldValue = it
                }

                // ELEMENT: Avatar select
                // TODO: Make avatar selectable
                ImageGridSingleSelect(
                    modifier = Modifier.fillMaxWidth(),
//                    gridModifier = Modifier.nestedScroll(
//                        connection = object : NestedScrollConnection {
//                            override fun onPreScroll(available: Offset, source: NestedScrollSource) = available.copy(x = 0f)
//                        }
//                    ),
                    label = stringResource(id = R.string.user_avatar_select_label),
                    imageIds = avatarArray,
                    selectedIndex = selectedAvatar,
                )
            }

            // CONTAINER: Primary action
            Row(modifier = Modifier.fillMaxWidth()) {
                // ELEMENT: Button to go to game settings screen
                PrimaryButton(
                    stringResource(id = when(viewModel.newUser){
                        true -> R.string.user_create_action
                        false -> R.string.user_save_action
                    }),
                    navController = navController,
                    destination = when(viewModel.newUser){
                        true -> Destination.UserSelectScreen
                        false -> Destination.ProfileScreen },
                    viewModel = viewModel,
                    event = TicTacEvent.SaveUser(playerTextFieldValue,avatarArray[selectedAvatar]),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}