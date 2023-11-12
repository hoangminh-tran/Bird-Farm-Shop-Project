package com.tttm.birdfarmshop.Service;



public interface ThymeleafService {
    String createContentForgotPassword(String password);

    String createContentVerifyAccount(String email);

    String sendMailCancelOrder(String message);
    String sendMailCompletedOrder(String message);
}
