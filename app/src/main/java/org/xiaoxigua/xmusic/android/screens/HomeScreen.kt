package org.xiaoxigua.xmusic.android.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.xiaoxigua.xmusic.android.components.PlaylistItem
import org.xiaoxigua.xmusic.android.room.UserViewModel
import org.xiaoxigua.xmusic.android.ui.theme.XMusicTheme

@Composable
fun HomeScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val playlist by userViewModel.allPlaylist.observeAsState(emptyList())

    LazyColumn(modifier = Modifier.padding(vertical = 10.dp)) {
        items(playlist, key = { it.playlistId }) {
            PlaylistItem(it.title, it.description, {}, {
                userViewModel.deletePlaylist(it)
            }, {
                navController.navigate("Home/${it.playlistId}")
            })
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()

    XMusicTheme {
        HomeScreen(navController, viewModel())
    }
}