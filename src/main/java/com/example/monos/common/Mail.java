package com.example.monos.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.example.monos.domain.TempUser;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class Mail  {
    private final MailSender mailSender;
    
    public Mail(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * GetTemplateTextメソッド
     * 指定されたメールテンプレートを取得する
     * @param templateFileName
     * @param variables
     * @return メールテンプレート
     */
    private String GetTemplateText(String templateFileName, Map<String,Object> variables) {
        // テンプレートエンジンを使用するための設定インスタンスを生成
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCharacterEncoding("UTF-8");
        
        // テンプレートエンジンを使用するためのインスタンスを生成
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);
        
        // テンプレートエンジンを実行してテキストを取得
        Context context = new Context();
        context.setVariables(variables);
        
        // 使用するテンプレートのファイル名とパラメータ情報を設定
        return engine.process("mails/" + templateFileName, context);
    }
    
    public boolean sendTempUserRegisteredMail(TempUser tempUser) {
        // メールテンプレートに設定するパラメータを設定
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", tempUser.getName());
        variables.put("url", getBaseUrl() + "/signup/userregist?uuid=" + tempUser.getUuid());
        
        // 本文取得
        String mailText = GetTemplateText("tempuser-registered.txt", variables);
        
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(tempUser.getEmail());
        simpleMailMessage.setSubject("【monos】仮登録完了のお知らせ");
        simpleMailMessage.setText(mailText);

        // メール送信
        this.mailSender.send(simpleMailMessage);
        
        return false;
    }

    /**
     * URL取得メソッド
     * @return URL
     */
    private String getBaseUrl(){
        ServletRequestAttributes attributes = 
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("HTTPリクエストが存在しません");
        }

        HttpServletRequest request = attributes.getRequest();

        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();

        return scheme + "://" + host + ":" + port;
    }
}
