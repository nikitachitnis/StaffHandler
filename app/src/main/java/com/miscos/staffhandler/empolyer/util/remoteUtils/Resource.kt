package com.miscos.staffhandler.empolyer.util.remoteUtils

import com.miscos.staffhandler.empolyer.util.remoteUtils.Status.SUCCESS
import com.miscos.staffhandler.empolyer.util.remoteUtils.Status.ERROR
import com.miscos.staffhandler.empolyer.util.remoteUtils.Status.LOADING


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(val status: Status, val data: T?, val message: String?, val code: Int) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null, -1)
        }

        fun <T> error(msg: String, data: T?, code: Int): Resource<T> {
            return Resource(ERROR, data, msg, code)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(LOADING, data, null, -1)
        }
    }
}