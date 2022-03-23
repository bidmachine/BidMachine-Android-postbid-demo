package io.bidmachine.applovinmaxdemo.ad.bidmachine

import io.bidmachine.PriceFloorParams

internal object BidMachineAdObjectUtils {

    fun Double.toPriceFloorParams(): PriceFloorParams? = PriceFloorParams().addPriceFloor(this)

}