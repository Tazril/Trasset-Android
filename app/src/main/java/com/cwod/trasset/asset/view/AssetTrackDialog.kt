package com.cwod.trasset.asset.view

import com.cwod.trasset.R
import com.cwod.trasset.base.BaseDialogFragment
import com.cwod.trasset.common.GeneralModel
import com.cwod.trasset.helper.DataFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.dialog_asset_track.*
import java.sql.Timestamp

class AssetTrackDialog(private val dateRangeSelector: DateRangeSelector?) :
    BaseDialogFragment<GeneralModel>() {
    companion object {
        const val TAG = "AssetTrackDialog"
        const val PICKER_TAG = "DateRangePicker"
    }

    override val layoutId: Int = R.layout.dialog_asset_track


    override fun loadResponse(responseModel: GeneralModel) {

    }

    val _polygon
        get() = (requireActivity() as AssetActivity)._polygon
    val _polyline
        get() = (requireActivity() as AssetActivity)._polyline

    override fun initView() {
        //init
        _isDateRange = isDateRange
        _dateRange = dateRange
        geofenceSwitch.isChecked = _polygon?.isVisible ?: false
        georouteSwitch.isChecked = _polyline?.isVisible ?: false
        dateRangeSwitch.isChecked = isDateRange
        if (isDateRange) {
            dateRangePickerBtn.show()
            range.show()
        } else {
            dateRangePickerBtn.hide()
            range.hide()
        }
        setRangeText()
        //listeners
        geofenceSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            _polygon?.isVisible = isChecked
        }
        georouteSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            _polyline?.isVisible = isChecked
        }
        dateRangeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            _isDateRange = isChecked
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
                .setTheme(theme)
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
                _dateRange = Pair(it.first!!, it.second!!)
                setRangeText()
            }
            picker.show(requireActivity().supportFragmentManager, PICKER_TAG)
        }

    }

    private fun timestamp(millis: Long) =
        Timestamp(millis).toString().split(" ").joinToString(separator = "T")

    private fun setRangeText() {
        if (_dateRange.first == 0L) return
        val start = DataFormatter.getInstance().getDateTime(_dateRange.first)
        val end = DataFormatter.getInstance().getDateTime(_dateRange.second)
        range.text = getString(R.string.range, start, end)
    }

    override fun onDismiss() {
        println("dismissed ${_isDateRange} ${_dateRange} ${dateRange}")
        if (_isDateRange && _dateRange != dateRange) {
            if (_dateRange.first != 0L)
                dateRangeSelector?.onDateRangeSelect(
                    timestamp(_dateRange.first),
                    timestamp(_dateRange.second)
                )
        }
        isDateRange = _isDateRange
        dateRange = _dateRange
    }

    var dateRange
        get() = (requireActivity() as AssetActivity).dateRange
        set(it) {
            (requireActivity() as AssetActivity).dateRange = it
        }
    var isDateRange
        get() = (requireActivity() as AssetActivity).isDateRange
        set(it) {
            (requireActivity() as AssetActivity).isDateRange = it
        }
    var _dateRange = Pair<Long, Long>(0, 0)
    var _isDateRange = false
}

