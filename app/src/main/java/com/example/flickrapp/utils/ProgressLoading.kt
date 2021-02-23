package com.example.flickrapp.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import com.example.flickrapp.R
import java.lang.Exception

object ProgressLoading
{
    private  var progress : Dialog? = null

    private fun init(activity: Activity)
    {
        progress = Dialog(activity)
        progress?.setContentView(R.layout.progress_layout)
        progress?.setCancelable(false)
        progress?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progress?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    } // init

    fun show (activity: Activity)
    {
        init(activity)

        if (!(activity).isFinishing) {
            //show dialog
            try {
                progress?.show()
            }catch (e : Exception)
            {
                e.printStackTrace()
            }

        }
    } // show

    fun isVisible() : Boolean
    {
        try {
            return progress!!.isShowing
        }catch (e : Exception)
        {
            return false
        }
    } // fun of isVisible

    fun dismiss()
    {
        progress?.dismiss()
    } // dismiss
} // class of ProgressLoading