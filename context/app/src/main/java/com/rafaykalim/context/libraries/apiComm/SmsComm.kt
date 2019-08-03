package com.rafaykalim.context.libraries.apiComm

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.telephony.SmsManager
import com.rafaykalim.context.libraries.ApiUrl
import com.rafaykalim.context.libraries.JavaUtils
import com.rafaykalim.context.libraries.JavaUtils.getActivityFromContext
import com.rafaykalim.context.libraries.KotlinUtils

class SmsComm(context: Context)
{
    lateinit var mContext : Context
    val PERMISSION_CODE = 0

    var smsManager = SmsManager.getDefault()
    var destNumber = ApiUrl().destNumber

    var PERMISSIONS = arrayOf(
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.SEND_SMS
    )

     init{
        mContext  = context
        if(!hasPermissions(context, *PERMISSIONS))
        {
            ActivityCompat.requestPermissions(getActivityFromContext(context), PERMISSIONS, PERMISSION_CODE)
        }
    }

    fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun sendSMS(message : String)
    {
        smsManager.sendTextMessage(destNumber, null, message.trim(), null, null)
    }
}

