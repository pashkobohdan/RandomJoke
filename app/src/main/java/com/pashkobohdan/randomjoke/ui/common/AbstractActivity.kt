package com.pashkobohdan.randomjoke.ui.common

import com.arellomobile.mvp.MvpActivity
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject
import javax.inject.Provider

abstract class AbstractActivity<P : MvpPresenter<*>> : MvpActivity(){

    @Inject
    protected lateinit var presenterProvider: Provider<P>
}