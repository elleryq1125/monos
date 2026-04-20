package com.example.monos.domain;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CodeMaster {
	private String codeType;
	private String code;
	private String name;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
