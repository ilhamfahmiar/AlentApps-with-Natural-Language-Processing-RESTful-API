package com.ifar.alentapps

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class SliderViewAdapter(private  var title:List<String>,
                        private var details: List<String>,
                        private var images:List<Int>,
                        private var background:List<Int>,
                        private var btn:List<Boolean>) : RecyclerView.Adapter<SliderViewAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.titletf)
        val itemDetails: TextView = itemView.findViewById(R.id.abouttf)
        val itemImage: ImageView = itemView.findViewById(R.id.imagetf)
        val itemBackground: RelativeLayout = itemView.findViewById(R.id.container)
        val itemBtn: ImageButton = itemView.findViewById(R.id.btntf)

        init {
            itemBtn.setBackgroundResource(R.drawable.btn_next)
            itemBtn.setOnClickListener {
                val i = Intent(itemView.context, MainActivity::class.java)
                itemView.context.startActivity(i)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewAdapter.Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))
    }

    override fun getItemCount(): Int {
        return title.size
    }

    override fun onBindViewHolder(holder: SliderViewAdapter.Pager2ViewHolder, position: Int) {
        holder.itemTitle.text = title[position]
        holder.itemDetails.text = details[position]
        holder.itemImage.setImageResource(images[position])
        holder.itemBackground.setBackgroundResource(background[position])
        holder.itemBtn.isVisible = btn[position]
    }
}