package com.tttm.birdfarmshop.Controller;


import com.tttm.birdfarmshop.Constant.ConstantAPI;
import com.tttm.birdfarmshop.Constant.ConstantParametter;
import com.tttm.birdfarmshop.Models.User;
import com.tttm.birdfarmshop.Service.AdminService;
import com.tttm.birdfarmshop.Service.AuthenticationService;
import com.tttm.birdfarmshop.Utils.Request.AuthenticationRequest;
import com.tttm.birdfarmshop.Utils.Response.AuthenticationResponse;
import com.tttm.birdfarmshop.Exception.CustomException;
import com.tttm.birdfarmshop.Utils.Response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ConstantAPI.ADMIN)
public class AdminController {
    private final AuthenticationService authenticationService;
    private final AdminService adminService;
    @GetMapping(ConstantAPI.GET_ALL_CUSTOMERS)
    public ResponseEntity<List<UserResponse>> getAllCustomers() throws CustomException {
        try {
            return new ResponseEntity<>(adminService.getAllCustomers(), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.GET_ALL_USERS)
    public ResponseEntity<List<UserResponse>> getAllUsers() throws CustomException {
        try {
            return new ResponseEntity<>(adminService.getAllUsers(), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.GET_ALL_HEALTHCARES)
    public ResponseEntity<List<UserResponse>> getAllHealthcares() throws CustomException {
        try {
            return new ResponseEntity<>(adminService.getAllHealthcares(), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.GET_ALL_SHIPPERS)
    public ResponseEntity<List<UserResponse>> getAllShippers() throws CustomException {
        try {
            return new ResponseEntity<>(adminService.getAllShippers(), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(ConstantAPI.BAN_USER_ACCOUNT + ConstantParametter.USER_ID)
    public ResponseEntity<UserResponse> BanUserAccount(@PathVariable("UserID") int UserID) throws CustomException {
        try {
            return new ResponseEntity<>(adminService.BanUserAccount(UserID), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(ConstantAPI.UNBAN_USER_ACCOUNT + ConstantParametter.USER_ID)
    public ResponseEntity<UserResponse> UnBanUserAccount(@PathVariable("UserID") int UserID) throws CustomException {
        try {
            return new ResponseEntity<>(adminService.UnBanUserAccount(UserID), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
