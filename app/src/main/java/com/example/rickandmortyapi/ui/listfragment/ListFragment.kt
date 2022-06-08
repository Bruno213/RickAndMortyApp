package com.example.rickandmortyapi.ui.listfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rickandmortyapi.R
import com.example.rickandmortyapi.Repositories.Repository
import com.example.rickandmortyapi.databinding.FragmentListBinding
import com.example.rickandmortyapi.ui.viewmodel.SharedViewModel
import com.example.rickandmortyapi.ui.viewmodel.SharedViewModelFactory
import com.example.rickandmortyapi.util.PaginationListener

class ListFragment : Fragment(),
    androidx.appcompat.widget.SearchView.OnQueryTextListener {
    private val sharedViewModel: SharedViewModel by activityViewModels { SharedViewModelFactory(Repository()) }

    private val listAdapter = CharacterAdapter()

    private val binding get() = _binding!!
    private var _binding: FragmentListBinding? = null

    private var verifySearch = true
    private var isFirstLoad = true

    private val sharedPrefs by lazy { this.context?.getSharedPreferences("filters", Context.MODE_PRIVATE) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(isFirstLoad)
            getFirstPageFromViewModel()


        binding.apply {
            searchView.setOnQueryTextListener(this@ListFragment)

            sharedViewModel.listCharactersFirstPage.observe(viewLifecycleOwner) {
                if (it.isSuccessful) {

                    if(isFirstLoad) {
                        sharedViewModel.pages = it.body()!!.info.pages

                        listAdapter.setListCharacters(it.body()!!.results)
                        isFirstLoad = false
                    }
                }
            }


            sharedViewModel.listCharacters.observe(viewLifecycleOwner) { response ->
                if (response!=null)
                    if (response.isSuccessful) {

                        sharedViewModel.pages = response.body()!!.info.pages

                        response.body()!!.info

                        if(isFirstLoad) {
                            listAdapter.setListCharacters(response.body()!!.results)
                            isFirstLoad = false
                        } else {
                            if(sharedViewModel.filterOnFirstTime) {
                                listAdapter.setListCharacters(response.body()!!.results)
                                sharedViewModel.filterOnFirstTime = false
                            }
                            else
                                listAdapter.addListCharacters(response.body()!!.results)
                        }

                        txtApiError.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    } else {

                        txtApiError.text = getString(R.string.text_error, response.code())
                        txtApiError.visibility = View.VISIBLE
                        recyclerView.visibility = View.INVISIBLE
                    }
            }

            recyclerView.apply {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = listAdapter

                addOnScrollListener(
                    object : PaginationListener() {
                        override fun loadNextPage() {
                            //Toast.makeText(context, "Not Scrollable", Toast.LENGTH_SHORT).show()
                            if(verifySearch) {
                                sharedViewModel.increasePageCount()

                                if(sharedViewModel.isFilter.value == true) {
                                    val chipChecked = sharedPrefs?.getString("filter1", "")
                                    val radioChecked = sharedPrefs?.getString("filter2", "")

                                    if(chipChecked != null && radioChecked != null) {

                                        if(chipChecked.isNotEmpty() && radioChecked.isNotEmpty()) {
                                            sharedViewModel.pages

                                            sharedViewModel.getCharactersByStatusAndGender(chipChecked,
                                                radioChecked, sharedViewModel.getCurrentPageList())
                                        }
                                        if(chipChecked.isNotEmpty() && radioChecked.isEmpty()) {
                                            sharedViewModel.getCharactersByStatus(chipChecked, sharedViewModel.getCurrentPageList())
                                        }
                                        if(chipChecked.isEmpty() && radioChecked.isNotEmpty()) {
                                            sharedViewModel.getCharactersByGender(radioChecked, sharedViewModel.getCurrentPageList())
                                        }

                                    }
                                } else {
                                    sharedViewModel.getCharacters(sharedViewModel.getCurrentPageList())
                                }

                            }
                        }

                        override fun isLastPage(): Boolean {
                            return sharedViewModel.getCurrentPageList() >= sharedViewModel.pages
                        }
                    })
            }

            sharedViewModel.isFilter.observe(viewLifecycleOwner) {
                if (it) btnReset.visibility = View.VISIBLE else btnReset.visibility = View.INVISIBLE
            }

            btnReset.setOnClickListener {
                reset()
            }

            btnFilter.setOnClickListener {
                findNavController().navigate(R.id.action_listFragment_to_filterFragment)
            }
        }
    }

    private fun getFirstPageFromViewModel() {
        sharedViewModel.getFirstPage()
    }

    private fun reset() {
        val editor = sharedPrefs?.edit()
        editor?.putString("filter1", "")
        editor?.putString("filter2", "")
        editor?.commit()

        sharedViewModel.currentPage = 1
        sharedViewModel.isFilter.value = false

        sharedViewModel.filterValue.value = arrayOf(0, 0)

        isFirstLoad = true
        sharedViewModel.listCharacters.value = sharedViewModel.listCharactersFirstPage.value
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        listAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            verifySearch = it.isEmpty()
        }
        listAdapter.filter.filter(newText)
        return false
    }

    override fun onPause() {
        super.onPause()
        sharedViewModel.clearList()
    }
}