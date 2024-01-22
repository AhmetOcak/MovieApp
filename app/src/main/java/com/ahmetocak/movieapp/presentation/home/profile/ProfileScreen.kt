package com.ahmetocak.movieapp.presentation.home.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.presentation.ui.components.AnimatedAsyncImage
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.utils.Dimens

private val PROFILE_IMG_SIZE = 128.dp

enum class Languages {
    ENGLISH,
    TURKISH
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToRoute: (String) -> Unit,
    onLogOutClick: () -> Unit
) {

    MovieScaffold(modifier = modifier) { paddingValues ->
        ProfileScreenContent(
            modifier = Modifier.padding(paddingValues),
            onLogOutClick = {},
            profileImageUrl = "https://picsum.photos/200/300",
            userEmail = "ahmetocak754@gmail.com",
            onDeleteAccountClick = {},
            onDarkThemeSwitchChange = {},
            onLanguageSelect = {},
            isAppThemeDark = false
        )
    }
}

@Composable
private fun ProfileScreenContent(
    modifier: Modifier,
    onLogOutClick: () -> Unit,
    profileImageUrl: String,
    userEmail: String,
    onDeleteAccountClick: () -> Unit,
    onDarkThemeSwitchChange: (Boolean) -> Unit,
    onLanguageSelect: (Languages) -> Unit,
    isAppThemeDark: Boolean
) {
    Column(modifier = modifier.fillMaxSize()) {
        ProfileSection(
            modifier = Modifier.weight(2f),
            onLogOutClick = onLogOutClick,
            profileImageUrl = profileImageUrl,
            userEmail = userEmail,
            onDeleteAccountClick = onDeleteAccountClick
        )
        SettingsSection(
            modifier = Modifier.weight(3f),
            onDarkThemeSwitchChange = onDarkThemeSwitchChange,
            onLanguageSelect = onLanguageSelect,
            isAppThemeDark = isAppThemeDark
        )
    }
    AppIcon()
}

@Composable
private fun ProfileSection(
    modifier: Modifier,
    onLogOutClick: () -> Unit,
    profileImageUrl: String,
    userEmail: String,
    onDeleteAccountClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    listOf(Color(0xFFff1b6b), Color(0xFF45caff))
                )
            )
    ) {
        TopAppBar(onLogOutClick = onLogOutClick, onDeleteAccountClick = onDeleteAccountClick)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ElevatedCard(modifier = Modifier.size(PROFILE_IMG_SIZE), shape = CircleShape) {
                AnimatedAsyncImage(
                    modifier = Modifier.fillMaxSize(), imageUrl = profileImageUrl
                )
            }
            Text(modifier = Modifier.padding(top = Dimens.twoLevelPadding), text = userEmail)
        }
    }
}

@Composable
private fun SettingsSection(
    modifier: Modifier,
    onDarkThemeSwitchChange: (Boolean) -> Unit,
    onLanguageSelect: (Languages) -> Unit,
    isAppThemeDark: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.twoLevelPadding),
    ) {
        Text(text = stringResource(id = R.string.settings_text))
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.twoLevelPadding)
        )
        DarkThemePicker(
            onDarkThemeSwitchChange = onDarkThemeSwitchChange,
            isAppThemeDark = isAppThemeDark
        )
        LanguagePicker(onLanguageSelect = onLanguageSelect)
    }
}

@Composable
private fun DarkThemePicker(onDarkThemeSwitchChange: (Boolean) -> Unit, isAppThemeDark: Boolean) {
    var checked by remember { mutableStateOf(isAppThemeDark) }

    SettingItem {
        Text(text = stringResource(id = R.string.dark_theme_text))
        Switch(
            checked = checked,
            onCheckedChange = {
                onDarkThemeSwitchChange(it)
                checked = !checked
            }
        )
    }
}

@Composable
private fun LanguagePicker(onLanguageSelect: (Languages) -> Unit) {
    SettingItem(height = 64.dp) {
        var expanded by remember { mutableStateOf(false) }

        Text(text = stringResource(id = R.string.languages_text))
        Box {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "English")
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.english_text))
                    },
                    onClick = {
                        onLanguageSelect(Languages.ENGLISH)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.turkish_text))
                    },
                    onClick = {
                        onLanguageSelect(Languages.TURKISH)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingItem(height: Dp = 48.dp, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
private fun AppIcon() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Image(
            modifier = Modifier
                .padding(Dimens.twoLevelPadding)
                .size(128.dp),
            painter = painterResource(id = R.drawable.auth_background_image),
            contentDescription = null
        )
    }
}

@Composable
private fun TopAppBar(onLogOutClick: () -> Unit, onDeleteAccountClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(onClick = onDeleteAccountClick) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
        }
        IconButton(onClick = onLogOutClick) {
            Icon(imageVector = Icons.Filled.Logout, contentDescription = null)
        }
    }
}