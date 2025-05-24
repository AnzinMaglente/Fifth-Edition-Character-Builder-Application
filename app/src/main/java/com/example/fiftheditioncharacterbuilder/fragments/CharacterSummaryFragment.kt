package com.example.fiftheditioncharacterbuilder.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.example.fiftheditioncharacterbuilder.CharacterCreation
import com.example.fiftheditioncharacterbuilder.R
import com.example.fiftheditioncharacterbuilder.dataStore
import com.example.fiftheditioncharacterbuilder.databinding.FragmentCharacterSummaryBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CharacterSummaryFragment : Fragment() {
    private lateinit var binding : FragmentCharacterSummaryBinding

    private val keyCharPhoto = stringPreferencesKey("photo")
    private val keyCharName = stringPreferencesKey("name")
    private val keyCharRace = intPreferencesKey("race")
    private val keyCharClass = intPreferencesKey("class")
    private val keyCharDesc = stringPreferencesKey("description")

    private val keyCharStr = stringPreferencesKey("strength")
    private val keyCharDex = stringPreferencesKey("dexterity")
    private val keyCharInt = stringPreferencesKey("intelligence")
    private val keyCharWis = stringPreferencesKey("wisdom")
    private val keyCharCha = stringPreferencesKey("charisma")
    private val keyCharCon = stringPreferencesKey("constitution")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCharacterSummaryBinding.bind(view)

        val pref = requireContext().dataStore

        fun isValidUri(uriString: String): Boolean {
            return try {
                val uri = uriString.toUri()
                uri.scheme != null && uri.scheme!!.isNotEmpty()
            } catch (e: Exception) {
                false
            }
        }

        fun modifierToString(n: Int): String {
            if (n > 0) {
                val newString = buildString {
                    append("+")
                    append(n.toString())
                }
                return newString
            } else {
                return n.toString()
            }
        }

        fun evaluateModifier(baseNumber: Int): Int {
            return if (baseNumber <= 1) { -5 }
            else if (baseNumber <= 3) { -4 }
            else if (baseNumber <= 5) { -3 }
            else if (baseNumber <= 7) { -2 }
            else if (baseNumber <= 9) { -1 }
            else if (baseNumber <= 11) { 0 }
            else if (baseNumber <= 13) { 1 }
            else if (baseNumber <= 15) { 2 }
            else if (baseNumber <= 17) { 3 }
            else if (baseNumber <= 19) { 4 }
            else if (baseNumber <= 21) { 5 }
            else { 0 }
        }

        lifecycleScope.launch {
            val savedCharPhoto = pref.data.map {
                it[keyCharPhoto] ?: ""
            }.first()

            val savedCharName = pref.data.map {
                it[keyCharName] ?: ""
            }.first()

            val savedCharRace = pref.data.map {
                it[keyCharRace] ?: 0
            }.first()

            val savedCharClass = pref.data.map {
                it[keyCharClass] ?: 0
            }.first()

            val savedCharDesc = pref.data.map {
                it[keyCharDesc] ?: ""
            }.first()

            val savedCharStr = pref.data.map {
                it[keyCharStr] ?: "10"
            }.first()

            val savedCharDex = pref.data.map {
                it[keyCharDex] ?: "10"
            }.first()

            val savedCharInt = pref.data.map {
                it[keyCharInt] ?: "10"
            }.first()

            val savedCharWis = pref.data.map {
                it[keyCharWis] ?: "10"
            }.first()

            val savedCharCha = pref.data.map {
                it[keyCharCha] ?: "10"
            }.first()

            val savedCharCon = pref.data.map {
                it[keyCharCon] ?: "10"
            }.first()

            if (savedCharPhoto.isNotEmpty() && isValidUri(savedCharPhoto)){
                Picasso.get().load(savedCharPhoto.toUri())
            }else {
                binding.charImg.setImageResource(R.drawable.avatar_default)
            }

            val raceList = resources.getStringArray(R.array.race_list)
            val classList = resources.getStringArray(R.array.class_list)

            binding.nameTxt.text = buildString {
                append("Name: ")
                append(savedCharName)
            }
            binding.raceTxt.text = buildString {
                append("Race: ")
                append(raceList[savedCharRace])
            }
            binding.classTxt.text = buildString {
                append("Race: ")
                append(classList[savedCharClass])
            }
            binding.descTxt.text = savedCharDesc

            binding.strEdt.text = savedCharStr
            binding.dexEdt.text = savedCharDex
            binding.intEdt.text = savedCharInt
            binding.wisEdt.text = savedCharWis
            binding.chaEdt.text = savedCharCha
            binding.conEdt.text = savedCharCon

            binding.strTxt.text = modifierToString(evaluateModifier(savedCharStr.toInt()))
            binding.dexTxt.text = modifierToString(evaluateModifier(savedCharDex.toInt()))
            binding.intTxt.text = modifierToString(evaluateModifier(savedCharInt.toInt()))
            binding.wisTxt.text = modifierToString(evaluateModifier(savedCharWis.toInt()))
            binding.chaTxt.text = modifierToString(evaluateModifier(savedCharCha.toInt()))
            binding.conTxt.text = modifierToString(evaluateModifier(savedCharCon.toInt()))
        }

        binding.readMoreBtn.setOnClickListener {
            val intent = Intent(context, CharacterCreation::class.java)
            startActivity(intent)
        }
    }
}