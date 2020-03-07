package com.msastudy.coreapi.account

import org.axonframework.modelling.command.TargetAggregateIdentifier


data class UserCreatedEvent(val userId: String, val username: String, val password: String, val nickname: String, val emailVerificationCode: String)
data class UserUpdatedEvent(val userId: String, val username: String, val nickname: String, val isActive: Boolean)
data class UserVerifiedEvent(@TargetAggregateIdentifier val userId: String)
data class FollowCreatedEvent(@TargetAggregateIdentifier val relId: String, val accountToFollow: String, val accountRequestingFollow: String)
data class FollowAcceptedEvent(@TargetAggregateIdentifier val relId: String)
data class FollowDeletedEvent(@TargetAggregateIdentifier val relId: String, val requestingUserId: String)