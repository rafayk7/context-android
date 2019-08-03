package com.rafaykalim.context

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.View
import android.widget.*
import com.rafaykalim.context.libraries.KotlinUtils
import com.rafaykalim.context.libraries.SharedInfo
import com.rafaykalim.context.libraries.apiComm.SmsComm

import kotlinx.android.synthetic.main.activity_feature_translations.*

class FeatureTranslations : AppCompatActivity() {

    val sInfo = SharedInfo()
    val OCR_TEXT_CODE = 1
    var textToTranslate = ""

    lateinit var smsComm : SmsComm

    lateinit var mInput : EditText
    lateinit var sendBtn : Button

    var fromLang : String?=null
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
//            var langList = ArrayList<String>()
//            for (lang in sInfo.LANGUAGES)
//            {
//                var x = lang.capitalize()
//                langList.add(x)
//            }
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sInfo.LANGUAGES)
            val toArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sInfo.LANGUAGES.copyOfRange(1,sInfo.LANGUAGES.size))

            fromLangSpinner.adapter = arrayAdapter
            toLangSpinner.adapter = toArrayAdapter

            fromLangSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if(position == 0)
                    {
                        fromLang = null
                    } else
                    {
                        fromLang = parent.getItemAtPosition(position).toString()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }

            toLangSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    toLang = parent.getItemAtPosition(position).toString()
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
        var msg : String ?
        if (fromLang.isNullOrEmpty())
        {
            msg = KotlinUtils().genTransMsg(toLang, mInput.text.toString())
        }
        else
        {
            msg = KotlinUtils().genTransMsg(fromLang, toLang, mInput.text.toString())
        }

        smsComm.sendSMS(msg)
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
