package com.project.how.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.data_class.dto.recode.receipt.GetReceiptListResponse

class RecordRepository {
    private val _currentReceiptList : MutableLiveData<GetReceiptListResponse> = MutableLiveData()
    val currentReceiptList : LiveData<GetReceiptListResponse>
        get() = _currentReceiptList

    fun getCurrentReceiptList(data : GetReceiptListResponse){
        _currentReceiptList.postValue(data)
    }
}