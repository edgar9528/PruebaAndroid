package com.gs.pruebaandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.gs.pruebaandroid.interfaces.APIService
import com.gs.pruebaandroid.model.PeliculaResponse
import com.gs.pruebaandroid.model.Results
import com.gs.pruebaandroid.model.VideosResponse
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.text.MessageFormat
import kotlin.math.roundToInt

class DetalleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        showToolbar("Detalles",true);


        val pelicula = intent.getSerializableExtra("Pelicula") as Results;

        val iv_icono = findViewById<ImageView>(R.id.iv_icono)
        val tv_titulo = findViewById<TextView>(R.id.tv_titulo)
        val tv_descripcion = findViewById<TextView>(R.id.tv_descripcion)
        val tv_puntuacion = findViewById<TextView>(R.id.tv_puntuacion)
        val tv_fechaLan = findViewById<TextView>(R.id.tv_fechaLan)

        iv_icono.setImageResource(R.drawable.noimagen)
        iv_icono.loadUrl("https://image.tmdb.org/t/p/w500"+pelicula.posterPath)

        //PONER ESTRELLAS EN BASE AL PROMEDIO DE VOTOS
        var pun: Int = pelicula.voteAverage?.roundToInt() ?: 1
        var strPun : String = ""
        while (pun > 0) {
            strPun+="★"
            pun--
        }
        tv_puntuacion.text=strPun
        tv_titulo.text=pelicula.title
        tv_descripcion.text=pelicula.overview
        tv_fechaLan.text=pelicula.releaseDate

        buscarVideos(pelicula.id.toString())

        var scrollView: ScrollView = findViewById(R.id.scrollView)
        scrollView.smoothScrollTo(0,0);



    }

    private fun showToolbar(titulo: String?, upButton: Boolean) {
        try
        {
            val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
            setSupportActionBar(toolbar)
            supportActionBar!!.title = titulo
            supportActionBar!!.setDisplayHomeAsUpEnabled(upButton)

            toolbar.setNavigationOnClickListener { finish() }

        } catch (e: Exception) {
            //Snackbar.make(contextView, getString(R.string.err_toolbar), Snackbar.LENGTH_LONG).show()
        }
    }

    fun ImageView.loadUrl(url: String) {
        Picasso.get().load(url).into(this)
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    private fun buscarVideos(idPelicula: String)
    {
        doAsync {
            val call = getRetrofit().create(APIService::class.java).getVideos("$idPelicula/videos?api_key=eaeb22f051df667d3e73c3d58540b0b6&page=1").execute()
            val videosResponse = call.body() as VideosResponse
            uiThread {

                if(videosResponse.results.size>0)
                {
                    var videoWeb: WebView = findViewById(R.id.webView)

                    videoWeb.settings.javaScriptEnabled = true
                    videoWeb.webChromeClient = object : WebChromeClient() {
                    }

                    val codigo : String? = videosResponse.results[0].key

                    videoWeb.loadData(getYoutubeLink(codigo.toString()), "text/html", "utf-8")

                }

            }
        }
    }

    private fun getYoutubeLink(codigo : String): String
    {
        val parte1: String = "<iframe width='100%' height='100%' src='https://www.youtube.com/embed/";
        val parte2: String = "' frameborder='0' allowfullscreen></iframe>";
        return parte1+codigo+parte2;

    }



}