package com.example.rickandmortyapi.ui.listfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyapi.R
import com.example.rickandmortyapi.databinding.ItemListBinding
import com.example.rickandmortyapi.model.Character
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class CharacterAdapter: RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>(), Filterable {
    private var listCharacters = mutableListOf<Character>()
    private var listCharactersFiltered = mutableListOf<Character>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListBinding.inflate(inflater, parent, false)

        return CharacterViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {

        holder.bind( listCharactersFiltered[position] )

    }


    override fun getItemCount(): Int {
        return listCharactersFiltered.size
    }


    class CharacterViewHolder( private val binding: ItemListBinding ): RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.txtIdCharacter.text = character.id.toString()
            binding.txtNameCharacter.text = character.name
            binding.txtStatusCharacter.text = character.status
            Picasso.get().load(character.image).into(binding.imageCharacter)

            itemView.setOnClickListener { view ->
               val charString = Gson().toJson(character)
                val bundle = bundleOf("character" to charString)
                view.findNavController().navigate(R.id.action_listFragment_to_detailFragment, bundle)
            }

        }


    }


    fun returnListCharacter(id: Int): Character {
        return listCharactersFiltered[id]
    }


    fun setListCharacters(characters: List<Character>) {
        listCharacters.clear()
        listCharacters.addAll(characters)
        listCharactersFiltered = listCharacters
        notifyDataSetChanged()
    }

    fun addListCharacters(characters: List<Character>) {
        listCharacters.addAll(characters)
        listCharactersFiltered = listCharacters
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                listCharactersFiltered = if (charString.isEmpty()) listCharacters else {

                    val filteredList = ArrayList<Character>()

                    listCharacters
                        .filter {
                            (it.name.contains(charString))
                        }
                        .forEach { filteredList.add(it) }

                    filteredList
                }

                return FilterResults().apply { values = listCharactersFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listCharactersFiltered = if (results?.values == null) ArrayList()
                else results.values as ArrayList<Character>

                notifyDataSetChanged()
            }
        }
    }
}