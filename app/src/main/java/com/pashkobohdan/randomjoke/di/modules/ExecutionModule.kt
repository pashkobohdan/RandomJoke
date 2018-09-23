package com.pashkobohdan.randomjoke.di.modules

import com.pashkobohdan.randomjoke.execution.scheduler.ThreadPoolScheduler
import com.pashkobohdan.randomjoke.execution.scheduler.impl.ThreadPoolSchedulerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ExecutionModule {

    @Provides
    @Singleton
    fun provideThreadPoolScheduler(threadPoolScheduler: ThreadPoolSchedulerImpl): ThreadPoolScheduler {
        return threadPoolScheduler
    }
}