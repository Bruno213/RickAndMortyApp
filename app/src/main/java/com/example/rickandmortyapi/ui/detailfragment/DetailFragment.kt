package com.example.rickandmortyapi.ui.detailfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.rickandmortyapi.databinding.FragmentDetailBinding
import com.example.rickandmortyapi.model.Character
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        val jsonString = arguments?.get("character") as String
        val character: Character = Gson().fromJson(jsonString, Character::class.java)

        binding.txtIdCharacter.text = character.id.toString()
        binding.txtName.text = character.name
        binding.txtStatus.text = character.status
        binding.txtGender.text = character.gender
        binding.txtSpecie.text = character.species

        binding.txtLocation.text = character.location.name
        binding.txtOrigin.text = character.origin.name

        Picasso.get().load(character.image).into(binding.imgCharacterDetail)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}