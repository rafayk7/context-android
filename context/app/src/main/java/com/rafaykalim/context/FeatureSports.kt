package com.rafaykalim.context

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.rafaykalim.context.libraries.KotlinUtils
import com.rafaykalim.context.libraries.SharedInfo
import com.rafaykalim.context.libraries.apiComm.SmsComm

class FeatureSports : AppCompatActivity() {
    lateinit var homeTeam : EditText
    lateinit var awayTeam : EditText
    lateinit var sendSports : Button
    lateinit var sportChoice : Spinner

    var sInfo = SharedInfo()
    lateinit var smsComm : SmsComm
    var sport : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature_sports)

        homeTeam = findViewById(R.id.homeTeam)
        awayTeam = findViewById(R.id.awayTeam)
        sendSports = findViewById(R.id.sendSports)
        sportChoice = findViewById(R.id.sportChoice)

        smsComm = SmsComm(this)

        if (sportChoice != null) {
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sInfo.SPORTS_LIST)
            sportChoice.adapter = arrayAdapter

            sportChoice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    sport = parent.getItemAtPosition(position).toString()
                    Log.d("CHOSEN", sport)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }

        sendSports.setOnClickListener { sendSports() }
    }

    fun sendSports()
    {
        var msg = KotlinUtils().genSportsMsg(sport, homeTeam.text.toString(), awayTeam.text.toString())
        smsComm.sendSMS(msg)
        Log.d("DO", msg)
    }
}
