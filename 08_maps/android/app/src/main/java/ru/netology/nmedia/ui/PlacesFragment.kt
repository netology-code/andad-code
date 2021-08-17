package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.nmedia.MapViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPlacesBinding

class PlacesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel by viewModels<MapViewModel>(ownerProducer = ::requireActivity)
        val adapter = PlacesAdapter { viewModel.selectPlace(it) }

        viewModel.places.observe(viewLifecycleOwner, adapter::submitList)

        return FragmentPlacesBinding.inflate(inflater, container, false).root.also {
            it.adapter = adapter
            it.recycledViewPool.setMaxRecycledViews(R.layout.item_place, 5)
        }
    }
}