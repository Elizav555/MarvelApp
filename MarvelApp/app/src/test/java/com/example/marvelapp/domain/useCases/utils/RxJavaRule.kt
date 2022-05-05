package com.example.marvelapp.domain.useCases.utils

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class RxJavaRule : TestWatcher() {
    val scheduler: Scheduler = Schedulers.trampoline()

    override fun starting(description: Description?) {
        super.starting(description)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler }
    }
}

