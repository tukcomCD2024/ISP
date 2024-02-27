package com.project.how.interface_af.interface_ada

import com.project.how.data_class.DaysSchedule

interface ItemStartDragListener {
    fun onDropActivity(initList : MutableList<DaysSchedule>, changeList: MutableList<DaysSchedule>)
}