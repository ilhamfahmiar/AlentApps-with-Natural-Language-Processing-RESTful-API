package com.ifar.alentapps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator3

class SliderActivity : AppCompatActivity() {

    private var titleList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imageesList = mutableListOf<Int>()
    private var backgrounddList = mutableListOf<Int>()
    private var btnList = mutableListOf<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)

        val viewpagerr2 = findViewById<ViewPager2>(R.id.view_pager2)

        postToList()
        viewpagerr2.adapter = SliderViewAdapter(titleList,descList,imageesList,backgrounddList,btnList)
        viewpagerr2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(viewpagerr2)
    }

    private fun addToList(title: String, description: String, image: Int, bg:Int, btn:Boolean){
        titleList.add(title)
        descList.add(description)
        imageesList.add(image)
        backgrounddList.add(bg)
        btnList.add(btn)
    }

    private fun postToList(){
        for (i in 1..6){
            when (i) {
                1 -> {
                    addToList("Face\nRecognition", "Pengecekan wajah sebagai pendeteksian umur pengguna yang nanti menentukan pertanyaan saat memulai proses seleksi.", R.drawable.photo_slide1, R.drawable.bg_1, false)
                }
                2 -> {
                    addToList("Voice Assistant dengan Bot", "Memulai untuk test pertanyaan yang diberikan oleh bot yang telah disystem menggunakan Natural language Processing untuk mengolah dan mengklasifikasikan sebuah hasil.", R.drawable.photo_slide2, R.drawable.bg_2, false)
                }
                3 -> {
                    addToList("Hasil Rekomendasi kemampuan", "Keluaran dari hasil proses pada system dengan hasil referensi rekomendasi dan tidak rekomendasi kemampuan dari pengguna.", R.drawable.photo_slide3, R.drawable.bg_3, false)
                }
                4 -> {
                    addToList("Fitur 2 Mode Assistant", "Fitur 2 Mode yang memungkinkan bisa digunakan selain Mode Utama yaitu Mode Talent Search, ada juga Mode Smart IoT, yang berguna untuk menghidupkan sebuah lampu menggunakan Voice Recognition.", R.drawable.photo_slide4, R.drawable.bg_4, false)
                }
                5 -> {
                    addToList("Ayo mulai mencari passionmu untuk menjadi yang terbaik !", "", R.drawable.photo_slide5, R.drawable.bg_5, true)
                }
            }
        }
    }
}