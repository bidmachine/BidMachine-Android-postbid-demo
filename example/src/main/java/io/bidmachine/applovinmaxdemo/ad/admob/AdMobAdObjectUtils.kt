package io.bidmachine.applovinmaxdemo.ad.admob

object AdMobAdObjectUtils {

    /**
     * Creates a set sorted by price ascending.
     */
    fun createSortedSet(vararg elements: AdMobAdUnit): Set<AdMobAdUnit> {
        return sortedSetOf(Comparator { adUnit1, adUnit2 ->
            adUnit1.price.compareTo(adUnit2.price)
        }, *elements)
    }

    /**
     * Finds the first [AdMobAdUnit] whose price is equal to or greater than the price floor.
     */
    fun Set<AdMobAdUnit>.findAdUnit(priceFloor: Double?): AdMobAdUnit? {
        if (isEmpty()) {
            return null
        }
        if (priceFloor == null) {
            return firstOrNull()
        }
        return firstOrNull {
            it.price >= priceFloor
        }
    }

}