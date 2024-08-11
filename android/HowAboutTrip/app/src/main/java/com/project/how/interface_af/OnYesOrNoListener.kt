package com.project.how.interface_af

interface OnYesOrNoListener {
    fun onScheduleDeleteListener(position: Int)
    fun onKeepCheckListener()
    fun onCameraListener(answer : Boolean)
}