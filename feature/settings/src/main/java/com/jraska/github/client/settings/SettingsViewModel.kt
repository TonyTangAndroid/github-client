package com.jraska.github.client.settings

import androidx.lifecycle.ViewModel
import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import javax.inject.Inject

internal class SettingsViewModel @Inject
constructor(
  private val eventAnalytics: EventAnalytics
) : ViewModel() {
  fun onPurchaseSubmitted(value: String) {
    val money = value.toDoubleOrNull() ?: return

    val event = AnalyticsEvent.builder(ANALYTICS_ECOMMERCE_PURCHASE)
      .addProperty("value", money)
      .addProperty("currency", "USD")
      .build()

    eventAnalytics.report(event)
  }

  companion object {
    val ANALYTICS_ECOMMERCE_PURCHASE = AnalyticsEvent.Key("ecommerce_purchase", Owner.USERS_TEAM)
  }
}
