package org.xiaoxigua.xmusic.android.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.xiaoxigua.xmusic.android.Screens
import org.xiaoxigua.xmusic.android.ui.theme.ContainerColor
import org.xiaoxigua.xmusic.android.ui.theme.DisabledLightGray
import org.xiaoxigua.xmusic.android.ui.theme.Purple
import org.xiaoxigua.xmusic.android.ui.theme.XMusicTheme

@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        containerColor = ContainerColor,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Screens.entries.forEach { screen ->
            val selectColor = if (currentRoute != screen.route) DisabledLightGray else Purple

            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(0) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = null,
                            tint = selectColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(text = screen.route, color = selectColor, fontSize = 11.sp)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Purple,
                    unselectedIconColor = DisabledLightGray,
                    indicatorColor = Color.Transparent
                ),
            )
        }
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    XMusicTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = { BottomBar(
            NavController(
                LocalContext.current)
        ) }) { innerPadding ->
            Text("", modifier = Modifier.padding(innerPadding))
        }
    }
}