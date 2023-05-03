package com.sigarda.jurnalkas.ui.fragment.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.auth.User
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.databinding.FragmentProfileBinding
import com.sigarda.jurnalkas.databinding.FragmentSettingBinding
import com.sigarda.jurnalkas.databinding.FragmentSpendingBinding
import com.sigarda.jurnalkas.ui.fragment.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()
    private val userViewModel: LoginViewModel by viewModels()

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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logout()
        navigate()
    }

    private fun logout() {
        binding.btLogout.setOnClickListener {
            viewModel.signOut()
            viewModel.statusLogin(false)
            viewModel.getLoginStatus().observe(viewLifecycleOwner) {
                if (it == true) {
                    findNavController().navigate(R.id.action_profileFragment_to_splashFragment)
                } else {
                    requireContext()
                }
            }
        }

    }

    private fun navigate(){
        toEditProfile()
    }

    private fun toEditProfile(){
        binding.editProfile.setOnClickListener(){
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }
}

data class ProfileState(
    val isLoading: Boolean = false,
    val error: String = "",
    val user: User? = null,
    val result: Int? = null
)