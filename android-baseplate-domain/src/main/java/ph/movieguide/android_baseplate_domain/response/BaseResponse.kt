package ph.movieguide.android_baseplate_domain.response

open class BaseResponse(
    val success: Boolean = false,
    val message: String = "",
    val http_status: Int = 201
) {
    val isResponseSuccess = http_status == 200
}