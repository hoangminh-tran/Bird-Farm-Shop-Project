package com.tttm.birdfarmshop.Service;


import com.tttm.birdfarmshop.Utils.Request.SendMailOrderRequest;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;

public interface MailService {

    MessageResponse ForgotPassword(String Email);

    String SendCode(String Email);

    MessageResponse sendMailForCancelOrder(SendMailOrderRequest sendMailOrderRequest);

    MessageResponse sendMailForCompleteOrder(SendMailOrderRequest sendMailOrderRequest);
}

