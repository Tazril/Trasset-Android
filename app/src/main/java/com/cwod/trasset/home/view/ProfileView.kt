package com.cwod.trasset.home.view

import android.app.AlertDialog
import android.content.Intent
import androidx.core.text.HtmlCompat
import com.cwod.trasset.R
import com.cwod.trasset.authentication.view.AuthActivity
import com.cwod.trasset.base.BaseFragment
import com.cwod.trasset.helper.SharedPref
import com.cwod.trasset.home.presenter.ProfilePresenter
import com.cwod.trasset.home.provider.ProfileProvider
import com.cwod.trasset.home.provider.model.ProfileModel
import kotlinx.android.synthetic.main.fragment_profile_view.*


class ProfileView : BaseFragment<ProfileModel>() {

    companion object {
        const val TAG = "ProfileView"
    }

    lateinit var presenter: ProfilePresenter


    override val layoutId: Int = R.layout.fragment_profile_view

    override fun loadResponse(responseModel: ProfileModel) {
        println(responseModel)
        assetImageView.bind(responseModel.name, "")
        name.text = responseModel.name
        role.text = responseModel.role
        sex.text = if (responseModel.isMale) getDecoratedText("Gender", "Male")
        else getDecoratedText("Gender", "Female")
        phone.text = getDecoratedText("Phone", responseModel.phone.toString())
        address.text = responseModel.address
        about.text = responseModel.about
        email.text = getDecoratedText("Email", responseModel.email)
    }

    override fun initView() {
        presenter = ProfilePresenter(this, ProfileProvider())
        presenter.getProfileResponse()
        logoutBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are You Sure?")
                .setPositiveButton("Yes"
                ) { dialog, id ->
                    SharedPref.clear()
                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("No"
                ) { dialog, id ->
                }
            builder.create().show()

        }
    }


    fun getDecoratedText(label: String, text: String) = HtmlCompat.fromHtml(
        getString(R.string.property, label, text),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )

}
