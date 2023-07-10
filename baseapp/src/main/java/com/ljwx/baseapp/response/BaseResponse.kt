package com.ljwx.baseapp.response

sealed class BaseResponse<out T : Any> {

    var code: Int? = null

    @Deprecated(message="useless", replaceWith = ReplaceWith(expression = "msg"))
    var message: String? = null

    var msg: String? = null

    data class Success<out T : Any>(val data: T) : BaseResponse<T>()
    data class Error(val exception: ResponseException) : BaseResponse<Nothing>()

    open fun isSuccess(): Boolean {
        return code == 200
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }

}