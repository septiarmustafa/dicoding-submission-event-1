package com.example.dicodingevent.ui.settings

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dicodingevent.data.repository.SettingsRepository
import com.example.dicodingevent.databinding.FragmentSettingsBinding
import com.example.dicodingevent.di.SettingViewModelFactory

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding
    private val viewModel: SettingViewModel by viewModels {
        SettingViewModelFactory(SettingsRepository(requireContext()))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        viewModel.themeSetting.observe(viewLifecycleOwner) { isDarkMode ->
            binding?.switchTheme?.isChecked = isDarkMode
        }

        binding?.switchTheme?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}