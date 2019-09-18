package androidx.lifecycle

import java.io.Closeable

internal fun <T : Closeable> ViewModel.setTagIfIsAbsent(key: String, closeable: T): T {
  return this.setTagIfAbsent(key, closeable)
}

internal fun <T : Closeable> ViewModel.getTag(key: String): T? {
  return this.getTag(key)
}
