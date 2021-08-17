package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import ru.netology.nmedia.MapViewModel
import ru.netology.nmedia.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.pager.adapter = MainPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.pager.isUserInputEnabled = false

        val viewModel by viewModels<MapViewModel>(ownerProducer = ::requireActivity)
        viewModel.selectedPlace.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.pager.currentItem = 0
            }
        }

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> "Карта"
                else -> "Места"
            }
        }.attach()

        return binding.root
    }
}