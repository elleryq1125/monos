package com.example.monos.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author t.ueta
 * ユーザ更新画面 リクエストデータ
 */
@Data
public class UserUpdateForm {
    @NotBlank
    @Size(max = 128)
    private String name;

    private String roleCode;
}
