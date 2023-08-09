package com.ifar.alentapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ifar.alentapps.databinding.ActivityTalentmenuBinding

class TalentMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTalentmenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFace.setBackgroundResource(R.drawable.face_btn)
        binding.btnManual.setBackgroundResource(R.drawable.manual_btn)

        binding.btnBack.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        // Age Recognition
        binding.btnFace.setOnClickListener {
//            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT ).show()
            val i = Intent(this, FaceRecoActivity::class.java)
            startActivity(i)
        }
        // manual setting
        binding.btnManual.setOnClickListener {
//            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT ).show()
            val i = Intent(this, ManualOptionAgeAct::class.java)
            startActivity(i)
        }
    }
}