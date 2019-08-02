package com.rafaykalim.context

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.rafaykalim.context.libraries.JavaUtils
import com.rafaykalim.context.libraries.KotlinUtils
import com.rafaykalim.context.libraries.apiComm.SmsComm
import com.rafaykalim.context.libraries.models.WebPageModel

class FeatureSearch : AppCompatActivity() {

    lateinit var searchButton : Button
    lateinit var saveWebPageButton : Button
    lateinit var queryText : EditText

    lateinit var wpmArray : ArrayList<WebPageModel>
    lateinit var smsComm : SmsComm

    var PERMISSIONS = arrayOf(
        Manifest.permission.INTERNET
    )

    val PERMISSION_CODE = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!hasPermissions(this, *PERMISSIONS))
        {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CODE)
        }
        else
        {
            setContentView(R.layout.activity_feature__search)

        }
        smsComm = SmsComm(this)

        searchButton = findViewById(R.id.sendSearchButton)
        saveWebPageButton = findViewById(R.id.saveWebPageButton)
        queryText = findViewById(R.id.searchText)

        searchButton.setOnClickListener { search() }
        saveWebPageButton.setOnClickListener {

                JavaUtils.getHtmlWebPage(queryText.text.toString(), JavaUtils.OnLinksInterface {
                wpmArray = it
                val filename = "${queryText.text}results"
                JavaUtils.writeFileOnInternalStorage(this, filename, wpmArray)
            })
        }
    }

    fun search()
    {
        var msg = KotlinUtils().genQueryMsg(queryText.text.toString())
        smsComm.sendSMS(msg)
        Log.d("DO", msg)
    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setContentView(R.layout.activity_feature__search)
                } else {
                    Toast.makeText(this, "You must allow Internet here. It is only used for saving web pages.", Toast.LENGTH_LONG)
                    finish()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }






}
