package com.pashkobohdan.randomjoke.execution.scheduler.impl

import com.pashkobohdan.randomjoke.execution.scheduler.ThreadPoolScheduler
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

import java.util.concurrent.Executors
import javax.inject.Inject

class ThreadPoolSchedulerImpl @Inject constructor(): ThreadPoolScheduler {

    private val threadPoolScheduler by lazy {
        Schedulers.from(Executors.newFixedThreadPool(MAX_THREAD_COUNT))
    }

    override fun getScheduler(): Scheduler {
        return threadPoolScheduler
    }

    companion object {
        private val MAX_THREAD_COUNT = 2
    }
}