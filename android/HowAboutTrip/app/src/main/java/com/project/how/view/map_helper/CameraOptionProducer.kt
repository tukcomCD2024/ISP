package com.project.how.view.map_helper

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds




class CameraOptionProducer {
    fun makeScheduleCarmeraUpdate(location: LatLng, zoom : Float) : CameraUpdate {
        val cameraOption = CameraPosition.builder()
            .target(location)
            .zoom(zoom)
            .build()
        return CameraUpdateFactory.newCameraPosition(cameraOption)
    }

    fun makeScheduleBoundsCameraUpdate(leftTopLocation : LatLng, rightBottomLocation : LatLng, padding: Int) : CameraUpdate{
        val builder = LatLngBounds.Builder()
        builder.include(leftTopLocation)
        builder.include(rightBottomLocation)
        val bounds = builder.build()
        return CameraUpdateFactory.newLatLngBounds(bounds, padding)
    }
}