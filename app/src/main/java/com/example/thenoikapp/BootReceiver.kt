package com.example.thenoikapp

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.material.internal.ContextUtils.getActivity


class BootReceiver() : BroadcastReceiver(){

    // I love stackoverflow
    @SuppressLint("RestrictedApi")
    override fun onReceive(context: Context, intent: Intent?) {
        val myIntent = Intent(context, MainActivity::class.java)
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val a = getActivity(context)

        if (a != null) {
            a.moveTaskToBack(true)
        }
    }
}