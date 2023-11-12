package com.tttm.birdfarmshop.Service.Impl;


import com.tttm.birdfarmshop.Models.Order;
import com.tttm.birdfarmshop.Models.User;
import com.tttm.birdfarmshop.Repository.OrderRepository;
import com.tttm.birdfarmshop.Repository.UserRepository;
import com.tttm.birdfarmshop.Service.JwtService;
import com.tttm.birdfarmshop.Service.MailService;
import com.tttm.birdfarmshop.Service.ThymeleafService;
import com.tttm.birdfarmshop.Utils.Request.SendMailOrderRequest;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender javaMailSender;

    private final UserRepository userRepository;

    private final ThymeleafService thymeleafService;

    private final OrderRepository orderRepository;

    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Value("${spring.mail.username")
    public String email;

    public String generateCode()
    {
        String numberString = "0123456789";
        Random random = new Random();
        String generateCode = "";
        for (int i = 0; i < 6; i++)
        {
            generateCode +=  numberString.charAt(random.nextInt(numberString.length()));
        }
        return generateCode;
    }

    public String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@";
        Random random = new Random();
        String password = "";
        for (int i = 0; i < 11; i++)
        {
            password +=  characters.charAt(random.nextInt(characters.length()));
        }
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile( regex );
        Matcher matcher = pattern.matcher( password );


        if (matcher.matches()) {
            return password;
        } else {
            return generatePassword(); // recursion
        }
    }



    @Override
    public String SendCode(String Email) {

        String email_to = Email;
        String email_subject = "Verify Account";
        String code =  generateCode();
        logger.info("Verify Account with Email {} and {}", Email, code);

        sendCodeToMail(email_to, email_subject, code);
        return code;
    }

    @Override
    public MessageResponse ForgotPassword(String Email) {
        User user = userRepository.findUserByEmail(Email);
        if(user != null) {
            String generate_Password = generatePassword();
            String password = passwordEncoder.encode(generate_Password);
            user.setPassword(password);
            String email_to = user.getEmail();
            String email_subject = "Update User Password";

            userRepository.save(user);
            sendMailForgotPassword(email_to, email_subject, generate_Password);

            var jwtToken = jwtService.generateToken(user);

            logger.info("New Password {} ", generate_Password);

            return new MessageResponse("Success");
        }
        return new MessageResponse("Fail to Access Forgot Password");
    }

    @Override
    public MessageResponse sendMailForCompleteOrder(SendMailOrderRequest sendMailOrderRequest) {
        Optional<User> user = userRepository.findById(sendMailOrderRequest.getCustomerId());
        Optional<Order> order = orderRepository.findById(sendMailOrderRequest.getOrderID());

        LocalDateTime localDateTime = LocalDateTime.now();
        Date systemDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateString = simpleDateFormat.format(systemDate);

        if(order.isEmpty() || user.isEmpty()) return new MessageResponse("Fail to send Mail for Completed Order");

        String email_to = sendMailOrderRequest.getEmail();
        String email_subject = "Customer Order Successfully";
        String message = "Customer Email : " + email_to +  " already Order Successfully with Order ID: " + order.get().getId() + " at " + currentDateString;

        logger.info("Inside Completed EmailServiceImpl with message: " + message);

        sendMailCompletedOrder(email_to, email_subject, message);

        return new MessageResponse("Success");
    }

    @Override
    public MessageResponse sendMailForCancelOrder(SendMailOrderRequest sendMailOrderRequest) {
        Optional<User> user = userRepository.findById(sendMailOrderRequest.getCustomerId());
        Optional<Order> order = orderRepository.findById(sendMailOrderRequest.getOrderID());

        LocalDateTime localDateTime = LocalDateTime.now();
        Date systemDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateString = simpleDateFormat.format(systemDate);

        if(order.isEmpty() || user.isEmpty()) return new MessageResponse("Fail to send Mail for Cancel Order");

        String email_to = sendMailOrderRequest.getEmail();
        String email_subject = "Customer Cancel Order";
        String message = "Customer Email : " + email_to +  " already cancel your Order with Order ID: " + order.get().getId() + " at " + currentDateString;

        logger.info("Inside CancelOrder EmailServiceImpl with message: " + message);

        sendMailCancelOrder(email_to, email_subject, message);

        return new MessageResponse("Success");
    }

    @Async
    public void sendMailCompletedOrder(String email_to, String email_subject, String msg) {
        try{

            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(email);
            helper.setTo(email_to);
            helper.setSubject(email_subject);
            helper.setText(thymeleafService.sendMailCompletedOrder(msg), true);
            logger.info("Inside Completed EmailServiceImpl Method");
            javaMailSender.send(message);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Async
    public void sendMailCancelOrder(String email_to, String email_subject, String msg) {
        try{

            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(email);
            helper.setTo(email_to);
            helper.setSubject(email_subject);
            helper.setText(thymeleafService.sendMailCancelOrder(msg), true);
            logger.info("Inside CancelOrder EmailServiceImpl Method");
            javaMailSender.send(message);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    @Async
    public void sendCodeToMail(String email_to, String email_subject, String code) {
        try{

            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(email);
            helper.setTo(email_to);
            helper.setSubject(email_subject);
            helper.setText(thymeleafService.createContentVerifyAccount(code), true);
            logger.info("Send Mail Verify Account Method");
            javaMailSender.send(message);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Async
    public void sendMailForgotPassword(String email_to, String email_subject, String password) {
        try{


            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(email);
            helper.setTo(email_to);
            helper.setSubject(email_subject);
            helper.setText(thymeleafService.createContentForgotPassword(password), true);
            javaMailSender.send(message);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}