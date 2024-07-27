package com.project.how.view.dialog.bottom_sheet_dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.adapter.recyclerview.schedule.AiDaysScheduleAdapter
import com.project.how.data_class.recyclerview.DaysSchedule
import com.project.how.databinding.EditScheduleBottomSheetBinding
import com.project.how.interface_af.OnScheduleListener
import com.project.how.view.dialog.bottom_sheet_dialog.ratio.BottomSheetRatioHeightManager
import com.project.how.view.map_helper.CameraOptionProducer
import com.project.how.view.map_helper.MarkerProducer
import kotlinx.coroutines.launch


class EditScheduleBottomSheetDialog(private val lat : Double, private val lng : Double, private val schedule : DaysSchedule, private val position : Int, private val onScheduleListener: OnScheduleListener)
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
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            BottomSheetRatioHeightManager().setMaxRatio(bottomSheetDialog, requireContext())
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = false

                behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                            behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        if (slideOffset < 0) {
                            behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }
                })
            }
        }
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

            Places.initializeWithNewPlacesApiEnabled(requireContext(), apiKey)

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
                Log.i("onPlaceSelected", "Place: ${place.name}, ${place.id}\nlatitude:${place.latLng?.latitude}\tlongitude:${place.latLng?.longitude}")
                supportMapFragment.getMapAsync { map ->

                    val placeLocation = LatLng(place.latLng.latitude, place.latLng.longitude)
                    this@EditScheduleBottomSheetDialog.place = place.name
                    latitude = place.latLng.latitude
                    longitude = place.latLng.longitude

                    val camera = CameraOptionProducer().makeScheduleCameraUpdate(placeLocation, 15f)
                    val markerOptions = MarkerProducer().makeScheduleMarkerOptions(requireContext(), type, position, placeLocation, place.name)

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
        if ((schedule.latitude != null || schedule.longitude != null) && (schedule.latitude != 0.0 && schedule.longitude != 0.0)){
            val placeLocation = LatLng(schedule.latitude!!, schedule.longitude!!)
            val camera = CameraOptionProducer().makeScheduleCameraUpdate(placeLocation, 15f)
            val markerOptions = MarkerProducer().makeScheduleMarkerOptions(requireContext(), type, position, placeLocation, schedule.places)

            map.moveCamera(camera)
            map.addMarker(markerOptions)
        }else{
            val placeLocation = LatLng(lat, lng)
            val camera = CameraOptionProducer().makeScheduleCameraUpdate(placeLocation, 10f)

            map.moveCamera(camera)
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
        title = binding.scheduleTitle.text.toString()
        cost = binding.scheduleBudget.text.toString()

        val data = DaysSchedule(
            getScheduleType(),
            title,
            place,
            latitude ?: 0.0,
            longitude ?: 0.0,
            cost.toLong(),
            schedule.purchaseStatus,
            schedule.purchaseDate
        )
        onScheduleListener.onDaysScheduleListener(data, position)
        dismiss()
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

}