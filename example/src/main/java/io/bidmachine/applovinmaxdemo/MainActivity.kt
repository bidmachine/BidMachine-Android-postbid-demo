package io.bidmachine.applovinmaxdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.applovin.sdk.AppLovinSdk
import com.appnexus.opensdk.SDKSettings
import com.google.android.gms.ads.MobileAds
import com.inmobi.sdk.InMobiSdk
import io.bidmachine.BidMachine
import io.bidmachine.applovinmaxdemo.adwrapper.*
import io.bidmachine.applovinmaxdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val MEDIATION_PROVIDER = "max"
    }

    private lateinit var bannerAdWrapper: BannerAdWrapper
    private lateinit var interstitialAdWrapper: InterstitialAdWrapper
    private lateinit var rewardedAdWrapper: RewardedAdWrapper

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bannerAdWrapper = BannerAdWrapper(getString(R.string.banner_max_ad_unit_id))
        interstitialAdWrapper = InterstitialAdWrapper(getString(R.string.interstitial_max_ad_unit_id))
        rewardedAdWrapper = RewardedAdWrapper(getString(R.string.rewarded_max_ad_unit_id))

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)

            bOpenAutoRefreshActivity.setOnClickListener {
                AutoRefreshActivity.createIntent(this@MainActivity).also { intent ->
                    startActivity(intent)
                }
            }
            bLoadBanner.setOnClickListener {
                bShowBanner.isEnabled = false
                bannerAdWrapper.loadAd(this@MainActivity, BannerAdWrapperListener())
            }
            bShowBanner.setOnClickListener {
                bShowBanner.isEnabled = false
                bannerAdWrapper.showAd(adContainer)
            }
            bLoadInterstitial.setOnClickListener {
                bShowInterstitial.isEnabled = false
                interstitialAdWrapper.loadAd(this@MainActivity, InterstitialAdWrapperListener())
            }
            bShowInterstitial.setOnClickListener {
                bShowInterstitial.isEnabled = false
                interstitialAdWrapper.showAd(this@MainActivity)
            }
            bLoadRewarded.setOnClickListener {
                bShowRewarded.isEnabled = false
                rewardedAdWrapper.loadAd(this@MainActivity, RewardedAdWrapperListener())
            }
            bShowRewarded.setOnClickListener {
                bShowRewarded.isEnabled = false
                rewardedAdWrapper.showAd(this@MainActivity)
            }
        }

        initializeSdk()
    }

    private fun initializeSdk() {
        BidMachine.setLoggingEnabled(true)
        BidMachine.setTestMode(true)
        BidMachine.initialize(this, getString(R.string.bid_machine_seller_id))

        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG)
        InMobiSdk.init(this, getString(R.string.inmobi_account_id), null, null)

        SDKSettings.enableTestMode(true)
        SDKSettings.init(this, {}, true, true)

        MobileAds.initialize(this)

        AppLovinSdk.getInstance(this).apply {
            mediationProvider = MEDIATION_PROVIDER
            initializeSdk()
        }
    }


    private inner class BannerAdWrapperListener : AdWrapperListener {

        override fun onAdLoaded() {
            binding.bShowBanner.isEnabled = true

            Toast.makeText(this@MainActivity, "Banner loaded", Toast.LENGTH_SHORT).show()
        }

        override fun onAdFailToLoad() {
            Toast.makeText(this@MainActivity, "Banner failed to load", Toast.LENGTH_SHORT).show()
        }

        override fun onAdShown() {
            Toast.makeText(this@MainActivity, "Banner shown", Toast.LENGTH_SHORT).show()
        }

        override fun onAdClicked() {
            Toast.makeText(this@MainActivity, "Banner clicked", Toast.LENGTH_SHORT).show()
        }

    }

    private inner class InterstitialAdWrapperListener : FullscreenAdWrapperListener {

        override fun onAdLoaded() {
            binding.bShowInterstitial.isEnabled = true

            Toast.makeText(this@MainActivity, "Interstitial loaded", Toast.LENGTH_SHORT).show()
        }

        override fun onAdFailToLoad() {
            Toast.makeText(this@MainActivity, "Interstitial failed to load", Toast.LENGTH_SHORT).show()
        }

        override fun onAdShown() {
            Toast.makeText(this@MainActivity, "Interstitial shown", Toast.LENGTH_SHORT).show()
        }

        override fun onAdClicked() {
            Toast.makeText(this@MainActivity, "Interstitial clicked", Toast.LENGTH_SHORT).show()
        }

        override fun onAdClosed() {
            Toast.makeText(this@MainActivity, "Interstitial closed", Toast.LENGTH_SHORT).show()
        }

    }

    private inner class RewardedAdWrapperListener : FullscreenAdWrapperListener {

        override fun onAdLoaded() {
            binding.bShowRewarded.isEnabled = true

            Toast.makeText(this@MainActivity, "Rewarded loaded", Toast.LENGTH_SHORT).show()
        }

        override fun onAdFailToLoad() {
            Toast.makeText(this@MainActivity, "Rewarded failed to load", Toast.LENGTH_SHORT).show()
        }

        override fun onAdShown() {
            Toast.makeText(this@MainActivity, "Rewarded shown", Toast.LENGTH_SHORT).show()
        }

        override fun onAdClicked() {
            Toast.makeText(this@MainActivity, "Rewarded clicked", Toast.LENGTH_SHORT).show()
        }

        override fun onAdClosed() {
            Toast.makeText(this@MainActivity, "Rewarded closed", Toast.LENGTH_SHORT).show()
        }

    }

}