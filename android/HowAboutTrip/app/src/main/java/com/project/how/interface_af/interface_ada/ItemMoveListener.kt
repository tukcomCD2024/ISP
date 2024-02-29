package com.project.how.interface_af.interface_ada

import com.project.how.data_class.DaysSchedule

interface ItemMoveListener {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onDropAdapter()
}