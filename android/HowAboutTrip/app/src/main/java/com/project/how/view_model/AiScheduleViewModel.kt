package com.project.how.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.how.adapter.recyclerview.AiDaysScheduleAdapter
import com.project.how.data_class.recyclerview.AiDaysSchedule
import com.project.how.data_class.recyclerview.AiSchedule
import com.project.how.data_class.AiScheduleListInput
import com.project.how.data_class.dto.CreateScheduleListRequest
import com.project.how.data_class.dto.CreateScheduleListResponse
import com.project.how.data_class.dto.CreateScheduleRequest
import com.project.how.data_class.dto.CreateScheduleResponse
import com.project.how.model.AiScheduleRepository
import com.project.how.network.client.ScheduleRetrofit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AiScheduleViewModel : ViewModel() {
    private val aiScheduleRepository = AiScheduleRepository()
    private val _aiScheduleLiveData = aiScheduleRepository.aiScheduleLiveData
    private val _aiScheduleListLiveData = aiScheduleRepository.aiScheduleListLiveData
    val aiScheduleLiveData : LiveData<AiSchedule>
        get() = _aiScheduleLiveData
    val aiScheduleListLiveData : LiveData<List<AiSchedule>>
        get() = _aiScheduleListLiveData

    fun getAiScheduleList(aiScheduleListInput : AiScheduleListInput) : Flow<Boolean> = callbackFlow {
        val createScheduleListRequest = CreateScheduleListRequest(
            aiScheduleListInput.des,
            aiScheduleListInput.purpose ?: listOf(),
            aiScheduleListInput.activities ?: listOf(),
            aiScheduleListInput.excludingActivity ?: listOf(),
            aiScheduleListInput.startDate,
            aiScheduleListInput.endDate
        )

        ScheduleRetrofit.getApiService()?.let { apiService ->
            apiService.createScheduleList(createScheduleListRequest)
                .enqueue(object : Callback<CreateScheduleListResponse>{
                    override fun onResponse(
                        call: Call<CreateScheduleListResponse>,
                        response: Response<CreateScheduleListResponse>
                    ) {
                        if(response.isSuccessful){
                            val result = response.body()
                            if (result != null){
                                Log.d("createSchedule is success", "startDate : ${result.schedules[0].schedules[0].date}")
                                val data = mutableListOf<AiSchedule>()
                                for (i in result.schedules.indices){
                                    data.add(getAiSchedule(result.schedules[i], createScheduleListRequest, result.countryImage, i))
                                }
                                aiScheduleRepository.getAiScheduleList(data.toList())
                                trySend(SUCCESS)
                            }else{
                                Log.d("createSchedule is success", "response.body is null\ncode : ${response.code()}")
                                trySend(FAILD)
                            }
                        }else{
                            Log.d("createSchedule is not success", "code : ${response.code()}")
                            trySend(FAILD)
                        }
                    }

                    override fun onFailure(call: Call<CreateScheduleListResponse>, t: Throwable) {
                        Log.d("createSchedule is failed", "${t.message}")
                        trySend(FAILD)
                    }

                })
        } ?: close()

        awaitClose()
    }

    fun getTestData(){
        viewModelScope.launch {
            aiScheduleRepository.getAiSchedule(getTestAiSchedule())
        }
    }

    private fun getAiSchedule(createScheduleResponse : CreateScheduleResponse, createScheduleListRequest: CreateScheduleListRequest, countryImage : String, position : Int) : AiSchedule {
        val title = "${createScheduleListRequest.destination} AI 일정 생성${position+1}"
        val country = createScheduleListRequest.destination
        val startDate = createScheduleListRequest.departureDate
        val endDate = createScheduleListRequest.returnDate
        var place = mutableListOf<String>()
        val dailySchedule = mutableListOf<MutableList<AiDaysSchedule>>()
        for (i in createScheduleResponse.schedules.indices){
            val oneDaySchedule = mutableListOf<AiDaysSchedule>()
            for(j in createScheduleResponse.schedules[i].scheduleDetail.indices){
                oneDaySchedule.add(AiDaysSchedule(
                    AiDaysScheduleAdapter.PLACE,
                    createScheduleResponse.schedules[i].scheduleDetail[j].detail,
                    createScheduleResponse.schedules[i].scheduleDetail[j].detail,
                    createScheduleResponse.schedules[i].scheduleDetail[j].coordinate.latitude,
                    createScheduleResponse.schedules[i].scheduleDetail[j].coordinate.longitude))
                place.add(createScheduleResponse.schedules[i].scheduleDetail[j].detail)
            }
            dailySchedule.add(oneDaySchedule)
        }

        return AiSchedule(
            title,
            country,
            place,
            countryImage,
            startDate,
            endDate,
            dailySchedule
        )
    }

    private fun getTestAiSchedule() : AiSchedule {
        val testAiDaysSchedule = mutableListOf<AiDaysSchedule>(AiDaysSchedule(AiDaysScheduleAdapter.PLACE, "test Todo", "test", 0.0, 0.0))
        testAiDaysSchedule.add(AiDaysSchedule(AiDaysScheduleAdapter.AIRPLANE, "test airplane", "airplane", 0.0, 0.0))
        testAiDaysSchedule.add(AiDaysSchedule(AiDaysScheduleAdapter.HOTEL, "test hotel", "hotel", 0.0, 0.0))
        val testAiDaysSchedule2 = mutableListOf<AiDaysSchedule>(AiDaysSchedule(AiDaysScheduleAdapter.AIRPLANE, "test airplane", "airplane", 0.0, 0.0))
        testAiDaysSchedule2.add(AiDaysSchedule(AiDaysScheduleAdapter.PLACE, "test Todo", "test", 0.0, 0.0))
        testAiDaysSchedule2.add(AiDaysSchedule(AiDaysScheduleAdapter.HOTEL, "test hotel", "hotel", 0.0, 0.0))
        val dailySchedule = mutableListOf<MutableList<AiDaysSchedule>>()
        for (i in 0..2){
            if (i % 2== 0){
                dailySchedule.add(testAiDaysSchedule)
            }else{
                dailySchedule.add(testAiDaysSchedule2)
            }
        }
        return AiSchedule(
            "TestTitle",
            "프랑스",
            listOf("test1", "test2", "test3", "test4", "test5", "tttteeeessssssss6"),
            "https://img.freepik.com/free-photo/vertical-shot-beautiful-eiffel-tower-captured-paris-france_181624-45445.jpg?w=740&t=st=1708260600~exp=1708261200~hmac=01d8abec61f555d0edb040d41ce8ea39904853aea6df7c37ce0b5a35e07c1954",
            "2024-02-18",
            "2024-02-20",
            dailySchedule)
    }
    companion object{
        const val FAILD = false
        const val SUCCESS = true
    }
}