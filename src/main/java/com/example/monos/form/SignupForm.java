package com.example.monos.form;

import java.util.Objects;

import com.example.monos.validation.UnusedEmail;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author t.Ueta
 * サインアップ画面 リクエストデータ
 */
@Data
public class SignupForm {
    
    @NotBlank
    @Size(max = 128)
    private String name;
    
    @NotBlank
    @Size(max = 256)
    @UnusedEmail
    private String email;
    
    @NotBlank
    @Size(max = 128)
    private String password;
    
    private String passwordConfirm;
    
    @NotBlank
    @Size(max = 128)
    private String companyName;
    
    @AssertTrue
    public boolean isPasswordValid() {
        return Objects.equals(password, passwordConfirm);
    }
}
