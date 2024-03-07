package com.rodrigo.mynotes.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rodrigo.mynotes.domain.model.Note
import com.rodrigo.mynotes.presentation.note_list.NoteListViewModel

@Composable
fun NoteItemComposable(note: Note, onClickNote: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClickNote)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = note.content,
                maxLines = 4,
                minLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}