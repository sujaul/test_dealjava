package com.test.test_karim2.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.test.test_karim2.R
import kotlinx.android.synthetic.main.activity_image_view.*


class ImageViewActivity : AppCompatActivity() {

    private var position: String = ""
    private var image_name: String = ""
    private var image_url: String = ""
    companion object {
        @JvmStatic
        fun newInstance(context: Context, position: String, image_name : String, image_url : String): Intent {
            val intent = Intent(context, ImageViewActivity::class.java)
            intent.putExtra("position", position)
            intent.putExtra("image_name", image_name)
            intent.putExtra("image_url", image_url)
            return intent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        initViews()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initViews() {
        position = intent.getStringExtra("position") ?: ""
        image_name = intent.getStringExtra("image_name") ?: ""
        image_url = intent.getStringExtra("image_url") ?: ""
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_baseline_groups_24)
            .error(R.drawable.ic_baseline_groups_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
        Glide.with(this@ImageViewActivity)
            .load(image_url)
            .apply(options)
            .into(image)
        date.text = image_name
        //user.text = users
        lbl_count.text = position
        imgback.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
