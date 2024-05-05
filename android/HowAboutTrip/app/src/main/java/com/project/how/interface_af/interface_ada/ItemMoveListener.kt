package com.project.how.interface_af.interface_ada

interface ItemMoveListener {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onDropAdapter()
}