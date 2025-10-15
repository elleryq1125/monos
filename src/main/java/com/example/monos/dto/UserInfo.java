package com.example.monos.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author t.ueta
 * ユーザー情報のDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private int userId;
    private String email;
    private String password;
    private String name;
    private String roleCode;
    private String roleName;
    private int companyId;
    private String companyName;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
