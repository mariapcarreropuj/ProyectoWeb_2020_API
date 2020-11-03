package com.puj.admincenter.dto.users

data class ModifyUserDto(
    val username: String,
    val previousPassword: String,
    val newPassword: String
)