package com.example.monos.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author t.ueta
 * usersテーブルのdomain
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	private int userId;
	private String email;
	private String password;
	private String name;
	private String roleCode;
	private int companyId;
	private Timestamp createdAt;
	private Timestamp updatedAt;
}
