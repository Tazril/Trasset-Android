package com.cwod.trasset.home.view

import com.cwod.trasset.base.BaseDialogFragment
import com.cwod.trasset.common.GeneralModel
import kotlinx.android.synthetic.main.dialog_asset_list.*

class AssetListDialog(val typeSelector: AssetTypeSelector?) :
    BaseDialogFragment<GeneralModel>() {

    companion object {
        const val TAG = "AssetListDialog"
    }

    override val layoutId: Int = com.cwod.trasset.R.layout.dialog_asset_list


    override fun loadResponse(responseModel: GeneralModel) {

    }


    override fun initView() {
        newAssetType = assetType
        salesmanSwitch.isChecked = (assetType == "" || assetType == "salesman")
        truckSwitch.isChecked = (assetType == "" || assetType == "truck")

        salesmanSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked && !truckSwitch.isChecked) {
                truckSwitch.isChecked = true
            }
            setAssetType(truckSwitch.isChecked, isChecked)
        }
        truckSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!salesmanSwitch.isChecked && !isChecked) {
                salesmanSwitch.isChecked = true
            }
            setAssetType(isChecked, salesmanSwitch.isChecked)
        }
    }

    private fun setAssetType(truck: Boolean, salesman: Boolean) {
        newAssetType =
            if (truck && salesman) "" else if (salesman) "salesman" else if (truck) "truck" else "none"
    }

    override fun onDismiss() {
        if (assetType != newAssetType) {
            typeSelector?.onTypeSelect(newAssetType)
            assetType = newAssetType
        }
    }

    var assetType
        get() = (requireActivity() as HomeActivity).assetType
        set(it) {
            (requireActivity() as HomeActivity).assetType = it
        }
    var newAssetType = "undefined"


}

