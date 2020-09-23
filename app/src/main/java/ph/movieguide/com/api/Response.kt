package ph.movieguide.com.api

open class Response(
    val success: Boolean = false,
    val message: String = "",
    val http_status: Int = 0,
    val error_code: String = ""
)