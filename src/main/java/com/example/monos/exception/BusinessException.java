package com.example.monos.exception;

import java.util.Map;

/**
 * 復帰可能な業務エラーの例外クラス	
 * @author t.ueta
 */
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private Map<String,String> errors;
	
	public BusinessException(Map<String,String> errors) {
		this.errors = errors;
	}
	
	public Map<String,String> getErrors(){
		return this.errors;
	}
}
