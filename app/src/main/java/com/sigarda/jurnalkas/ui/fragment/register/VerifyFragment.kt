package com.sigarda.jurnalkas.ui.fragment.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.databinding.FragmentHomeBinding
import com.sigarda.jurnalkas.databinding.FragmentRegisterBinding
import com.sigarda.jurnalkas.databinding.FragmentVerifyBinding
import com.sigarda.jurnalkas.ui.fragment.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VerifyFragment : BaseFragment() {
    private var _binding: FragmentVerifyBinding? = null
    private val binding get() = _binding!!


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
        _binding = FragmentVerifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationViewVisibility = View.GONE
        binding.back.setOnClickListener{
            toGmail()
        }
        Snackbar.make(
            binding.root,
            getString(R.string.successSignUp),
            Snackbar.LENGTH_LONG
        )
            .apply {
                setAction(getString(R.string.login)) {
                    findNavController().navigate(R.id.action_verifyFragment_to_loginFragment)
                }
                show()
            }


    }

    private fun toGmail(){
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://mail.google.com/mail/mu/mp/70/#tl/priority/%5Esmartlabel_personal") }
        if (intent != null) {
            startActivity(intent)
        }
        else{
            Toast.makeText(requireContext(), "Sorry You Dont Have Email Apps", Toast.LENGTH_SHORT).show()
        }
    }


}