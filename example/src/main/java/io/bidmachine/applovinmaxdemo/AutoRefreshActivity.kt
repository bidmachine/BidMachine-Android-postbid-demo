package io.bidmachine.applovinmaxdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.bidmachine.applovinmaxdemo.adwrapper.AdWrapperListener
import io.bidmachine.applovinmaxdemo.adwrapper.AutoRefreshBannerView
import io.bidmachine.applovinmaxdemo.databinding.ActivityAutoRefreshBinding

class AutoRefreshActivity : AppCompatActivity() {

    companion object {
        private const val REFRESH_TIME_MS = 15000L

        fun createIntent(context: Context): Intent {
            return Intent(context, AutoRefreshActivity::class.java)
        }
    }

    private lateinit var autoRefreshBannerView: AutoRefreshBannerView

    private lateinit var binding: ActivityAutoRefreshBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        autoRefreshBannerView = AutoRefreshBannerView(this).apply {
            layoutParams = ViewGroup.LayoutParams(Utils.dp2px(context, 320F),
                                                  Utils.dp2px(context, 50F))
            setAdUnitId(getString(R.string.banner_max_ad_unit_id))
            setAutoRefreshTime(REFRESH_TIME_MS)
            setListener(BannerAdWrapperListener())
        }

        binding = ActivityAutoRefreshBinding.inflate(layoutInflater).apply {
            setContentView(root)

            bLoad.setOnClickListener {
                bShow.isEnabled = false
                autoRefreshBannerView.loadAd()
            }
            bShow.setOnClickListener {
                adContainer.removeAllViews()
                adContainer.addView(autoRefreshBannerView)
            }
            bHide.setOnClickListener {
                adContainer.removeAllViews()
            }
            bStopRefresh.setOnClickListener {
                autoRefreshBannerView.stopAutoRefresh()
            }
            bStartRefresh.setOnClickListener {
                autoRefreshBannerView.startAutoRefresh()
            }
            bDestroy.setOnClickListener {
                autoRefreshBannerView.destroy()
            }
        }
    }

    private inner class BannerAdWrapperListener : AdWrapperListener {

        override fun onAdLoaded() {
            binding.bShow.isEnabled = true

            Toast.makeText(this@AutoRefreshActivity, "Banner loaded", Toast.LENGTH_SHORT).show()
        }

        override fun onAdFailToLoad() {
            Toast.makeText(this@AutoRefreshActivity, "Banner failed to load", Toast.LENGTH_SHORT).show()
        }

        override fun onAdShown() {
            Toast.makeText(this@AutoRefreshActivity, "Banner shown", Toast.LENGTH_SHORT).show()
        }

        override fun onAdClicked() {
            Toast.makeText(this@AutoRefreshActivity, "Banner clicked", Toast.LENGTH_SHORT).show()
        }

    }

}