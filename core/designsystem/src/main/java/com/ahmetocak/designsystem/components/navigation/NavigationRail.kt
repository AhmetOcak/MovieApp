package com.ahmetocak.designsystem.components.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetocak.navigation.HomeSections

@Composable
fun MovieNavigationRail(
    tabs: Array<HomeSections>,
    navigateToRoute: (String) -> Unit,
    currentRoute: String
) {
    NavigationRail(modifier = Modifier.offset(x = (-1).dp)) {
        Spacer(Modifier.weight(1f))
        tabs.forEachIndexed { _, section ->
            NavigationRailItem(
                selected = currentRoute == section.route,
                onClick = {
                    navigateToRoute(section.route)
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == section.route)
                            section.selectedIcon else section.unSelectedIcon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(id = section.title))
                }
            )
        }
        Spacer(Modifier.weight(1f))
    }
}