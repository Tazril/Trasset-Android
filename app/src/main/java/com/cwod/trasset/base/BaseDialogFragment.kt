package com.cwod.trasset.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.regex.Pattern

abstract class BaseDialogFragment<T> : DialogFragment() {

    abstract val layoutId: Int
    abstract fun loadResponse(responseModel: T)

    // To initialize in OnCreateView
    abstract fun initView()
    abstract fun onDismiss()

    lateinit var baseActivity: BaseActivity
    var fragmentView: View? = null

    //material theming
    var dialogView: View? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), theme).apply {
            dialogView =
                onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState)
            dialogView?.let { onViewCreated(it, savedInstanceState) }
            setView(dialogView)
        }.create()
    }

    override fun getView(): View? = dialogView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(layoutId, container, false)
        baseActivity = (activity as BaseActivity)
        return fragmentView
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismiss()
        super.onDismiss(dialog)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    infix fun show(message: String) = baseActivity.show(message)
    infix fun showError(message: String) = baseActivity.showError(message)
    infix fun showLong(message: String) = baseActivity.showLong(message)


    fun hideKeyboard() {
        baseActivity.currentFocus.let {
            val imm =
                baseActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it?.windowToken, 0)
        }
    }


    fun showProgressBar() {
        baseActivity.progressBar.show()
    }

    fun hideProgressBar() {
        baseActivity.progressBar.hide()
    }

    // extending function of view class

    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.hide() {
        visibility = View.INVISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }

    // check internet connection
    fun isConnected(): Boolean = baseActivity.isConnected()

    // check for empty text
    fun check(message: String): Boolean {
        return message.trim().isNotEmpty()
    }



}
