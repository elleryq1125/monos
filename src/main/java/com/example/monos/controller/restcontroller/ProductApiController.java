package com.example.monos.controller.restcontroller;

import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.monos.domain.Product;
import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.ApiResponse;
import com.example.monos.exception.BusinessException;
import com.example.monos.form.ProductInputForm;
import com.example.monos.service.ProductService;

import jakarta.validation.Valid;

/**
 * <p>商品関連画面の非同期処理を担当する。</p>
 * @author t.ueta
 */
@RestController
@RequestMapping("/api/products")
public class ProductApiController {
	private final MessageSource messageSource;
	private final ProductService productService;
	
	public ProductApiController(MessageSource messageSource, ProductService productSerivce) {
		this.messageSource = messageSource;
		this.productService = productSerivce;
	}

	/**
	 * <p>IDに紐づく商品情報を返却する。</p>
	 * @param productId 商品ID
	 * @return 商品情報またはエラーメッセージ
	 */
	@GetMapping("/{productId}")
	public ApiResponse<Product> get(@AuthenticationPrincipal UserDetailsImpl signinUser,
								  @PathVariable int productId) {
		Optional<Product> product = productService.findById(productId, signinUser.getCompanyId());
		
		if (product.isPresent()) {
			return ApiResponse.successData(product.get());
		} else {
			return ApiResponse.errorMessage(
					messageSource.getMessage("dataNotExists", new String[] {}, Locale.JAPAN));
		}
	}
	
	@PostMapping("/save")
	public ApiResponse<Product> save(@AuthenticationPrincipal UserDetailsImpl signinUser,
															 @Valid @RequestBody  ProductInputForm form,
															 BindingResult result){
		// バリデーションチェック
		if (result.hasErrors()) {
			return ApiResponse.validationError(result);
		}
		
		try {
			// 追加・更新用のDomainを作成
			var product = new Product();
			product.setProductId(form.getProductId());
			product.setCompanyId(signinUser.getCompanyId());
			product.setProductCode(form.getProductCode());
			product.setName(form.getName());
			product.setUnit(form.getUnit());
			
			// 商品情報の追加・更新処理
			productService.save(product);
			
		} catch (BusinessException e) {
			return ApiResponse.validationError(e.getErrors());
		}
		
		return null;
	}
}
