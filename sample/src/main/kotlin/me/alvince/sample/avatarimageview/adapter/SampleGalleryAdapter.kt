package me.alvince.sample.avatarimageview.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import me.alvince.android.avatarimageview.AvatarImageView

/**
 * Created by alvince on 2018/4/18
 *
 * @author 杨小咩 alvince.zy@gmail.com
 */
class SampleGalleryAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val dataSource: MutableList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val image = AvatarImageView(parent.context)
        return ViewHolder(image)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)