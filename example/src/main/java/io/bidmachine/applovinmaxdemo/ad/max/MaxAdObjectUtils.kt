package io.bidmachine.applovinmaxdemo.ad.max

import com.applovin.mediation.MaxAd

object MaxAdObjectUtils {

    /**
     * Obtains revenue and then converting it to CPM.
     */
    fun MaxAd.getCPM(): Double = revenue * 1000

}