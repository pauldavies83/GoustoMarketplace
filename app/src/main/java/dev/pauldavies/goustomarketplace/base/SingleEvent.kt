package dev.pauldavies.goustomarketplace.base

// Used as a wrapper for data that is exposed via a LiveData that represents an event.
open class SingleEvent<out T>(private val content: T) {

    var hasBeenConsumed = false
        private set

    /** Returns the content and prevents its use again. */
    val event: T?
        get() {
            return if (hasBeenConsumed) {
                null
            } else {
                hasBeenConsumed = true
                content
            }
        }
}