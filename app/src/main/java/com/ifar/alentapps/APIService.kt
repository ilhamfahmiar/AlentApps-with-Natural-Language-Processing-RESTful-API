package com.ifar.alentapps

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface APIService {
    @FormUrlEncoded
    @POST("chat")
    fun chatWithTheBit(@Field("select") select_txt : String, @Field("chatInput") chat_txt : String): Call<ChatResponse>
}
data class ChatResponse(val chatBotReply: String?)