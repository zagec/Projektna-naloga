package com.example.restaurantkodatagetter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView

class dataAdapter(private var data: MutableList<Data>, var monNodeListener: onNodeListener, private val clickListener: (Int) -> Unit) : RecyclerView.Adapter<dataAdapter.ViewHolder>()  {
    lateinit var onNodeL: onNodeListener
    var mOnNodeListener: onNodeListener = monNodeListener

    class ViewHolder(itemView: View, onNodeL: onNodeListener): RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        var name: TextView = itemView.findViewById(R.id.dataName)
        var time: TextView = itemView.findViewById(R.id.dataTime)
        var infoD: TextView = itemView.findViewById(R.id.dataInfo)
        var location: TextView = itemView.findViewById(R.id.location)
        var btn: SwitchCompat = itemView.findViewById(R.id.btn)

        var onNodeL = onNodeL

        var nekaj = itemView.setOnClickListener(this)
        var nekaj2 = itemView.setOnLongClickListener(this)

        override fun onClick(p0: View?) {
            onNodeL.onNoteClick(adapterPosition)
        }

        override fun onLongClick(p0: View?): Boolean {
            onNodeL.onNodeLongClick(adapterPosition)
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.data_rv_item, parent, false)

        return ViewHolder(view, mOnNodeListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].name
        holder.time.text = data[position].time
        holder.infoD.text = data[position].info
        holder.location.text = data[position].location

        holder.btn.setOnClickListener{
            clickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface onNodeListener{
        fun onNoteClick(position2: Int)
        fun onNodeLongClick(position2: Int)
    }
}
