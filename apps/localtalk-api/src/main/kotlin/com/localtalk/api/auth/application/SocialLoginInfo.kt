package com.localtalk.api.auth.application

data class SocialLoginInfo(
    val accessToken: String,
    val refreshToken: String,
    val isSignedUser: Boolean,
) {

}
