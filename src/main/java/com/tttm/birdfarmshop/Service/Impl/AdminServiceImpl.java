package com.tttm.birdfarmshop.Service.Impl;

import com.tttm.birdfarmshop.Enums.AccountStatus;
import com.tttm.birdfarmshop.Models.Admin;
import com.tttm.birdfarmshop.Enums.ERole;
import com.tttm.birdfarmshop.Models.User;
import com.tttm.birdfarmshop.Repository.AdminRepository;
import com.tttm.birdfarmshop.Repository.UserRepository;
import com.tttm.birdfarmshop.Service.AdminService;
import com.tttm.birdfarmshop.Utils.Response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;
    private static final Logger logger = LogManager.getLogger(AdminServiceImpl.class);
    @Override
    public void createAdmin(User user) {
        Admin customer = new Admin(user.getUserID());
        adminRepository.save(customer);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        logger.info("Get All Users");
        logger.info("Get All Users");
        return userRepository.getAllUsers()
                .stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getAllCustomers() {
        logger.info("Get All Customers");
        return userRepository.getAllUsersBasedOnRole(ERole.CUSTOMER.name())
                .stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }
    @Override
    public List<UserResponse> getAllHealthcares() {
        logger.info("Get All Healthcare Professionals");
        return userRepository.getAllUsersBasedOnRole(ERole.HEALTHCAREPROFESSIONAL.name())
                .stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getAllShippers() {
        logger.info("Get All Shippers");
        return userRepository.getAllUsersBasedOnRole(ERole.SHIPPER.name()).stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse BanUserAccount(int UserID) {
        Optional<User> user = userRepository.findById(UserID);
        if(user.get() != null)
        {
            user.get().setAccountStatus(AccountStatus.INACTIVE);
            userRepository.save(user.get());
            logger.info("Ban User with User ID {} Successfully", UserID);
        }
        return convertToUserResponse(user.get());
    }

    @Override
    public UserResponse UnBanUserAccount(int UserID) {
        Optional<User> user = userRepository.findById(UserID);
        if(user.get() != null)
        {
            user.get().setAccountStatus(AccountStatus.ACTIVE);
            userRepository.save(user.get());
            logger.info("UnBan User with User ID {} Successfully", UserID);
        }
        return convertToUserResponse(user.get());
    }

    private UserResponse convertToUserResponse(User user)
    {
        return UserResponse.builder()
                .userID(user.getUserID())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .password(user.getPassword())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .accountStatus(user.getAccountStatus())
                .role(user.getRole())
                .build();
    }
}
