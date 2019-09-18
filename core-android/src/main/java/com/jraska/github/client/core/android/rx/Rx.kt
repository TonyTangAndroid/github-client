package com.jraska.github.client.core.android.rx

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.getTag
import androidx.lifecycle.setTagIfIsAbsent
import androidx.lifecycle.toLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.Closeable

fun <T> Observable<T>.toLiveData(): LiveData<T> {
  return toFlowable(BackpressureStrategy.MISSING).toLiveData()
}

const val KEY_DISPOSABLES = "disposables"

fun ViewModel.disposables(): CompositeDisposable {
  val disposable = getTag<DisposeOnClose>(KEY_DISPOSABLES)
  if (disposable == null) {
    return setTagIfIsAbsent(KEY_DISPOSABLES, DisposeOnClose(CompositeDisposable())).disposables
  } else {
    return disposable.disposables
  }
}

fun ViewModel.disposeOnClear(disposable: Disposable) {
  disposables().add(disposable)
}

class DisposeOnClose(val disposables: CompositeDisposable) : Closeable {
  override fun close() {
    disposables.dispose()
  }
}
