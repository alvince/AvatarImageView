package me.alvince.sample.avatarimageview.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import me.alvince.android.avatarimageview.AvatarImageView
import me.alvince.sample.avatarimageview.GlideApp
import me.alvince.sample.avatarimageview.Params
import me.alvince.sample.avatarimageview.R
import me.alvince.sample.avatarimageview.convertFromDp
import java.util.*

/**
 * Created by alvince on 2018/4/18
 *
 * @author 杨小咩 alvince.zy@gmail.com
 * @version 1.0.1, 2018/8/1
 */
class SampleGalleryAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TAG = "SampleGalleryAdapter"
    }

    interface ItemSelectListener {
        fun onItemSelected(item: Params, v: View)
    }

    var listener: ItemSelectListener? = null

    private val dataSource: MutableList<Params> = ArrayList()
    private val imageSize: Float

    init {
        val source = context.resources.getStringArray(R.array.steins_gate)
        val random = Random()
        val colorPool = arrayOf(Color.BLACK, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY,
                Color.GREEN, Color.LTGRAY, Color.MAGENTA, Color.RED, Color.TRANSPARENT, Color.YELLOW)
        source.forEach {
            val data = Params(it)
            dataSource.add(data)
            data.roundAsCircle = random.nextBoolean()
            data.roundedCorner = random.nextInt(convertFromDp(8F).toInt())
            data.strokeColor = colorPool[random.nextInt(11)]
            data.strokeWidth = random.nextInt(convertFromDp(8F).toInt())
        }
        imageSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 128F, context.resources.displayMetrics)
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
        val itemView = holder.itemView
        when (itemView) {
            is AvatarImageView -> {
                val item = dataSource[position]
                itemView.circularDisplay(item.roundAsCircle)
                itemView.setStrokeColor(item.strokeColor)
                itemView.setStrokeWidth(item.strokeWidth)
                itemView.setRoundedCorner(item.roundedCorner)
                GlideApp.with(itemView)
                        .load(item.url)
                        .override(imageSize.toInt())
                        .into(itemView as ImageView)
                holder.itemView.setOnClickListener { listener?.onItemSelected(item, it) }
            }
            else -> Log.d(TAG, dataSource[position].url)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(ItemDecoration(recyclerView.context))
    }

    fun update(params: Params) {
        val index = dataSource.indexOf(params)
        if (index != -1) {
            notifyItemChanged(index)
        }
    }


    internal class SampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    internal class ItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

        val offset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F, context.resources.displayMetrics).toInt()

        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
            outRect?.left = offset
            outRect?.right = offset
        }
    }
}
