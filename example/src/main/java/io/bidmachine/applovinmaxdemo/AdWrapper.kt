package io.bidmachine.applovinmaxdemo

import android.app.Activity
import android.util.Log
import io.bidmachine.PriceFloorParams

interface AdWrapper {

    companion object {
        val TAG: String = AdWrapper::class.java.simpleName
    }

    /**
     * If price floor does not defined, no need to create instance of PriceFloorParams.
     * By default will be used PriceFloorParams with price floor of 0.01.
     */
    fun definePriceFloorParams(maxRevenue: Double): PriceFloorParams? {
        return definePriceFloor(maxRevenue)?.let { priceFloor ->
            Log.d(TAG, "BidMachine price floor - $priceFloor")

            PriceFloorParams().addPriceFloor(priceFloor)
        }
    }

    /**
     * If Applovin MAX return revenue it must be converted to price floor,
     * otherwise no need price floor (by default it will be 0.01) or you can set your price floor.
     */
    private fun definePriceFloor(maxRevenue: Double): Double? {
        return if (maxRevenue > 0) {
            maxRevenue * 1000
        } else {
            null
        }
    }

    fun loadAd(activity: Activity, adWrapperLoadListener: AdWrapperLoadListener)

    fun showAd()

    fun destroy()

}