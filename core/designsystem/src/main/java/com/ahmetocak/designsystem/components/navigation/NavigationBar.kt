package com.ahmetocak.designsystem.components.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ahmetocak.navigation.HomeSections

@Composable
fun MovieNavigationBar(
    tabs: Array<HomeSections>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit
) {
    val currentSection = tabs.first { it.route == currentRoute }

    NavigationBar {
        tabs.forEach { section ->
            NavigationBarItem(
                selected = currentSection.route == section.route,
                onClick = {
                    navigateToRoute(section.route)
                },
                icon = {
                    Icon(
                        imageVector = if (currentSection.route == section.route)
                            section.selectedIcon else section.unSelectedIcon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(id = section.title))
                }
            )
        }
    }
}