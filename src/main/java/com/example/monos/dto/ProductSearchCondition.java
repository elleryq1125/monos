package com.example.monos.dto;

import lombok.Data;

@Data
public class ProductSearchCondition {
	int companyId;
	String productCode;
	String name;
	boolean active;
}
