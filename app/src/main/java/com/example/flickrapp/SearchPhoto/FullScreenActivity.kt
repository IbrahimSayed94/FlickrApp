package com.example.flickrapp.SearchPhoto

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.flickrapp.R
import kotlinx.android.synthetic.main.activity_full_screen.*

class FullScreenActivity : AppCompatActivity() {


    companion object
    {

        var imagePath = ""

        fun startActivity(context: Context,imagePath : String)
        {
            this.imagePath = imagePath
            context.startActivity(Intent(context, FullScreenActivity::class.java))
        } // fun of startActivity

    } // companion object

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)

        initView()

    } // fun of onCreate

    private fun initView()
    {
        Glide.with(this)
            .load(imagePath)
            .into(img)
    }  // fun of initView
}