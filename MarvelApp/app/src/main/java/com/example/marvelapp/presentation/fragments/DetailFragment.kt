package com.example.marvelapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import coil.api.load
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentDetailBinding
import com.example.marvelapp.domain.entities.Character
import com.example.marvelapp.presentation.presenters.DetailPresenter
import com.example.marvelapp.presentation.views.DetailView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

@AndroidEntryPoint
class DetailFragment : MvpAppCompatFragment(), DetailView {
    @Inject
    @InjectPresenter
    lateinit var detailPresenter: DetailPresenter

    @ProvidePresenter
    fun provideDetailPresenter(): DetailPresenter = detailPresenter
    private val characterId: Int? by lazy {
        arguments?.getInt(CHARACTER_ID, -1)
    }
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterId?.let { if (it != -1) detailPresenter.getCharacter(it) }
    }


    private fun bindInfo(character: Character) {
        with(binding) {
            tvName.text = character.name
            tvDescription.text = character.description
            ivImage.load(character.imageURI)
        }
    }

    private fun manageLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.viewsGroup.isVisible = !isLoading
    }

    override fun showCharacter(character: Character) = bindInfo(character)

    override fun showLoading() = manageLoading(true)

    override fun hideLoading() = manageLoading(false)

    override fun showError(error: Throwable) {
        manageLoading(false)
        Snackbar.make(
            binding.root,
            getString(R.string.error),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val CHARACTER_ID = "CHARACTER_ID"

        fun newInstance(characterId: Int) =
            DetailFragment().apply { arguments = bundleOf(CHARACTER_ID to characterId) }
    }
}
