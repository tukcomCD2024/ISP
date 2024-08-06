package com.project.how.interface_af

import com.project.how.data_class.recyclerview.schedule.DaysSchedule

interface OnDateTimeListener {
    fun onSaveDate(d: DaysSchedule, selectedDate: String, changedDate: String, position: Int)
}