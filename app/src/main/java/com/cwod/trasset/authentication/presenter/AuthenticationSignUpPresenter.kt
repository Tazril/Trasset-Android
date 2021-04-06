package com.cwod.trasset.authentication.presenter

import com.cwod.trasset.authentication.model.AuthenticationModel
import com.cwod.trasset.authentication.model.AuthenticationProvider
import com.cwod.trasset.authentication.view.SignUpView
import com.cwod.trasset.base.BasePresenter
import com.cwod.trasset.helper.PresenterCallback

class AuthenticationSignUpPresenter(var view : SignUpView, var provider : AuthenticationProvider) : BasePresenter()  {

     fun getSignUpResponse() {
        view.showProgressBar()
        provider.getUserSignUpResponse(object : PresenterCallback<AuthenticationModel> {

            override fun onSuccess(responseModel: AuthenticationModel) {
                view.hideProgressBar()
                view.loadResponse(responseModel)
            }
            override fun onFailure(message: String) {
                view show message
                view.hideProgressBar()
            }
        }).also { compositeDisposable.add(it) }
    }
    override fun onCleared() {
        view.hideProgressBar()
        super.onCleared()
    }
}