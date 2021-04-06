package com.cwod.trasset.authentication.presenter

import com.cwod.trasset.authentication.model.AuthenticationModel
import com.cwod.trasset.authentication.model.AuthenticationProvider
import com.cwod.trasset.authentication.view.SignInView
import com.cwod.trasset.base.BasePresenter
import com.cwod.trasset.helper.PresenterCallback

class AuthenticationSignInPresenter(var view: SignInView, var provider : AuthenticationProvider) : BasePresenter() {

     fun getSignInResponse() {
        view.showProgressBar()
        provider.getUserSignInResponse(object : PresenterCallback<AuthenticationModel> {

            override fun onSuccess(responseModel: AuthenticationModel) {
                view.hideProgressBar()
                view.loadResponse(responseModel)
            }
            override fun onFailure(message: String) {
                view.show("Fails")
                view.hideProgressBar()
            }
        }).also { compositeDisposable.add(it) }
    }
    override fun onCleared() {
        view.hideProgressBar()
        super.onCleared()
    }

}