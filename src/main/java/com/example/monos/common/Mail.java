package com.example.monos.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.example.monos.domain.TempUser;

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
        // TODO 自動生成されたメソッド・スタブ
        
        // メールテンプレートに設定するパラメータを設定
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", tempUser.getName());
        variables.put("url", Const.URL + "/signup/userregist?uuid=" + tempUser.getUuid());
        
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
}
