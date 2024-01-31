package com.ahmetocak.movieapp.presentation.home.profile

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.DialogUiEvent
import com.ahmetocak.movieapp.presentation.home.HomeSections
import com.ahmetocak.movieapp.presentation.home.MovieNavigationBar
import com.ahmetocak.movieapp.presentation.ui.components.AnimatedAsyncImage
import com.ahmetocak.movieapp.presentation.ui.components.ButtonCircularProgressIndicator
import com.ahmetocak.movieapp.presentation.ui.components.FullScreenCircularProgressIndicator
import com.ahmetocak.movieapp.presentation.ui.components.MovieDialog
import com.ahmetocak.movieapp.presentation.ui.components.MovieScaffold
import com.ahmetocak.movieapp.presentation.ui.components.auth.AuthPasswordOutlinedTextField
import com.ahmetocak.movieapp.presentation.ui.theme.backgroundDark
import com.ahmetocak.movieapp.presentation.ui.theme.backgroundLight
import com.ahmetocak.movieapp.presentation.ui.theme.primaryContainerDark
import com.ahmetocak.movieapp.presentation.ui.theme.primaryContainerLight
import com.ahmetocak.movieapp.presentation.ui.theme.primaryDark
import com.ahmetocak.movieapp.presentation.ui.theme.primaryLight
import com.ahmetocak.movieapp.utils.Dimens

private val PROFILE_IMG_SIZE = 144.dp
private val APP_ICON_SIZE = 96.dp

