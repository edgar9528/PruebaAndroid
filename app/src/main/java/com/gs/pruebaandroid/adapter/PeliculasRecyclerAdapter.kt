package com.gs.pruebaandroid.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gs.pruebaandroid.DetalleActivity
import com.gs.pruebaandroid.MainActivity
import com.gs.pruebaandroid.R
import com.gs.pruebaandroid.model.Results
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoAsyncContext
import kotlin.math.roundToInt

class PeliculasRecyclerAdapter : RecyclerView.Adapter<PeliculasRecyclerAdapter.ViewHolder>() {

    var peliculas: MutableList<Results>  = ArrayList()
    lateinit var context: Context

    fun TicketsRecyclerAdapter(peliculas: MutableList<Results>, context: Context){
        this.peliculas = peliculas
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = peliculas.get(position)
        holder.bind(item, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.cardview_pelicula, parent, false))
    }

    override fun getItemCount(): Int {
        return peliculas.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val iv_icono = view.findViewById(R.id.iv_icono) as ImageView
        val tv_titulo = view.findViewById(R.id.tv_titulo) as TextView
        val tv_puntuacion = view.findViewById(R.id.tv_puntuacion) as TextView
        val tv_descripcion = view.findViewById(R.id.tv_descripcion) as TextView
        val cl_pelicula = view.findViewById(R.id.cl_pelicula) as ConstraintLayout

        fun bind(pelicula:Results, context: Context)
        {
            //CORTAR LA DESCRIPCION A 60 CARACTERES
            var des = pelicula.overview as String
            if(des.length>60)
                des = pelicula.overview!!.substring(0,60)+"..."

            //PONER ESTRELLAS EN BASE AL PROMEDIO DE VOTOS
            var pun: Int = pelicula.voteAverage?.roundToInt() ?: 1
            var strPun : String = ""
            while (pun > 0) {
                strPun+="★"
                pun--
            }

            tv_titulo.text = pelicula.title
            tv_puntuacion.text = strPun
            tv_descripcion.text = des

            iv_icono.setImageResource(R.drawable.noimagen)
            iv_icono.loadUrl("https://image.tmdb.org/t/p/w500"+pelicula.posterPath)

            cl_pelicula.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, DetalleActivity::class.java).apply {
                    putExtra("Pelicula", pelicula);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent)

            })
        }

        fun ImageView.loadUrl(url: String) {
            Picasso.get().load(url).into(this)
        }

    }
}