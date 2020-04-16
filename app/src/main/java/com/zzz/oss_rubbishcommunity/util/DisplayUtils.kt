package com.zzz.oss_rubbishcommunity.util

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.zzz.oss_rubbishcommunity.R

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    Glide.with(imageView.context).load(getImageFromServer(url))
            .placeholder(R.drawable.icon_img)
            .centerCrop()
            .into(imageView)
}

@BindingAdapter("imageUrlWithAddIcon")
fun loadImageWithAddIcon(imageView: ImageView, url: String?) {
    Glide.with(imageView.context).load(getImageFromServer(url))
            .placeholder(R.drawable.icon_add_pic)
            .centerCrop()
            .into(imageView)
}

@SuppressLint("NewApi")
@BindingAdapter("gender")
fun getGenderDrawable(imageView: ImageView, gender: String?) {
    when (gender) {
        "F" -> {
            Glide.with(imageView.context).load(R.drawable.icon_genderfemale)
                    .placeholder(R.drawable.icon_gendermale)
                    .dontAnimate()
                    .into(imageView)
            imageView.imageTintList =
                    ColorStateList.valueOf(
                            ContextCompat.getColor(
                                    imageView.context,
                                    R.color.colorFemale
                            )
                    )
        }
        else -> {
            Glide.with(imageView.context).load(R.drawable.icon_gendermale)
                    .placeholder(R.drawable.icon_gendermale)
                    .dontAnimate()
                    .into(imageView)

            imageView.imageTintList =
                    ColorStateList.valueOf(
                            ContextCompat.getColor(
                                    imageView.context,
                                    R.color.colorMale
                            )
                    )
        }
    }


}

fun imageWithServerHost(key: String) = "http://image.upuphub.com/$key"

fun getImageFromServer(url: String?) = when {
    (url ?: "").startsWith("http")
            || (url ?: "").startsWith("upuphub")
            || (url ?: "").startsWith("/storage/emulated")
    -> url
    else -> imageWithServerHost(url ?: "")
}




