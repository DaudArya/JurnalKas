package com.sigarda.jurnalkas.ui.fragment.register

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
import com.sigarda.jurnalkas.data.network.model.auth.register.RegisterRequestBody
import com.sigarda.jurnalkas.databinding.FragmentRegisterBinding
import com.sigarda.jurnalkas.ui.fragment.base.BaseFragment
import com.sigarda.jurnalkas.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel : RegisterViewModel by viewModels()

    private val existUsername = listOf<String>("shawn","peter","raul","mendes")
    private val existEmail = listOf<String>("shawn@test.com","peter@test.com","raul@test.com","mendes@test.com")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toLoginButton.setOnClickListener { openLogin() }
        observeEvent()
        observeData()
        initViews()
    }

    private fun openLogin() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }
    private fun openVerify() {
        findNavController().navigate(R.id.action_registerFragment_to_verifyFragment)
    }
    private fun initViews() {
        binding.registerButton.setOnClickListener {
            viewModel.signInWithEmailAndPassword(
                email = binding.emailEditText.text.toString(),
                name = binding.usernameEditText.text.toString(),
                password = binding.passwordEditText.text.toString(),
                verifyPassword = binding.confirmPasswordEditText.text.toString()
            )
            register()
            binding.emailEditText.text?.clear()
            binding.usernameEditText.text?.clear()
            binding.passwordEditText.text?.clear()
            binding.confirmPasswordEditText.text?.clear()

        }
        bottomNavigationViewVisibility = View.GONE
    }

    private fun observeEvent() {
        viewModel.authResult.observe(viewLifecycleOwner) {

            when(it) {
                is SignUpState.SendEmailIsSuccess -> {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.successSignUp),
                        Snackbar.LENGTH_LONG
                    )
                        .apply {
                            setAction(getString(R.string.login)) {
                                findNavController().navigate(R.id.action_registerFragment_to_verifyFragment)
                            }
                            show()
                        }
                }
                is SignUpState.SendEmailIsFailure-> {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.failSignUp),
                        Snackbar.LENGTH_LONG
                    )
                        .apply {
                            setAction(getString(R.string.Retry)) {
                            }
                            show()
                        }
                }
                is SignUpState.SignUpIsSuccess-> {
                    viewModel.saveUser(
                        getFirebaseUserUid = it.result.user?.uid!!,
                        email = binding.emailEditText.text.toString(),
                        name = binding.usernameEditText.text.toString(),
                        password = binding.passwordEditText.text.toString()
                    )
                }
                is SignUpState.SignUpIsFailure -> {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.failSignUp),
                        Snackbar.LENGTH_LONG
                    )
                        .apply {
                            setAction(getString(R.string.Retry)) {
                            }
                            show()
                        }
                }
                is SignUpState.Loading-> {
                    if(it.visibility) {
                    }else {

                    }
                }
            }
        }
    }

    private fun register() {
        if (validateInput()) {
            applyRegister()
        }
    }

    private fun registerUser(username: String,email: String, password: String) {
        viewModel.postRegisterUser(RegisterRequestBody(name = username, email = email, password = password))
        Log.d("register", RegisterRequestBody(name = username, email = email, password = password).toString())
    }

    private fun observeData() {

        viewModel.postRegisterUserResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (it.data?.metadata?.message.equals("User created successfully")) {
                        Toast.makeText(requireContext(), it.data?.metadata?.message, Toast.LENGTH_LONG).show()
                        Toast.makeText(requireContext(), "Register User Success, Please Login", Toast.LENGTH_SHORT).show()
                        openVerify()
                        Log.d("registerresponse", it.data?.metadata?.message.toString())
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Register Gagal : Email Telah Terdaftar", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }

    }

    private fun applyRegister() {
        val email = binding.emailEditText.text.toString()
        val username = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val password2 = binding.confirmPasswordEditText.text.toString()


        registerUser(username,email,password)

    }

    private fun validateInput(): Boolean {
        var isValid = true
        val email = binding.emailEditText.text.toString().trim()
        val username = binding.usernameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val passwordConfrim = binding.confirmPasswordEditText.text.toString().trim()



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
        if (username.isEmpty()) {
            isValid = false
            Toast.makeText(requireContext(), "Username must not be empty", Toast.LENGTH_SHORT)
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

        if (passwordConfrim != password) {
            isValid = false
            Toast.makeText(
                requireContext(),
                "Confirmation Password Is Wrong",
                Toast.LENGTH_SHORT
            ).show()
        }
        return isValid
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun validateRegisterFragmentInput(
        username: String,
        password: String,
        repeatPassword: String,
        email: String

    ): Boolean {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()){
            return false
        }

        if (username in existUsername) {
            return false
        }

        if (password.length < 6) {
            false
        }

        if (password.length > 50) {
            false
        }
        if (password != repeatPassword){
            return false
        }

        if (email in existEmail) {
            return false
        }

        return true
    }

}

sealed class SignUpState {
    data class SendEmailIsSuccess(val value: String): SignUpState()
    data class SendEmailIsFailure(val value: String): SignUpState()
    data class SignUpIsSuccess(val result: AuthResult): SignUpState()
    data class SignUpIsFailure(val value: String): SignUpState()
    data class Loading(val visibility: Boolean): SignUpState()
}