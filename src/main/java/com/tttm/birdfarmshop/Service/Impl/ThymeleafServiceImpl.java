package com.tttm.birdfarmshop.Service.Impl;


import com.tttm.birdfarmshop.Service.ThymeleafService;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;


@Service
public class ThymeleafServiceImpl implements ThymeleafService {
    private static final String MAIL_TEMPLATE_BASE_NAME = "/mail/MailMessages";


    private static final String MAIL_TEMPLATE_PREFIX = "/templates/";


    private static final String MAIL_TEMPLATE_SUFFIX = ".html";


    private static final String UTF_8 = "UTF-8";


    private static final String TEMPLATE_FORGOT_PASSWORD = "mail_ForgotPassword";
    private static final String TEMPLATE_VERIFY_ACCOUNT = "mail_VerifyAccount";
    private static final String TEMPLATE_CANCEL_ORDER = "mail_CancelOrder";
    private static final String TEMPLATE_COMPLETE_ORDER = "mail_CompletedOrder";

    private static TemplateEngine templateEngine;


    static
    {
        templateEngine = emailTemplateEngine();
    }


    private static TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }


    private static ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(MAIL_TEMPLATE_PREFIX);
        templateResolver.setSuffix(MAIL_TEMPLATE_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(UTF_8);
        templateResolver.setCacheable(false);


        return templateResolver;
    }


    private static ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(MAIL_TEMPLATE_BASE_NAME);
        return messageSource;
    }


    @Override
    public String createContentForgotPassword(String password) {
        final Context context = new Context();
        System.out.println(password);

        context.setVariable("password", password);


        return templateEngine.process(TEMPLATE_FORGOT_PASSWORD, context);
    }

    @Override
    public String createContentVerifyAccount(String VerifyCode) {
        final Context context = new Context();

        context.setVariable("VerifyCode", VerifyCode);


        return templateEngine.process(TEMPLATE_VERIFY_ACCOUNT, context);
    }

    @Override
    public String sendMailCancelOrder(String message) {
        final Context context = new Context();
        System.out.println(message);

        context.setVariable("CancelOrder", message);


        return templateEngine.process(TEMPLATE_CANCEL_ORDER, context);
    }

    @Override
    public String sendMailCompletedOrder(String message) {
        final Context context = new Context();
        System.out.println(message);

        context.setVariable("CompletedOrder", message);


        return templateEngine.process(TEMPLATE_COMPLETE_ORDER, context);
    }
}