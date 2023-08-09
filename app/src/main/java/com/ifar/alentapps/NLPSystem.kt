@file:Suppress("DEPRECATION")

package com.ifar.alentapps

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.view.Gravity
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifar.alentapps.databinding.ActivityNlpsystemBinding
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NLPSystem : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityNlpsystemBinding
    private lateinit var textToSpeech: TextToSpeech
    private val coroutineScope = CoroutineScope( Dispatchers.Main )
    private val idn = "id"
    private val resultspeech = 1
    private var knowNullSpeech = ""
    private var dominanTalent = ""
    private var count = 0
    private var risk = ""
    private var recom = ""
    private var dominan = ""
    private var dataIn = 0
    private val adapterchatbot = AdapterChatBot()
    private lateinit var namaSe:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNlpsystemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        val selectId = bundle?.get("btnValue").toString()
        val namaId = bundle?.get("namaTxt")
        val umurId = bundle?.get("umur")

        binding.namauser.text = namaId.toString()
        binding.umuruser.text = umurId.toString()
        namaSe = namaId.toString()

        val win1 = PopupWindow(this@NLPSystem)
        val view1 = layoutInflater.inflate(R.layout.activity_pop_up, null)
        val txtView = view1.findViewById<TextView>(R.id.titlepopup)
        val btnView = view1.findViewById<Button>(R.id.sub)

        binding.rvChatList.layoutManager = LinearLayoutManager(this@NLPSystem)
        binding.rvChatList.adapter = adapterchatbot

        //Text to Speech
        textToSpeech = TextToSpeech(this@NLPSystem, this@NLPSystem)

        when (selectId) {
            "1" -> {
                Handler().postDelayed({
                    txtView.text = "Ayo Mulai !"
                    btnView.text = "Mulai"
                    win1.contentView = view1
                    btnView.setOnClickListener {
                        coroutineScope.launch {
                            win1.dismiss()
                            if(count < 10){
                                if(count < 5){
                                    questionOnePost()
                                }
                                if(count > 4){
                                    questionOneNega()
                                }
                                binding.micTv.setOnClickListener {
                                    coroutineScope.launch {
                                        speakIn()
                                        delay(5000L)
                                        if (count == 5) {
                                            val data = knowNullSpeech
                                            botChatIn("0", data)
                                            knowNullSpeech = ""
                                        }
                                        if(count < 10){
                                            txtView.text = "Lanjut Pertanyaan"
                                            btnView.text = "Next"
                                            win1.contentView = view1
                                            win1.showAtLocation(binding.root, Gravity.CENTER, 5, -10)
                                            count++
                                        }
                                    }
                                }
                            }else{
                                delay(5000L)
                                Toast.makeText(this@NLPSystem, " Selesai ", Toast.LENGTH_LONG).show()
                                botChatIn("1", knowNullSpeech)
                                delay(5000L)
                                botChatIn("2", dominanTalent)
                                delay(5000L)
                                if ((recom != "") && (risk != "")) {
                                    adapterchatbot.addChatToList(ChatModel(dominan,true))
                                    delay(1000L)
                                    adapterchatbot.addChatToList(ChatModel(recom, true))
                                    delay(1000L)
                                    adapterchatbot.addChatToList(ChatModel(risk,true))
                                }
                                else{
                                    Toast.makeText(this@NLPSystem, "Maaf, masukkan anda masih kurang coba ulangi lagi..", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                    win1.showAtLocation(binding.root, Gravity.CENTER, 5, -10)
                }, 5000)
            }
            "2" -> {
                Handler().postDelayed({
                    txtView.text = "Ayo Mulai !"
                    btnView.text = "Mulai"
                    win1.contentView = view1
                    btnView.setOnClickListener {
                        coroutineScope.launch {
                            win1.dismiss()
                            if(count < 10){
                                if(count < 5){
                                    questionTwoPost()
                                }
                                if(count > 4){
                                    questionTwoNega()
                                }
                                binding.micTv.setOnClickListener {
                                    coroutineScope.launch {
                                        speakIn()
                                        delay(5000L)
                                        if (count == 5) {
                                            botChatIn("0", knowNullSpeech)
                                            knowNullSpeech = ""
                                        }
                                        if(count < 10){
                                            txtView.text = "Lanjut Pertanyaan"
                                            btnView.text = "Next"
                                            win1.contentView = view1
                                            win1.showAtLocation(binding.root, Gravity.CENTER, 5, -10)
                                            count++
                                        }
                                    }
                                }

                            }else{
                                delay(5000L)
                                Toast.makeText(this@NLPSystem, " Selesai ", Toast.LENGTH_LONG).show()
                                botChatIn("1", knowNullSpeech)
                                delay(5000L)
                                botChatIn("2", dominanTalent)
                                delay(5000L)
                                if ((recom != "") && (risk != "")) {
                                    adapterchatbot.addChatToList(ChatModel(dominan,true))
                                    delay(1000L)
                                    adapterchatbot.addChatToList(ChatModel(recom, true))
                                    delay(1000L)
                                    adapterchatbot.addChatToList(ChatModel(risk,true))
                                }
                                else{
                                    Toast.makeText(this@NLPSystem, "Maaf, masukkan anda masih kurang coba ulangi lagi..", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                    win1.showAtLocation(binding.root, Gravity.CENTER, 5, -10)
                }, 5000)
            }
            "3" -> {
                Handler().postDelayed({
                    txtView.text = "Ayo Mulai !"
                    btnView.text = "Mulai"
                    win1.contentView = view1
                    btnView.setOnClickListener {
                        coroutineScope.launch {
                            win1.dismiss()
                            if(count < 10){
                                if(count < 5){
                                    questionThreePost()
                                }
                                if(count > 4){
                                    questionThreeNega()
                                }
                                binding.micTv.setOnClickListener {
                                    coroutineScope.launch {
                                        speakIn()
                                        delay(5000L)
                                        if (count == 5) {
                                            botChatIn("0", knowNullSpeech)
                                            knowNullSpeech = ""
                                        }
                                        if(count < 10){
                                            txtView.text = "Lanjut Pertanyaan"
                                            btnView.text = "Next"
                                            win1.contentView = view1
                                            win1.showAtLocation(binding.root, Gravity.CENTER, 5, -10)
                                        }
                                        count++
                                    }
                                }
                            }else{
                                delay(5000L)
                                Toast.makeText(this@NLPSystem, " Selesai ", Toast.LENGTH_LONG).show()
                                botChatIn("1", knowNullSpeech)
                                delay(500L)
                                botChatIn("2", dominanTalent)
                                delay(5000L)
                                if ((recom != "") && (risk != "") && (dominan != "")) {
                                    adapterchatbot.addChatToList(ChatModel(dominan,true))
                                    delay(1000L)
                                    adapterchatbot.addChatToList(ChatModel(recom, true))
                                    delay(1000L)
                                    adapterchatbot.addChatToList(ChatModel(risk,true))
                                }
                                else{
                                    Toast.makeText(this@NLPSystem, "Maaf, masukkan anda masih kurang coba ulangi lagi..", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                    win1.showAtLocation(binding.root, Gravity.CENTER, 5, -10)
                }, 5000)
            }
        }
    }

    private fun questionOnePost() {
        when (count) {
            0 -> {
                val pertanyaan1 = "2 Hal apa yang kamu sukai saat dirumah dan disekolah ?"
                speakOut(pertanyaan1)
                adapterchatbot.addChatToList(ChatModel(pertanyaan1, true))
            }
            1 -> {
                val pertanyaan2 = "3 hal yang membuatmu bisa bahagia ?"
                speakOut(pertanyaan2)
                adapterchatbot.addChatToList(ChatModel(pertanyaan2, true))
            }
            2 -> {
                val pertanyaan3 = "3 pelajaran sekolah yang kamu sukai dan membuatmu bersemangat ?"
                speakOut(pertanyaan3)
                adapterchatbot.addChatToList(ChatModel(pertanyaan3, true))
            }
            3 -> {
                val pertanyaan4 = "Sebutkan apa cita cita kamu ?"
                speakOut(pertanyaan4)
                adapterchatbot.addChatToList(ChatModel(pertanyaan4, true))
            }
            4 -> {
                val pertanyaan5 = "2 hobi apa yang kamu sukai dan sering kamu lakukan ?"
                speakOut(pertanyaan5)
                adapterchatbot.addChatToList(ChatModel(pertanyaan5, true))
            }
            else -> {
                Toast.makeText(this@NLPSystem, "Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun questionOneNega() {
        when (count) {
            5 -> {
                val pertanyaan5 = "3 hal apa yang kamu lakukan jika ada pelajaran kosong disekolah ?"
                speakOut(pertanyaan5)
                adapterchatbot.addChatToList(ChatModel(pertanyaan5, true))
            }
            6 -> {
                val pertanyaan6 = "2 Hal apa yang tidak kamu sukai saat dirumah dan disekolah ?"
                speakOut(pertanyaan6)
                adapterchatbot.addChatToList(ChatModel(pertanyaan6, true))
            }
            7 -> {
                val pertanyaan7 = "3 Pelajaran sekolah apa yang tidak kamu sukai ?"
                speakOut(pertanyaan7)
                adapterchatbot.addChatToList(ChatModel(pertanyaan7, true))
            }
            8 -> {
                val pertanyaan9 = "2 hal apa yang membuat kamu marah ?"
                speakOut(pertanyaan9)
                adapterchatbot.addChatToList(ChatModel(pertanyaan9, true))
            }
            9 -> {
                val pertanyaan10 = "2 hal apa yang membuat kamu takut ?"
                speakOut(pertanyaan10)
                adapterchatbot.addChatToList(ChatModel(pertanyaan10, true))
            }
            else -> {
                Toast.makeText(this@NLPSystem, "Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun questionTwoPost() {
        when (count) {
            0 -> {
                val pertanyaan1 = "2 hal apa yang kamu sukai saat dirumah dan saat bersekolah ?"
                speakOut(pertanyaan1)
                adapterchatbot.addChatToList(ChatModel(pertanyaan1, true))
            }
            1 -> {
                val pertanyaan2 = "3 pelajaran yang kamu sukai dan membuatmu bersemangat ketika saat bersekolah ?"
                speakOut(pertanyaan2)
                adapterchatbot.addChatToList(ChatModel(pertanyaan2, true))
            }
            2 -> {
                val pertanyaan3 = "2 hal apa yang kamu sukai dari diri kamu ?"
                speakOut(pertanyaan3)
                adapterchatbot.addChatToList(ChatModel(pertanyaan3, true))
            }
            3 -> {
                val pertanyaan4 = "sebutkan hal apa saja yang menarik dari teknologi ?"
                speakOut(pertanyaan4)
                adapterchatbot.addChatToList(ChatModel(pertanyaan4, true))
            }
            4 -> {
                val pertanyaan5 = "3 hobi apa yang kamu sukai dan sering kamu lakukan di bidang teknologi ?"
                speakOut(pertanyaan5)
                adapterchatbot.addChatToList(ChatModel(pertanyaan5, true))
            }
            else -> {
                Toast.makeText(this@NLPSystem, "Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun questionTwoNega(){
        when (count) {
            5 -> {
                val pertanyaan5 = "3 hal apa yang kamu lakukan jika ada pelajaran kosong ketika bersekolah ?"
                speakOut(pertanyaan5)
                adapterchatbot.addChatToList(ChatModel(pertanyaan5, true))
            }
            6 -> {
                val pertanyaan6 = "3 Pelajaran apa yang tidak kamu sukai ketika bersekolah?"
                speakOut(pertanyaan6)
                adapterchatbot.addChatToList(ChatModel(pertanyaan6, true))
            }
            7 -> {
                val pertanyaan7 = "3 hal apa yang tidak kamu sukai dari diri kamu ?"
                speakOut(pertanyaan7)
                adapterchatbot.addChatToList(ChatModel(pertanyaan7, true))
            }
            8 -> {
                val pertanyaan8 = "3 hal apa saja yang membuat kamu tidak suka dari teknologi ?"
                speakOut(pertanyaan8)
                adapterchatbot.addChatToList(ChatModel(pertanyaan8, true))
            }
            9 -> {
                val pertanyaan9 = "3 bidang IT apa saja yang tidak kamu sukai ?"
                speakOut(pertanyaan9)
                adapterchatbot.addChatToList(ChatModel(pertanyaan9, true))
            }
            else -> {
                Toast.makeText(this@NLPSystem, "Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun questionThreePost() {
        when (count) {
            0 -> {
                val pertanyaan1 = "3 kata yang menggambarkan diri kamu ?"
                speakOut(pertanyaan1)
                adapterchatbot.addChatToList(ChatModel(pertanyaan1, true))
            }
            1 -> {
                val pertanyaan2 = "3 hobi apa yang paling banyak menghabiskan waktumu di bidang teknologi ?"
                speakOut(pertanyaan2)
                adapterchatbot.addChatToList(ChatModel(pertanyaan2, true))
            }
            2 -> {
                val pertanyaan3 = "Sebutkan apa saja yang membuat kamu tertarik belajar teknologi ?"
                speakOut(pertanyaan3)
                adapterchatbot.addChatToList(ChatModel(pertanyaan3, true))
            }
            3 -> {
                val pertanyaan4 = "3 tujuan yang ingin kamu capai dimasa depan ?"
                speakOut(pertanyaan4)
                adapterchatbot.addChatToList(ChatModel(pertanyaan4, true))
            }
            4 -> {
                val pertanyaan5 = "Apa yang bisa kamu lakukan untuk bisa mencapai tujuan tersebut ?"
                speakOut(pertanyaan5)
                adapterchatbot.addChatToList(ChatModel(pertanyaan5, true))
            }
            else -> {
                Toast.makeText(this@NLPSystem, "Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun questionThreeNega(){
        when (count) {
            5 -> {
                val pertanyaan1 = "3 hal apa yang kamu suka lakukan pada waktu senggang ?"
                speakOut(pertanyaan1)
                adapterchatbot.addChatToList(ChatModel(pertanyaan1, true))
            }
            6 -> {
                val pertanyaan2 = "3 hal apa yang tidak kamu sukai dari diri kamu ?"
                speakOut(pertanyaan2)
                adapterchatbot.addChatToList(ChatModel(pertanyaan2, true))
            }
            7 -> {
                val pertanyaan3 = "3 hal apa saja yang kamu tidak sukai dari teknologi ?"
                speakOut(pertanyaan3)
                adapterchatbot.addChatToList(ChatModel(pertanyaan3, true))
            }
            8 -> {
                val pertanyaan4 = "3 bidang IT apa saja yang tidak kamu sukai ?"
                speakOut(pertanyaan4)
                adapterchatbot.addChatToList(ChatModel(pertanyaan4, true))
            }
            9 -> {
                val pertanyaan5 = "Alasan kamu tidak menyukai 3 bidang IT tersebut ?"
                speakOut(pertanyaan5)
                adapterchatbot.addChatToList(ChatModel(pertanyaan5, true))
            }
            else -> {
                Toast.makeText(this@NLPSystem, "Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun botChatIn(selectS: String, dataS: String) {
        RetrofitClient.instance.chatWithTheBit(selectS,dataS).enqueue(object: Callback<ChatResponse>{
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if(response.isSuccessful &&  response.body()!= null){
                    when (dataIn) {
                        0 -> {
                            recom = response.body()!!.chatBotReply.toString()
                            dataIn++
                        }
                        1 -> {
                            risk = response.body()!!.chatBotReply.toString()
                            dataIn++
                        }
                        else -> {
                            dominan = response.body()!!.chatBotReply.toString()
                        }
                    }
                }else{
                    if((recom == "") && (risk == "")){
                    Toast.makeText(this@NLPSystem, "Mohon maaf tidak ada pekerjaan yang cocok untuk anda", Toast.LENGTH_LONG).show()
                    }
                }
            }
            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
//                Toast.makeText(this@NLPSystem, "Server tidak terhubung", Toast.LENGTH_LONG).show()
            }})
    }

//    private fun typeIn() {
//        adapterchatbot.addChatToList(ChatModel(binding.etChat.text.toString()))
//        RetrofitClient.instance.chatWithTheBit("1",binding.etChat.text.toString()).enqueue(object: Callback<ChatResponse>{
//            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
//                if(response.isSuccessful &&  response.body()!= null){
//                    adapterchatbot.addChatToList(ChatModel(response.body()!!.chatBotReply, true))
//                }else{
//                    Toast.makeText(this@NLPSystem, "Something went wrong-1", Toast.LENGTH_LONG).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
//                Toast.makeText(this@NLPSystem, "Something went wrong-2", Toast.LENGTH_LONG).show()
//            }})
//        binding.etChat.text.clear()
//    }

    private fun speakIn() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, idn)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Katakan Sesuatu..")
        try {
            startActivityForResult(intent, resultspeech)
        }catch (exp:ActivityNotFoundException){
            Toast.makeText(this@NLPSystem, "Bahasa tidak Support", Toast.LENGTH_SHORT).show()
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
                    adapterchatbot.addChatToList(ChatModel(speechChat))
                    dominanTalent += speechtxt[0].toString() + " "
                    if(knowNullSpeech == ""){
                        knowNullSpeech = speechtxt[0].toString() + " "
                    }
                    else{
                        knowNullSpeech += speechtxt[0].toString() + " "
                    }
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

            val salam = "Halo" + namaSe + "mari kita mulai test"
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
