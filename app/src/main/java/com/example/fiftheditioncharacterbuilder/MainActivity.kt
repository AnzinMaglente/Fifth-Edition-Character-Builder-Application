package com.example.fiftheditioncharacterbuilder

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fiftheditioncharacterbuilder.databinding.ActivityMainBinding
import com.example.fiftheditioncharacterbuilder.fragments.CharacterSummaryFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// dataStore is used to store data.
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settingPrefs")

class MainActivity : AppCompatActivity() {
    // bindings connects the file to the xml activity to call the elements directly
    private lateinit var binding: ActivityMainBinding

    private val keyCharName = stringPreferencesKey("name")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // To load the character summary
        fun loadFragment(fragment: Fragment) {
            val fm = supportFragmentManager
            val fragmentTransaction = fm.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout, fragment)
            fragmentTransaction.commit()
        }

        // Sets a listener for when the floating action button is pressed, this is used to go to a
        // different activity
        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, CharacterCreation::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )

            startActivity(intent, options.toBundle())
            finish()
        }

        // This checks if the character's name is not empty, if it is true then it loads in the
        // character summary fragment.
        lifecycleScope.launch {
            val savedCharName = dataStore.data.map {
                it[keyCharName] ?: ""
            }.first()

            if (savedCharName.isNotEmpty()) {
                loadFragment(CharacterSummaryFragment())
                binding.descStart.visibility = View.GONE
            }
        }

        // This builds an alert to provide information about the app.
        binding.infoBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("How it works.")
            builder.setMessage("Hi there! Welcome to the 5th edition character builder for d&d. " +
                    "\n\n If you don't have a character yet, press on the bottom right button! This will lead you to the character creation screen where it will ask you for some details." +
                    "\n\n Fill in each field and then press onto the save info button. This should create a new element on the home screen." +
                    "\n\n If you want to go more indepth with your character or see their substats, you can press on the view other button," +
                    "\n\n That is all, thank you for downloading this application and I hope you have fun with your creations." +
                    "\n\n Ps: this does not save multiple characters and the image viewer is partially implemented.")
            val dialog = builder.create()
            dialog.show()
        }
    }
}