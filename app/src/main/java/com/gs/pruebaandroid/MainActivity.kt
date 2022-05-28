package com.gs.pruebaandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gs.pruebaandroid.adapter.PeliculasRecyclerAdapter
import com.gs.pruebaandroid.interfaces.APIService
import com.gs.pruebaandroid.model.PeliculaResponse
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var mRecyclerView : RecyclerView
    val mAdapter : PeliculasRecyclerAdapter = PeliculasRecyclerAdapter()

    var list_of_items = arrayOf("Reproduciendo ahora", "Más populares")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showToolbar(getString(R.string.app_name),false);

        mRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(applicationContext)

        var spinner = findViewById<Spinner>(R.id.spinner)

        spinner!!.setOnItemSelectedListener(this)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.setAdapter(arrayAdapter)

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun buscarCategoria(categoria: String)
    {

        doAsync {

            try
            {
                val call = getRetrofit().create(APIService::class.java).getPeliculas(categoria).execute()
                val peliculaResponse = call.body() as PeliculaResponse

                uiThread {
                    mAdapter.TicketsRecyclerAdapter(peliculaResponse.results, applicationContext)
                    mRecyclerView.adapter = mAdapter
                }

            }
            catch(e: Exception)
            {
                uiThread {
                    Mensaje("Error al obtener peliculas, se mostrarán las guardadas de manera local")
                }
            }
        }

    }

    private fun showToolbar(titulo: String?, upButton: Boolean) {
        try {
            val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
            setSupportActionBar(toolbar)
            supportActionBar!!.title = titulo
            supportActionBar!!.setDisplayHomeAsUpEnabled(upButton)
        } catch (e: Exception) {
            //Snackbar.make(contextView, getString(R.string.err_toolbar), Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.bt_actualizar) {
            Log.d("Salida","Precionar actualizar")
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        if(p2==0)
            buscarCategoria("now_playing?api_key=eaeb22f051df667d3e73c3d58540b0b6&page=1")
        else
            buscarCategoria("popular?api_key=eaeb22f051df667d3e73c3d58540b0b6&page=1")

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {

        val dialogo1 = AlertDialog.Builder(this)

        dialogo1.setTitle(R.string.msg_importante)
        dialogo1.setMessage(R.string.msg_salir)
        dialogo1.setCancelable(false)
        dialogo1.setPositiveButton(R.string.msg_si)
        { dialogo1, id ->
            finish()
        }
        dialogo1.setNegativeButton(R.string.msg_no)
        { dialogo1, id ->
            //cancelar();
        }
        dialogo1.show()

    }

    private fun Mensaje(mensaje: String?)
    {
        try
        {
            val dialogo1 = AlertDialog.Builder(this)

            dialogo1.setTitle(R.string.msg_importante)
            dialogo1.setMessage(mensaje)
            dialogo1.setCancelable(false)
            dialogo1.setPositiveButton(R.string.msg_aceptar)
            { dialogo1, id ->

            }

            dialogo1.show()

        } catch (e: Exception) {

        }
    }



}
