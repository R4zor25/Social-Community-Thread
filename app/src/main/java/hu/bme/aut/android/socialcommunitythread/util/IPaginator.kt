package hu.bme.aut.android.socialcommunitythread.util

interface IPaginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}