package com.tttm.birdfarmshop.Service.Impl;

import com.tttm.birdfarmshop.Constant.ConstantMessage;
import com.tttm.birdfarmshop.DTO.MailDTO;
import com.tttm.birdfarmshop.Enums.AccountStatus;
import com.tttm.birdfarmshop.Enums.ERole;
import com.tttm.birdfarmshop.Enums.TokenType;
import com.tttm.birdfarmshop.Exception.CustomException;
import com.tttm.birdfarmshop.Models.Token;
import com.tttm.birdfarmshop.Models.User;
import com.tttm.birdfarmshop.Repository.TokenRepository;
import com.tttm.birdfarmshop.Repository.UserRepository;
import com.tttm.birdfarmshop.Service.*;
import com.tttm.birdfarmshop.Utils.Response.AuthenticationResponse;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CodeStorageServiceImpl implements CodeStorageService {

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final Map<String, Long> EmailAndExpiration = new HashMap<>(); //Store Email and Expiration Of Code
    private final Map<String, String> EmailAndCode = new HashMap<>(); //Store Email and Code

    private final CustomerService customerService;

    private final AdminService adminService;

    private final ShipperService shipperService;

    private final TokenRepository tokenRepository;

    private final MailService mailService;

    private final SellerService sellerService;

    private final HealthcareProfessionalService healthcareProfessionalService;
    private static final Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class);

    @Override
    public MessageResponse getCodeFromSession(MailDTO dto, HttpSession session) {
        Long expirationTime = EmailAndExpiration.get(dto.getEmail());
        if(expirationTime != null && expirationTime < System.currentTimeMillis()) // Check the Expiration Time for each Keys store in KeySessions
        {
            EmailAndCode.remove(dto.getEmail());
            EmailAndExpiration.remove(dto.getEmail());
            return new MessageResponse("The Code is Expired.");
        }
        String code = (String) EmailAndCode.get(dto.getEmail());
        if(code.equals(dto.getCode())) // Compare Code from System with Customer
        {
            User user = (User) session.getAttribute(dto.getEmail());
            var savedUser = userRepository.save(user);

            var accessToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(savedUser, accessToken);

            var jwtToken = jwtService.generateToken(user);

            logger.info("Create new Email: {} Successfully", dto.getEmail());

            switch (user.getRole().name())
            {
                case "CUSTOMER":
                    customerService.createCustomer(user);
                    break;
                case "ADMINISTRATOR":
                    adminService.createAdmin(user);
                    break;
                case "SHIPPER":
                    shipperService.createShipper(user);
                    break;
                case "SELLER":
                    sellerService.createSeller(user);
                    break;
                case "HEALTHCAREPROFESSIONAL":
                    healthcareProfessionalService.createHealthcareProfessional(user);
                    break;
            }

            EmailAndCode.remove(dto.getEmail());
            EmailAndExpiration.remove(dto.getEmail());
            session.removeAttribute(dto.getEmail());
            session.removeAttribute(user.toString());

            return new MessageResponse("Success Register Account");
        }
        return new MessageResponse("Invalid Code");
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
    public MessageResponse register(User dto, HttpSession session) throws CustomException
    {
        ERole role = null;
        switch (dto.getRole().toString().toUpperCase())
        {
            case "CUSTOMER":
                role = ERole.CUSTOMER;
                break;
            case "ADMINISTRATOR":
                role = ERole.ADMINISTRATOR;
                break;
            case "SHIPPER":
                role = ERole.SHIPPER;
                break;
            case "SELLER":
                role = ERole.SELLER;
                break;
            case "HEALTHCAREPROFESSIONAL":
                role = ERole.HEALTHCAREPROFESSIONAL;
                break;
        }
        if (userRepository.findUserByEmail(dto.getEmail()) != null)
        {
            logger.warn(ConstantMessage.EMAIL_IS_EXIST);
            return new MessageResponse("Fail");
        }
        if (userRepository.findUserByPhone(dto.getPhone()) != null)
        {
            logger.warn(ConstantMessage.PHONE_IS_EXIST);
            return new MessageResponse("Fail");
        }
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .gender(dto.getGender())
                .dateOfBirth(dto.getDateOfBirth())
                .address(dto.getAddress())
                .accountStatus(AccountStatus.ACTIVE)
                .role(role)
                .build();
        var jwtToken = jwtService.generateToken(user);

        String code = mailService.SendCode(dto.getEmail());

        session.setAttribute(dto.getEmail(), user);
        session.setAttribute(user.toString(), jwtToken);
        EmailAndCode.put(dto.getEmail(), code);
        Long expirationTime = System.currentTimeMillis() + (60 * 1000);
        EmailAndExpiration.put(dto.getEmail(), expirationTime);

        return new MessageResponse("Success");
    }
}
