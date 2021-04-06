package com.cwod.trasset.asset.view

import com.cwod.trasset.R
import com.cwod.trasset.base.BaseDialogFragment
import com.cwod.trasset.helper.DataFormatter
import com.cwod.trasset.helper.GeneralModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.dialog_asset_track.*
import java.sql.Timestamp

class AssetTrackDialog(val dateRangeSelector: DateRangeSelector?) :
    BaseDialogFragment<GeneralModel>() {
    companion object {
        const val TAG = "AssetTrackDialog"
        const val PICKER_TAG = "DateRangePicker"
    }

    override val layoutId: Int = com.cwod.trasset.R.layout.dialog_asset_track

    override fun loadResponse(responseModel: GeneralModel) {

    }

    val _polygon
        get() = (requireActivity() as AssetActivity)._polygon
    val _polyline
        get() = (requireActivity() as AssetActivity)._polyline

    override fun initView() {
        geofenceSwitch.isChecked = _polygon?.isVisible ?: false
        georouteSwitch.isChecked = _polyline?.isVisible ?: false
        geofenceSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            _polygon?.isVisible = isChecked
        }
        georouteSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            _polyline?.isVisible = isChecked
        }
        dateRangeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                dateRangePickerBtn.show()
                range.show()
            } else {
                dateRangePickerBtn.hide()
                range.hide()
            }
        }
        dateRangePickerBtn.setOnClickListener {
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("")
                .setSelection(
                    androidx.core.util.Pair(
                        MaterialDatePicker.todayInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()
            picker.addOnNegativeButtonClickListener {

                dismiss()
            }
            picker.addOnPositiveButtonClickListener {
                dateRange = Pair(it.first!!, it.second!!)
                println("The selected date range is ${it.first} - ${it.second}")
                setRangeText()
                println("dateRangeSelector "+dateRangeSelector)
                if (dateRangeSwitch.isChecked && dateRange.first != 0L)
                    dateRangeSelector?.onDateRangeSelect(
                        timestamp(dateRange.first),
                        timestamp(dateRange.second)
                    )
            }
            picker.show(requireActivity().supportFragmentManager, PICKER_TAG)
        }
        setRangeText()
    }

    fun timestamp(millis: Long) =
        Timestamp(millis).toString().split(" ").joinToString(separator = "T")

    private fun setRangeText() {
        if (dateRange.first == 0L) return;
        println(dateRange)
        val start = DataFormatter.getInstance().getDateTime(dateRange.first)
        val end = DataFormatter.getInstance().getDateTime(dateRange.second)
        range.text = getString(R.string.range, start, end)
    }


    var dateRange
        get() = (requireActivity() as AssetActivity).dateRange
        set(it) {
            (requireActivity() as AssetActivity).dateRange = it
        }

}

