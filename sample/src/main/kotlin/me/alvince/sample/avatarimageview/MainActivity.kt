package me.alvince.sample.avatarimageview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import me.alvince.sample.avatarimageview.adapter.SampleGalleryAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = SampleGalleryAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        Glide.with(this)
                .load(getString(R.string.steins_gate_poster))
                .into(avatarImageView)
    }
}
