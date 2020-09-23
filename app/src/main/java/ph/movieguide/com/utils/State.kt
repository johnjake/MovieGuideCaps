package ph.movieguide.com.utils

sealed class State<out T> {
    object Loading : State<Nothing>()

    data class Data<T>(val data: T) : State<T>()

    data class Error<T>(val error: Throwable) : State<T>()
}