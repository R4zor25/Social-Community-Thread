package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.autocomplete

import android.os.Parcel

typealias CustomFilter<T> = (T, String) -> Boolean

fun <T> List<T>.asAutoCompleteEntities(filter: CustomFilter<T>): List<ValueAutoCompleteEntity<T>> {
    return map {
        object : ValueAutoCompleteEntity<T> {
            override val value: T = it

            override fun filter(query: String): Boolean {
                return filter(value, query)
            }

            override fun describeContents(): Int {
                return 0;
            }


            override fun writeToParcel(p0: Parcel, p1: Int) {
                p0.writeValue(value);
            }
        }
    }
}

const val AutoCompleteSearchBarTag = "AutoCompleteSearchBar"