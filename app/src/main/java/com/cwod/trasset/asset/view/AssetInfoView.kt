package com.cwod.trasset.asset.view

import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.cwod.trasset.R
import com.cwod.trasset.asset.presenter.AssetInfoPresenter
import com.cwod.trasset.asset.provider.AssetTrackProvider
import com.cwod.trasset.asset.provider.model.AssetModel
import com.cwod.trasset.base.BaseFragment
import com.cwod.trasset.helper.DataFormatter
import kotlinx.android.synthetic.main.fragment_asset_info.*
import org.joda.time.DateTime


class AssetInfoView : BaseFragment<AssetModel>() {
    companion object {
        const val TAG = "DashboardView"
    }

    private lateinit var presenter: AssetInfoPresenter

    override val layoutId: Int = R.layout.fragment_asset_info

    override fun loadResponse(responseModel: AssetModel) {

        val asset = responseModel

        //name and type
        nameTextView.text = asset.name
        typeTextView.text = asset.type
        Glide.with(this)
            .load(asset.image_url)
            .into(assetImageView)

        //description
        desc.text = asset.desc

        //properties
        setText(modelNo, "Model Number", asset.body.modelNo)
        setText(companyName, "Company Name", asset.body.companyName)
        setText(employeeId, "Employee Id", asset.body.employeeId)
        setText(address, "Address", asset.body.address)

        //Location
        lat.text = getDecoratedText("Latitude", asset.lat.toString())
        lon.text = getDecoratedText("Longitude", asset.lon.toString())
        val timeMsg =
            DataFormatter.getInstance().getTimeAgo(DateTime(asset.timestamp).millis) + " ago"
        time.text = getDecoratedText("Last Updated", timeMsg)
    }

    private fun setText(textView: TextView, label: String, text: Any?) {
        if (text !== null) textView.text = getDecoratedText(label, text.toString())
        else textView.hide()
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

    override fun onDestroyView() {
        if (this::presenter.isInitialized)
            presenter.onCleared()
        super.onDestroyView()
    }

}
