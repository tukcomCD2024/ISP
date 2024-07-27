package com.project.how.view.map_helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.how.R
import com.project.how.adapter.recyclerview.schedule.AiDaysScheduleAdapter
import com.project.how.databinding.MapMarkerScheduleBinding

class MarkerProducer {

    fun makeScheduleMarkerOptions(context: Context, type: Int, position: Int, location : LatLng, name : String) : MarkerOptions {
        return MarkerOptions()
            .position(location)
            .title(name)
            .anchor(0.25f, 0.5f)
            .icon(inflateLayoutToBitmap(context, type, position)?.let {
                BitmapDescriptorFactory.fromBitmap(
                    it
                )
            })
    }

    fun inflateLayoutToBitmap(context: Context, type : Int, position: Int): Bitmap? {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.map_marker_schedule, null)
        val markerBinding: MapMarkerScheduleBinding? = DataBindingUtil.bind(view)
        if (markerBinding != null){
            when(type){
                AiDaysScheduleAdapter.AIRPLANE ->{
                    markerBinding.scheduleMarker.setBackgroundResource(R.drawable.icon_ticket_bold)
                    markerBinding.scheduleMarker.text = ""
                }
                AiDaysScheduleAdapter.HOTEL -> {
                    markerBinding.scheduleMarker.setBackgroundResource(R.drawable.black_oval)
                    markerBinding.scheduleMarker.text = (position+1).toString()
                }
                AiDaysScheduleAdapter.PLACE -> {
                    markerBinding.scheduleMarker.setBackgroundResource(R.drawable.black_oval)
                    markerBinding.scheduleMarker.text = (position+1).toString()
                }
            }
        }

        view.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return bitmap
    }
}