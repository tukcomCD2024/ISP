package com.project.how.view.map_helper

import android.util.Log
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds




class CameraOptionProducer {
    fun makeScheduleCameraUpdate(location: LatLng, zoom : Float) : CameraUpdate {
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

    fun makeLatLngBounds(latitudes : List<Double>, longitudes : List<Double>) : List<LatLng>{
        val leftTopLocation = LatLng(latitudes.max(), longitudes.min())
        val rightBottomLocation = LatLng(latitudes.min(), longitudes.max())
        Log.d("CameraOptionProducer", "makeLatLngBounds\nleftTopLocation : ${leftTopLocation.latitude}\t${leftTopLocation.longitude}\nrightBottomLocation : ${rightBottomLocation.latitude}\t${rightBottomLocation.longitude}")
        return listOf(leftTopLocation, rightBottomLocation)
    }
}