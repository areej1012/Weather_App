package com.example.weatherapp

import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val APIK = "f1ec9853f58d17337e162700eac4fa3f"
    private var code = "20082"
    private var units = "metric"
    private lateinit var sharedPreferences: SharedPreferences
    private var weatherObject : Weather? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = ""
        APIRequest()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.ManageCity -> showAlert()
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateDate(view: View) {
        APIRequest()
    }
    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        val input = EditText(this)
        input.hint = " Enter ZIP code"
        dialog.setMessage("Manage Cites")
            .setPositiveButton("Ok",DialogInterface.OnClickListener { dialogInterface, id ->
                code = input.text.toString()
                APIRequest()
            })
            .setNegativeButton("Cancel",DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })
        val alert = dialog.create()
        alert.setView(input)
        alert.show()
    }

    private fun APIRequest() {
        val apiInterface  = APIClient().getClient()?.create(AIPInterface::class.java)
        apiInterface?.getWeather(code,APIK, units)?.enqueue(object :Callback<Weather>{
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                try {
                    weatherObject = response.body()!!
                    setUpUI()
                }catch (e :Exception){

                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Log.e("Main","$t")
            }

        })

    }

    fun setUpUI(){
        Log.e("NAME", weatherObject?.sys?.country.toString())
        binding.tvCity.text= "${weatherObject?.name}, ${weatherObject?.sys?.country}"
        binding.tvWeather.text = weatherObject?.weather?.get(0)?.description
        weatherIcon()
        var temp = weatherObject?.main?.temp.toString()
        var maxTemp = weatherObject?.main?.temp_max.toString()
        var minTemp = weatherObject?.main?.temp_min.toString()
        binding.tvTemp.text =  "${temp.subSequence(0,temp.indexOf("."))}°C"
        binding.tvHightLow.text = "${minTemp.subSequence(0, temp.indexOf("."))}°C/${maxTemp.subSequence(0,temp.indexOf("."))}°C"
        val lastUpdate = weatherObject!!.dt.toLong()
        binding.tvUpdate.text = "Last Update ${SimpleDateFormat("yyyy-mm-dd hh:mm a",Locale.ENGLISH).format(Date(lastUpdate*1000))}"
        val sunrise = weatherObject!!.sys.sunrise.toLong()
        val sunset = weatherObject!!.sys.sunset.toLong()
        binding.tvSunrise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
        binding.tvSunset.text =  SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
        binding.tvPressure.text = weatherObject!!.main.pressure.toString()
        binding.tvWind.text = weatherObject!!.wind.speed.toString()
        binding.tvHumidity.text = weatherObject!!.main.humidity.toString()


    }

    fun weatherIcon(){
        when(weatherObject?.weather?.get(0)?.main){
            "Clouds"->
                binding.tvWeather.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.cloudy,theme), null, null)

            "Clear" ->
                binding.tvWeather.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.sun,theme), null, null)

            "Rain" ->
                binding.tvWeather.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.rainyday,theme), null, null)

            "Snow" ->
                binding.tvWeather.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.snow,theme), null, null)

            "Storm" ->
                binding.tvWeather.setCompoundDrawablesWithIntrinsicBounds(null, resources.getDrawable(R.drawable.storm,theme), null, null)
        }
    }


}