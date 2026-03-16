package com.example.monos.dto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;

import lombok.Data;

/**
 * RestAPIのレスポンスDTO
 * @author t.ueta
 * @param <T>
 */
@Data
public class ApiResponse<T> {
	private boolean success;
	private String message;
	private T data;
	private Map<String, String> fieldErrors;
	
	/**
	 *<p> 取得に成功したデータを返却する。</p>
	 * @param <T>
	 * @param data APIの取得データ
	 * @return ApiResponse
	 */
	public static <T>ApiResponse<T> successData(T data){
		var res = new ApiResponse<T>();
		res.success = true;
		res.data = data;
		return res;
	}
	
	/**
	 * <p>成功メッセージを返却する。</p>
	 * @param <T>
	 * @param message メッセージ
	 * @return ApiResponse
	 */
	public static <T>ApiResponse<T> successMessage(String message){
		var res = new ApiResponse<T>();
		res.success = true;
		res.message = message;
		return res;
	}
	
	/**
	 * <p>エラーメッセージを返却する。</p>
	 * @param <T>
	 * @param message メッセージ
	 * @return ApiResponse フィールドエラー
	 */
	public static  <T>ApiResponse<T> errorMessage(String message){
		var res = new ApiResponse<T>();
		res.success = false;
		res.message = message;
		return res;
	}
	
	/**
	 * <p>バリデーションエラーを返却する。</p>
	 * @param <T>
	 * @param result BindingResult
	 * @return ApiResponse
	 */
	 public static <T>ApiResponse<T> validationError(BindingResult result){
		 Map<String,String> errors = new HashMap<>();
		 result.getFieldErrors().forEach(e ->
		 	errors.put(e.getField(), e.getDefaultMessage())
		 );

	     var res = new ApiResponse<T>();
	     res.success = false;
	     res.fieldErrors = errors;

	     return res;
	 }
	 
	 /**
	  * <p>バリデーションエラーを返却する</p>
	 * @param <T>
	 * @param errors HashMap<入力項目のid, エラーメッセージ>
	 * @return
	 */
	public static <T>ApiResponse<T> validationError(Map<String, String> errors){
	     var res = new ApiResponse<T>();
	     res.success = false;
	     res.fieldErrors = errors;

	     return res;
	 }
}
