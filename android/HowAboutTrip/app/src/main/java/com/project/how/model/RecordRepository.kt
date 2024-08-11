package com.project.how.model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.data_class.dto.recode.ocr.OcrResponse
import com.project.how.data_class.dto.recode.receipt.GetReceiptListResponse

class RecordRepository {
    private val _currentReceiptListLiveData : MutableLiveData<GetReceiptListResponse> = MutableLiveData()
    private val _uriLiveData : MutableLiveData<Uri> = MutableLiveData()
    private val _ocrResponseLiveData : MutableLiveData<OcrResponse?> = MutableLiveData()
    val currentReceiptListLiveData : LiveData<GetReceiptListResponse>
        get() = _currentReceiptListLiveData
    val uriLiveData : LiveData<Uri>
        get() = _uriLiveData
    val ocrResponseLiveData : LiveData<OcrResponse?>
        get() = _ocrResponseLiveData

    fun getCurrentReceiptList(data : GetReceiptListResponse){
        _currentReceiptListLiveData.postValue(data)
    }

    fun getUri(uri : Uri){
        _uriLiveData.postValue(uri)
    }

    fun getOcrResponseLiveData(data : OcrResponse?){
        _ocrResponseLiveData.postValue(data)
    }
}