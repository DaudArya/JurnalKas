package com.sigarda.jurnalkas.ui.fragment.profile

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.databinding.FragmentEditProfileBinding
import com.sigarda.jurnalkas.ui.fragment.base.BaseFragment
import com.sigarda.jurnalkas.ui.fragment.login.LoginViewModel
import com.sigarda.jurnalkas.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


@AndroidEntryPoint
class EditProfileFragment : BaseFragment() {

    private val permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var image_uri: Uri? = null
    private var imageFile: File? = null
    private var imageMultiPart: MultipartBody.Part? = null

    private var _binding: FragmentEditProfileBinding? = null
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
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeGet()
        getProfile()
        updateProfile()
    }

    private fun observeGet(){
        viewModel.GetProfileUserResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success ->{
                    Log.d("GetUserProfileResponse", it.data.toString())
                    binding.apply {
                        etFullName.setText("${it.data?.data?.name.toString()}")
                        tvEmail.setText("${it.data?.data?.email.toString()}")

                        var fullName = it.data?.data?.username
                        if (fullName != null){
                            etFullName.setText("${it.data?.data?.username.toString()}")
                        }else {
                            tvUsername.setText("User-${it.data?.data?.id.toString()}")
                            etUsername.setText("User-${it.data?.data?.id.toString()}")
                        }


                        var avatar = it.data?.data?.avatar
                        if (avatar != null){
                            Glide.with(requireContext())
                                .load(it.data?.data?.avatar)
                                .circleCrop()
                                .into(binding.photoProfile)
                        }
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Reload Gagal : ObserveGet", Toast.LENGTH_LONG).show()

                }
                is Resource.Loading ->{

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
                    etFullName.setText("${it.data?.name.toString()}")
                    tvEmail.setText("${it.data?.email.toString()}")

                    var fullName = it.data?.username
                    if (fullName != null){
                        etFullName.setText("${it.data?.username.toString()}")
                    }else {
                        tvUsername.setText("User-${it.data?.id.toString()}")
                        etUsername.setText("User-${it.data?.id.toString()}")
                    }
                    var avatar = it.data?.avatar
                    if (avatar != null){
                        Glide.with(requireContext())
                            .load(it.data?.avatar)
                            .circleCrop()
                            .into(binding.photoProfile)
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

                }

                else -> {} }
        }}

    fun openGallery() {
        binding.photoProfile.setOnClickListener {
            changePicture.launch("image/*")
        }
    }

    private val changePicture =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val contentResolver: ContentResolver = requireContext().contentResolver
                val type = contentResolver.getType(it)
                image_uri = it

                val fileNameimg = "${System.currentTimeMillis()}.png"
                binding.photoProfile.setImageURI(it)
                Toast.makeText(requireContext(), "$image_uri", Toast.LENGTH_SHORT).show()

                val tempFile = File.createTempFile("OtakKanan-", fileNameimg, null)
                imageFile = tempFile
                val inputstream = contentResolver.openInputStream(uri)
                tempFile.outputStream().use { result ->
                    inputstream?.copyTo(result)
                }
                val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())
                imageMultiPart =
                    MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
            }
        }

    private fun updateProfile(){
        userViewModel.getDataStoreToken().observe(viewLifecycleOwner) {
            viewModel.GetProfileUser("Bearer $it")
        }
        binding.accUpdate.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaType())
            val fullName = binding.etFullName.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaType())
            userViewModel.getDataStoreToken().observe(viewLifecycleOwner) {
                viewModel.updateUser(fullName,username,imageMultiPart!!,"Bearer $it")
            }
            Toast.makeText(requireContext(), "Update Success", Toast.LENGTH_SHORT).show()
            moveToProfile()
        }

        viewModel.data.observe(viewLifecycleOwner) {
            binding.apply {
                if (it != null) {
                    etFullName.setText(it.data?.name)
                    etUsername.setText(it.data?.username)
                }
            }
        }
        openGallery()
    }

    private fun moveToProfile() {
        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)

    }


}