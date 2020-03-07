package com.msastudy.accountquery.controller;

import com.msastudy.accountquery.common.exception.UserNotFoundException;
import com.msastudy.coreapi.repo.UserSummaryRepository;
import com.msastudy.coreapi.summary.UserSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserSummaryRepository userSummaryRepository;

    @GetMapping
    public ResponseEntity<UserSummary> getUser(@AuthenticationPrincipal String username){
        UserSummary userSummary = userSummaryRepository.findById(username)
                .orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(userSummary);
    }
}
