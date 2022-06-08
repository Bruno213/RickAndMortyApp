package com.example.rickandmortyapi.ui.filterfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.rickandmortyapi.R
import com.example.rickandmortyapi.Repositories.Repository
import com.example.rickandmortyapi.databinding.FragmentFilterBinding
import com.example.rickandmortyapi.ui.MainActivity
import com.example.rickandmortyapi.ui.extensions.getTextButtonChecked
import com.example.rickandmortyapi.ui.extensions.getTextChipChecked
import com.example.rickandmortyapi.ui.extensions.setButtonChecked
import com.example.rickandmortyapi.ui.extensions.setChipChecked
import com.example.rickandmortyapi.ui.viewmodel.SharedViewModel
import com.example.rickandmortyapi.ui.viewmodel.SharedViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_list.*

class FilterFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels { SharedViewModelFactory(
        Repository()
    )}

    private val sharedPrefs by lazy { this.context?.getSharedPreferences("filters", Context.MODE_PRIVATE) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            sharedViewModel.filterValue.observe(viewLifecycleOwner) { item ->
                chipGroupStatus.setChipChecked(item[0])
                radioGroupGender.setButtonChecked(item[1])
            }

            clearRadioCheck.setOnClickListener { radioGroupGender.clearCheck() }

            btnApplyFilter.setOnClickListener {
                val chipChecked = chipGroupStatus.getTextChipChecked().trim()
                val radioChecked = radioGroupGender.getTextButtonChecked().trim()

                val editor = sharedPrefs?.edit()

                editor?.putString("filter1", chipChecked)
                editor?.putString("filter2", radioChecked)

                editor?.commit()

                sharedViewModel.apply {

                    if(chipChecked.isNotEmpty() && radioChecked.isNotEmpty()) {
                        getCharactersByStatusAndGender(chipChecked,
                            radioChecked,1)
                    }
                    if(chipChecked.isNotEmpty() && radioChecked.isEmpty()) {
                        getCharactersByStatus(chipChecked,1)
                    }
                    if(chipChecked.isEmpty() && radioChecked.isNotEmpty()) {
                        getCharactersByGender(radioChecked,1)
                    }
                    if(chipChecked.isEmpty() && radioChecked.isEmpty()) {
                        getCharacters(1)
                    }

                    sharedViewModel.filterOnFirstTime = true
                    filterValue.value = arrayOf( chipGroupStatus.checkedChipId, radioGroupGender.checkedRadioButtonId)
                }

                findNavController().popBackStack(R.id.listFragment, false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}