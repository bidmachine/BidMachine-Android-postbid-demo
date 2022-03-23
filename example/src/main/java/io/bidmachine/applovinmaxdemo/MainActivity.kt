package io.bidmachine.applovinmaxdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.applovin.sdk.AppLovinSdk
import com.appnexus.opensdk.SDKSettings
import com.inmobi.sdk.InMobiSdk
import io.bidmachine.BidMachine
import io.bidmachine.applovinmaxdemo.adwrapper.AdWrapperLoadListener
import io.bidmachine.applovinmaxdemo.adwrapper.BannerAdWrapper
import io.bidmachine.applovinmaxdemo.adwrapper.InterstitialAdWrapper
import io.bidmachine.applovinmaxdemo.adwrapper.RewardedAdWrapper
import io.bidmachine.applovinmaxdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val BID_MACHINE_SELLER_ID = "5"
        private const val INMOBI_ACCOUNT_ID = "_____YOUR_INMOBI_ACCOUNT_ID_____"
        private const val MEDIATION_PROVIDER = "max"
        private const val BANNER_MAX_AD_UNIT_ID = "YOUR_BANNER_AD_UNIT_ID"
        private const val INTERSTITIAL_MAX_AD_UNIT_ID = "YOUR_INTERSTITIAL_AD_UNIT_ID"
        private const val REWARDED_MAX_AD_UNIT_ID = "YOUR_REWARDED_AD_UNIT_ID"
    }

    private val bannerAdWrapper = BannerAdWrapper(BANNER_MAX_AD_UNIT_ID)
    private val interstitialAdWrapper = InterstitialAdWrapper(INTERSTITIAL_MAX_AD_UNIT_ID)
    private val rewardedAdWrapper = RewardedAdWrapper(REWARDED_MAX_AD_UNIT_ID)

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)

            bLoadBanner.setOnClickListener {
                bShowBanner.isEnabled = false
                bannerAdWrapper.loadAd(this@MainActivity, BannerAdWrapperLoadListener())
            }
            bShowBanner.setOnClickListener {
                bannerAdWrapper.showAd(adContainer)
            }
            bLoadInterstitial.setOnClickListener {
                bShowInterstitial.isEnabled = false
                interstitialAdWrapper.loadAd(this@MainActivity, InterstitialAdWrapperLoadListener())
            }
            bShowInterstitial.setOnClickListener {
                interstitialAdWrapper.showAd()
            }
            bLoadRewarded.setOnClickListener {
                bShowRewarded.isEnabled = false
                rewardedAdWrapper.loadAd(this@MainActivity, RewardedAdWrapperLoadListener())
            }
            bShowRewarded.setOnClickListener {
                rewardedAdWrapper.showAd()
            }
        }

        initializeSdk()
    }

    private fun initializeSdk() {
        BidMachine.setLoggingEnabled(true)
        BidMachine.setTestMode(true)
        BidMachine.initialize(this, BID_MACHINE_SELLER_ID)

        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG)
        InMobiSdk.init(this, INMOBI_ACCOUNT_ID, null, null)

        SDKSettings.enableTestMode(true)
        SDKSettings.init(this, {}, true, true)

        AppLovinSdk.getInstance(this).apply {
            mediationProvider = MEDIATION_PROVIDER
            initializeSdk()
        }
    }


    private inner class BannerAdWrapperLoadListener : AdWrapperLoadListener {

        override fun onAdLoaded() {
            binding.bShowBanner.isEnabled = true
        }

        override fun onAdFailToLoad() {
            Toast.makeText(this@MainActivity, "Banner fail to load", Toast.LENGTH_SHORT).show()
        }

    }

    private inner class InterstitialAdWrapperLoadListener : AdWrapperLoadListener {

        override fun onAdLoaded() {
            binding.bShowInterstitial.isEnabled = true
        }

        override fun onAdFailToLoad() {
            Toast.makeText(this@MainActivity, "Interstitial fail to load", Toast.LENGTH_SHORT).show()
        }

    }

    private inner class RewardedAdWrapperLoadListener : AdWrapperLoadListener {

        override fun onAdLoaded() {
            binding.bShowRewarded.isEnabled = true
        }

        override fun onAdFailToLoad() {
            Toast.makeText(this@MainActivity, "Rewarded fail to load", Toast.LENGTH_SHORT).show()
        }

    }

}