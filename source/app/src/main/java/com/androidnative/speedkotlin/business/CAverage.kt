package com.androidnative.speedkotlin.business

class CAverage(
    var average: Double = 0.0,
    value: Short,
    var counter: Int = 0) {

    var value: Short = value
        set(value) {
            counter++
            average = ((average * (counter - 1) + value) / counter)
            field = value
        }

    fun getResult() : String = "$counter - $average"

}