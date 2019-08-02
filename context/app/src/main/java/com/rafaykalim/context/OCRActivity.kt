package com.rafaykalim.context

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.vision.text.TextBlock
import android.util.SparseArray
import com.google.android.gms.vision.Detector
import android.view.SurfaceHolder
import android.Manifest.permission
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.view.SurfaceView
import android.widget.TextView
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.text.TextRecognizer
import java.io.IOException


class OCRActivity : AppCompatActivity() {

    lateinit var mCameraSource : CameraSource

    lateinit var mCameraView : SurfaceView
    lateinit var mTextView : TextView
    lateinit var SendButton : FloatingActionButton

    var textRecognized  = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr)

        mCameraView  = findViewById(R.id.surfaceView)
        mTextView = findViewById(R.id.ocrTextHolder)
        SendButton = findViewById(R.id.sendOCRText)

        SendButton.setOnClickListener { returnToParent() }

        startCameraSource()
    }

    private fun returnToParent()
    {
        var rIntent = Intent()
        rIntent.putExtra("recText", textRecognized)
        setResult(RESULT_OK, rIntent)
        finish()
    }

    private fun startCameraSource() {

        //Create the TextRecognizer
        val textRecognizer = TextRecognizer.Builder(applicationContext).build()

        if (!textRecognizer.isOperational) {
            Log.w("Error", "Detector dependencies not loaded yet")
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = CameraSource.Builder(applicationContext, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setAutoFocusEnabled(true)
                .setRequestedFps(2.0f)
                .build()

            mCameraView.getHolder().addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(
                                applicationContext,
                                permission.CAMERA
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {

                            ActivityCompat.requestPermissions(
                                this@OCRActivity,
                                arrayOf(permission.CAMERA), 1
                            )
                            return
                    }
                        mCameraSource.start(mCameraView.getHolder())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }

                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

                /**
                 * Release resources for cameraSource
                 */
                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    mCameraSource.stop()
                }
            })

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
                override fun release() {}

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 */
                override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
                    val items = detections.detectedItems
                    if (items.size() != 0) {

                        mTextView.post(Runnable {
                            val stringBuilder = StringBuilder()
                            for (i in 0 until items.size()) {
                                val item = items.valueAt(i)
                                stringBuilder.append(item.value)
                                stringBuilder.append("\n")
                            }
                            mTextView.setText(stringBuilder.toString())
                            textRecognized = stringBuilder.toString()
                        })
                    }
                }
            })
        }
    }
}
