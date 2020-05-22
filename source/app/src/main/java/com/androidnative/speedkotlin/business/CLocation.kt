package com.androidnative.speedkotlin.business

import android.location.Location

class CLocation(location: Location?) : Location(location) {

    override fun getSpeed(): Float {
        return super.getSpeed() * 3.6f
    }
}