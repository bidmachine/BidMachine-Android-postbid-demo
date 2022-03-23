package io.bidmachine.applovinmaxdemo.ad

import android.app.Activity

abstract class AdObject {

    abstract fun load(activity: Activity,
                      priceFloor: Double?,
                      loadListener: AdObjectLoadListener<AdObject>)

    abstract fun getPrice(): Double?

    abstract fun canShow(): Boolean

    abstract fun destroy()

}