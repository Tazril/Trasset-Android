package com.cwod.trasset.helper

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cwod.trasset.R
import me.fahmisdk6.avatarview.AvatarView
import me.fahmisdk6.avatarview.FontTextView
import me.fahmisdk6.avatarview.rounded.DynamicRoundedImageView
import kotlin.random.Random.Default.nextInt

class AvatarImageView(context: Context?, attrs: AttributeSet?) : AvatarView(context, attrs) {

    var imgAvatar: DynamicRoundedImageView =
        findViewById<View>(R.id.round_img_avatar) as DynamicRoundedImageView
    var textAvatar: FontTextView = findViewById<View>(R.id.text_avatar_name) as FontTextView
    val colors = listOf(
        R.color.md_blue_800,
        R.color.md_green_800,
        R.color.md_red_800
    ).map { resources.getColor(it) }

    override fun bind(name: String, pic: String?) {
        textAvatar.visibility = View.GONE
        (textAvatar.background as GradientDrawable).setColor(colors[nextInt(3)])
        if (!TextUtils.isEmpty(name)) {
            val nameArray = name.trim().split(" ").toTypedArray()
            var initial = ""
            if (!nameArray.isEmpty()) initial += nameArray[0][0]
            if (nameArray.size > 1) initial += nameArray[nameArray.size - 1][0]
            textAvatar.text = initial
        }
        if (TextUtils.isEmpty(pic)) {
            textAvatar.visibility = View.VISIBLE
            imgAvatar.setImageDrawable(null)
        } else {
            Glide.with(context)
                .load(pic)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        textAvatar.visibility = View.VISIBLE
                        imgAvatar.setImageDrawable(null)
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let { imgAvatar.setImageDrawable(it) }
                        return true
                    }

                })
                .into(imgAvatar)
        }
    }

}