package com.androidnative.speedkotlin.dependency

import com.androidnative.speedkotlin.business.CAverage
import com.androidnative.speedkotlin.business.CLocation
import org.koin.dsl.module

val appModule = module {
    single { CLocation(get()) }
    single { CAverage(get(), get(), get()) }
}