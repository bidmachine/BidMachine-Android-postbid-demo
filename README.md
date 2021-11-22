# BidMachine-Android-Applovin-MAX-Demo

[<img src="https://img.shields.io/badge/SDK%20Version-1.8.0-brightgreen">](https://github.com/bidmachine/BidMachine-Android-SDK)
[<img src="https://img.shields.io/badge/Applovin%20MAX%20Version-10.3.5-blue">](https://dash.applovin.com/documentation/mediation/android/getting-started/integration)

* [Overview](#overview)
* [Loading Applovin MAX](#loading-applovin-max)
* [Loading BidMachine based on Applovin MAX result](#loading-bidmachine-based-on-applovin-max-result)
* [Showing the loaded ad object](#showing-the-loaded-ad-object)
* [Sample](#sample)

## Overview

Showing an ad object is performed in 3 stages:

1) Loading Applovin MAX
2) Loading BidMachine based on Applovin MAX result
3) Showing the loaded ad object

## Loading Applovin MAX

How to load the Applovin MAX ad object,
see [the official documentation](https://dash.applovin.com/documentation/mediation/android/getting-started/integration)
.

## Loading BidMachine based on Applovin MAX result

How to load the BidMachine ad object,
see [the official documentation](https://docs.bidmachine.io/docs/in-house-mediation).

Loading the Applovin MAX ad object may finish with two results: ```onAdLoaded```
or ```onAdLoadFailed```

* If loading finished successful, then start load BidMachine with the price from the Applovin MAX
  object (```MaxAd#revenue```) multiplied by 1000.

* If loading did not complete successfully, then start loading BidMachine, either without price (by
  default it will be 0.01), or specify the price you need.

## Showing the loaded ad object

After the loading stage, you should give preference to the BidMachine ad object when displaying the
ad, since it was requested with a price floor that is higher than the price of the loaded ad from
Applovin MAX, therefore the you will gain more revenue from that.

## Sample

* [Interstitial](example/src/main/java/io/bidmachine/applovinmaxdemo/InterstitialAdWrapper.kt)
* [Rewarded](example/src/main/java/io/bidmachine/applovinmaxdemo/RewardedAdWrapper.kt)