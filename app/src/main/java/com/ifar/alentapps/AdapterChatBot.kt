package com.ifar.alentapps

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterChatBot : RecyclerView.Adapter<AdapterChatBot.MyViewHolder>() {

    private val list = ArrayList<ChatModel>()

    inner class MyViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.listitem_chat, parent, false)){
        fun bind(chat:ChatModel) = with(itemView){
            val txtchat: TextView = itemView.findViewById(R.id.txtChat)
            if(!chat.isBot) {
                txtchat.setBackgroundResource(R.drawable.shape_btn_right)
                txtchat.setTextColor(Color.BLACK)
                txtchat.text = chat.chatbox

            }else{
                txtchat.setBackgroundResource(R.drawable.shape_btn_left)
                txtchat.setTextColor(Color.WHITE)
                txtchat.text = chat.chatbox

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(parent)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }
    override fun getItemCount() = list.size
    fun addChatToList(chat: ChatModel) {
        list.add(chat)
        notifyDataSetChanged()
    }
}