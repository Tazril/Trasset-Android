package com.cwod.trasset.home.presenter

import com.cwod.trasset.base.BasePresenter
import com.cwod.trasset.common.PresenterCallback
import com.cwod.trasset.home.provider.ProfileProvider
import com.cwod.trasset.home.provider.model.ProfileModel
import com.cwod.trasset.home.view.ProfileView

class ProfilePresenter(var view: ProfileView, var provider: ProfileProvider) : BasePresenter() {

    fun getProfileResponse() {
        view.showProgressBar()
        provider.getProfileResponse(object : PresenterCallback<ProfileModel> {
            override fun onSuccess(responseModel: ProfileModel) {
                view.loadResponse(responseModel)
                view.hideProgressBar()
            }

            override fun onFailure(message: String) {
                view.show(message)
                view.hideProgressBar()
            }
        }).also { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        view.hideProgressBar()
        super.onCleared()
    }


}