package com.example.monos.exception;

/**
 * 復帰不能な業務エラーの例外クラス
 * @author t.ueta
 */
public class FatalBusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public FatalBusinessException(String message) {
		super(message);
	}
}
