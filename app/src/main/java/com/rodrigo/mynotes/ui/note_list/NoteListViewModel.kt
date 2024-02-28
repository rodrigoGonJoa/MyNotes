package com.rodrigo.mynotes.ui.note_list

import androidx.lifecycle.ViewModel
import com.rodrigo.mynotes.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class NoteListState(

)

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val noteListState: NoteListState
): ViewModel() {

    init {
        refresh()
    }
}