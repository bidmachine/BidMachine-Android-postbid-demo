package io.bidmachine.applovinmaxdemo.ad

abstract class AdObject {

    abstract fun getPrice(): Double?

    abstract fun canShow(): Boolean

    abstract fun destroy()

}