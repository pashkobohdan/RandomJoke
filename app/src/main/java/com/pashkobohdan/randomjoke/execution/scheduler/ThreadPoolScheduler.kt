package com.pashkobohdan.randomjoke.execution.scheduler

import io.reactivex.Scheduler

interface ThreadPoolScheduler {
    fun getScheduler() : Scheduler
}