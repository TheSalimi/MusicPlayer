package com.example.musicplayer

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.databinding.ActivitySettingBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(MainActivity.currentThemeNav[MainActivity.themeIndex])
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Setting"
        setContentView(binding.root)

        when(MainActivity.themeIndex){
            0 -> binding.whiteTheme.setBackgroundColor(Color.YELLOW)
            1 -> binding.blackTheme.setBackgroundColor(Color.YELLOW)
        }

        binding.whiteTheme.setOnClickListener { saveTheme(0) }
        binding.blackTheme.setOnClickListener { saveTheme(1) }
        binding.versionName.text = setVersionDetails()

        binding.sort.setOnClickListener {
            val menulist = arrayOf("Recently added", "Song title", "File size")
            var currentSort = MainActivity.sortOrder
            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Sorting")
                .setPositiveButton("OK") { _, _ ->
                    val editor = getSharedPreferences("SORTING", MODE_PRIVATE).edit()
                    editor.putInt("sortOrder", currentSort)
                    editor.apply()
                }
                .setSingleChoiceItems(menulist, currentSort) { _, which ->
                    currentSort = which
                }
            val customDialog = builder.create()
            customDialog.show()
            customDialog.getButton((AlertDialog.BUTTON_POSITIVE)).setTextColor(Color.RED)
        }
    }

    private fun saveTheme(index : Int){
        if(MainActivity.themeIndex != index){
            val editor = getSharedPreferences("THEMES" , MODE_PRIVATE).edit()
            editor.putInt("themeIndex" , index)
            editor.apply()

            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Apply theme?")
                .setMessage("Do you want to apply theme?")
                .setPositiveButton("Yes") { _, _ ->
                    exitApplication()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val customDialog = builder.create()
            customDialog.show()
            customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        }
    }

    private fun setVersionDetails():String{
        return "Verion name : ${BuildConfig.VERSION_NAME}"
    }

}