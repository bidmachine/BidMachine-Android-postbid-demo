package io.bidmachine.applovinmaxdemo.ad.inmobi

class InMobiInterstitialAdObject : InMobiFullscreenAdObject() {

    companion object {
        private const val PLACEMENT_ID = 1475973082314L
    }

    override fun getPlacementId(): Long {
        return PLACEMENT_ID
    }

}