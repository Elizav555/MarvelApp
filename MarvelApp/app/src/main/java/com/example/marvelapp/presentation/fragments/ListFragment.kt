package com.example.marvelapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvelapp.databinding.FragmentListBinding
import com.example.marvelapp.domain.entities.Character
import com.example.marvelapp.presentation.list.CharactersAdapter
import com.example.marvelapp.presentation.presenters.ListPresenter
import com.example.marvelapp.presentation.utils.autoCleared
import com.example.marvelapp.presentation.views.CharactersListView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : MvpAppCompatFragment(), CharactersListView {
    private lateinit var binding: FragmentListBinding
    private var charactersAdapter: CharactersAdapter by autoCleared()
    private var characters = listOf<Character>()

    @Inject
    @InjectPresenter
    lateinit var listPresenter: ListPresenter

    @ProvidePresenter
    fun provideListPresenter(): ListPresenter = listPresenter
    private val disposables = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        configureSearch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.dispose()
    }

    private fun configureSearch() {
        binding.searchView.observeText()
            .filter { it.length > 1 && it.isNotBlank() }
            .distinctUntilChanged()
            .debounce(500L, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                onSearchSubmit(it)
            }, onError = {
                Log.e("search", it.message.toString())
            }).addTo(disposables)
    }

    private fun init() {
        val navigateAction = { characterId: Int -> navigateToDetail(characterId) }
        charactersAdapter = CharactersAdapter(navigateAction)
        with(binding.recyclerView) {
            adapter = charactersAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val dividerItemDecoration = DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
            addItemDecoration(dividerItemDecoration)
        }
        charactersAdapter.submitList(characters)
    }

    override fun showLoading() = manageLoading(true)

    override fun hideLoading() = manageLoading(false)

    override fun updateList(characters: List<Character>) {
        this.characters = characters
        charactersAdapter.submitList(characters)
        if (characters.isEmpty())
            Snackbar.make(
                binding.root,
                "Ups, there's no characters we can show you",
                Snackbar.LENGTH_SHORT
            ).show()
    }

    override fun showError(text: String) {
        manageLoading(false)
        Log.e("ListError", text)
        Snackbar.make(
            binding.root,
            "Some error appeared while loading characters",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun navigateToDetail(characterId: Int) {
        binding.searchView.setQuery("", false)
        listPresenter.navigateToDetails(characterId)
    }

    private fun manageLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.recyclerView.isVisible = !isLoading
    }

    fun onSearchSubmit(query: String) = listPresenter.onCharactersSearch(query)

    private fun SearchView.observeText(): Flowable<String> = Flowable.create({ emitter ->
        this.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { onSearchSubmit(it) }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { emitter.onNext(it) }
                    return true
                }
            }
        )
    }, BackpressureStrategy.LATEST)
}
