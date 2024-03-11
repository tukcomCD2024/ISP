package com.project.how.interface_af

import com.project.how.data_class.DaysSchedule

interface OnScheduleListener {
    fun onDaysScheduleListener(schedule : DaysSchedule, position : Int)
}