package com.project.how.view.fragment.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.project.how.R
import com.project.how.databinding.FragmentMypageBinding
import com.project.how.view_model.MemberViewModel

class MypageFragment : Fragment() {
    private var _binding : FragmentMypageBinding? = null
    private val binding : FragmentMypageBinding
        get() = _binding!!
    private val memberViewModel : MemberViewModel by viewModels()
    var name : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)
        binding.mypage = this
        binding.lifecycleOwner = viewLifecycleOwner
        memberViewModel.memberInfoLiveData.observe(viewLifecycleOwner){info ->
            Log.d("memberInfo", "name : ${info.name}")
            name = info.name
            binding.name.text = name
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { MobileAds.initialize(it) }
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d("AdListener", "Banner Ad load success.")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("AdListener", "Banner Ad load failed ${adError.responseInfo}")
            }

            override fun onAdOpened() {
                Log.d("AdListener", "Banner Ad opened")
            }

            override fun onAdClicked() {
                Log.d("AdListener", "Banner Ad clicked")
            }

            override fun onAdClosed() {
                Log.d("AdListener", "Banner Ad closed")
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if(memberViewModel.tokensSaveLiveData.value == true){
            name = memberViewModel.memberInfoLiveData.value?.name ?:resources.getString(R.string.error)
        }else{
            context?.let { context->
                memberViewModel.tokensLiveData.value?.let { tokens->
                    memberViewModel.getInfo(context, tokens.accessToken) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        name = null
        _binding = null
    }
}