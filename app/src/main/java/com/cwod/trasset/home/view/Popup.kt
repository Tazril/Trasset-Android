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
import kotlinx.android.synthetic.main.fragment_asset_info.*
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
        val asset = markersToData[marker]!!
        view.findViewById<TextView>(R.id.name).text = asset.name
        view.findViewById<TextView>(R.id.desc).text = asset.desc
        setText(view.findViewById(R.id.type), "Type", asset.type ?: "Invalid")

        val timeMsg =
            DataFormatter.getInstance().getTimeAgo(DateTime(asset.timestamp).millis) + " ago"
        setText(view.findViewById(R.id.time), "Last Updated", timeMsg)

        setText(view.findViewById(R.id.modelNo), "Model Number", asset.body.modelNo)
        setText(view.findViewById(R.id.companyName), "Company Name", asset.body.companyName)
        setText(view.findViewById(R.id.employeeId), "Employee Id", asset.body.employeeId)
        setText(view.findViewById(R.id.address), "Address", asset.body.address)

        this.view = view
        return this.view
    }

    private fun setText(textView: TextView, label: String, text: Any?) {
        if (text !== null) textView.text = getDecoratedText(label, text.toString())
        else textView.visibility = View.GONE
    }

    fun getDecoratedText(label: String, text: String) = HtmlCompat.fromHtml(
        context.getString(R.string.property, label, text),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}