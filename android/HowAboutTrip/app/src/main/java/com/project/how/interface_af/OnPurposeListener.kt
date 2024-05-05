package com.project.how.interface_af

interface OnPurposeListener {
    fun onWhoListener(who : String)
    fun onActivityLevelListener(activityLevel : String)
    fun onPurposeListener(purpose : List<String>)
    fun onTransportationListener(transportation : List<String>)
}