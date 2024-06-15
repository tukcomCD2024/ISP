package com.project.how.interface_af

import com.project.how.data_class.recyclerview.DaysSchedule

interface OnDateTimeListener {
    fun onSaveDate(d : DaysSchedule, date : String, position: Int)
}