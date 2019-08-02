package com.rafaykalim.context

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.View
import android.widget.*
import com.rafaykalim.context.libraries.apiComm.SmsComm

import kotlinx.android.synthetic.main.activity_feature_translations.*

class FeatureTranslations : AppCompatActivity() {

    val OCR_TEXT_CODE = 1
    var textToTranslate = ""

    lateinit var smsComm : SmsComm

    lateinit var mInput : EditText
    lateinit var sendBtn : Button

    var fromLang = ""
    var toLang = ""
    var dataToTranslate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature_translations)
        setSupportActionBar(toolbar)

        startOCR.setOnClickListener { view ->
            var ocrIntent = Intent(this, OCRActivity::class.java)
            startActivityForResult(ocrIntent, OCR_TEXT_CODE)
        }

        sendBtn = findViewById(R.id.sendTranslations)
        sendBtn.setOnClickListener { getTranslation() }

        val fromLangSpinner = findViewById<Spinner>(R.id.fromLang)
        val toLangSpinner = findViewById<Spinner>(R.id.toLang)

        smsComm = SmsComm(this@FeatureTranslations)

        if (fromLangSpinner != null) {
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.languages_arr))
            fromLangSpinner.adapter = arrayAdapter
            toLangSpinner.adapter = arrayAdapter

            fromLangSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    fromLang = parent.getItemAtPosition(position).toString()
                    Log.d("CHOSEN", fromLang)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }

            toLangSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    toLang = parent.getItemAtPosition(position).toString()
                    Log.d("CHOSEN", toLang)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }

        mInput = findViewById(R.id.textToTranslate)
    }

    fun getTranslation()
    {
        dataToTranslate = mInput.text.toString()
        Log.d("DO", "get translation for ${dataToTranslate} from ${fromLang} to ${toLang}")
        //Make API Call
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK)
        {
            if(requestCode == OCR_TEXT_CODE)
            {
                textToTranslate = data!!.extras.getString("recText")
                mInput.setText(data!!.extras.getString("recText"))
            }
        }
    }
}
