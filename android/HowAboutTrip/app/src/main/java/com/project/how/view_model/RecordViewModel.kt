package com.project.how.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.how.data_class.dto.recode.receipt.GetReceiptListResponse
import com.project.how.model.RecordRepository
import com.project.how.network.client.RecordRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RecordViewModel : ViewModel() {
    val recordRepository : RecordRepository = RecordRepository()
    private val _currentReceiptList = recordRepository.currentReceiptList
    val currentReceiptList : LiveData<GetReceiptListResponse>
        get() = _currentReceiptList

    fun getReceiptList(scheduleId : Long){
        viewModelScope.launch (Dispatchers.IO){
            RecordRetrofit.getApiService()?.let { apiService->
                apiService.getReceiptList(scheduleId)
                    .enqueue(object : Callback<GetReceiptListResponse>{
                        override fun onResponse(
                            p0: Call<GetReceiptListResponse>,
                            p1: Response<GetReceiptListResponse>
                        ) {
                            try {
                                if (p1.code() == SUCCESS){
                                    val result = p1.body()
                                    recordRepository.getCurrentReceiptList(result!!)
                                }else{
                                    Log.e("getReceiptList", "code : ${p1.code()}\nreponse is success = ${p1.isSuccessful}")
                                }
                            }catch (e : Exception){
                                Log.e("getReceiptList", "code : ${p1.code()}\nerror : ${e.message}")
                            }
                        }

                        override fun onFailure(p0: Call<GetReceiptListResponse>, p1: Throwable) {
                            Log.e("getReceiptList", "onFailure\nnetwork error : ${p1.message}")
                        }

                    })

            }
        }
    }

    fun getDaysTitle(startDate : String, tabNum : Int) : String {
        val sd = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE)
        val formatter = DateTimeFormatter.ofPattern("MM.dd")
        return sd.plusDays(tabNum.toLong()).format(formatter)
    }

    fun getCurrentDate(startDate: String, tabNum: Int) : String {
        val sd = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return sd.plusDays(tabNum.toLong()).format(formatter)
    }


    companion object{
        const val SUCCESS = 200
    }
}