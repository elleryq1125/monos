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
import com.example.monos.domain.Warehouse;
import com.example.monos.dto.ApiResponse;
import com.example.monos.form.WarehouseInputForm;
import com.example.monos.service.WarehouseService;

import jakarta.validation.Valid;

/**
 * <p>倉庫関連画面の非同期処理を担当する。</p>
 * @author t.ueta
 */
@RestController
@RequestMapping("/api/warehouses")
public class WarehouseApiController {
	private final MessageSource messageSource;
	private final WarehouseService warehouseService;
	
	public WarehouseApiController(MessageSource messageSource, WarehouseService warehouseService) {
		this.messageSource = messageSource;
		this.warehouseService = warehouseService;
	}

	/**
	 * 指定された倉庫IDの倉庫情報を取得する。
	 * @param signinUser サインインユーザー
	 * @param productId 倉庫ID
	 * @return 倉庫情報を含むAPIレスポンス。
	 * <p>該当データが存在しない場合はエラーメッセージを返却する。</p>
	 */
	@GetMapping("/{warehouseId}")
	public ApiResponse<?> get(@AuthenticationPrincipal UserDetailsImpl signinUser,
								  @PathVariable int warehouseId) {
		Optional<Warehouse> warehouse = warehouseService.findById(warehouseId, signinUser.getCompanyId());
		
		if (warehouse.isPresent()) {
			return ApiResponse.successData(warehouse.get());
		} else {
			return ApiResponse.errorMessage(
					messageSource.getMessage("dataNotExists", new String[] {}, Locale.JAPAN));
		}
	}
	
	/**
	 * 倉庫情報を登録または更新する。
	 * @param signinUser サインインユーザー
	 * @param form 倉庫入力情報
	 * @param result バリデーション結果
	 * @return 処理結果を含むAPIレスポンス
	 * <p>バリデーションエラーがある場合はエラー内容を返却する。</p>
	 * <p>業務エラーが発生した場合はエラー内容を返却する。</p>
	 */
	@PostMapping("/save")
	public ApiResponse<?> save(@AuthenticationPrincipal UserDetailsImpl signinUser,
															 @Valid @RequestBody  WarehouseInputForm form,
															 BindingResult result){
		// バリデーションチェック
		if (result.hasErrors()) {
			return ApiResponse.validationError(result);
		}
			
		// 追加・更新用のDomainを作成
		var warehouse = new Warehouse();
		warehouse.setWarehouseId(form.getWarehouseId());
		warehouse.setCompanyId(signinUser.getCompanyId());	
		warehouse.setWarehouseCode(form.getWarehouseCode());
		warehouse.setName(form.getName());
		warehouse.setActive(form.isActive());
		
		// 倉庫情報の追加・更新処理
		String resultMessage = warehouseService.save(warehouse);
		
		return ApiResponse.successMessage(resultMessage);
	}
}
