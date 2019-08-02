package com.rafaykalim.context

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.content_result.*
import kotlinx.android.synthetic.main.feature_translations.*

class ResultActivity : AppCompatActivity() {
    var incomingMsg = ""

    lateinit var mBody : TextView
    lateinit var mTitle : TextView

    lateinit var mAppBarLayout : AppBarLayout

    var type = 0
    var title = ""
    var body = ""
    var worked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setSupportActionBar(toolbar)

        var extras = intent.extras

        incomingMsg = extras.getString("message")

        fab.setOnClickListener { view ->
            finish();
        }

        getPartsFromMessage(incomingMsg)


        mAppBarLayout = findViewById(R.id.app_bar)


        if (worked) {
            when (type) {
                1 -> { // Directions
                    directionsLayout.visibility = View.VISIBLE
                    translationsLayout.visibility = View.GONE

                    mAppBarLayout.setBackgroundResource(R.drawable.test_directions)

                    var steps = body.split(".")
                    var sb = StringBuilder()
                    var i = 1

                    for (step in steps)
                    {
                        if(!step.equals(""))
                        {
                            sb.append("${i}. ${step} \n")
                            i++
                        }
                    }

                    directions_result_title.text = title
                    directions_result_body.text = sb.toString()
                }
                2 -> { //Translations
                    directionsLayout.visibility = View.GONE
                    translationsLayout.visibility = View.VISIBLE


                    mAppBarLayout.setBackgroundResource(R.drawable.test_translations)
                    var content = body.split(".")

                    translations_result_title.text = title

                    try
                    {
                    var fromLang = content[0]
                    var toLang = content[1]
                    var fromString = content[2]
                    var toString = content[3]

                        translations_result_fromLang.text = fromLang
                        translations_result_fromString.text = fromString
                        translations_result_toLang.text = toLang
                        translations_result_toString.text = toString
                    }
                    catch (e : IndexOutOfBoundsException)
                    {
                        translations_result_fromString.text = getString(R.string.error_msg)
                    }
                }
                3 -> {
                    mAppBarLayout.setBackgroundResource(R.drawable.travel_pic_header)
                }
            }
        }
    }

    /*
       The incoming message is structured as -
       (type ID)!!!(title)!!!(body)

       type ID - 1 for directions
                 2 for translations
                 3 for google result
                 4 for sports

     */
    fun getPartsFromMessage(message: String)
    {
        var components = message.split("!!!")
        try
        {
            type = Integer.valueOf(components[0])
            title = components[1]
            body = components[2]

            worked = true
        }
        catch (e : IndexOutOfBoundsException)
        {
            type = 0
            title = getString(R.string.error_msg_title)
            body = getString(R.string.error_msg)
            worked = false
        }
    }
}
