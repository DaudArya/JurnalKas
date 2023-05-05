package com.sigarda.jurnalkas.ui.fragment.profile

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.node.getOrAddAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.auth.User
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.databinding.FragmentProfileBinding
import com.sigarda.jurnalkas.databinding.FragmentSettingBinding
import com.sigarda.jurnalkas.databinding.FragmentSpendingBinding
import com.sigarda.jurnalkas.ui.fragment.login.LoginViewModel
import com.sigarda.jurnalkas.wrapper.Extension.gone
import com.sigarda.jurnalkas.wrapper.Extension.visible
import com.sigarda.jurnalkas.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import java.io.File

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var image_uri: Uri? = null
    private var imageFile: File? = null
    private var imageMultiPart: MultipartBody.Part? = null

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
        observeGet()
        getProfile()
    }
    private fun observeGet(){
        viewModel.GetProfileUserResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success ->{
                    Log.d("GetUserProfileResponse", it.data.toString())
                    binding.apply {
                        tvFullName.setText("${it.data?.data?.username.toString()}")
                        tvUsername.setText("${it.data?.data?.name.toString()}")
                        tvEmail.setText("${it.data?.data?.email.toString()}")
                        tvId.setText("${it.data?.data?.id.toString()}")

                        var fullName = it.data?.data?.username
                        if (fullName != null){
                            tvFullName.setText("${it.data?.data?.username.toString()}")
                        }else {
                            tvFullName.setText("User-${it.data?.data?.id.toString()}")
                        }


                        var avatar = it.data?.data?.avatar
                        if (avatar != null){
                            Glide.with(requireContext())
                                .load(it.data?.data?.avatar)
                                .circleCrop()
                                .into(binding.ivAva)
                        }
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Reload Gagal : ObserveGet", Toast.LENGTH_LONG).show()

                }
                is Resource.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }
                else -> {}
            }

        }
    }

    private fun getProfile() {
        userViewModel.getDataStoreToken().observe(viewLifecycleOwner) {
            viewModel.GetProfileUser("Bearer $it")
        }
        viewModel.data.observe(viewLifecycleOwner) {
            binding.apply {
                if (it != null) {
                    tvUsername.setText("${it.data?.name.toString()}")
                    tvEmail.setText("${it.data?.email.toString()}")
                    tvId.setText("${it.data?.id.toString()}")

                    var fullName = it.data?.username
                    if (fullName != null){
                        tvFullName.setText("${it.data?.username.toString()}")
                    }else {
                        tvFullName.setText("User-${it.data?.id.toString()}")
                    }
                    var avatar = it.data?.avatar
                    if (avatar != null){
                        Glide.with(requireContext())
                            .load(it.data?.avatar)
                            .circleCrop()
                            .into(binding.ivAva)
                    }
                }
            }
        }
        userViewModel.postLoginUserResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success ->{
                    val token = "${it.data?.response?.token}"
                    if (token != ""){
                        Log.d("TokenResponse", it.data.toString())
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Reload Gagal : GetProfile", Toast.LENGTH_LONG).show()
                }
                is Resource.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }

                else -> {} }
        }}

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