package com.msastudy.coreapi.account



data class EmailVerificationCodeQuery(val userId: String);
data class GetFollowersQuery(val userId: String, val requestingUserId: String)
