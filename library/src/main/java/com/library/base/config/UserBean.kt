package com.library.base.config

data class UserBean(
        var email: String,
        var email_validated: Int,
        var id: Int,
        var last_active: String,
        var phone: String,
        var phone_validated: Int,
        var photo_addr: String,
        var photo_file_sha1: String,
        var signup_at: String,
        var status: Int,
        var user_name: String
)