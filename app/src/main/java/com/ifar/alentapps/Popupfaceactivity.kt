package com.ifar.alentapps

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.ifar.alentapps.databinding.ActivityPopUpFacerecoBinding

class Popupfaceactivity : AppCompatActivity() {

    private lateinit var binding:ActivityPopUpFacerecoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopUpFacerecoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val bundle: Bundle? = intent.extras
        val AgeOut = bundle?.get("AgeOut").toString()

        val ageOut = AgeOut.toInt()

        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        window.setLayout((width * .8).toInt(), (height * .25).toInt())

        val params = window.attributes
        params.gravity = Gravity.CENTER
        params.x = 5
        params.y = -10


        window.attributes = params
        binding.facetitle.text = "Identitas Diri"
        binding.btnlanjut.text = "Next"

        binding.btnlanjut.setOnClickListener {

            binding.btnlanjut.setOnClickListener {
                if (binding.namachbtn.text.isNullOrEmpty()){
                    return@setOnClickListener
                }
                val textName = binding.namachbtn.text.toString()
                if ((7 <= ageOut) && (ageOut <= 14)){
                    val btnValue = 1
                    val umurId = "7-14 tahun"
                    val i = Intent(this@Popupfaceactivity, NLPSystem::class.java)
                    i.putExtra("namaTxt", textName)
                    i.putExtra("btnValue", btnValue)
                    i.putExtra("umur", umurId)
                    startActivity(i)
                }else if ((15 <= ageOut) && (ageOut <= 22)){
                    val btnValue = 2
                    val umurId = "15-22 tahun"
                    val i = Intent(this@Popupfaceactivity, NLPSystem::class.java)
                    i.putExtra("namaTxt", textName)
                    i.putExtra("btnValue", btnValue)
                    i.putExtra("umur", umurId)
                    startActivity(i)
                }else{
                    val btnValue = 3
                    val umurId = "23-30 tahun"
                    val i = Intent(this@Popupfaceactivity, NLPSystem::class.java)
                    i.putExtra("namaTxt", textName)
                    i.putExtra("btnValue", btnValue)
                    i.putExtra("umur", umurId)
                    startActivity(i)
                }

            }
        }
    }
}