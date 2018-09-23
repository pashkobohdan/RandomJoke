package com.pashkobohdan.randomjoke.execution.observers

class ErrorObserver<T>(val onErrorCallback: (e: Throwable)->Unit) : EmptyObserver<T>() {

    override fun onNext(t: T) {
        //nop
    }

    override fun onError(e: Throwable) = onErrorCallback(e)
}