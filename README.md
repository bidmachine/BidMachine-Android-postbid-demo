# BidMachine Android PostBid Demo

[<img src="https://img.shields.io/badge/SDK%20Version-1.9.2-brightgreen">](https://github.com/bidmachine/BidMachine-Android-SDK)
[<img src="https://img.shields.io/badge/Applovin%20MAX%20Version-11.3.1-blue">](https://dash.applovin.com/documentation/mediation/android/getting-started/integration)

* [Overview](#overview)
* [Loading Applovin MAX](#loading-applovin-max)
* [Loading post bid networks](#loading-post-bid-networks)
* [Showing the loaded ad object](#showing-the-loaded-ad-object)
* [Auto-refresh banner](#auto-refresh-banner)
* [Sample](#sample)

## Overview

Showing an ad object is performed in 3 stages:

1) Loading Applovin MAX
2) Loading post bid networks based on Applovin MAX result
3) Showing the loaded ad object with most expensive price

## Loading Applovin MAX

How to load the Applovin MAX ad object,
see [the official documentation](https://dash.applovin.com/documentation/mediation/android/getting-started/integration).

## Loading post bid networks

How to load the BidMachine ad object,
see [the official documentation](https://docs.bidmachine.io/docs/in-house-mediation).

Loading the Applovin MAX ad object may finish with two results: ```onAdLoaded``` or ```onAdLoadFailed```

* If loading finished successful, then start load post bid networks with the price from the Applovin MAX
  object (```MaxAd#revenue```) multiplied by 1000.

* If loading did not complete successfully, then start loading post bid networks, either without price, or specify the
  price you need.

> Please note that MAX banners has auto-refresh enabled by default.
> You must turn it off after ad was loaded.
> ```java
> MaxAdView#stopAutoRefresh()
> ```

## Showing the loaded ad object

After the loading stage, you should give preference to the most expensive loaded ad object when displaying the ad.

## Auto-refresh banner

The auto-refresh banner contains logic for updating content based on refresh time.

To get started you need:

1) Set ad unit id
2) Set auto-refresh time in milliseconds (by default 15 seconds)
3) Perform ```loadAd``` on ```AutoRefreshBannerView```

## Sample

* [Banner](example/src/main/java/io/bidmachine/applovinmaxdemo/adwrapper/BannerAdWrapper.kt)
* [Interstitial](example/src/main/java/io/bidmachine/applovinmaxdemo/adwrapper/InterstitialAdWrapper.kt)
* [Rewarded](example/src/main/java/io/bidmachine/applovinmaxdemo/adwrapper/RewardedAdWrapper.kt)
* [Auto-refresh banner](example/src/main/java/io/bidmachine/applovinmaxdemo/adwrapper/AutoRefreshBannerView.kt)
