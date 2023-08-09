package com.ifar.alentapps

import android.net.wifi.WifiManager
import android.text.format.Formatter
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient:AppCompatActivity() {

    private const val BASE_URL = "http://192.168.43.100:5050/"
//    private const val BASE_URL = "http://192.168.137.45:5050/"
//    private const val BASE_URL = "http://192.168.137.214:5050/"

//    private val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
//    private val ip = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
//    private val BASE_URL = "http://"+ip+":5050/"

    val instance: APIService by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(APIService::class.java)
    }


}