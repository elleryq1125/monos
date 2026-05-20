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

import com.example.monos.domain.InboundSchedule;
import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.ApiResponse;
import com.example.monos.dto.InboundScheduleDetailDto;
import com.example.monos.form.InboundResultInputForm;
import com.example.monos.form.InboundScheduleInputForm;
import com.example.monos.service.InboundScheduleService;

import jakarta.validation.Valid;

/**
 * <p>入庫関連の非同期処理を担当する。</p>
 * @author t.ueta
 */
@RestController
@RequestMapping("/api")
public class InboundApiController {
	private final MessageSource messageSource;
	private final InboundScheduleService inboundScheduleService;
	
	public InboundApiController(MessageSource messageSource,
												 InboundScheduleService inboundScheduleService) {
		this.messageSource = messageSource;
		this.inboundScheduleService = inboundScheduleService;
	}
	
	@GetMapping("/inboundschedules/{inboundScheduleId}")
	public ApiResponse<?> get(@AuthenticationPrincipal UserDetailsImpl signinUser,
												 @PathVariable int inboundScheduleId){
		
		Optional<InboundScheduleDetailDto> inboundSchedule = inboundScheduleService.findById(inboundScheduleId, signinUser.getCompanyId());
		
		if (inboundSchedule.isPresent()) {
			return ApiResponse.successData(inboundSchedule.get());
		} else {
			return ApiResponse.errorMessage(
					messageSource.getMessage("dataNotExists", new String[] {}, Locale.JAPAN));
		}
	}
	
	/**
	 * 入庫予定情報を登録または更新する。
	 * @param signinUser サインインユーザー
	 * @param form 倉庫入力情報
	 * @param result バリデーション結果
	 * @return 処理結果を含むAPIレスポンス
	 * <p>バリデーションエラーがある場合はエラー内容を返却する。</p>
	 * <p>業務エラーが発生した場合はエラー内容を返却する。</p>
	 */
	@PostMapping("/inboundschedules/save")
	public ApiResponse<?> save(@AuthenticationPrincipal UserDetailsImpl signinUser,
												   @Valid @RequestBody InboundScheduleInputForm form,
												   BindingResult result){
		
		// バリデーションチェック
		if (result.hasErrors()) {
			return ApiResponse.validationError(result);
		}
		
		// 追加・更新用のDomainを作成
		var inboundSchedule = new InboundSchedule();
		inboundSchedule.setInboundScheduleId(form.getInboundScheduleId());
		inboundSchedule.setCompanyId(signinUser.getCompanyId());
		inboundSchedule.setProductId(form.getProductId());
		inboundSchedule.setWarehouseId(form.getWarehouseId());
		inboundSchedule.setScheduleQty(form.getScheduleQty());
		inboundSchedule.setScheduleDate(form.getScheduleDate());
		inboundSchedule.setVersion(form.getVersion());
		
		// 入庫予定の追加・更新処理
		String resultMessage = inboundScheduleService.save(inboundSchedule);
		
		return ApiResponse.successMessage(resultMessage);
	}
	
	/**
	 * 入庫予定情報を登録または更新する。
	 * @param signinUser サインインユーザー
	 * @param form 倉庫入力情報
	 * @param result バリデーション結果
	 * @return 処理結果を含むAPIレスポンス
	 * <p>バリデーションエラーがある場合はエラー内容を返却する。</p>
	 * <p>業務エラーが発生した場合はエラー内容を返却する。</p>
	 */
	@PostMapping("/inboundschedules/result/regist")
	public ApiResponse<?> registerInboundResult(@AuthenticationPrincipal UserDetailsImpl signinUser,
																			   @Valid @RequestBody InboundResultInputForm form,
																			   BindingResult result){
		
		// バリデーションチェック
		if (result.hasErrors()) {
			return ApiResponse.validationError(result);
		}
		
//		// 追加・更新用のDomainを作成
//		var inboundSchedule = new InboundSchedule();
//		inboundSchedule.setInboundScheduleId(form.getInboundScheduleId());
//		inboundSchedule.setCompanyId(signinUser.getCompanyId());
//		inboundSchedule.setProductId(form.getProductId());
//		inboundSchedule.setWarehouseId(form.getWarehouseId());
//		inboundSchedule.setScheduleQty(form.getScheduleQty());
//		inboundSchedule.setScheduleDate(form.getScheduleDate());
//		inboundSchedule.setVersion(form.getVersion());
//		
//		// 入庫予定の追加・更新処理
//		String resultMessage = inboundScheduleService.save(inboundSchedule);
		
		return null;
	}
}
