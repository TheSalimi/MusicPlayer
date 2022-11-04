package com.example.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificatinReciever : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            ApplicationClass.PREVIOUS -> Toast.makeText(context , "previous clicked" , Toast.LENGTH_SHORT).show()
            ApplicationClass.PLAY -> Toast.makeText(context , "play clicked" , Toast.LENGTH_SHORT).show()
            ApplicationClass.NEXT -> Toast.makeText(context , "next clicked" , Toast.LENGTH_SHORT).show()
            ApplicationClass.EXIT -> Toast.makeText(context , "exit clicked" , Toast.LENGTH_SHORT).show()
        }
    }
}