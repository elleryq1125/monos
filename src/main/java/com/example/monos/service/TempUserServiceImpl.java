package com.example.monos.service;

import java.util.Locale;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.monos.common.Const;
import com.example.monos.common.Mail;
import com.example.monos.domain.TempUser;
import com.example.monos.dto.ResultMessage;
import com.example.monos.mapper.TempUserMapper;

/**
 * 仮ユーザー関連のサービスを担当する実装クラス。
 * @author t.ueta
 */
@Service
public class TempUserServiceImpl implements TempUserService {
    private final TempUserMapper tempUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final Mail mailUtil;
    private final MessageSource messageSource;

    public TempUserServiceImpl(TempUserMapper tempUserMapper, PasswordEncoder passwordEncoder, Mail mailUtil, MessageSource messageSource) {
        this.tempUserMapper = tempUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.mailUtil = mailUtil;
        this.messageSource = messageSource;
    }
    
    /**
     * ユーザ情報を仮登録し、仮登録完了メールを送信する。
     * @param tempUser temp_userのドメインクラス
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage register(TempUser tempUser) {
        // TODO 自動生成されたメソッド・スタブ
        ResultMessage resultMessage = new ResultMessage();
        
        // 仮登録
        String uuid = UUID.randomUUID().toString();
        tempUser.setUuid(uuid);
        tempUser.setPassword(passwordEncoder.encode(tempUser.getPassword()));
        tempUserMapper.insert(tempUser);
        
        // 仮登録完了メール送信
        mailUtil.sendTempUserRegisteredMail(tempUser);
        
        resultMessage.setType(Const.MESSAGE_TYPE_SUCCESS);
        resultMessage.setMessage(messageSource.getMessage("tempUserRegistComplete", new String[] {}, Locale.JAPAN));
        
        return resultMessage;
    }

}
