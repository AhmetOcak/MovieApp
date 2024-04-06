package com.ahmetocak.designsystem.components.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ahmetocak.navigation.HomeSections

@Composable
fun MovieNavigationRail(
    tabs: Array<HomeSections>,
    navigateToRoute: (String) -> Unit
) {
    var selectedNavIndex by rememberSaveable { mutableIntStateOf(0) }

    NavigationRail(modifier = Modifier.offset(x = (-1).dp)) {
        Spacer(Modifier.weight(1f))
        tabs.forEachIndexed { index, section ->
            NavigationRailItem(
                selected = selectedNavIndex == index,
                onClick = {
                    selectedNavIndex = index
                    navigateToRoute(section.route)
                },
                icon = {
                    Icon(
                        imageVector = if (selectedNavIndex == index)
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