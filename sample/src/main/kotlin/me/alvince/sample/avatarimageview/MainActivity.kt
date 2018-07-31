package me.alvince.sample.avatarimageview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import me.alvince.sample.avatarimageview.adapter.SampleGalleryAdapter
import me.alvince.sample.avatarimageview.dialog.ParamsDialog

class MainActivity : AppCompatActivity(), SampleGalleryAdapter.ItemSelectListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = SampleGalleryAdapter(this)
        adapter.listener = this
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        Glide.with(this)
                .load(getString(R.string.steins_gate_poster))
                .into(avatarImageView)
    }

    override fun onItemSelected(item: Params, v: View) {
        ParamsDialog(this, item) {
            val adapter = recyclerView?.adapter as SampleGalleryAdapter?
            adapter?.update(it)
        }.show()
    }
}
