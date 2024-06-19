package com.project.how.adapter.item_touch_helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.project.how.interface_af.interface_ada.ItemMoveListener

class RecyclerViewItemTouchHelperCallback(private val moveListener: ItemMoveListener) : ItemTouchHelper.Callback() {
    private var fromPosition: Int = -1
    private var toPosition: Int = -1

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if(fromPosition == -1) fromPosition = viewHolder.adapterPosition
        toPosition = target.adapterPosition
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        when(actionState) {
            ItemTouchHelper.ACTION_STATE_IDLE -> {
                if(fromPosition != -1 && toPosition != -1 && fromPosition != toPosition) {
                    moveListener.onItemMove(fromPosition, toPosition)
                }
                resetPositions()
            }
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { }

    private fun resetPositions() {
        fromPosition = -1
        toPosition = -1
    }
}
