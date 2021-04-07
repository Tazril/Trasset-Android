package com.cwod.trasset.asset.view

import android.view.View
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.cwod.trasset.R
import com.cwod.trasset.asset.presenter.AssetInfoPresenter
import com.cwod.trasset.asset.provider.AssetTrackProvider
import com.cwod.trasset.asset.provider.model.TrackWrapper
import com.cwod.trasset.base.BaseFragment
import com.cwod.trasset.helper.DataFormatter
import kotlinx.android.synthetic.main.fragment_asset_info.*
import org.joda.time.DateTime


class AssetInfoView : BaseFragment<TrackWrapper>() {
    companion object {
        const val TAG = "DashboardView"
    }

    private lateinit var presenter: AssetInfoPresenter

    override val layoutId: Int = R.layout.fragment_asset_info

    override fun loadResponse(responseModel: TrackWrapper) {

        val asset = responseModel.asset_data
        //name and type
        nameTextView.text = asset.name
        typeTextView.text = asset.type
        Glide.with(this)
            .load(asset.image_url)
            .into(assetImageView)

        //description
        desc.text = asset.desc

        //properties
        if (asset.body.modelNo !== null)
            modelNo.text = getDecoratedText("Model Number", asset.body.modelNo)
        else
            modelNo.visibility = View.GONE
        if (asset.body.companyName !== null)
            companyName.text = getDecoratedText("Company Name ", asset.body.companyName)
        else
            companyName.visibility = View.GONE
        if (asset.body.employeeId !== null)
            employeeId.text =
                getDecoratedText("Employee Id", asset.body.employeeId.toString())
        else
            employeeId.visibility = View.GONE
        if (asset.body.address !== null)
            address.text =
                getDecoratedText("Address", asset.body.address)
        else
            address.visibility = View.GONE

        //Location
        lat.text = getDecoratedText("Latitude", asset.lat.toString())
        lon.text = getDecoratedText("Longitude", asset.lon.toString())
        val timeMsg =
            DataFormatter.getInstance().getTimeAgo(DateTime(asset.timestamp).millis) + " ago"
        time.text = getDecoratedText("Last Updated", timeMsg)
    }

    override fun initView() {
        val id = requireActivity().intent.getStringExtra("id") ?: "none"
        presenter = AssetInfoPresenter(this, AssetTrackProvider(id))
        presenter.getAssetTrackResponse()
    }

    fun getDecoratedText(label: String, text: String) = HtmlCompat.fromHtml(
        getString(R.string.property, label, text),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}
