package com.example.fiftheditioncharacterbuilder

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.example.fiftheditioncharacterbuilder.databinding.ActivityCharacterCreationBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CharacterCreation : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterCreationBinding
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>

    private val keyCharPhoto = stringPreferencesKey("photo")
    private val keyCharName = stringPreferencesKey("name")
    private val keyCharRace = intPreferencesKey("race")
    private val keyCharClass = intPreferencesKey("class")
    private val keyCharLvl = stringPreferencesKey("level")
    private val keyCharBackground = intPreferencesKey("background")
    private val keyCharAlignment = intPreferencesKey("alignment")
    private val keyCharDesc = stringPreferencesKey("description")
    private val keyCharExp = stringPreferencesKey("experience")

    private val keyCharStr = stringPreferencesKey("strength")
    private val keyCharDex = stringPreferencesKey("dexterity")
    private val keyCharInt = stringPreferencesKey("intelligence")
    private val keyCharWis = stringPreferencesKey("wisdom")
    private val keyCharCha = stringPreferencesKey("charisma")
    private val keyCharCon = stringPreferencesKey("constitution")

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCharacterCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // This builds a string depending on what modifier they got inside the
        // evaluateModifier function.
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

        // This evaluates each base stat and changes it to a certain modifier.
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

        // Checks if the URI is a valid resource.
        fun isValidUri(uriString: String): Boolean {
            return try {
                val uri = uriString.toUri()
                uri.scheme != null && uri.scheme!!.isNotEmpty()
            } catch (e: Exception) {
                false
            }
        }

        // These create adapters for spinners.
        val arrayAdapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.race_list))
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.raceDd.adapter = arrayAdapter1

        val arrayAdapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.class_list))
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.classDd.adapter = arrayAdapter2

        val arrayAdapter3 = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.background_list))
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.backgroundDd.adapter = arrayAdapter3

        val arrayAdapter4 = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.alignment_list))
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.alignmentDd.adapter = arrayAdapter4

        // Gets image from the photo gallery.
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.charImg.setImageURI(it)
            }
        }

        // Places the image to the imageView.
        binding.charImg.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        lifecycleScope.launch {
            val savedCharPhoto = dataStore.data.map {
                it[keyCharPhoto] ?: ""
            }.first()

            val savedCharName = dataStore.data.map {
                it[keyCharName] ?: ""
            }.first()

            val savedCharRace = dataStore.data.map {
                it[keyCharRace] ?: 0
            }.first()

            val savedCharClass = dataStore.data.map {
                it[keyCharClass] ?: 0
            }.first()

            val savedCharLvl = dataStore.data.map {
                it[keyCharLvl] ?: "1"
            }.first()

            val savedCharBackground = dataStore.data.map {
                it[keyCharBackground] ?: 0
            }.first()

            val savedCharAlignment = dataStore.data.map {
                it[keyCharAlignment] ?: 0
            }.first()

            val savedCharDesc = dataStore.data.map {
                it[keyCharDesc] ?: ""
            }.first()

            val savedCharExp = dataStore.data.map {
                it[keyCharExp] ?: ""
            }.first()

            val savedCharStr = dataStore.data.map {
                it[keyCharStr] ?: "10"
            }.first()

            val savedCharDex = dataStore.data.map {
                it[keyCharDex] ?: "10"
            }.first()

            val savedCharInt = dataStore.data.map {
                it[keyCharInt] ?: "10"
            }.first()

            val savedCharWis = dataStore.data.map {
                it[keyCharWis] ?: "10"
            }.first()

            val savedCharCha = dataStore.data.map {
                it[keyCharCha] ?: "10"
            }.first()

            val savedCharCon = dataStore.data.map {
                it[keyCharCon] ?: "10"
            }.first()

            if (savedCharPhoto.isNotEmpty() && isValidUri(savedCharPhoto)){
                Picasso.get().load(savedCharPhoto.toUri())
            }else {
                binding.charImg.setImageResource(R.drawable.avatar_default)
            }

            // sets editText and dropdowns into the saved info
            binding.nameEdt.setText(savedCharName)
            binding.raceDd.setSelection(savedCharRace)
            binding.classDd.setSelection(savedCharClass)

            binding.lvlEdt.setText(savedCharLvl)
            binding.backgroundDd.setSelection(savedCharBackground)
            binding.alignmentDd.setSelection(savedCharAlignment)
            binding.expEdt.setText(savedCharExp)
            binding.descEdt.setText(savedCharDesc)

            binding.strEdt.setText(savedCharStr)
            binding.dexEdt.setText(savedCharDex)
            binding.intEdt.setText(savedCharInt)
            binding.wisEdt.setText(savedCharWis)
            binding.chaEdt.setText(savedCharCha)
            binding.conEdt.setText(savedCharCon)

            binding.strTxt.text = modifierToString(evaluateModifier(savedCharStr.toInt()))
            binding.dexTxt.text = modifierToString(evaluateModifier(savedCharDex.toInt()))
            binding.intTxt.text = modifierToString(evaluateModifier(savedCharInt.toInt()))
            binding.wisTxt.text = modifierToString(evaluateModifier(savedCharWis.toInt()))
            binding.chaTxt.text = modifierToString(evaluateModifier(savedCharCha.toInt()))
            binding.conTxt.text = modifierToString(evaluateModifier(savedCharCon.toInt()))

            binding.armorClassEdt.text = (10 + evaluateModifier(binding.dexEdt.text.toString().toInt())).toString()
            binding.htlEdt.text = (10 + evaluateModifier(binding.conEdt.text.toString().toInt())).toString()

            binding.saveInfoBtn.setOnClickListener {
                lifecycleScope.launch {
                    dataStore.edit { prefs ->
                        prefs[keyCharPhoto] = binding.charImg.getDrawable().toString()
                        prefs[keyCharName] = binding.nameEdt.text.toString()
                        prefs[keyCharRace] = binding.raceDd.selectedItemPosition
                        prefs[keyCharClass] = binding.classDd.selectedItemPosition
                        prefs[keyCharLvl] = binding.lvlEdt.text.toString()
                        prefs[keyCharBackground] = binding.backgroundDd.selectedItemPosition
                        prefs[keyCharAlignment] = binding.alignmentDd.selectedItemPosition
                        prefs[keyCharDesc] = binding.descEdt.text.toString()
                        prefs[keyCharExp] = binding.expEdt.text.toString()

                        prefs[keyCharStr] = binding.strEdt.text.toString()
                        prefs[keyCharDex] = binding.dexEdt.text.toString()
                        prefs[keyCharInt] = binding.intEdt.text.toString()
                        prefs[keyCharWis] = binding.wisEdt.text.toString()
                        prefs[keyCharCha] = binding.chaEdt.text.toString()
                        prefs[keyCharCon] = binding.conEdt.text.toString()
                    }
                }
                Toast.makeText(this@CharacterCreation, "Information Saved.", Toast.LENGTH_SHORT).show()
            }

            binding.strEdt.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus && binding.strEdt.text.isNotEmpty()) {
                    binding.strTxt.text = modifierToString(evaluateModifier(binding.strEdt.text.toString().toInt()))
                }
            }

            binding.dexEdt.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus && binding.dexEdt.text.isNotEmpty()) {
                    binding.dexTxt.text = modifierToString(evaluateModifier(binding.dexEdt.text.toString().toInt()))
                    binding.armorClassEdt.text = (10 + evaluateModifier(binding.dexEdt.text.toString().toInt())).toString()
                }
            }

            binding.intEdt.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus && binding.intEdt.text.isNotEmpty()) {
                    binding.intTxt.text = modifierToString(evaluateModifier(binding.intEdt.text.toString().toInt()))
                }
            }

            binding.wisEdt.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus && binding.wisEdt.text.isNotEmpty()) {
                    binding.wisTxt.text = modifierToString(evaluateModifier(binding.wisEdt.text.toString().toInt()))
                }
            }

            binding.chaEdt.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus && binding.chaEdt.text.isNotEmpty()) {
                    binding.chaTxt.text = modifierToString(evaluateModifier(binding.chaEdt.text.toString().toInt()))
                }
            }

            binding.conEdt.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus && binding.conEdt.text.isNotEmpty()) {
                    binding.conTxt.text = modifierToString(evaluateModifier(binding.conEdt.text.toString().toInt()))
                    binding.htlEdt.text = (10 + evaluateModifier(binding.conEdt.text.toString().toInt())).toString()
                }
            }
        }

        binding.otherBtn.setOnClickListener {
            val intent = Intent(this@CharacterCreation, OtherDetails::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            startActivity(intent, options.toBundle())
            finish()
        }

        binding.backBtn.setOnClickListener {
            val intent = Intent(this@CharacterCreation, MainActivity::class.java)
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