@file:Suppress("DEPRECATION")

package com.ifar.alentapps

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ifar.alentapps.databinding.ActivityLampIotBinding
import com.ifar.helpers.MqttHelper
import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.io.UnsupportedEncodingException
import java.util.*

class LampIot : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var mqttHelper: MqttHelper
    private lateinit var binding: ActivityLampIotBinding
    private val coroutineScope = CoroutineScope( Dispatchers.Main )
    private val adapterchatbot = AdapterChatBot()
    private lateinit var textToSpeech: TextToSpeech
    private val idn = "id"
    private val resultspeech = 1
    private var knowNullSpeech = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLampIotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Text to Speech
        textToSpeech = TextToSpeech(this@LampIot, this@LampIot)
        startMqtt()

        binding.rvChatList1.setOnClickListener {
            if (knowNullSpeech == ""){
                return@setOnClickListener
            }
            coroutineScope.launch {
                speakIn()
                delay(5000L)
                if (knowNullSpeech == "nyala"){
                    onLamp()
                    knowNullSpeech = ""
                }
                if (knowNullSpeech == "mati") {
                    offLamp()
                    knowNullSpeech = ""
                }
            }

        }
    }

//    Publish
    private fun onLamp(){
        val topic = "kontroler/trima"
        val payload = "2"
        var encodedPayload = ByteArray(0)

        try {
            encodedPayload = payload.toByteArray(charset("UTF-8"))
            val message = MqttMessage(encodedPayload)
            mqttHelper.mqttAndroidClient.publish(topic, message)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun offLamp(){
        val topic = "kontroler/trima"
        val payload = "3"
        var encodedPayload = ByteArray(0)

        try {
            encodedPayload = payload.toByteArray(charset("UTF-8"))
            val message = MqttMessage(encodedPayload)
            mqttHelper.mqttAndroidClient.publish(topic, message)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
//    area publish

    private fun startMqtt() {
        mqttHelper = MqttHelper(applicationContext)
        mqttHelper.mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(b: Boolean, s: String) {
                Log.w("Debug", "Connected")
            }

            override fun connectionLost(throwable: Throwable) {}

            @Throws(Exception::class)
            override fun messageArrived(topic: String, mqttMessage: MqttMessage) {
                Log.w("Debug Data: ", mqttMessage.toString())
                Log.w("topic = ", topic)
                adapterchatbot.addChatToList(ChatModel(mqttMessage.toString(), true))
            }

            override fun deliveryComplete(iMqttDeliveryToken: IMqttDeliveryToken) {}
        })
    }

    private fun speakIn() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, idn)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Katakan Sesuatu..")
        try {
            startActivityForResult(intent, resultspeech)
        }catch (exp: ActivityNotFoundException){
            Toast.makeText(this@LampIot, "Bahasa tidak Support", Toast.LENGTH_SHORT).show()
//            typeIn()
            exp.printStackTrace()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            resultspeech -> {
                if (resultCode == Activity.RESULT_OK && data != null){
                    val speechtxt = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<Editable>
                    val speechChat = speechtxt[0].toString()
                    knowNullSpeech = speechtxt[0].toString()
                    adapterchatbot.addChatToList(ChatModel(speechChat))

                }
            }
        }
    }

    private fun speakOut(data:String) {
        textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null)
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            textToSpeech.language = Locale("id","ID")

            val salam = "Halo, selamat datang di fitur iot gunakan microphone untuk mematikan dan menyalakan lampu"
            speakOut(salam)
        }else{
            Toast.makeText(applicationContext, "Gagal Coba Lagi!", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }
}