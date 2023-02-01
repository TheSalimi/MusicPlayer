package com.example.musicplayer

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.databinding.ActivityFeedBackBinding
import kotlinx.android.synthetic.main.activity_feed_back.*
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class FeedBackActivity : AppCompatActivity() {
    private lateinit var  binding : ActivityFeedBackBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.currentThemeNav[MainActivity.themeIndex])
        binding = ActivityFeedBackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar?.title = "Feedback";
        supportActionBar?.title = "Feedback";
        binding.feedbackSend.setOnClickListener {
            val feedbackMsg = feedbackText.text.toString() + "\n" + feedbackEmail.text.toString()
            val subject  = feedbackTopic.text.toString()
            val userName = "amir3lym@gmail.com"
            val pass = "amir3lym"
            val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if(feedbackMsg.isNotEmpty() && subject.isNotEmpty() && (cm.activeNetworkInfo?.isConnectedOrConnecting==true))
            {
                Thread{
                    try{
                        val properties = Properties()
                        properties["mail.smtp.auth"] = "true"
                        properties["mail.smtp.starttls.enable"] = "true"
                        properties["mail.smtp.host"] = "smtp.gmail.com"
                        properties["mail.smtp.port"] = "587"

                        val session = Session.getInstance(properties , object  : Authenticator(){
                            override fun getPasswordAuthentication(): PasswordAuthentication {
                                return PasswordAuthentication(userName , pass)
                            }
                        })

                        val mail = MimeMessage(session)
                        mail.subject = subject
                        mail.setText(feedbackMsg)
                        mail.setFrom(InternetAddress(userName))
                        mail.setRecipients(Message.RecipientType.TO , InternetAddress.parse(userName))
                        Transport.send(mail)
                    }
                    catch ( e : java.lang.Exception) {
                        Toast.makeText(this, e.toString() , Toast.LENGTH_SHORT).show()
                    }
                }.start()
                Toast.makeText(this , "Thanks for feedback" , Toast.LENGTH_SHORT).show()
                finish()
            }
            else Toast.makeText(this , "Something went wrong!!" , Toast.LENGTH_SHORT)
        }

        supportActionBar?.title = "Feedback"
    }
}