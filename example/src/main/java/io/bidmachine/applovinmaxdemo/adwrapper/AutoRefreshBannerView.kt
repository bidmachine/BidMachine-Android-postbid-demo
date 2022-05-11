package io.bidmachine.applovinmaxdemo.adwrapper

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.IntRange
import io.bidmachine.applovinmaxdemo.Utils
import java.lang.ref.WeakReference

class AutoRefreshBannerView : FrameLayout {

    companion object {
        private const val DEFAULT_REFRESH_TIME_MS = 15L
    }

    private var showRunnable = ShowRunnable(this)
    private var internalLoadListener = InternalListener(this)
    private var currentBannerAdWrapper: BannerAdWrapper? = null
    private var pendingBannerAdWrapper: BannerAdWrapper? = null
    private var adUnitId: String? = null
    private var refreshTimeMs: Long = DEFAULT_REFRESH_TIME_MS
    private var listener: AdWrapperListener? = null

    private var isAdLoaded = false
    private var isShowPending = false
    private var isRefreshStarted = true

    constructor(context: Context) :
            super(context)

    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)

        if (getVisibility() == VISIBLE) {
            showAd()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        showAd()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        isShowPending = true
    }

    /**
     * Sets MAX ad unit id.
     */
    fun setAdUnitId(adUnitId: String) {
        this.adUnitId = adUnitId
    }

    /**
     * Sets auto-refresh time in milliseconds.
     */
    fun setAutoRefreshTime(@IntRange(from = 1) refreshTimeMs: Long) {
        if (refreshTimeMs > 0) {
            this.refreshTimeMs = refreshTimeMs
        }
    }

    /**
     * Sets listener.
     */
    fun setListener(adWrapperListener: AdWrapperListener?) {
        listener = adWrapperListener
    }

    /**
     * Starts load ad.
     */
    fun loadAd() {
        isShowPending = true
        loadBannerAdWrapper()
    }

    /**
     * Stops auto-refreshing.
     */
    fun stopAutoRefresh() {
        if (!isRefreshStarted) {
            return
        }
        isRefreshStarted = false
        removeCallbacks(showRunnable)
    }

    /**
     * Starts auto-refreshing.
     */
    fun startAutoRefresh() {
        if (isRefreshStarted) {
            return
        }
        isRefreshStarted = true
        startShowRunnable()
    }

    /**
     * Destroys ad objects and cleanups memory.
     */
    fun destroy() {
        stopAutoRefresh()

        setCurrentBannerAdWrapper(null)

        pendingBannerAdWrapper?.destroy()
        pendingBannerAdWrapper = null
    }

    private fun startShowRunnable() {
        postDelayed(showRunnable, refreshTimeMs)
    }

    private fun canShowAd(): Boolean {
        return isAdLoaded && isAttachedToWindow && isShowPending && visibility == VISIBLE
    }

    private fun loadBannerAdWrapper() {
        if (adUnitId.isNullOrEmpty()) {
            Utils.log(this, "adUnitId is null or empty", true)
            return
        }

        isAdLoaded = false
        pendingBannerAdWrapper?.destroy()
        pendingBannerAdWrapper = BannerAdWrapper(adUnitId!!).apply {
            loadAd(context, internalLoadListener)
        }
    }

    private fun showAd() {
        if (!canShowAd()) {
            return
        }

        isShowPending = false
        pendingBannerAdWrapper?.showAd(this)
        setCurrentBannerAdWrapper(pendingBannerAdWrapper)
        pendingBannerAdWrapper = null
        loadBannerAdWrapper()

        if (isRefreshStarted) {
            startShowRunnable()
        }
    }

    private fun setCurrentBannerAdWrapper(bannerAdWrapper: BannerAdWrapper?) {
        currentBannerAdWrapper?.destroy()
        currentBannerAdWrapper = bannerAdWrapper
    }


    private class InternalListener(autoRefreshBannerView: AutoRefreshBannerView) :
            AdWrapperListener {

        private val weakBannerAdWrapperView = WeakReference(autoRefreshBannerView)

        override fun onAdLoaded() {
            weakBannerAdWrapperView.get()?.apply {
                listener?.onAdLoaded()

                isAdLoaded = true
                showAd()
            }
        }

        override fun onAdFailToLoad() {
            weakBannerAdWrapperView.get()?.listener?.onAdFailToLoad()
        }

        override fun onAdShown() {
            weakBannerAdWrapperView.get()?.listener?.onAdShown()
        }

        override fun onAdClicked() {
            weakBannerAdWrapperView.get()?.listener?.onAdClicked()
        }

    }

    private class ShowRunnable(autoRefreshBannerView: AutoRefreshBannerView) : Runnable {

        private val weakBannerAdWrapperView = WeakReference(autoRefreshBannerView)

        override fun run() {
            weakBannerAdWrapperView.get()?.apply {
                isShowPending = true
                showAd()
            }
        }

    }

}