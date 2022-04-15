package com.example.marvelapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.marvelapp.databinding.FragmentDetailBinding
import com.example.marvelapp.domain.entities.Character
import com.example.marvelapp.presentation.viewModels.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val detailViewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        detailViewModel.getCharacter(args.characterId)
    }

    private fun bindInfo(character: Character) =
        with(binding) {
            tvName.text = character.name
            tvDescription.text = character.description
            ivImage.load(character.imageURI)
        }

    private fun initObservers() {
        detailViewModel.character.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                bindInfo(it)
            }, onFailure = {
                Log.e("character error", it.message.toString())
            })
        }
        detailViewModel.isLoading.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                showLoading(it)
            }, onFailure = {
                Log.e("character error", it.message.toString())
            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.viewsGroup.isVisible = !isLoading
    }
}
