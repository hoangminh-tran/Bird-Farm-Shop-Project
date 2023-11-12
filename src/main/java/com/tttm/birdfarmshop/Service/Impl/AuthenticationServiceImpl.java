package com.tttm.birdfarmshop.Service.Impl;

import com.tttm.birdfarmshop.Enums.AccountStatus;
import com.tttm.birdfarmshop.Enums.TokenType;
import com.tttm.birdfarmshop.Models.Token;
import com.tttm.birdfarmshop.Models.User;
import com.tttm.birdfarmshop.Repository.TokenRepository;
import com.tttm.birdfarmshop.Repository.UserRepository;
import com.tttm.birdfarmshop.Service.*;
import com.tttm.birdfarmshop.Utils.Request.AuthenticationRequest;
import com.tttm.birdfarmshop.Utils.Response.AuthenticationResponse;
import com.tttm.birdfarmshop.Exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);


    @Override
    public AuthenticationResponse login(AuthenticationRequest dto){
        try
        {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(),
                            dto.getPassword()
                    )
            );
            var user = userRepository.findUserByEmailAndActiveStatus(dto.getEmail(), AccountStatus.ACTIVE.name())
                    .orElseThrow();
            var accessToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, accessToken);

            return AuthenticationResponse
                    .builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userID(user.getUserID())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();
        }
        catch (Exception ex)
        {
            logger.error("Authentication fail");
        }
        return new AuthenticationResponse();
    }

    private void saveUserToken(User user, String jwtToken)
    {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user)
    {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserID());
        if(validUserTokens.isEmpty())
        {
            return;
        }
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String  authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer "))
        {
            return new AuthenticationResponse();
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if(userEmail != null)
        {
            var user = this.userRepository.findUserByEmailAndActiveStatus(userEmail, AccountStatus.ACTIVE.name()).orElseThrow(); // get email from database

            if(jwtService.isValidToken(refreshToken, user))
            {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                return authResponse;
            }
        }
        return new AuthenticationResponse();
    }
}
