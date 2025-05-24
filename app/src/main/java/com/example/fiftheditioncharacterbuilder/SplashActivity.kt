package com.example.fiftheditioncharacterbuilder

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.fiftheditioncharacterbuilder.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val animationTopSlide = AnimationUtils.loadAnimation(this, R.anim.slide_in_top)
        val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        lifecycleScope.launch {
            // Navigate to main activity.
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            // This is used to create animations when switching to a different activity.
            val options = ActivityOptions.makeCustomAnimation(
                this@SplashActivity,
                R.anim.fade_in,
                R.anim.fade_out
            )

            // This plays other animations that happen inside the splash activity.
            binding.title3.startAnimation(animationTopSlide)
            binding.title4.startAnimation(animationTopSlide)
            binding.title1.startAnimation(animationFadeIn)
            binding.title2.startAnimation(animationFadeIn)
            binding.title5.startAnimation(animationFadeIn)

            // Delay for 3 seconds
            delay(3000)

            startActivity(intent, options.toBundle())

            // Close splash activity to prevent going back
            finish()
        }
    }
}