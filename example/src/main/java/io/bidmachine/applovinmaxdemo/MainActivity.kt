package io.bidmachine.applovinmaxdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.applovin.mediation.*
import com.applovin.sdk.AppLovinSdk
import io.bidmachine.BidMachine
import io.bidmachine.applovinmaxdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SELLER_ID = "5"
        private const val MEDIATION_PROVIDER = "max"
    }

    private val bannerAdWrapper = BannerAdWrapper()
    private val interstitialAdWrapper = InterstitialAdWrapper()
    private val rewardedAdWrapper = RewardedAdWrapper()

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
        BidMachine.initialize(this, SELLER_ID)

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