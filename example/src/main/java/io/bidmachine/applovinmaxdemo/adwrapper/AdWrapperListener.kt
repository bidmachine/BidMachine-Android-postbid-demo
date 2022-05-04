package io.bidmachine.applovinmaxdemo.adwrapper

interface AdWrapperListener {

    fun onAdLoaded()

    fun onAdFailToLoad()

    fun onAdShown()

    fun onAdClicked()

}