package com.example.monos.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = {UnusedEmailValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnusedEmail {
    String message() default "既に登録されているメールアドレスです";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};
}
