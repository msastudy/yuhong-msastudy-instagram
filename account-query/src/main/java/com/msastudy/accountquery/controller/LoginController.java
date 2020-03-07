package com.msastudy.accountquery.controller;

import com.msastudy.accountquery.dto.request.LoginRequestDto;
import com.msastudy.accountquery.dto.response.LoginResponseDto;
import com.msastudy.accountquery.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        String jwt = loginService.handleLogin(dto.getUsername(), dto.getPassword());
        return ResponseEntity.ok(new LoginResponseDto(jwt));
    }

}
