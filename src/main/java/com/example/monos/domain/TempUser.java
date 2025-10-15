package com.example.monos.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempUser {
    private String uuid;
    private String name;
    private String email;
    private String password;
    private String roleCode;
    private int companyId;
    private String companyName;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
