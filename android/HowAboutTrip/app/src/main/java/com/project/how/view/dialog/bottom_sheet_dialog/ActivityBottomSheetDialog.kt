package com.project.how.view.dialog.bottom_sheet_dialog

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.how.R
import com.project.how.adapter.recyclerview.RadioButtonAdapter
import com.project.how.databinding.ThemeBottomSheetBinding
import com.project.how.interface_af.OnActivityListener
import kotlinx.coroutines.launch

class ActivityBottomSheetDialog(private val onActivityListener: OnActivityListener, private val type : Int)
    : BottomSheetDialogFragment(), RadioButtonAdapter.OnItemClickListener{
    private var _binding : ThemeBottomSheetBinding? = null
    private val binding : ThemeBottomSheetBinding
        get() = _binding!!
    private val sportList : MutableList<String> = mutableListOf()
    private val natureList : MutableList<String> = mutableListOf()
    private val cityList : MutableList<String> = mutableListOf()
    private val mediaList : MutableList<String> = mutableListOf()
    private val atmosphereList : MutableList<String> = mutableListOf()
    private lateinit var sportAdapter : RadioButtonAdapter
    private lateinit var natureAdapter : RadioButtonAdapter
    private lateinit var cityAdapter : RadioButtonAdapter
    private lateinit var mediaAdapter: RadioButtonAdapter
    private lateinit var atmosphereAdapter: RadioButtonAdapter
    private val activity : MutableList<String> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            sportList.addAll(mutableListOf(getString(R.string.fishing),
                getString(R.string.hiking),
                getString(R.string.camping),
                getString(R.string.Trekking),
                getString(R.string.Boating),
                getString(R.string.skiing),
                getString(R.string.scuba_diving),
                getString(R.string.paragliding),
                getString(R.string.surfing), getString(R.string.water_skiing),
                getString(R.string.cycling), getString(R.string.golfing),
                getString(R.string.horseback_riding)))
            sportAdapter = RadioButtonAdapter(sportList, true, this@ActivityBottomSheetDialog, RadioButtonAdapter.SPORT)

            cityList.addAll(mutableListOf(getString(R.string.hotspot),
                getString(R.string.sns_trend),
                getString(R.string.selfie_spot),
                getString(R.string.shopping_text),
                getString(R.string.market),
                getString(R.string.department_store),
                getString(R.string.public_insitution),
                getString(R.string.landmark),
                getString(R.string.food_tour),
                getString(R.string.art_museum),
                getString(R.string.museum),
                getString(R.string.city_hall),
                getString(R.string.landmark_excluding_museums),
                getString(R.string.landmark_excluding_art_museums),
                getString(R.string.campus_tour), getString(R.string.cafe_tour),
                getString(R.string.fine_dining), getString(R.string.brewery_tour),
                getString(R.string.park)))
            cityAdapter = RadioButtonAdapter(cityList, true, this@ActivityBottomSheetDialog, RadioButtonAdapter.CITY)
        }
        lifecycleScope.launch {
            natureList.addAll(mutableListOf(getString(R.string.beach),
                getString(R.string.sea),
                getString(R.string.plain),
                getString(R.string.lake),
                getString(R.string.river),
                getString(R.string.mountain),
                getString(R.string.volcano),
                getString(R.string.glacier),
                getString(R.string.desert), getString(R.string.forest),
                getString(R.string.national_park), getString(R.string.hot_spring),
                getString(R.string.island), getString(R.string.mangrove_forest)))
            natureAdapter = RadioButtonAdapter(natureList, true, this@ActivityBottomSheetDialog, RadioButtonAdapter.NATURE)

            mediaList.addAll(mutableListOf(getString(R.string.blog),
                getString(R.string.drama), getString(R.string.movie),
                getString(R.string.catoon), getString(R.string.game)))
            mediaAdapter = RadioButtonAdapter(mediaList, true, this@ActivityBottomSheetDialog, RadioButtonAdapter.MEDIA)

            atmosphereList.addAll(mutableListOf(
                getString(R.string.seasonal),
                getString(R.string.romantic),
                getString(R.string.historic),
                getString(R.string.futuristic),
                getString(R.string.renowned),
                getString(R.string.adventurous),
                getString(R.string.retro),
                getString(R.string.luxurious),
                getString(R.string.artistic),
                getString(R.string.natural),
                getString(R.string.enjoying_local_cuisine),
                getString(R.string.cultured),
                getString(R.string.stress_relieving)))
            atmosphereAdapter = RadioButtonAdapter(atmosphereList, true, this@ActivityBottomSheetDialog, RadioButtonAdapter.ATMOSPHERE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.theme_bottom_sheet, container, false)
        binding.theme = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.sportList.adapter = sportAdapter
        binding.natureList.adapter = natureAdapter
        binding.cityList.adapter = cityAdapter
        binding.mediaList.adapter = mediaAdapter
        binding.atmosphereList.adapter = atmosphereAdapter
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setRatio(bottomSheetDialog, requireContext())
        }
        return binding.root
    }

    fun reset(){
        lifecycleScope.launch {
            sportAdapter.reset()
            sportAdapter.notifyDataSetChanged()
            natureAdapter.reset()
            natureAdapter.notifyDataSetChanged()
            cityAdapter.reset()
            cityAdapter.notifyDataSetChanged()
            mediaAdapter.reset()
            mediaAdapter.notifyDataSetChanged()
            atmosphereAdapter.reset()
            atmosphereAdapter.notifyDataSetChanged()
        }

    }

    fun confirm(){
        lifecycleScope.launch {
            getSport()
            getNature()
            getCity()
            getMedia()
            getAtmosphere()

            onActivityListener.onActivityListener(activity, type)
            dismiss()
        }
    }

    private fun getSport(){
        activity.addAll(sportAdapter.getDatas())
    }

    private fun getNature(){
        activity.addAll(natureAdapter.getDatas())
    }

    private fun getCity(){
        activity.addAll(cityAdapter.getDatas())
    }

    private fun getMedia(){
        activity.addAll(mediaAdapter.getDatas())
    }

    private fun getAtmosphere(){
        activity.addAll(atmosphereAdapter.getDatas())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClickListener(data: String, type: Int) {
    }

    companion object{
        const val BASIC = 0
        const val EXCLUDING = -1

        fun setRatio(bottomSheetDialog : BottomSheetDialog, context : Context){
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
            val behavior = BottomSheetBehavior.from<View>(bottomSheet)
            val layoutParams = bottomSheet!!.layoutParams
            layoutParams.height = getWindowHeight(context)
            bottomSheet.layoutParams = layoutParams
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

        }

        fun getWindowHeight(context : Context) : Int {
            val displayMetrics = DisplayMetrics()
            (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }
    }
}