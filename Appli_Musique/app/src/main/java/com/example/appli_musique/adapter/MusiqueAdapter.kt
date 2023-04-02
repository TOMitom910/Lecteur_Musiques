package com.example.appli_musique.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.appli_musique.R
import com.example.appli_musique.model.MusiqueModel

class MusiqueAdapter (var context: Context, var countries : ArrayList<MusiqueModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return countries.size
    }

    override fun getItem(position: Int): MusiqueModel {
        return  countries[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater : LayoutInflater =context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView =inflater.inflate(R.layout.item_list,parent,false)

        val imageView = rowView.findViewById<ImageView>(R.id.imgMusic)
        val textView1 = rowView.findViewById<TextView>(R.id.tvNom)
        val textView2 = rowView.findViewById<TextView>(R.id.tvDuree)

        val country = getItem(position)

        imageView.setImageResource(country.image)
        textView1.text = country.nom
        textView2.text = country.duree

        return rowView
    }
}