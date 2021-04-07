package com.cwod.trasset.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.cwod.trasset.R
import com.cwod.trasset.helper.DataFormatter
import com.cwod.trasset.home.provider.model.AssetListModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import org.joda.time.DateTime

class Popup(
    val context: Context,
    val markersToData: Map<Marker, AssetListModel>
) : GoogleMap.InfoWindowAdapter {
    lateinit var view: View

    override fun getInfoContents(marker: Marker?): View {
        return this.view
    }

    override fun getInfoWindow(marker: Marker?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_popup, null, false)
        val asset = markersToData[marker]
        view.findViewById<TextView>(R.id.name).text = asset?.name
        view.findViewById<TextView>(R.id.desc).text = asset?.desc
        view.findViewById<TextView>(R.id.type).text =
            getDecoratedText("Type", asset?.type ?: "Invalid")
        val timeMsg =
            DataFormatter.getInstance().getTimeAgo(DateTime(asset?.timestamp).millis) + " ago"
        view.findViewById<TextView>(R.id.time).text = getDecoratedText("Last Updated", timeMsg)
        if (asset?.body?.modelNo !== null)
            view.findViewById<TextView>(R.id.modelNo).text =
                getDecoratedText("Model Number", asset.body.modelNo)
        else
            view.findViewById<TextView>(R.id.modelNo).visibility = View.GONE
        if (asset?.body?.companyName !== null)
            view.findViewById<TextView>(R.id.companyName).text =
                getDecoratedText("Company Name ", asset.body.companyName)
        else
            view.findViewById<TextView>(R.id.companyName).visibility = View.GONE
        if (asset?.body?.employeeId !== null)
            view.findViewById<TextView>(R.id.employeeId).text =
                getDecoratedText("Employee Id", asset.body.employeeId.toString())
        else
            view.findViewById<TextView>(R.id.employeeId).visibility = View.GONE
        if (asset?.body?.address !== null)
            view.findViewById<TextView>(R.id.address).text =
                getDecoratedText("Address", asset.body.address)
        else
            view.findViewById<TextView>(R.id.address).visibility = View.GONE
//        Glide.with(view)
//            .load(markersToData[marker]?.image_url)
//            .into(view.findViewById(R.id.asset_img))
        this.view = view
        return this.view
    }

    fun getDecoratedText(label: String, text: String) = HtmlCompat.fromHtml(
        context.getString(R.string.property, label, text),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}