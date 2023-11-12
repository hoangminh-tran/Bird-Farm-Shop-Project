package com.tttm.birdfarmshop.Constant;

import lombok.Getter;

@Getter
public enum ConstantMessage {
    SUCCESS("Success"),
    INVALID_USERNAME_OR_PASSWORD("Invalid Username or Password"),
    USERNAME_NOT_EXIST("Username is not exist"),
    USERNAME_IS_EXIST("Username has already existed"),
    PHONE_IS_EXIST("Phone has already existed"),
    EMAIL_IS_EXIST("Email has already existed"),
    ID_IS_EXIST("The Id has already existed"),
    INVALID_ARGUMENT("Invalid argument"),
    FORBIDDEN("Customer do not have role to access the page");

    private final String message;
    ConstantMessage(String message)
    {
        this.message = message;
    }
}
