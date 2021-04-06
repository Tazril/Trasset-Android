package com.cwod.trasset.base

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter {

    val compositeDisposable = CompositeDisposable()

    open fun onCleared() {
        compositeDisposable.dispose()
    }

}