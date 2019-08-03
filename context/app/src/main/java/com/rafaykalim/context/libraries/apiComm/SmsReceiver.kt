package com.rafaykalim.context.libraries.apiComm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.rafaykalim.context.OCRActivity
import com.rafaykalim.context.ResultActivity
import com.rafaykalim.context.libraries.JavaUtils
import com.rafaykalim.context.libraries.SharedInfo

class SmsReceiver() : BroadcastReceiver() {
    val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.getAction().equals(SMS_RECEIVED)) {
            val bundle = intent!!.extras
            if (bundle != null) {
                val rMsg = JavaUtils.getMessageFromTextIntent(bundle)
                if(rMsg != "") { onSuccessGlobal(rMsg, context) }
                else { onFailGlobal() }
            }
        }
    }

    fun onSuccessGlobal(rMsg: String, context: Context?)
    {
        var intent = Intent()
        intent.setClassName("com.rafaykalim.context", "com.rafaykalim.context.ResultActivity")
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("message", rMsg)

        context!!.startActivity(intent)
    }

    fun onFailGlobal()
    {
    }

}