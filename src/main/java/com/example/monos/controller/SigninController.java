package com.example.monos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * サインイン画面およびトップページへの表示を担当する。
 * @author t.ueta
 */
@Controller
@RequestMapping("/")
public class SigninController {

	/**
	 * <p>トップページを表示する。</p>
	 * @return index.html
	 */
	@GetMapping
	public String showIndex() {
		return "index";
	}
	
	/**
	 * <p>サインイン画面を表示する。</p>
	 * @return signin.html
	 */
	@GetMapping("/signin")
	public String showSignin() {
		return "signin";
	}
}
