package com.rodrigo.mynotes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.rodrigo.mynotes.presentation.add_edit_note.AddEditScreen
import com.rodrigo.mynotes.presentation.navigation.NavigationGraph
import com.rodrigo.mynotes.presentation.note_list.NoteListScreen
import com.rodrigo.mynotes.ui.theme.MyNotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MyNotesTheme {
                AppEntryPoint()
            }
        }
    }
}

@Composable
fun AppEntryPoint() {
    NavigationGraph()
}