package com.project.how.view.dialog.bottom_sheet_dialog

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.adapter.recyclerview.AiDaysScheduleAdapter
import com.project.how.data_class.DaysSchedule
import com.project.how.databinding.EditScheduleBottomSheetBinding
import com.project.how.databinding.MapMarkerScheduleBinding
import com.project.how.interface_af.OnScheduleListener
import com.project.how.view.dialog.ConfirmDialog
import kotlinx.coroutines.launch


class EditScheduleBottomSheetDialog(private val schedule : DaysSchedule, private val position : Int, private val onScheduleListener: OnScheduleListener)
    : BottomSheetDialogFragment(), OnMapReadyCallback {
    private var _binding : EditScheduleBottomSheetBinding? = null
    private val binding : EditScheduleBottomSheetBinding
        get() = _binding!!
    private lateinit var placesClient : PlacesClient
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var autocompleteFragment : AutocompleteSupportFragment
    var cost = schedule.cost.toString()
    var type = schedule.type
    var title = schedule.todo
    var place = schedule.places
    var latitude = schedule.latitude
    var longitude = schedule.longitude

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.edit_schedule_bottom_sheet, container, false)
        binding.edit = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRadioButton()

        lifecycleScope.launch {

            val apiKey = BuildConfig.GOOGLE_PLACE_API_KEY

            if (apiKey.isEmpty()) {
                Log.e("Places test", "No api key")
                dismiss()
                return@launch
            }

            Places.initialize(requireContext(), apiKey)

            placesClient = Places.createClient(requireContext())
            val googleMapOptions = GoogleMapOptions()
                .zoomControlsEnabled(true)
            supportMapFragment = SupportMapFragment.newInstance(googleMapOptions)
            childFragmentManager.beginTransaction()
                .replace(R.id.map_bs, supportMapFragment)
                .commit()
            supportMapFragment.getMapAsync(this@EditScheduleBottomSheetDialog)

            autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
            autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
            autocompleteFragment.setHint(getString(R.string.autocomplete_fragment_hint))
        }

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.i("onPlaceSelected", "Place: ${place.name}, ${place.id}\nlatitude:${place.latLng.latitude}\tlongitude:${place.latLng.longitude}")
                supportMapFragment.getMapAsync { map ->

                    val placeLocation = LatLng(place.latLng.latitude, place.latLng.longitude)
                    this@EditScheduleBottomSheetDialog.place = place.name
                    latitude = place.latLng.latitude
                    longitude = place.latLng.longitude

                    val camera = makeScheduleCarmeraUpdate(placeLocation, 15f)
                    val markerOptions = makeScheduleMarkerOptions(requireContext(), type, position, placeLocation, place.name)

                    map.clear()
                    map.moveCamera(camera)
                    map.addMarker(markerOptions)
                }
            }

            override fun onError(status: Status) {
                Log.i("onPlaceSelected", "An error occurred: $status")
            }
        })
        
        binding.scheduleType.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.airplane -> {
                    type = AiDaysScheduleAdapter.AIRPLANE
                }
                R.id.hotel -> {
                    type = AiDaysScheduleAdapter.HOTEL
                }
                R.id.place -> {
                    type = AiDaysScheduleAdapter.PLACE
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(map: GoogleMap) {
        if (schedule.latitude != null || schedule.longitude != null){
            val placeLocation = LatLng(schedule.latitude!!, schedule.longitude!!)
            val camera = makeScheduleCarmeraUpdate(placeLocation, 15f)
            val markerOptions = makeScheduleMarkerOptions(requireContext(), type, position, placeLocation, schedule.places)

            map.moveCamera(camera)
            map.addMarker(markerOptions)
        }
    }

    private fun initRadioButton(){
        when(schedule.type){
            AiDaysScheduleAdapter.AIRPLANE-> binding.scheduleType.check(R.id.airplane)
            AiDaysScheduleAdapter.HOTEL -> binding.scheduleType.check(R.id.hotel)
            AiDaysScheduleAdapter.PLACE -> binding.scheduleType.check(R.id.place)
        }
    }

    fun save(){
        if (longitude != null && latitude != null){
            title = binding.scheduleTitle.text.toString()
            cost = binding.scheduleBudget.text.toString()
            val data = DaysSchedule(
                getScheduleType(),
                title,
                place,
                latitude,
                longitude,
                cost.toLong(),
                schedule.purchaseStatus,
                schedule.purchaseDate
            )
            onScheduleListener.onDaysScheduleListener(data, position)
            dismiss()
        }else{
            val message = listOf<String>(getString(R.string.schedule_location))
            val confirmDialog = ConfirmDialog(message)
            confirmDialog.show(childFragmentManager, "ConfirmDialog")
        }
    }

    private fun getScheduleType(): Int {
        var type = AiDaysScheduleAdapter.PLACE

        when(binding.scheduleType.checkedRadioButtonId){
            R.id.airplane-> type = AiDaysScheduleAdapter.AIRPLANE
            R.id.hotel -> type = AiDaysScheduleAdapter.HOTEL
            R.id.place -> type = AiDaysScheduleAdapter.PLACE
        }


        return type
    }

    fun reset(){
        title = schedule.todo
        place = schedule.places
        longitude = schedule.longitude
        latitude = schedule.latitude
        cost = schedule.cost.toString()
        initRadioButton()
    }

    companion object{

        fun makeScheduleCarmeraUpdate(location: LatLng, zoom : Float) : CameraUpdate{
            val cameraOption = CameraPosition.builder()
                .target(location)
                .zoom(zoom)
                .build()
             return CameraUpdateFactory.newCameraPosition(cameraOption)
        }

        fun makeScheduleMarkerOptions(context: Context, type: Int, position: Int, location : LatLng, name : String) : MarkerOptions{
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

}