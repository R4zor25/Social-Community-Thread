package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.autocomplete

import android.os.Parcelable
import androidx.compose.runtime.Stable

@Stable
interface AutoCompleteEntity: Parcelable {
    fun filter(query: String): Boolean
}

@Stable
interface ValueAutoCompleteEntity<T> : AutoCompleteEntity {
    val value: T
}