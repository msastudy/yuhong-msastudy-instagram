package com.msastudy.accountquery.projection;

import com.msastudy.accountquery.common.exception.AccountRelNotFoundException;
import com.msastudy.accountquery.common.exception.NotPermittedToDeleteAccountRelException;
import com.msastudy.accountquery.common.exception.UserNotFoundException;
import com.msastudy.coreapi.account.EmailVerificationCodeQuery;
import com.msastudy.coreapi.account.FollowCreatedEvent;
import com.msastudy.coreapi.account.FollowDeletedEvent;
import com.msastudy.coreapi.account.UserCreatedEvent;
import com.msastudy.coreapi.account.UserUpdatedEvent;
import com.msastudy.coreapi.account.UserVerifiedEvent;
import com.msastudy.coreapi.repo.AccountRelSummaryRepository;
import com.msastudy.coreapi.repo.UserSummaryRepository;
import com.msastudy.coreapi.summary.AccountRelSummary;
import com.msastudy.coreapi.summary.UserSummary;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Clock;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
public class UserSummaryProjection {
    private final UserSummaryRepository userSummaryRepository;
    private final AccountRelSummaryRepository accountRelSummaryRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EntityManager em;
    private final CommandGateway cg;

    @EventHandler
    public void on(UserCreatedEvent e){
        UserSummary user = UserSummary.builder()
                .username(e.getUsername())
                .nickname(e.getNickname())
                .password(bCryptPasswordEncoder.encode(e.getPassword()))
                .emailVerificationCode(e.getEmailVerificationCode())
                .isActive(true)
                .isVerified(false)
                .isPrivate(false)
                .build();
        userSummaryRepository.save(user);
    }

    @EventHandler
    public void on(UserUpdatedEvent e){
        UserSummary user = userSummaryRepository.findById(e.getUserId()).orElseThrow(UserNotFoundException::new);
        user.updateUsername(user.getUsername());
        user.updateNickname(user.getNickname());
        user.setActive(user.isActive());
    }

    @EventHandler
    public void on(UserVerifiedEvent e){
        UserSummary user = userSummaryRepository.findById(e.getUserId()).orElseThrow(UserNotFoundException::new);
        user.setVerified(true);
    }

    @EventHandler
    public void on(FollowCreatedEvent e){
        UserSummary follower = userSummaryRepository.findById(e.getAccountRequestingFollow()).orElseThrow(UserNotFoundException::new);
        UserSummary followed = userSummaryRepository.findById(e.getAccountToFollow()).orElseThrow(UserNotFoundException::new);


        AccountRelSummary newRel = AccountRelSummary.builder()
                .relId(e.getRelId())
                .follower(follower)
                .followed(followed)
                .approved(!followed.isPrivate())
                .createdDt(LocalDateTime.now(Clock.systemUTC()))
                .build();

        accountRelSummaryRepository.save(newRel);
        follower.getAccountsFollowing().add(newRel);
        followed.getAccountsFollowed().add(newRel);
    }

    @EventHandler
    public void on(FollowDeletedEvent e){
        AccountRelSummary rel = accountRelSummaryRepository.findById(e.getRelId()).orElseThrow(AccountRelNotFoundException::new);
        if(e.getRequestingUserId().equals(rel.getFollowed().getUserId()) || e.getRequestingUserId().equals(rel.getFollower().getUserId())){
            accountRelSummaryRepository.delete(rel);
        }else{
            throw new NotPermittedToDeleteAccountRelException();
        }
    }

    @QueryHandler
    public String handle(EmailVerificationCodeQuery query){
        UserSummary userSummary = userSummaryRepository.findById(query.getUserId()).orElseThrow(UserNotFoundException::new);
        return userSummary.getEmailVerificationCode();
    }
}
