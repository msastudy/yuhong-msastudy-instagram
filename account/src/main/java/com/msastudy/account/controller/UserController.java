package com.msastudy.account.controller;

import com.msastudy.account.dto.SignUpRequestDto;
import com.msastudy.account.dto.res.SignUpResponseDto;
import com.msastudy.coreapi.account.CancelFollowCommand;
import com.msastudy.coreapi.account.CreateUserCommand;
import com.msastudy.coreapi.account.EmailVerificationCodeQuery;
import com.msastudy.coreapi.account.FollowCommand;
import com.msastudy.coreapi.account.GetFollowersQuery;
import com.msastudy.coreapi.account.VerifyUserCommand;
import com.msastudy.coreapi.summary.UserSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final CommandGateway cg;
    private final QueryGateway qg;

    @PostMapping
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto dto){
        String userId = UUID.randomUUID().toString();
        String emailVerificationCode = UUID.randomUUID().toString();
        String username = cg.sendAndWait(
                new CreateUserCommand(userId, dto.getUsername(), dto.getPassword(), dto.getNickname(), emailVerificationCode)
        );
        log.info(username);
        return ResponseEntity.ok(new SignUpResponseDto(dto.getUsername()));
    }

    @GetMapping("/{userId}/verification/{verificationCode}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void emailVerification(@PathVariable String userId, @PathVariable String verificationCode){
        CompletableFuture<String> veriCodeFuture = qg.query(new EmailVerificationCodeQuery(userId), String.class);
        veriCodeFuture.thenAccept(storedVerificationCode -> {
            if(verificationCode.equals(storedVerificationCode)){
                cg.send(new VerifyUserCommand(userId));
            }
        });
    }

    @PostMapping("/follow/{accountToFollow}")
    public void followAccount(@AuthenticationPrincipal String username, @PathVariable String accountToFollow) {
        String relId = UUID.randomUUID().toString();

        cg.send(new FollowCommand(relId, accountToFollow, username));
    }

    @DeleteMapping("/relation/{relId}")
    public void deleteRelation(@AuthenticationPrincipal String username, @PathVariable String relId){
        cg.send(new CancelFollowCommand(relId, username));
    }

    @GetMapping("/{userId}/follower")
    public void getFollowers(@AuthenticationPrincipal String requesterId, @PathVariable String userId){
        qg.query(new GetFollowersQuery(userId, requesterId), UserSummary.class);
    }
}
