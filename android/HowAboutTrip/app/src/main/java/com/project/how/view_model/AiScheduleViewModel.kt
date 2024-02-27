package com.project.how.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.how.adapter.AiDaysScheduleAdapter
import com.project.how.data_class.AiDaysSchedule
import com.project.how.data_class.AiSchedule
import com.project.how.data_class.AiScheduleInput
import com.project.how.data_class.dto.CreateScheduleRequest
import com.project.how.data_class.dto.CreateScheduleRespone
import com.project.how.model.AiScheduleRepository
import com.project.how.network.client.ScheduleRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AiScheduleViewModel : ViewModel() {
    private val aiScheduleRepository = AiScheduleRepository()
    private val _aiScheduleLiveData = aiScheduleRepository.aiScheduleLiveData
    val aiScheduleLiveData : LiveData<AiSchedule>
        get() = _aiScheduleLiveData

    fun getAiSchedule(aiScheduleInput : AiScheduleInput, test : Boolean){
        if (test){
            aiScheduleRepository.getAiSchedule(getTestAiSchedule())
            return
        }
        val createScheduleRequest = CreateScheduleRequest(
            aiScheduleInput.des,
            aiScheduleInput.purpose ?: listOf(),
            aiScheduleInput.startDate,
            aiScheduleInput.endDate
        )
        ScheduleRetrofit.getApiService()!!
            .createSchedule(createScheduleRequest)
            .enqueue(object : Callback<CreateScheduleRespone>{
                override fun onResponse(
                    call: Call<CreateScheduleRespone>,
                    response: Response<CreateScheduleRespone>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<CreateScheduleRespone>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun getTestAiSchedule() : AiSchedule{
        val testAiDaysSchedule = mutableListOf<AiDaysSchedule>(AiDaysSchedule(AiDaysScheduleAdapter.PLACE, "test Todo", "test"))
        testAiDaysSchedule.add(AiDaysSchedule(AiDaysScheduleAdapter.AIRPLANE, "test airplane", "airplane"))
        testAiDaysSchedule.add(AiDaysSchedule(AiDaysScheduleAdapter.HOTEL, "test hotel", "hotel"))
        val testAiDaysSchedule2 = mutableListOf<AiDaysSchedule>(AiDaysSchedule(AiDaysScheduleAdapter.AIRPLANE, "test airplane", "airplane"))
        testAiDaysSchedule2.add(AiDaysSchedule(AiDaysScheduleAdapter.PLACE, "test Todo", "test"))
        testAiDaysSchedule2.add(AiDaysSchedule(AiDaysScheduleAdapter.HOTEL, "test hotel", "hotel"))
        val dailySchedule = mutableListOf<MutableList<AiDaysSchedule>>()
        for (i in 0..5){
            if (i % 2== 0){
                dailySchedule.add(testAiDaysSchedule)
            }else{
                dailySchedule.add(testAiDaysSchedule2)
            }
        }
        return AiSchedule(
            "TestTitle",
            listOf("test1", "test2", "test3", "test4", "test5", "tttteeeessssssss6"),
            "https://img.freepik.com/free-photo/vertical-shot-beautiful-eiffel-tower-captured-paris-france_181624-45445.jpg?w=740&t=st=1708260600~exp=1708261200~hmac=01d8abec61f555d0edb040d41ce8ea39904853aea6df7c37ce0b5a35e07c1954",
            "2024-02-18",
            "2024-02-23",
            dailySchedule)
    }
}