package com.sigarda.jurnalkas.ui.fragment.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.databinding.FragmentSettingBinding
import com.sigarda.jurnalkas.databinding.FragmentSpendingBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
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
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate()
    }

    private fun navigate(){
        goToAccount()
        goToDrive()
        goToAbout()
        goToCari()
        goTokategori()
        goToBahasa()
        goToPremium()
        goToBuku()
        goToTagihan()
    }
    private fun goToAccount (){
        binding.akun.setOnClickListener(){
            findNavController().navigate(R.id.action_settingFragment_to_profileFragment)
        }
    }
    private fun goToDrive (){
        binding.Gd.setOnClickListener(){
            Toast.makeText(requireContext(), "Fitur Belum Tersedia", Toast.LENGTH_LONG).show()
        }
    }
    private fun goToAbout (){
        binding.about.setOnClickListener(){
            findNavController().navigate(R.id.action_settingFragment_to_aboutFragment)
        }
    }

    private fun goToBahasa (){
        binding.bahasa.setOnClickListener(){
            Toast.makeText(requireContext(), "Fitur Belum Tersedia", Toast.LENGTH_LONG).show()
        }
    }

    private fun goToBuku (){
        binding.book.setOnClickListener(){
            Toast.makeText(requireContext(), "Fitur Belum Tersedia", Toast.LENGTH_LONG).show()
        }
    }

    private fun goToPremium (){
        binding.premium.setOnClickListener(){
            Toast.makeText(requireContext(), "Fitur Belum Tersedia", Toast.LENGTH_LONG).show()
        }
    }

    private fun goToCari (){
        binding.cari.setOnClickListener(){
            Toast.makeText(requireContext(), "Fitur Belum Tersedia", Toast.LENGTH_LONG).show()
        }
    }

    private fun goTokategori (){
        binding.kategori.setOnClickListener(){
            Toast.makeText(requireContext(), "Fitur Belum Tersedia", Toast.LENGTH_LONG).show()
        }
    }
    private fun goToTagihan (){
        binding.tagihan.setOnClickListener(){
            Toast.makeText(requireContext(), "Fitur Belum Tersedia", Toast.LENGTH_LONG).show()
        }
    }
}