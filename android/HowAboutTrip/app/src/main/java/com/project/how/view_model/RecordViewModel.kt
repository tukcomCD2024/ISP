package com.project.how.view_model

import androidx.lifecycle.ViewModel
import com.project.how.model.RecordRepository

class RecordViewModel : ViewModel() {
    val recordRepository : RecordRepository = RecordRepository()
}