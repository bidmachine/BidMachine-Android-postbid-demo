package io.bidmachine.applovinmaxdemo.ad

interface AdObjectListener<out AdObjectType : AdObject> {

    fun onLoaded(adObject: @UnsafeVariance AdObjectType)

    fun onFailToLoad(adObject: @UnsafeVariance AdObjectType, errorMessage: String)

    fun onShown(adObject: @UnsafeVariance AdObjectType)

    fun onClicked(adObject: @UnsafeVariance AdObjectType)

}