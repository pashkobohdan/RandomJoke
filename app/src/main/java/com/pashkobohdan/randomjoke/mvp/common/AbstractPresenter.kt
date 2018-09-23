package com.pashkobohdan.randomjoke.mvp.common

import com.arellomobile.mvp.MvpPresenter

open class AbstractPresenter<V : AbstractScreenView> : MvpPresenter<V>() {

    //    public static final String INDEX_SCREEN_KEY = Screen.BOOK_LIST;
    //    public static String currentScreen = "";

    //    @Inject
    //    protected Router router;

//    override fun attachView(view: V) {
//        super.attachView(view)
//        view.onPresenterAttached(this)
//    }

    //    public void backNavigation() {
    //        router.exit();
    //    }
}
