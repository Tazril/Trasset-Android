package com.cwod.trasset.authentication.view

import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.findNavController

import com.cwod.trasset.R
import com.cwod.trasset.authentication.model.AuthenticationModel
import com.cwod.trasset.authentication.model.AuthenticationProvider
import com.cwod.trasset.authentication.presenter.AuthenticationSignInPresenter
import com.cwod.trasset.base.BaseFragment
import com.cwod.trasset.helper.Constants
import com.cwod.trasset.helper.SharedPref
import com.cwod.trasset.home.view.HomeActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.*

class SignInView() : BaseFragment<AuthenticationModel>() {

    override val layoutId: Int = R.layout.fragment_sign_in
    var navController: NavController? = null
    var userLoginEmail: String? = null
    var userLoginPassword: String? = null
    lateinit var email: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var signInPresenter: AuthenticationSignInPresenter

    override fun initView() {
        email = userEmailAddress.userEmailAddressText
        password = userPassword.userPasswordText
        navController = requireView().findNavController()
        loginButton.setOnClickListener {
            loginCall()
        }
        SignUpNavButton.setOnClickListener {
            navController!!.navigate(R.id.action_signInView_to_signUpView)
        }

    }

    override fun loadResponse(responseModel: AuthenticationModel) {
        SharedPref.putString(Constants.AUTHORIZATION, responseModel.token)
        SharedPref.putString(Constants.EMAIL, responseModel.email)
        val intent = Intent(this.context, HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun loginCall() {

        if (!this.check(email.text.toString()) || !this.check(password.text.toString())) {
            this.showLong("Please Enter all details")
        } else if (this.checkPhone(email.text.toString())) {
            this.showLong("Please Enter Correct Phone Number")
        } else {
            userLoginEmail = email.text.toString()
            userLoginPassword = password.text.toString()
            val jsonObject = JsonObject()
            jsonObject.addProperty("email", userLoginEmail)
            jsonObject.addProperty("password", userLoginPassword)
            signInPresenter =
                AuthenticationSignInPresenter(this, AuthenticationProvider(jsonObject))
            signInPresenter.getSignInResponse()
        }
    }

    override fun onDestroyView() {
        if (this::signInPresenter.isInitialized)
            signInPresenter.onCleared()
        super.onDestroyView()
    }

}
