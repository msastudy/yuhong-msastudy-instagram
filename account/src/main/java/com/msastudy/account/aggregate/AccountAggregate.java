package com.msastudy.account.aggregate;


import com.msastudy.coreapi.account.CancelFollowCommand;
import com.msastudy.coreapi.account.CreateUserCommand;
import com.msastudy.coreapi.account.FollowCommand;
import com.msastudy.coreapi.account.FollowCreatedEvent;
import com.msastudy.coreapi.account.FollowDeletedEvent;
import com.msastudy.coreapi.account.UpdateUserCommand;
import com.msastudy.coreapi.account.UserCreatedEvent;
import com.msastudy.coreapi.account.UserUpdatedEvent;
import com.msastudy.coreapi.account.UserVerifiedEvent;
import com.msastudy.coreapi.account.VerifyUserCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AccountAggregate {

    @AggregateIdentifier
    @Id
    private String userId;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String nickname;
    private String emailVerificationCode;

    private boolean isActive = true;
    private boolean isVerified = false;
    private boolean isPrivate = false;

    @AggregateMember
    @OneToMany(fetch = FetchType.LAZY)
    private List<AccountRel> accountsFollowed = new ArrayList<>();

    @AggregateMember
    @OneToMany(fetch = FetchType.LAZY)
    private List<AccountRel> accountsFollowing = new ArrayList<>();


    @CommandHandler
    public AccountAggregate(CreateUserCommand createUserCommand) {
        username = createUserCommand.getUsername();
        password = createUserCommand.getPassword();
        nickname = createUserCommand.getNickname();
        emailVerificationCode = createUserCommand.getEmailVerificationCode();
        //        throw new AccountNotCreatedException(createUserCommand.getUsername());
        apply(new UserCreatedEvent(
                createUserCommand.getUserId(),
                createUserCommand.getUsername(),
                createUserCommand.getPassword(),
                createUserCommand.getNickname(),
                emailVerificationCode)
        );
    }

    @CommandHandler
    public void on(UpdateUserCommand cmd) {
        apply(new UserUpdatedEvent(cmd.getUserId(), cmd.getUsername(), cmd.getNickname(), cmd.isActive()));
    }
    @CommandHandler
    public void on(VerifyUserCommand cmd) {
        apply(new UserVerifiedEvent(cmd.getUserId()));
    }

    @CommandHandler
    public void on(FollowCommand cmd) {
        apply(new FollowCreatedEvent(cmd.getRelId(), cmd.getAccountToFollow(), cmd.getAccountRequestingFollow()));
    }
    @CommandHandler
    public void on(CancelFollowCommand cmd) {
        apply(new FollowDeletedEvent(cmd.getRelId(), cmd.getRequestingUserId()));
    }

    @EventSourcingHandler
    protected void on(UserCreatedEvent event){
        this.username = event.getUsername();
        this.password = event.getPassword();
    }
    @EventSourcingHandler
    protected void on(UserVerifiedEvent event){
        this.isVerified = true;
    }

//    @EventSourcingHandler
//    protected void on(FollowCreatedEvent event){
//        AccountRe;
//    }

}