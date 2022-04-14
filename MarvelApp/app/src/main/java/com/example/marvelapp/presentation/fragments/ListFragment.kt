package com.example.marvelapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentListBinding
import com.example.marvelapp.domain.entities.Character
import com.example.marvelapp.domain.useCases.GetByNameUseCase
import com.example.marvelapp.presentation.list.CharactersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {
    lateinit var binding: FragmentListBinding
    private lateinit var charactersAdapter: CharactersAdapter
    private var characters = listOf<Character>()

    @Inject
    lateinit var getByName: GetByNameUseCase
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
        init()
        configureSearch()
    }

    private fun configureSearch() {
//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                if (validateQuery(query)) {
//                    getCharacters(query)
//                    return true
//                }
//                return false
//            }
//
//            private fun validateQuery(query: String): Boolean {
//                return query.isNotBlank() && query.length > 2
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })
    }

    private fun getCharacters(name: String) = lifecycleScope.launch {
        characters = getByName(name)
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
}
