package com.cwod.trasset.authentication.view

import android.content.Intent
import android.text.TextUtils
import android.widget.EditText
import androidx.navigation.findNavController

import com.cwod.trasset.R
import com.cwod.trasset.authentication.model.AuthenticationModel
import com.cwod.trasset.authentication.model.AuthenticationProvider
import com.cwod.trasset.authentication.presenter.AuthenticationSignUpPresenter
import com.cwod.trasset.base.BaseActivity
import com.cwod.trasset.base.BaseFragment
import com.cwod.trasset.helper.Constants
import com.cwod.trasset.helper.SharedPref
import com.cwod.trasset.home.view.HomeActivity
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

class SignUpView : BaseFragment<AuthenticationModel>() {

    override val layoutId: Int = R.layout.fragment_sign_up
    private lateinit var signUpPresenter : AuthenticationSignUpPresenter

    lateinit var userName : EditText
    lateinit var userPassword : EditText
    lateinit var userEmail : EditText


    override fun loadResponse(responseModel: AuthenticationModel) {
        // response model
        SharedPref.putString(Constants.AUTHORIZATION, responseModel.token)
        SharedPref.putString(Constants.EMAIL, responseModel.email)
        val intent = Intent(this.context, HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun initView() {
        // taking care of synthetics and ids
        userName = userNameSignUp.userNameSignUpText
        userPassword = passwordSignUp.passwordSignUpText
        userEmail = emailSignUp.emailSignUpText
        signUpButton.setOnClickListener {
            signUp()
        }
        signInButton.setOnClickListener {
            requireView().findNavController().navigateUp()
        }
    }

    private fun signUp() {
        val jsonObject = JsonObject()
        if (TextUtils.isEmpty(userName.text) || TextUtils.isEmpty(userPassword.text) ||
                                                    TextUtils.isEmpty(userEmail.text)) {
            this.showLong("Please enter all details")
        }
        else if (this.checkPhone(userEmail.text.toString())){
            this.showLong("Please Enter Correct Phone Number")
        }
        else {
            jsonObject.addProperty("email", userEmail.text.toString())
            jsonObject.addProperty("name", userName.text.toString())
            jsonObject.addProperty("password", userPassword.text.toString())
            signUpPresenter = AuthenticationSignUpPresenter(this, AuthenticationProvider(jsonObject))
            signUpPresenter.getSignUpResponse()
        }
    }
    override fun onDestroyView() {
        if(this::signUpPresenter.isInitialized)
            signUpPresenter.onCleared()
        super.onDestroyView()
    }
}
