package com.sigarda.jurnalkas.ui.fragment.splash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.databinding.FragmentSplashBinding
import com.sigarda.jurnalkas.ui.fragment.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationViewVisibility = View.GONE
        toolbarVisibility = false
        startSplashScreen()
    }

    private fun startSplashScreen() {
        binding.logo.alpha = 0f
        binding.logo.animate().setDuration(1000).alpha(1f).withEndAction {
            checkCredential()
        }
    }

    private fun checkCredential() {
        viewModel.getLoginStatus().observe(viewLifecycleOwner) {
            if (it == true) {
                observeEvent()
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            }
            else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
        }

    private fun observeEvent() {
        viewModel.isCurrentUserExist.observe(viewLifecycleOwner) {

            if (it != null && it.isEmailVerified) {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }
    }
    }

