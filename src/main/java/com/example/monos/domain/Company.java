package com.example.monos.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    private int companyId;
    private String name;
    private Timestamp createdAt; 
    private Timestamp updatedAt;
}
