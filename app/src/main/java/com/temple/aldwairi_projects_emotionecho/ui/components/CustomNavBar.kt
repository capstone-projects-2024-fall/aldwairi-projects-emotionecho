package com.temple.aldwairi_projects_emotionecho.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun CustomNavBar(navController: NavController){

}

@Preview(showBackground = true)
@Composable
fun PreviewCustomNavBar(){
    val navcon = rememberNavController()
    CustomNavBar(navcon)
}