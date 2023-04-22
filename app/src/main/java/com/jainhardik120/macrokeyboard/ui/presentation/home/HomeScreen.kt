package com.jainhardik120.macrokeyboard.ui.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jainhardik120.macrokeyboard.data.local.entity.ScreenEntity

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)){
        items(viewModel.state.screenEntities.size + 1){it->
            if(it==viewModel.state.screenEntities.size){
                NewItemButton()
            }else{
                ButtonItem(screenEntity = viewModel.state.screenEntities[it])
            }
        }
    }
}

@Composable
fun ButtonItem(screenEntity: ScreenEntity) {
    Box(modifier = Modifier.size(128.dp)){
        Text(text = screenEntity.label)
    }
}

@Composable
fun NewItemButton() {
    Box(modifier = Modifier.size(128.dp)){
        Text(text = "New")
    }
}