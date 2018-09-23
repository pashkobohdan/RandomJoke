package com.pashkobohdan.randomjoke.execution.observers

import io.reactivex.observers.DisposableObserver

abstract class EmptyObserver <T>: DisposableObserver<T>() {
    override fun onComplete() {
        onFinally()
    }

    override fun onError(e: Throwable) {
        onFinally()
    }

    open fun onFinally() {
        //nop
    }
}