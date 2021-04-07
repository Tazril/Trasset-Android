package com.cwod.trasset.common

data class ErrorModel(
    val message: String
)

data class BaseModel<T>(
    val data: T?,
    val error: ErrorModel?
)