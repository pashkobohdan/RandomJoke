package com.pashkobohdan.randomjoke.execution.observers

abstract class UnitObserver : EmptyObserver<Unit>() {

    override fun onNext(t: Unit) = onNext()

    abstract fun onNext()
}