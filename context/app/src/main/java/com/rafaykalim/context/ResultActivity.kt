package com.rafaykalim.context

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.rafaykalim.context.libraries.SharedInfo
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.content_result.*
import kotlinx.android.synthetic.main.feature_translations.*
import java.lang.Exception

class ResultActivity : AppCompatActivity() {
    var incomingMsg = ""
    val sInfo = SharedInfo()

    lateinit var directionsLayout : LinearLayout
    lateinit var translationsLayout : LinearLayout
    lateinit var sportsLayout: LinearLayout
    lateinit var googleLayout: LinearLayout

    lateinit var mAppBarLayout: AppBarLayout

    lateinit var titleComponents: ArrayList<String>
    // Without titleComponents
    lateinit var outerComponents: ArrayList<String>
    var reqType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setSupportActionBar(toolbar)

        var extras = intent.extras

        directionsLayout = findViewById(R.id.directionsLayout)
        translationsLayout = findViewById(R.id.translationsLayout)
        sportsLayout = findViewById(R.id.sportsLayout)
        googleLayout = findViewById(R.id.googleLayout)

        // The message being received here is with the type
        incomingMsg = extras.getString("message")

        fab.setOnClickListener { view ->
            finish();
        }

        getPartsFromMessage(incomingMsg)


        mAppBarLayout = findViewById(R.id.app_bar)

        when (reqType) {
            sInfo.DIRECTIONS -> { // Directions
                toggleVisible(directionsLayout)
                mAppBarLayout.setBackgroundResource(R.drawable.directions_background)

                var from_ = titleComponents[0].capitalize()
                var to_ = titleComponents[1].capitalize()

                var title = "Directions from ${from_} to ${to_}"

                var sb = StringBuilder()
                var i = 1

                for (step in outerComponents) {
                    var stepComponents = step.split(sInfo.resultInnerDelim)
                    try {
                        if (!stepComponents[0].equals("")) {
                            sb.append("${i}. In ${stepComponents[0]} after ${stepComponents[1]}, ${stepComponents[2]} \n")
                            i++
                        }
                    } catch (e: Exception) {
                        continue;
                    }

                }

                var resultTitleView = findViewById<TextView>(R.id.directions_result_title)
                var resultBodyView = findViewById<TextView>(R.id.directions_result_body)

                resultTitleView.text = title
                resultBodyView.text = sb.toString()
            }
            sInfo.TRANSLATION -> { //Translations - all data is in titleComponents
                toggleVisible(translationsLayout)

                mAppBarLayout.setBackgroundResource(R.drawable.translations_background)

                try {
                    var fromLangText = titleComponents[0]
                    var toLangText = titleComponents[1]
                    var fromStringText = titleComponents[2]
                    var toStringText = titleComponents[3]

                    var title = "${fromLangText} > ${toLangText} Translation"

                    // Set text here
                    var resultTitleView = findViewById<TextView>(R.id.translations_result_title)
                    var fromLangView = findViewById<TextView>(R.id.translations_result_fromLang)
                    var fromTextView = findViewById<TextView>(R.id.translations_result_fromString)
                    var toLangView = findViewById<TextView>(R.id.translations_result_toLang)
                    var toTextView = findViewById<TextView>(R.id.translations_result_toString)

                    resultTitleView.text = title
                    fromLangView.text = fromLangText
                    toLangView.text = toLangText
                    fromTextView.text = fromStringText
                    toTextView.text = toStringText
                } catch (e: IndexOutOfBoundsException) {
                    translations_result_fromString.text = getString(R.string.error_msg)
                }
            }
            sInfo.SPORTS -> { // TODO: Make Sports layout
                toggleVisible(sportsLayout)

                mAppBarLayout.setBackgroundResource(R.drawable.sports_background)

                try {
                    var homeTeam = titleComponents[0]
                    var awayTeam = titleComponents[1]
                    var homeScore = titleComponents[2]
                    var awayScore = titleComponents[3]

                    // Set text here
                    var homeNameView = findViewById<TextView>(R.id.home_team_name)
                    var homeScoreView = findViewById<TextView>(R.id.home_team_score)
                    var awayNameView = findViewById<TextView>(R.id.away_team_name)
                    var awayScoreView = findViewById<TextView>(R.id.away_team_score)

                    homeNameView.text = homeTeam
                    homeScoreView.text = homeScore
                    awayNameView.text = awayTeam
                    awayScoreView.text = awayScore
                } catch (e: IndexOutOfBoundsException) {
                    translations_result_fromString.text = getString(R.string.error_msg)
                }
            }
            sInfo.GOOGLE_SEARCH -> {
                toggleVisible(googleLayout)
                mAppBarLayout.setBackgroundResource(R.drawable.google_background)

                var title = "Google Result for ${titleComponents[0]}"
                var sb = StringBuilder()
                var i = 1
                for (result in outerComponents) {
                    var resText = result.split(sInfo.resultInnerDelim)
                    try {
                        if (!resText[0].equals("")) {
                            sb.append("${i}. ${resText[0]}\n")
                            i++
                        }
                    } catch (e: Exception) {
                        continue;
                    }
                }

                var titleView = findViewById<TextView>(R.id.google_result_title)
                var bodyView = findViewById<TextView>(R.id.google_result_body)

                titleView.text = title
                bodyView.text = sb.toString()
            }
        }
    }

    fun toggleVisible(x : LinearLayout)
    {
        var layouts = arrayListOf<LinearLayout>(directionsLayout, translationsLayout, googleLayout, sportsLayout)
        layouts.remove(x)
        for (layout : LinearLayout in layouts)
        {
            layout.visibility = View.GONE
        }
        x.visibility = View.VISIBLE
    }


    /*
       The incoming message is structured as -
       (type ID)!!!(title)!!!(body)

       type ID - 1 for directions
                 2 for translations
                 3 for google result
                 4 for sports

     */
    fun getPartsFromMessage(message: String) {
        // Pipeline is -
        // 1. remove twilio message,
        // 2. trim,
        // 3. extract reqType as first element,
        // 4. split with outerDelim for outerComponents,
        // 5. extract first as titleComponent,
        // 6. split with innerDelim to get components
        var msg = message.replace("Sent from your Twilio trial account -", "").trim()
        reqType = Integer.valueOf(msg[0].toString())
        msg = msg.removeRange(0, 1)
        outerComponents = ArrayList(msg.split(sInfo.resultOutterDelim))
        titleComponents = ArrayList(outerComponents.get(0).split(sInfo.resultInnerDelim))
        outerComponents.removeAt(0)
    }
}
