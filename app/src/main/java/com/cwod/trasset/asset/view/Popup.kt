package com.cwod.trasset.asset.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.cwod.trasset.R
import com.cwod.trasset.asset.provider.model.TrackItemModel
import com.cwod.trasset.helper.DataFormatter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import org.joda.time.DateTime

class Popup(
    val context: Context,
    private val markersToData: Map<Marker, TrackItemModel>
) : GoogleMap.InfoWindowAdapter {
    lateinit var view: View

    override fun getInfoContents(marker: Marker?): View {
        return this.view
    }

    override fun getInfoWindow(marker: Marker?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_popup_track, null, false)
        val trackItem = markersToData[marker]
        view.findViewById<TextView>(R.id.lat).text = getDecoratedText(
            "Latitude",
            DataFormatter.getInstance()
                .formatDecimalPlaces(trackItem?.lat ?: 0.0)
        )
        view.findViewById<TextView>(R.id.lon).text =
            getDecoratedText(
                "Longitude", DataFormatter.getInstance()
                    .formatDecimalPlaces(trackItem?.lon ?: 0.0)
            )
        val timeMsg =
            DataFormatter.getInstance().getMediumDateTime(DateTime(trackItem?.timestamp).millis)
        view.findViewById<TextView>(R.id.time).text = getDecoratedText("At", timeMsg)
        this.view = view
        return this.view
    }


    fun getDecoratedText(label: String, text: String) = HtmlCompat.fromHtml(
        context.getString(R.string.property, label, text),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}