package com.msastudy.accountquery.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.msastudy.accountquery.common.exception.UsernameOrPasswordDoNotMatchException;
import com.msastudy.coreapi.repo.UserSummaryRepository;
import com.msastudy.coreapi.summary.UserSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final UserSummaryRepository userSummaryRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String handleLogin(String username, String password) throws JWTCreationException {
        UserSummary userSummary = userSummaryRepository.findById(username)
                .orElseThrow(UsernameOrPasswordDoNotMatchException::new);
        verifyPassword(password, userSummary.getPassword());

        Algorithm algorithm = Algorithm.HMAC512("super-secret");
        return JWT.create()
                .withSubject(userSummary.getUserId())
                .sign(algorithm);
    }

    /**
     * TODO: Replace with spring security
     * @param password
     * @return
     */
    private void verifyPassword(String password, String hash){
        boolean matches =bCryptPasswordEncoder.matches(password,hash);
        if(!matches)
            throw new UsernameOrPasswordDoNotMatchException();
    }

}