private enum class Languages {
    ENGLISH,
    TURKISH
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToRoute: (String) -> Unit,
    onLogOutClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            viewModel.uploadUserProfileImage(uri)
        }

    if (uiState.deleteAccountDialogUiEvent != DialogUiEvent.InActive) {
        DeleteAccountDialog(
            onDismissRequest = viewModel::endDeleteAccountDialog,
            onCancelClick = viewModel::endDeleteAccountDialog,
            onDeleteClick = remember(viewModel) {
                {
                    viewModel.deleteUserAccount(onAccountDeleteEnd = onLogOutClick)
                }
            },
            password = viewModel.password,
            onPasswordValueChange = remember(viewModel) { viewModel::updatePasswordValue },
            isLoading = uiState.deleteAccountDialogUiEvent == DialogUiEvent.Loading
        )
    }

    if (uiState.userMessages.isNotEmpty()) {
        Toast.makeText(
            LocalContext.current,
            uiState.userMessages.first().asString(),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.userMessageConsumed()
    }

    MovieScaffold(
        modifier = modifier,
        bottomBar = {
            MovieNavigationBar(
                tabs = HomeSections.entries.toTypedArray(),
                currentRoute = HomeSections.PROFILE.route,
                navigateToRoute = onNavigateToRoute
            )
        }
    ) { paddingValues ->
        ProfileScreenContent(
            modifier = Modifier.padding(paddingValues),
            onLogOutClick = onLogOutClick,
            profileImageUrl = uiState.profileImgUri?.toString() ?: "",
            userEmail = uiState.userEmail,
            onDeleteAccountClick = remember(viewModel) { viewModel::startDeleteAccountDialog },
            onDarkThemeSwitchChange = {},
            onLanguageSelect = {},
            isAppThemeDark = isSystemInDarkTheme(),
            onDynamicColorSwitchChange = {},
            isDynamicColorActive = false,
            onPickUpFromGalleryClick = remember {
                {
                    pickMedia.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            },
            isProfileImgUploading = uiState.isProfileImgUploading
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
    isAppThemeDark: Boolean,
    onDynamicColorSwitchChange: (Boolean) -> Unit,
    isDynamicColorActive: Boolean,
    onPickUpFromGalleryClick: () -> Unit,
    isProfileImgUploading: Boolean
) {
    Column(modifier = modifier.fillMaxSize()) {
        ProfileSection(
            modifier = Modifier.weight(2f),
            onLogOutClick = onLogOutClick,
            profileImageUrl = profileImageUrl,
            userEmail = userEmail,
            onDeleteAccountClick = onDeleteAccountClick,
            isAppThemeDark = isAppThemeDark,
            onPickUpFromGalleryClick = onPickUpFromGalleryClick,
            isProfileImgUploading = isProfileImgUploading
        )
        SettingsSection(
            modifier = Modifier.weight(3f),
            onDarkThemeSwitchChange = onDarkThemeSwitchChange,
            onLanguageSelect = onLanguageSelect,
            isAppThemeDark = isAppThemeDark,
            onDynamicColorSwitchChange = onDynamicColorSwitchChange,
            isDynamicColorActive = isDynamicColorActive
        )
    }
    AppIcon(modifier = modifier.fillMaxSize())
}

@Composable
private fun ProfileSection(
    modifier: Modifier,
    onLogOutClick: () -> Unit,
    profileImageUrl: String,
    userEmail: String,
    onDeleteAccountClick: () -> Unit,
    isAppThemeDark: Boolean,
    onPickUpFromGalleryClick: () -> Unit,
    isProfileImgUploading: Boolean
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    if (isAppThemeDark) {
                        listOf(primaryDark, primaryContainerDark, backgroundDark)
                    } else {
                        listOf(primaryLight, primaryContainerLight, backgroundLight)
                    }
                )
            )
    ) {
        TopAppBar(onLogOutClick = onLogOutClick, onDeleteAccountClick = onDeleteAccountClick)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(PROFILE_IMG_SIZE),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (isProfileImgUploading) {
                    FullScreenCircularProgressIndicator()
                } else {
                    AnimatedAsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        imageUrl = profileImageUrl,
                        borderShape = CircleShape,
                        borderStroke = BorderStroke(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                listOf(Color(0xFF00C3FF), Color(0xFFFFFF1C))
                            )
                        ),
                        errorImageDrawableId = R.drawable.blank_profile_image
                    )
                    IconButton(
                        modifier = Modifier.size(48.dp),
                        onClick = onPickUpFromGalleryClick,
                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.background)
                    ) {
                        Icon(imageVector = Icons.Filled.Image, contentDescription = null)
                    }
                }
            }
            Text(
                modifier = Modifier.padding(top = Dimens.twoLevelPadding),
                text = userEmail,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun SettingsSection(
    modifier: Modifier,
    onDarkThemeSwitchChange: (Boolean) -> Unit,
    onLanguageSelect: (Languages) -> Unit,
    isAppThemeDark: Boolean,
    onDynamicColorSwitchChange: (Boolean) -> Unit,
    isDynamicColorActive: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.twoLevelPadding),
    ) {
        Text(
            text = stringResource(id = R.string.settings_text),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
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
        if (Build.VERSION.SDK_INT >= 31) {
            DynamicColorPicker(
                onDynamicColorSwitchChange = onDynamicColorSwitchChange,
                isDynamicColorActive = isDynamicColorActive
            )
        }
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

@OptIn(ExperimentalMaterial3Api::class)
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
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    }
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
private fun DynamicColorPicker(
    onDynamicColorSwitchChange: (Boolean) -> Unit,
    isDynamicColorActive: Boolean
) {
    var checked by remember { mutableStateOf(isDynamicColorActive) }

    SettingItem {
        Text(text = stringResource(id = R.string.dynamic_color_text))
        Switch(
            checked = checked,
            onCheckedChange = {
                onDynamicColorSwitchChange(it)
                checked = !checked
            }
        )
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
private fun AppIcon(modifier: Modifier) {
    Box(
        modifier = modifier.padding(bottom = Dimens.oneLevelPadding),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier
                    .padding(Dimens.twoLevelPadding)
                    .size(APP_ICON_SIZE),
                bitmap = LocalContext.current.packageManager.getApplicationIcon("com.ahmetocak.movieapp")
                    .toBitmap().asImageBitmap(),
                contentDescription = null
            )
            Text(
                text = "Ahmet Ocak - ${stringResource(id = R.string.app_name)}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun TopAppBar(onLogOutClick: () -> Unit, onDeleteAccountClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(onClick = onDeleteAccountClick) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = null, tint = Color.Black)
        }
        IconButton(onClick = onLogOutClick) {
            Icon(imageVector = Icons.Filled.Logout, contentDescription = null, tint = Color.Black)
        }
    }
}

@Composable
private fun DeleteAccountDialog(
    onDismissRequest: () -> Unit,
    onCancelClick: () -> Unit,
    onDeleteClick: () -> Unit,
    password: String,
    onPasswordValueChange: (String) -> Unit,
    isLoading: Boolean
) {
    MovieDialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.threeLevelPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.twoLevelPadding)
        ) {
            Text(
                text = stringResource(id = R.string.delete_account_text),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(text = stringResource(id = R.string.delete_account_description_text))
            AuthPasswordOutlinedTextField(
                value = password,
                onValueChange = onPasswordValueChange,
                labelText = stringResource(id = R.string.password_label)
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                ElevatedButton(onClick = onCancelClick) {
                    Text(text = stringResource(id = R.string.cancel_text))
                }
                Spacer(modifier = Modifier.width(Dimens.twoLevelPadding))
                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    enabled = password.isNotBlank() && !isLoading
                ) {
                    if (isLoading) {
                        ButtonCircularProgressIndicator()
                    } else {
                        Text(text = stringResource(id = R.string.delete_text))
                    }
                }
            }
        }
    }
}