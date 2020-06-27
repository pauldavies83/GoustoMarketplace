package dev.pauldavies.goustomarketplace.util

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * JUnit Test Rule which overrides RxJava and Android schedulers for use in unit tests.
 */
class RxSchedulerRule() : TestRule {
    private val trampolineScheduler: Scheduler = Schedulers.trampoline()

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                RxAndroidPlugins.reset()
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { trampolineScheduler }
                RxAndroidPlugins.setMainThreadSchedulerHandler { trampolineScheduler }

                RxJavaPlugins.reset()
                RxJavaPlugins.setIoSchedulerHandler { trampolineScheduler }
                RxJavaPlugins.setNewThreadSchedulerHandler { trampolineScheduler }
                RxJavaPlugins.setComputationSchedulerHandler { trampolineScheduler }
                RxJavaPlugins.setSingleSchedulerHandler { trampolineScheduler }

                try {
                    base.evaluate()
                } finally {
                    RxAndroidPlugins.reset()
                    RxJavaPlugins.reset()
                }
            }
        }
    }
}
