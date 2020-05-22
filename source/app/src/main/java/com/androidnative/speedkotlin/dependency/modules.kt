package com.androidnative.speedkotlin.dependency

import com.androidnative.speedkotlin.business.CAverage
import org.koin.dsl.module

val appModule = module {
    single { CAverage(get(), get(), get()) }
}