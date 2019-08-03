package com.rafaykalim.context

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import com.rafaykalim.context.R

import kotlinx.android.synthetic.main.activity_result_web_page.*
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileReader

class ResultWebPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_web_page)
        setSupportActionBar(toolbar)

        val path = "${this.filesDir}/webpages"

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        var dir = File(path)
        var files = dir.listFiles()

        var i = 0

        //For each file which is of this type, generate one card.
        //Read all text from each file. Get a list of lines. Line 1 is title. Line 2 is url. Line 3 is html.
        //Display line 1 and 2 in each card. If clicked, open webview with line 3 rendered.

        for (x: File in files)
        {
            if(x.isFile)
            {
            }

            var sb = StringBuilder()
            var line : String?=null;

        }


    }
}
