package com.example.monos.validation;

import java.util.Optional;

import com.example.monos.dto.UserInfo;
import com.example.monos.mapper.UserMapper;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UnusedEmailValidator implements ConstraintValidator<UnusedEmail, String>{

    private final UserMapper userMapper;
    
    public UnusedEmailValidator(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // TODO 自動生成されたメソッド・スタブ
        
        Optional<UserInfo> userOp = userMapper.findByEmail(value);
        
        if (userOp.isPresent()) {
            return false;
        } 
        else {
            return true;  
        }
    }
}
