package com.example.fiftheditioncharacterbuilder.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.example.fiftheditioncharacterbuilder.R
import com.example.fiftheditioncharacterbuilder.dataStore
import com.example.fiftheditioncharacterbuilder.databinding.FragmentStatTab3Binding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class StatTab3Fragment : Fragment() {
    private lateinit var binding: FragmentStatTab3Binding

    private val keyCharDex = stringPreferencesKey("dexterity")
    private val keyCharWis = stringPreferencesKey("wisdom")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stat_tab_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatTab3Binding.bind(view)

        val pref = requireContext().dataStore

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
            val savedCharDex = pref.data.map {
                it[keyCharDex] ?: "10"
            }.first()


            val savedCharWis = pref.data.map {
                it[keyCharWis] ?: "10"
            }.first()

            val dexBase = savedCharDex.toInt()
            val wisBase = savedCharWis.toInt()

            val stealthModifier =  modifierToString(evaluateModifier(dexBase))
            binding.stealthTxt.text =  stealthModifier

            val survivalModifier = modifierToString(evaluateModifier(wisBase))
            binding.stealthTxt.text = survivalModifier
        }
    }
}