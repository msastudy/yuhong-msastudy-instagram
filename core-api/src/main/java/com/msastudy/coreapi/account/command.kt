package com.msastudy.coreapi.account


import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateUserCommand(
        @TargetAggregateIdentifier val userId: String,
        val username: String,
        val password: String,
        val nickname: String,
        val emailVerificationCode: String
)

data class UpdateUserCommand(
        @TargetAggregateIdentifier val userId: String,
        val username: String,
        val nickname: String,
        val isActive: Boolean
)

data class VerifyUserCommand(@TargetAggregateIdentifier val userId: String)

data class FollowCommand(@TargetAggregateIdentifier val relId: String, val accountToFollow: String, val accountRequestingFollow: String)
data class CancelFollowCommand(@TargetAggregateIdentifier val relId: String, val requestingUserId: String)