package com.example.monos.dto;

import java.util.Map;

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
	 * <p>入力エラーを返却する。</p>
	 * @param <T>
	 * @param message メッセージ
	 * @param fieldErrors　フィールドエラー
	 * @return ApiResponse
	 */
	public static  <T>ApiResponse<T> fieldError(String message, Map<String, String> fieldErrors){
		var res = new ApiResponse<T>();
		res.success = false;
		res.message = message;
		res.fieldErrors = fieldErrors;
		return res;
	}
}
