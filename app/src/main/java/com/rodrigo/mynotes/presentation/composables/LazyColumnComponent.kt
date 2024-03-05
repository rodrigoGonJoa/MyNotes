package com.rodrigo.mynotes.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp

@Composable
@Preview(showSystemUi = true)
@PreviewScreenSizes
fun LazyColumnComponent() {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/}) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }

    ) {innerPaddig ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPaddig,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = true
        ) {
            items(30) {
                NoteItemComposable()
            }
        }
    }

}

@Composable
fun NoteItemComposable() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /*TODO*/}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Title",
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Content asd  asd as as ad s sd ads \nContent\nContent\nContent\nContent\n",
                maxLines = 4,
                minLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}