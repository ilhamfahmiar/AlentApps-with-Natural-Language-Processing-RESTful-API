package com.ifar.alentapps

import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.Formatter
import android.widget.TextView
import java.net.Inet4Address
import java.net.InetAddress

class aboutActivity : AppCompatActivity() {

    private var ippAdd: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        ippAdd = findViewById(R.id.ipadd)
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
//        val ip: String = Inet4Address.getLocalHost().hostAddress

        ippAdd?.text = "IP Address: $ip"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}