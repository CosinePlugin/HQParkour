package kr.cosine.parkour.registry

import kr.cosine.parkour.data.ErrorMessage
import kr.cosine.parkour.enums.Error
import kr.hqservice.framework.global.core.component.Bean

@Bean
class ErrorMessageRegistry {

    private val errorMap = mutableMapOf<Error, ErrorMessage>()

    fun findErrorMessage(error: Error): ErrorMessage? = errorMap[error]

    fun getErrorMessage(error: Error): ErrorMessage = findErrorMessage(error) ?: throw IllegalArgumentException()

    fun setErrorMessage(error: Error, errorMessage: ErrorMessage) {
        errorMap[error] = errorMessage
    }

    internal fun clear() {
        errorMap.clear()
    }
}