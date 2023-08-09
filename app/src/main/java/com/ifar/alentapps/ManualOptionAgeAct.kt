package com.ifar.alentapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.ifar.alentapps.databinding.ActivityManualOptionAgeBinding

class ManualOptionAgeAct : AppCompatActivity() {

    private lateinit var binding: ActivityManualOptionAgeBinding
    private var btn1: Boolean = false
    private var btn2: Boolean = false
    private var btn3: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManualOptionAgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            val i = Intent(this, TalentMenu::class.java)
            startActivity(i)
        }

        binding.goBtn.setOnClickListener {
            val textName = binding.namaBtn.text.toString()
            if(btn1 && textName != ""){
                val btnValue = 1
                val umurId = "7-14 tahun"
                val i = Intent(this,NLPSystem::class.java)
                i.putExtra("namaTxt", textName)
                i.putExtra("btnValue", btnValue)
                i.putExtra("umur", umurId)
                startActivity(i)
            }else if(btn2 && textName != ""){
                val btnValue = 2
                val umurId = "15-22 tahun"
                val i = Intent(this,NLPSystem::class.java)
                i.putExtra("namaTxt", textName)
                i.putExtra("btnValue", btnValue)
                i.putExtra("umur", umurId)
                startActivity(i)
            }else if(btn3 && textName != ""){
                val btnValue = 3
                val umurId = "23-30 tahun"
                val i = Intent(this,NLPSystem::class.java)
                i.putExtra("namaTxt", textName)
                i.putExtra("btnValue", btnValue)
                i.putExtra("umur", umurId)
                startActivity(i)
            }
        }

    }

    fun anakBtn(v: View) {
        val clicked: Button = v.findViewById(R.id.anak_btn)
        //Toast.makeText(applicationContext,"button clicked", Toast.LENGTH_SHORT).show()
        btn1 = if (!btn1){
            clicked.setBackgroundResource(R.drawable.btn_enable)
            binding.remajaBtn.isEnabled = false
            binding.dewasaBtn.isEnabled = false
            true
        }else{
            clicked.setBackgroundResource(R.drawable.btn_disable)
            binding.remajaBtn.isEnabled = true
            binding.dewasaBtn.isEnabled = true
            false
        }

    }
    fun remajaBtn(v: View) {
        val clicked: Button = v.findViewById(R.id.remaja_btn)
        //Toast.makeText(applicationContext,"button clicked", Toast.LENGTH_SHORT).show()
        btn2 = if (!btn2){
            clicked.setBackgroundResource(R.drawable.btn_enable)
            binding.anakBtn.isEnabled = false
            binding.dewasaBtn.isEnabled = false
            true
        }else{
            clicked.setBackgroundResource(R.drawable.btn_disable)
            binding.anakBtn.isEnabled = true
            binding.dewasaBtn.isEnabled = true
            false
        }
    }
    fun dewasaBtn(v: View) {
        val clicked: Button = v.findViewById(R.id.dewasa_btn)
        //Toast.makeText(applicationContext,"button clicked", Toast.LENGTH_SHORT).show()
        btn3 = if (!btn3){
            clicked.setBackgroundResource(R.drawable.btn_enable)
            binding.remajaBtn.isEnabled = false
            binding.anakBtn.isEnabled = false
            true
        }else{
            clicked.setBackgroundResource(R.drawable.btn_disable)
            binding.remajaBtn.isEnabled = true
            binding.anakBtn.isEnabled = true
            false
        }
    }
}