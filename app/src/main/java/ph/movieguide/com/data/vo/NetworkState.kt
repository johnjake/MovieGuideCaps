package ph.movieguide.com.data.vo

sealed class NetworkState {
    object FAILED : NetworkState()
    object LOADING : NetworkState()
    object LOADED : NetworkState()
}