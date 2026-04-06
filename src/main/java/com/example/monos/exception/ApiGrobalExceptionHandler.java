package com.example.monos.exception;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.monos.common.logging.LoggingAspect;
import com.example.monos.dto.ApiResponse;

/**
 * API用の共通例外クラス
 * @author t.ueta
 */
@RestControllerAdvice(annotations = RestController.class)
public class ApiGrobalExceptionHandler {
	private final MessageSource messageSource;
	private static final Logger logger = LoggerFactory.getLogger(ApiGrobalExceptionHandler.class);
	
	public ApiGrobalExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@ExceptionHandler(BusinessException.class)
	public ApiResponse<?> handleBusiness(BusinessException e){
		return ApiResponse.validationError(e.getErrors());
	}
	
	@ExceptionHandler(FatalBusinessException.class)
	public ApiResponse<?> handleFatalBusiness(FatalBusinessException e){
		return ApiResponse.errorMessage(e.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	public ApiResponse<?> handle(Exception e){
		logger.error("Unexpected error occurred", e);
		return ApiResponse.errorMessage(messageSource.getMessage("systemException", null, Locale.JAPAN));
	}
}
