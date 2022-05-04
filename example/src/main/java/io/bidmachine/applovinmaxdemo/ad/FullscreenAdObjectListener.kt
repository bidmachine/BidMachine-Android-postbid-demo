package io.bidmachine.applovinmaxdemo.ad

interface FullscreenAdObjectListener : AdObjectListener<FullscreenAdObject> {

    fun onClosed(adObject: FullscreenAdObject)

}