package com.sigarda.jurnalkas.ui.fragment.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.ui.activity.MainActivity



abstract class BaseFragment : Fragment() {


    open var toolbarVisibility = false
    open var bottomNavigationViewVisibility = View.VISIBLE

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            val mainActivity =  activity as MainActivity
            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
            mainActivity.setToolbarVisibility(toolbarVisibility)
        }
    }


}