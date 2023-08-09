package com.ifar.alentapps

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ifar.alentapps.databinding.ActivitySplashscreenBinding

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivNote.alpha = 0f
        binding.ivNote.animate().setDuration(1500).alpha(1f).withEndAction {
            val i = Intent(this@SplashScreen, SliderActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
    }
}