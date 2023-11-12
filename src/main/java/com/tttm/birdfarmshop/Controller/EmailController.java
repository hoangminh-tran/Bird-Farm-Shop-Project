package com.tttm.birdfarmshop.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tttm.birdfarmshop.Constant.ConstantAPI;
import com.tttm.birdfarmshop.Constant.ConstantParametter;
import com.tttm.birdfarmshop.Exception.CustomException;
import com.tttm.birdfarmshop.Service.CodeStorageService;
import com.tttm.birdfarmshop.Service.MailService;
import com.tttm.birdfarmshop.DTO.MailDTO;
import com.tttm.birdfarmshop.Utils.Request.SendMailOrderRequest;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ConstantAPI.EMAIL)
public class EmailController {
    private final MailService mailService;
    private final CodeStorageService codeStorageService;
    @PostMapping(ConstantAPI.FORGOT_PASSWORD)
    public ResponseEntity<MessageResponse> ForgotPassword(@RequestBody String json) throws CustomException
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            MailDTO dto = mapper.readValue(json, MailDTO.class);
            return new ResponseEntity<>(mailService.ForgotPassword(dto.getEmail()), HttpStatus.OK);
        }
        catch (JsonProcessingException ex)
        {
            return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @PostMapping(ConstantAPI.SEND_MAIL_CANCEL_ORDER)
    public ResponseEntity<MessageResponse> sendMailForCancelOrder(@RequestBody SendMailOrderRequest sendMailOrderRequest) throws CustomException
    {
        try
        {
            return new ResponseEntity<>(mailService.sendMailForCancelOrder(sendMailOrderRequest), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @PostMapping(ConstantAPI.SEND_MAIL_COMPLETE_ORDER)
    public ResponseEntity<MessageResponse> sendMailCompleteOrder(@RequestBody SendMailOrderRequest sendMailOrderRequest) throws CustomException
    {
        try
        {
            return new ResponseEntity<>(mailService.sendMailForCompleteOrder(sendMailOrderRequest), HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.NOT_IMPLEMENTED);
        }
    }

}
