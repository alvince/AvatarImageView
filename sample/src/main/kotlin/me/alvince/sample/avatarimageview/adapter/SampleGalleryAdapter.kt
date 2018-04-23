package me.alvince.sample.avatarimageview.adapter

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import me.alvince.android.avatarimageview.AvatarImageView
import me.alvince.sample.avatarimageview.R

/**
 * Created by alvince on 2018/4/18
 *
 * @author 杨小咩 alvince.zy@gmail.com
 */
class SampleGalleryAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TAG = "SampleGalleryAdapter"
    }

    private val dataSource: MutableList<String> = ArrayList()
    private val imageSize: Float

    init {
        dataSource.addAll(context.resources.getStringArray(R.array.steins_gate))
        imageSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 128F, context.resources.displayMetrics)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val image = AvatarImageView(parent.context)
        image.layoutParams = ViewGroup.LayoutParams(imageSize.toInt(), imageSize.toInt())
        image.scaleType = ImageView.ScaleType.CENTER
        return SampleViewHolder(image)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemView) {
            is AvatarImageView -> {
                Glide.with(holder.itemView.context)
                        .load(dataSource[position])
                        .into(holder.itemView as ImageView)
            }
            else -> Log.d(TAG, dataSource[position])
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(ItemDecoration(recyclerView.context))
    }


    internal class SampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    internal class ItemDecoration(context: Context): RecyclerView.ItemDecoration() {

        val offset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F, context.resources.displayMetrics).toInt()

        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
            outRect?.left = offset
            outRect?.right = offset
        }
    }
}
