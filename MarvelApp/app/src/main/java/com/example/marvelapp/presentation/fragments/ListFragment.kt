package com.example.marvelapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentListBinding
import com.example.marvelapp.domain.entities.Character
import com.example.marvelapp.presentation.list.CharactersAdapter
import com.example.marvelapp.presentation.utils.autoCleared
import com.example.marvelapp.presentation.viewModels.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var binding: FragmentListBinding
    private var charactersAdapter: CharactersAdapter by autoCleared()
    private var characters = listOf<Character>()
    private val listViewModel: ListViewModel by viewModels()
    val disposables = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        listViewModel.onCharactersSearch("Ba")
        init()
        configureSearch()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.dispose()
    }

    private fun configureSearch() {
        binding.searchView.observeText()
            .filter { it.length > 2 && it.isNotBlank() }
            .distinctUntilChanged()
            .debounce(500L, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                onSearchSubmit(it)
            }, onError = {
                Log.e("search", it.message.toString())
            }).addTo(disposables)
    }

    private fun init() {
        charactersAdapter = CharactersAdapter()
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

    private fun initObservers() {
        listViewModel.charactersList.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                characters = it
            }, onFailure = {
                Log.e("characters error",it.message.toString())
            })
        }
    }

    fun onSearchSubmit(query: String) = listViewModel.onCharactersSearch(query)

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
