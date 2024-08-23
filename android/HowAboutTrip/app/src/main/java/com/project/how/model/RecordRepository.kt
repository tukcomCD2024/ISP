package com.project.how.model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.data_class.dto.recode.ocr.OcrResponse
import com.project.how.data_class.dto.recode.receipt.GetReceiptListResponse
import com.project.how.data_class.dto.recode.receipt.ReceiptListItem
import com.project.how.data_class.dto.recode.receipt.ReceiptSimpleList

class RecordRepository {
    private val _currentReceiptListLiveData : MutableLiveData<GetReceiptListResponse> = MutableLiveData()
    private val _uriLiveData : MutableLiveData<Uri> = MutableLiveData()
    private val _ocrResponseLiveData : MutableLiveData<OcrResponse?> = MutableLiveData()
    private val _receiptSimpleListLiveData : MutableLiveData<ReceiptSimpleList> = MutableLiveData()
    private val _saveCheckLiveData : MutableLiveData<Boolean> = MutableLiveData()
    val currentReceiptListLiveData : LiveData<GetReceiptListResponse>
        get() = _currentReceiptListLiveData
    val uriLiveData : LiveData<Uri>
        get() = _uriLiveData
    val ocrResponseLiveData : LiveData<OcrResponse?>
        get() = _ocrResponseLiveData
    val receiptSimpleListLiveData : LiveData<ReceiptSimpleList>
        get() = _receiptSimpleListLiveData
    val saveCheckLiveData : LiveData<Boolean>
        get() = _saveCheckLiveData

    fun getCurrentReceiptList(data : GetReceiptListResponse){
        _currentReceiptListLiveData.postValue(data)
    }

    fun getUri(uri : Uri){
        _uriLiveData.postValue(uri)
    }

    fun getOcrResponseLiveData(data : OcrResponse?){
        _ocrResponseLiveData.postValue(data)
    }

    fun getReceiptSimpleList(data : ReceiptSimpleList?){
        _receiptSimpleListLiveData.postValue(data ?: listOf(ReceiptListItem(0,null, "", "", "", 0.0, 0)))
    }

    fun getSaveCheck(data : Boolean){
        _saveCheckLiveData.postValue(data)
    }
}