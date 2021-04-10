package com.cwod.trasset.common

import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.cwod.trasset.R
import com.cwod.trasset.base.BaseRecyclerAdapter
import com.cwod.trasset.helper.DataFormatter
import kotlinx.android.synthetic.main.item_notification.view.*
import org.joda.time.DateTime

class NotificationAdapter : BaseRecyclerAdapter<NotificationModel>(R.layout.item_notification) {
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val notification = list[position]
        holder.itemView.apply {
            name.text = notification.name
            (DataFormatter.getInstance()
                .getTimeAgo(DateTime(notification.timestamp).millis) + " ago").let {
                time.text = it
            }
            message.text = message(notification)
            linearLayout.setBackgroundColor(
                if (notification.status == "success") ContextCompat.getColor(
                    context,
                    R.color.primaryColor
                )
                else if (notification.status == "warning") ContextCompat.getColor(
                    context,
                    R.color.primaryLightColor
                )
                else ContextCompat.getColor(context, R.color.primaryDarkColor)
            )
            val url =
                if (notification.type == "geofence") "https://icon-library.com/images/geofence-icon/geofence-icon-7.jpg"
                else "https://cdn1.iconfinder.com/data/icons/leto-blue-navigation/64/_-22-512.png"

            Glide.with(context).load(url).into(imageView)
        }
    }

    fun message(notification: NotificationModel): String {
        DataFormatter.getInstance().formatDecimalPlaces(notification.lat)
        var msg = "Asset Located [Lat: ${
            DataFormatter.getInstance().formatDecimalPlaces(notification.lat)
        },Lon: ${DataFormatter.getInstance().formatDecimalPlaces(notification.lon)}]"
        msg += if (notification.status == "success") " has entered "
        else if (notification.status == "warning") " has exited "
        else " is still outside "
        msg += if (notification.type == "geofence") "Geo Fence" else "Geo Route"
        return msg
    }
}