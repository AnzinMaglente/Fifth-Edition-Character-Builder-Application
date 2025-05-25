package com.example.fiftheditioncharacterbuilder

import android.app.ActivityOptions
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.example.fiftheditioncharacterbuilder.databinding.ActivityOtherDetailsBinding
import com.example.fiftheditioncharacterbuilder.fragments.StatsTabAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class OtherDetails : AppCompatActivity() {

    private lateinit var binding: ActivityOtherDetailsBinding

    private val keyCharPers = stringPreferencesKey("personality")
    private val keyCharIdl = stringPreferencesKey("ideals")
    private val keyCharBnd = stringPreferencesKey("bonds")
    private val keyCharFlws = stringPreferencesKey("")
    private val keyCharItms = stringPreferencesKey("items")

    // Checks if any value is empty, if so deletes the value.
    private fun removeValuesViaIteration(listWithNullsAndEmpty: MutableList<String>): List<String> {
        val iterator = listWithNullsAndEmpty.iterator()
        while (iterator.hasNext()) {
            val element = iterator.next()
            if (element.isEmpty()) {
                iterator.remove()
            }
        }
        return listWithNullsAndEmpty
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtherDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Creates a tab adapter.
        binding.statsDesc.adapter = StatsTabAdapter(this)

        // Explains to the kotlin file how the tab would work.
        TabLayoutMediator(
            binding.statsPages,
            binding.statsDesc
        ) { tab, position ->
            tab.text =
                when (position) {
                    0 -> "1"
                    1 -> "2"
                    else -> "3"
                }
        }.attach()

        lifecycleScope.launch {
            val savedCharPers = dataStore.data.map {
                it[keyCharPers] ?: ""
            }.first()

            val savedCharIdl = dataStore.data.map {
                it[keyCharIdl] ?: ""
            }.first()

            val savedChaBnd = dataStore.data.map {
                it[keyCharBnd] ?: ""
            }.first()

            val savedChaFlws = dataStore.data.map {
                it[keyCharFlws] ?:""
            }.first()

            var savedChaItm = dataStore.data.map {
                it[keyCharItms] ?: ""
            }.first()

            binding.persEdt.setText(savedCharPers)
            binding.idlEdt.setText(savedCharIdl)
            binding.bndEdt.setText(savedChaBnd)
            binding.flwsEdt.setText(savedChaFlws)

            // Since the data is stored as a string, this converts it to a list using the commas as
            // delimiters to indicate when it is a new value.
            val itmList : MutableList<String> = savedChaItm.split(",").toMutableList()

            // Deletes leading whitespace.
            for (i in itmList.indices) {
                itmList[i] = itmList[i].trimStart()
            }

            // Removes any empty elements inside the list
            removeValuesViaIteration(itmList)

            // creates an adapter to place the list inside the listview element.
            var arrayItmAdapter = ArrayAdapter(this@OtherDetails, R.layout.listview_item, itmList)
            binding.equipLV.setAdapter(arrayItmAdapter)

            binding.saveInfoBtn.setOnClickListener {
                lifecycleScope.launch {
                    dataStore.edit { prefs ->
                        prefs[keyCharPers] = binding.persEdt.text.toString()
                        prefs[keyCharIdl] = binding.idlEdt.text.toString()
                        prefs[keyCharBnd] = binding.bndEdt.text.toString()
                        prefs[keyCharFlws] = binding.flwsEdt.text.toString()
                        prefs[keyCharItms] = savedChaItm
                    }
                    Toast.makeText(this@OtherDetails, "Information Saved.", Toast.LENGTH_SHORT).show()
                }
            }

            // This is used to add a new item to the equipment list.
            binding.addBtn.setOnClickListener {
                val builder = AlertDialog.Builder(this@OtherDetails)
                builder.setTitle("Add item")

                // This is used to create a custom layout for alerts.
                val customLayout: View = layoutInflater.inflate(R.layout.custom_alert, null)
                builder.setView(customLayout)

                builder.setPositiveButton("OK") { _: DialogInterface?, _: Int ->

                    val editText: EditText = customLayout.findViewById(R.id.edt_field)
                    if (editText.text.toString() == "") {
                        Toast.makeText(this@OtherDetails, "Please Add an Item.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@OtherDetails, "Added New Item.", Toast.LENGTH_SHORT).show()

                        itmList.add(editText.text.toString())
                        savedChaItm = itmList.joinToString()

                        lifecycleScope.launch {
                            dataStore.edit { prefs ->
                                prefs[keyCharItms] = savedChaItm
                            }
                        }

                        arrayItmAdapter = ArrayAdapter(this@OtherDetails, R.layout.listview_item, itmList)
                        binding.equipLV.setAdapter(arrayItmAdapter)
                    }
                }

                val dialog = builder.create()
                dialog.show()
            }

            // This is used to delete a new item to the equipment list.
            binding.deleteBtn.setOnClickListener{
                val builder = AlertDialog.Builder(this@OtherDetails)
                builder.setTitle("Delete item")

                val customLayout: View = layoutInflater.inflate(R.layout.custom_alert, null)
                builder.setView(customLayout)

                builder.setPositiveButton("OK") { _: DialogInterface?, _: Int ->
                    val editText: EditText = customLayout.findViewById(R.id.edt_field)
                    if (itmList.contains(editText.text.toString())) {
                        itmList.remove(editText.text.toString())
                        Toast.makeText(this@OtherDetails, "Item successfully deleted.", Toast.LENGTH_SHORT).show()

                        savedChaItm = itmList.joinToString()
                        lifecycleScope.launch {
                            dataStore.edit { prefs ->
                                prefs[keyCharItms] = savedChaItm
                            }
                        }
                        arrayItmAdapter = ArrayAdapter(this@OtherDetails, R.layout.listview_item, itmList)
                        binding.equipLV.setAdapter(arrayItmAdapter)
                    }
                    else {
                        Toast.makeText(this@OtherDetails, "Item not found.", Toast.LENGTH_SHORT).show()
                    }
                }

                val dialog = builder.create()
                dialog.show()
            }
        }

        binding.backBtn.setOnClickListener {
            val intent = Intent(this@OtherDetails, CharacterCreation::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            startActivity(intent, options.toBundle())
            finish()
        }
    }
}