package com.pashkobohdan.randomjoke.execution

import com.pashkobohdan.randomjoke.execution.scheduler.ThreadPoolScheduler
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

abstract class AbstractUseCase<T> {

    @Inject
    lateinit var scheduler: ThreadPoolScheduler

    fun execute(observable: Observable<T>, subscriber: DisposableObserver<T>): Disposable {
        return observable
                .subscribeOn(scheduler.getScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { error ->
//                    Crashlytics.logException(Exception(error))
                    //TODO add
                }
                .subscribeWith(subscriber)
    }
}