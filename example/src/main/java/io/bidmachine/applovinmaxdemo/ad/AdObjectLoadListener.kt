package io.bidmachine.applovinmaxdemo.ad

interface AdObjectLoadListener<out AdObjectType : AdObject> {

    fun onLoaded(adObject: @UnsafeVariance AdObjectType)

    fun onFailToLoad(adObject: @UnsafeVariance AdObjectType, errorMessage: String)

}