package com.jraska.github.client.core.android

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jraska.github.client.DeepLinkLauncher
import com.jraska.github.client.Owner
import com.jraska.github.client.analytics.AnalyticsEvent
import com.jraska.github.client.analytics.EventAnalytics
import com.jraska.github.client.core.android.logging.SetupLogging
import com.jraska.github.client.rx.AppSchedulers
import com.jraska.github.client.time.DateTimeProvider
import com.jraska.github.client.time.RealDateTimeProvider
import com.jraska.github.client.time.RealTimeProvider
import com.jraska.github.client.time.TimeProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
object CoreAndroidModule {
  @Provides
  @Singleton
  internal fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory {
    return factory
  }

  @Provides
  @Singleton
  internal fun provideServiceModelFactory(factory: ServiceModelFactory): ServiceModel.Factory {
    return factory
  }

  @Provides
  fun bindDeepLinkLauncher(
    topActivityProvider: TopActivityProvider,
    launchers: @JvmSuppressWildcards Set<LinkLauncher>
  ): DeepLinkLauncher {
    return RealDeepLinkLauncher.create(topActivityProvider, launchers)
  }

  @Provides
  @IntoMap
  @ClassKey(UriHandlerViewModel::class)
  fun uriHandlerViewModel(viewModel: UriHandlerViewModel): ViewModel {
    return viewModel
  }

  @Provides
  @Singleton
  internal fun topActivityProvider(): TopActivityProvider {
    return TopActivityProvider()
  }

  @Provides
  @IntoSet
  fun topActivityOnCreate(setup: TopActivityProvider.RegisterCallbacks): OnAppCreate {
    return setup
  }

  @Provides
  @Singleton
  internal fun dateTimeProvider(): DateTimeProvider {
    return RealDateTimeProvider()
  }

  @Provides
  @Singleton
  internal fun timeProvider(): TimeProvider {
    return RealTimeProvider.INSTANCE
  }

  @Provides
  @Singleton
  fun schedulers(): AppSchedulers {
    return AppSchedulers(
      AndroidSchedulers.mainThread(),
      Schedulers.io(), Schedulers.computation()
    )
  }

  @Provides
  @IntoSet
  internal fun setupLoggingOnCreate(setupLogging: SetupLogging): OnAppCreate {
    return setupLogging
  }

  @Provides
  @IntoSet
  fun reportAppCreateEvent(eventAnalytics: EventAnalytics): OnAppCreate {
    return object : OnAppCreate {
      override fun onCreate(app: Application) {
        val createEvent = AnalyticsEvent.create(AnalyticsEvent.Key("app_create", Owner.CORE_TEAM))
        eventAnalytics.report(createEvent)
      }
    }
  }
}
