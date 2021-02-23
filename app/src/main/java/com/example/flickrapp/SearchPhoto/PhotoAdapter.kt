package com.example.flickrapp.SearchPhoto

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.flickrapp.R
import com.example.flickrapp.network.NetworkState
import kotlinx.android.synthetic.main.item_network_state.view.*
import kotlinx.android.synthetic.main.item_photo.view.*

class PhotoAdapter (val context: Context, val onPhotoListener: OnPhotoListener) : PagedListAdapter<Photo, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
)
{

    private var networkState: NetworkState.Status? = null

    private val TYPE_PROGRESS = 0
    private val TYPE_ITEM = 1
    private val TYPE_BANNAR = 2


    companion object {

        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Photo> =
            object : DiffUtil.ItemCallback<Photo>() {
                override fun areItemsTheSame(listBean: Photo, t1: Photo): Boolean {
                    return listBean.id == t1.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(listBean: Photo, t1: Photo): Boolean {
                    return listBean == t1
                }
            }


    } // CMO


    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== NetworkState.Status.SUCCESS
    } // fun hasExtraRow

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            TYPE_PROGRESS
        } else {
            if(position % 5 == 0 && position != 0)
            TYPE_BANNAR
            else
            TYPE_ITEM
        }

    } // fun getItemViewType

    fun setNetworkState(newNetworkState: NetworkState.Status) {
        val previousState = networkState
        val previousExtraRow = hasExtraRow()
        networkState = newNetworkState
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    } // fun setNetworkState


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_PROGRESS) {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_network_state,
                parent,
                false
            )
            NetworkStateItemViewHolder(view)
        }
       else if (viewType == TYPE_BANNAR) {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_bannar,
                parent,
                false
            )
            BannerViewHolder(view)
        }
        else {
            val view: View = LayoutInflater.from(parent.context).inflate(
                R.layout.item_photo,
                parent,
                false
            )
            ViewHolder(view)
        }
    } // fun of onCreateViewHolder

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        if (holder is ViewHolder)
        {
            val photo: Photo? = getItem(position)
            (holder as ViewHolder).bind(
                photo!!,
                holder as ViewHolder,
                context = context,
                onPhotoListener = onPhotoListener
            )

        } else if (holder is NetworkStateItemViewHolder)
        {
            holder.bind(networkState!!, holder, context = context)
        }

    } // fun of onBindViewHolder



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val imgPhoto : ImageView = itemView.img
        

        fun bind(
            photo: Photo,
            holder: ViewHolder,
            context: Context,
            onPhotoListener: OnPhotoListener
        ) {
            //http://farm​{farm}​.static.flickr.com/​{server}​/​{id}​_​{secret}​.jpg


            val imagePath = "https://farm${photo.farm}.static.flickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg"

            Log.e("QP","imagePath : $imagePath")

            Glide.with(context)
                .load(imagePath)
                .into(holder.imgPhoto)


            itemView.setOnClickListener {
                onPhotoListener.onClick(photo = imagePath)
            }
            
        }



    } // class of ViewHolder

    class NetworkStateItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val  progressBar: ProgressBar = itemView.progress_bar
        private val  error_msg: TextView = itemView.error_msg

        fun bind(
            networkState: NetworkState.Status,
            holder: NetworkStateItemViewHolder,
            context: Context
        ) {

            if (networkState == NetworkState.Status.RUNNING) {
                holder.progressBar.visibility = View.VISIBLE
            } else {
                holder.progressBar.visibility = View.GONE
            }

            if (networkState == NetworkState.Status.FAILED) {
                holder.error_msg.visibility = View.VISIBLE
                holder.error_msg.text =  context.getString(R.string.something_wrong)
            } else {
                holder.error_msg.visibility = View.GONE
            }
        }

    } // class of NetworkStateItemViewHolder

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {




    } // class of ViewHolder



} // class of PhotoAdapter