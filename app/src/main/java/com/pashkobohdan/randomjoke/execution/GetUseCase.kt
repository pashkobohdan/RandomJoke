package com.pashkobohdan.randomjoke.execution

import com.pashkobohdan.randomjoke.execution.observers.EmptyObserver
import io.reactivex.Observable
import io.reactivex.disposables.Disposable


abstract class GetUseCase<T> : AbstractUseCase<T>() {

    abstract fun getData(): T

    fun execute(subscriber: EmptyObserver<T>): Disposable {
        return execute(Observable.fromCallable(this::getData), subscriber)
    }
}