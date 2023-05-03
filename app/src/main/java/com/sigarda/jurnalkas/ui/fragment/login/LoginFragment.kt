package com.sigarda.jurnalkas.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.data.network.model.auth.login.email.LoginRequestBody
import com.sigarda.jurnalkas.databinding.FragmentLoginBinding
import com.sigarda.jurnalkas.ui.fragment.base.BaseFragment
import com.sigarda.jurnalkas.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()


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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationViewVisibility = View.GONE
        toolbarVisibility = false

        observeDataLogin()
        observeEvent()
        initViews()
        binding.toRegisterButton.setOnClickListener { openRegister() }
        binding.loginButton.setOnClickListener (){
            loginUser()
            SaveToken()
        }
    }

    private fun observeEvent() {
        viewModel.authResult.observe(viewLifecycleOwner) {
            when(it) {
                is SignInState.SignInIsFailure-> {
                    Toast.makeText(context,it.value, Toast.LENGTH_SHORT).show()
                }
                is SignInState.VerificationIsFailure-> {
                    Toast.makeText(context,it.value, Toast.LENGTH_SHORT).show()
                }
                is SignInState.VerificationIsSuccess-> {
                    Toast.makeText(context,"Login successful", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                }
                is SignInState.Loading-> {

                }
            }
        }
    }
    private fun initViews() {
        with(binding) {
            loginButton.setOnClickListener {
                viewModel.signInWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }

    }
    private fun loginUser() {
        if (validateInput()) {

            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            binding.emailEditText.isEnabled = false
            binding.passwordEditText.isEnabled = false
            viewModel.login(parseFormIntoEntity(email, password))

        }
    }


    private fun observeDataLogin() {
        viewModel.postLoginUserResponse.observe(viewLifecycleOwner) {
            binding.emailEditText.isEnabled = true
            binding.passwordEditText.isEnabled = true
            when (it) {
                is Resource.Success -> {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.successSignIn),
                        Snackbar.LENGTH_LONG
                    )
                        .apply {
                            setAction(getString(R.string.ok)) {
                            }
                            show()
                        }
                    Log.d("loginResponse", it.data.toString())
                    viewModel.statusLogin(true)
                    navigateToHome()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Login Failed, Check Your Data", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
        //datastore
        viewModel.getUserLoginStatus().observe(viewLifecycleOwner) {
            Log.d("getlogin", it.toString())
            if (it) {
                navigateToHome()
            }
        }
    }
    private fun navigateToHome() {
        viewModel.setUserLogin(true)
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

    }
    private fun parseFormIntoEntity(email: String, password: String): LoginRequestBody {
        return LoginRequestBody(email, password)
    }


    private fun validateInput(): Boolean {
        var isValid = true
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isEmpty()) {
            isValid = false
            binding.emailEditText.error = "Email must not be empty"
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false
            binding.emailEditText.error = "Invalid email"
        }
        if (password.isEmpty()) {
            isValid = false
            Toast.makeText(requireContext(), "Password must not be empty", Toast.LENGTH_SHORT)
                .show()
        }
        if (password.length < 6) {
            isValid = false
            Toast.makeText(
                requireContext(),
                "Password should be at least 6 characters",
                Toast.LENGTH_SHORT
            ).show()
        }
        return isValid
    }

    private fun openRegister() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun SaveToken() {
        viewModel.postLoginUserResponse.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success ->{
                    val token = "${it.data?.response?.token}"
                    if (token != ""){
                        //ini untuk set tokennya ke datastore
                        viewModel.setUserToken(token)
                        viewModel.SaveUserToken(token)
                    } else {
                        Toast.makeText(requireContext(), "token gagal di set", Toast.LENGTH_LONG).show()
                    }
                }else -> {} }
        }
    }

}

sealed class SignInState {
    data class VerificationIsSuccess(val result: AuthResult): SignInState()
    data class VerificationIsFailure(val value: String): SignInState()
    data class SignInIsFailure(val value: String): SignInState()
    data class Loading(val visibility: Boolean): SignInState()
}