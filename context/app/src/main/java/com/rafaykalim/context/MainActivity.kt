package com.rafaykalim.context

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val contactsLinksArray = arrayOf("https://www.github.com/rafayk7", "https://linkedin.com/in/rafayk7", "rafay.kalim@mail.utoronto.ca")
    val contactsTitleArray = arrayOf("Github", "Linkedin", "Email")
    val contactsImageArray = arrayOf(R.drawable.ic_github, R.drawable.ic_linkedin, R.drawable.ic_mail)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        //Feature cards
        val firstFeatureCard: CardView = findViewById(R.id.firstFeature)
        val secondFeatureCard: CardView = findViewById(R.id.secondFeature)
        val thirdFeatureCard: CardView = findViewById(R.id.thirdFeature)
        val fourthFeatureCard: CardView = findViewById(R.id.fourthFeature)
        val fifthFeatureCard: CardView = findViewById(R.id.fifthFeature)
        val sixthFeatureCard: CardView = findViewById(R.id.sixthFeature)

        //Set resources
        firstFeatureCard.findViewById<ImageView>(R.id.featureImg).setImageResource(R.drawable.directions_24dp)
        firstFeatureCard.findViewById<TextView>(R.id.featureTitle).text = getText(R.string.feature_directions_title)
        firstFeatureCard.findViewById<TextView>(R.id.featureDesc).text = getText(R.string.feature_directions_desc)
        firstFeatureCard.setOnClickListener {
            val intent = Intent(this, FeatureDirections::class.java)
            startActivity(intent)
        }

        secondFeatureCard.findViewById<ImageView>(R.id.featureImg).setImageResource(R.drawable.translate_24dp)
        secondFeatureCard.findViewById<TextView>(R.id.featureTitle).text = getText(R.string.feature_translations_title)
        secondFeatureCard.findViewById<TextView>(R.id.featureDesc).text = getText(R.string.feature_translations_desc)
        secondFeatureCard.setOnClickListener {
            val intent = Intent(this, FeatureTranslations::class.java)
            startActivity(intent)
        }

        thirdFeatureCard.findViewById<ImageView>(R.id.featureImg).setImageResource(R.drawable.search_24dp)
        thirdFeatureCard.findViewById<TextView>(R.id.featureTitle).text = getText(R.string.feature_search_title)
        thirdFeatureCard.findViewById<TextView>(R.id.featureDesc).text = getText(R.string.feature_search_desc)
        thirdFeatureCard.setOnClickListener {
            val intent = Intent(this, FeatureSearch::class.java)
            startActivity(intent)
        }

        fourthFeatureCard.findViewById<ImageView>(R.id.featureImg).setImageResource(R.drawable.webpage_24dp)
        fourthFeatureCard.findViewById<TextView>(R.id.featureTitle).text = getText(R.string.feature_saveWebPage_title)
        fourthFeatureCard.findViewById<TextView>(R.id.featureDesc).text = getText(R.string.feature_saveWebPage_desc)
        fourthFeatureCard.setOnClickListener {
            Toast.makeText(this, "Feature coming soon!", Toast.LENGTH_LONG).show()
        }
        fifthFeatureCard.findViewById<ImageView>(R.id.featureImg).setImageResource(R.drawable.chatbot_24dp)
        fifthFeatureCard.findViewById<TextView>(R.id.featureTitle).text = getText(R.string.feature_chatbot_title)
        fifthFeatureCard.findViewById<TextView>(R.id.featureDesc).text = getText(R.string.feature_chatbot_desc)
        fifthFeatureCard.setOnClickListener {
            Toast.makeText(this, "Feature coming soon!", Toast.LENGTH_LONG).show()

        }
        sixthFeatureCard.findViewById<ImageView>(R.id.featureImg).setImageResource(R.drawable.sports_24dp)
        sixthFeatureCard.findViewById<TextView>(R.id.featureTitle).text = getText(R.string.feature_sports_title)
        sixthFeatureCard.findViewById<TextView>(R.id.featureDesc).text = getText(R.string.feature_sports_desc)
        sixthFeatureCard.setOnClickListener {
            val intent = Intent(this, FeatureSports::class.java)
            startActivity(intent)
        }
        //Nav card
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    //Make this a get started page. Just display information on features. Make a displaycardview for each

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_directions -> {
                val intent = Intent(this, FeatureDirections::class.java)
                startActivity(intent)
            }
            R.id.nav_search -> {
                val intent = Intent(this, FeatureSearch::class.java)
                startActivity(intent)
            }
            R.id.nav_webpages -> {
                Toast.makeText(this, "Feature coming soon!", Toast.LENGTH_LONG).show()
            }
            R.id.nav_translations -> {
                val intent = Intent(this, FeatureTranslations::class.java)
                startActivity(intent)
            }
            R.id.nav_sports -> {
                val intent = Intent(this, FeatureSports::class.java)
                startActivity(intent)
            }
            R.id.nav_contact -> {
                val aDialog = AlertDialog.Builder(this)
                aDialog.setTitle("Choose contact")
                    .setItems(contactsTitleArray) {dialog, which ->
                        if (which ==2)
                        {
                            sendEmail()
                        }
                        else
                        {
                            sendBrowser(contactsLinksArray.get(which))
                        }
                    }.create().show()
            }
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun sendEmail()
    {
        var eIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto",contactsLinksArray[2], null))
        eIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact from Context");
        eIntent.putExtra(Intent.EXTRA_TEXT, "Hey, just wanted to contact you about...");
        startActivity(Intent.createChooser(eIntent, "Send email..."));
    }

    fun sendBrowser(link: String)
    {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }
    //Activity list
    // 1. Translations activity - From language, to language, text to translate, camera for OCR, OCR
    // 2. Directions activity - From, to, map view?
    // 3. Google search - Search query input field, submit button
    // 4. Save web pages - Search query input field, submit button, half page recyclerview with results. card view for recyclerview
    // 5. View web pages activity - html viewer. make popup
    // 6. Sports updates - send name of event field. recyclerview with updates for half page
    // 7. MAYBE - chatbot?

    //Backend
    // 1. Translations - use google translations api, easy. send results back
    // 2. Directions - use google maps api, easy. send results back
    // 3. Google search - make scraper, scrape results and pick the google recommended answer one? or first <p>>
    // 4. Save web pages - make scraper, go to each web page after google search (use crawler) and save the html from bs4
    // 5. Sports updates - use twitter data around it to see if significant? if sig, use espninfo to send update.
    // 6. Train chatbot NLP to talk
}
