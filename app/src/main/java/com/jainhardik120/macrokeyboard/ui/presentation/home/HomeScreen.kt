package com.jainhardik120.macrokeyboard.ui.presentation.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity
import com.jainhardik120.macrokeyboard.util.UiEvent

@Composable
fun HomeScreen(onNavigate: (UiEvent.Navigate) -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    val TAG = "HomeScreen"
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.Navigate -> {
                    onNavigate(it)
                }
                is UiEvent.ShowSnackbar->{

                }
            }
        }
    }
    BackHandler(enabled = true) {
        Log.d(TAG, "HomeScreen: BackPressed")
    }
    Column(Modifier.fillMaxSize()) {
        Button(onClick = {viewModel.navigateSettings()}) {
            Text(text = "Settings")
        }
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 48.dp)){
            items(viewModel.state.screenEntities.size + 1){it->
                if(it==viewModel.state.screenEntities.size){
                    NewItemButton(onClick = {
                        viewModel.navigateNewButton()
                    })
                }else{
                    ButtonItem(screenEntity = viewModel.state.screenEntities[it])
                }
            }
        }
    }

}

@Composable
fun ButtonItem(screenEntity: ScreenEntity) {
    Box(modifier = Modifier.size(32.dp)){
        Text(text = screenEntity.label)
    }
}

@Composable
fun NewItemButton(onClick:()->Unit) {
    Box(modifier = Modifier
        .size(48.dp)
        .clickable {
            onClick()
        }
        ){
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround) {
            Icon(Icons.Filled.Add, contentDescription = "New Icon")
            Text(text = "New")
        }
    }
}