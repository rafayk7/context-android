package com.rafaykalim.context

import android.content.pm.PackageManager
import android.media.Image
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.rafaykalim.context.libraries.apiComm.SmsComm

import kotlinx.android.synthetic.main.activity_feature_directions.*

class FeatureDirections : AppCompatActivity() {

    val PERMISSION_CODE = 0
    lateinit var fromDirections: EditText
    lateinit var toDirections: EditText
    lateinit var getDirections : Button

    lateinit var carButton : ImageView
    lateinit var busButton : ImageView
    lateinit var walkButton : ImageView

    lateinit var smsComm : SmsComm

    var currMode = "Car"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature_directions)
        setSupportActionBar(toolbar)

        smsComm = SmsComm(this@FeatureDirections)

        fromDirections = findViewById(R.id.fromDirections)
        toDirections = findViewById(R.id.toDirections)
        getDirections = findViewById(R.id.sendDirections)

        carButton = findViewById(R.id.carButton)
        busButton = findViewById(R.id.busButton)
        walkButton = findViewById(R.id.walkButton)

        carButton.setOnClickListener { onCarClicked() }
        busButton.setOnClickListener { onBusClicked() }
        walkButton.setOnClickListener { onWalkClicked() }

        getDirections.setOnClickListener { getDirections() }
    }

    fun getDirections(){
        Log.d("DO", "get dirs from ${fromDirections.text} to ${toDirections.text}")
    }

    fun onCarClicked()
    {
        carButton.setImageResource(R.drawable.car_selected_24dp)
        busButton.setImageResource(R.drawable.bus_24dp)
        walkButton.setImageResource(R.drawable.walk_24dp)

        currMode = "Car"
    }

    fun onBusClicked()
    {
        carButton.setImageResource(R.drawable.car_24dp)
        busButton.setImageResource(R.drawable.bus_selected_24dp)
        walkButton.setImageResource(R.drawable.walk_24dp)

        currMode = "Bus"
    }

    fun onWalkClicked()
    {
        carButton.setImageResource(R.drawable.car_24dp)
        busButton.setImageResource(R.drawable.bus_24dp)
        walkButton.setImageResource(R.drawable.walk_selected_24dp)

        currMode = "Walk"
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Great! Thanks. Remember, you may be charged by your service provider for each service you use.", Toast.LENGTH_SHORT)
                } else {
                    Toast.makeText(this, "This app won't work without allowing SMS permissions.", Toast.LENGTH_SHORT)
                }
                return
            }
        }
    }
}
