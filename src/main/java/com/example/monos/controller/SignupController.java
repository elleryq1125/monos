package com.example.monos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.monos.common.Const;
import com.example.monos.domain.TempUser;
import com.example.monos.dto.ResultMessage;
import com.example.monos.form.SignupForm;
import com.example.monos.service.TempUserService;
import com.example.monos.service.UserService;

/**
 * <p>サインアップ画面、仮登録完了画面、本登録完了画面への遷移を担当する。</p>
 * @author t.ueta
 */
@Controller
@RequestMapping("/signup")
public class SignupController {
    private final TempUserService tempUserService;
    private final UserService userService;
    
    public SignupController(TempUserService  tempUserService, UserService userService) {
        this.tempUserService = tempUserService;
        this.userService = userService;
    }
    
    /**
     * <p>サインアップ画面を表示する。</p>
     * @param signupForm フォームバインディング用
     * @return signup/signup.html
     */
    @GetMapping
    public String showSignup(SignupForm signupForm) {
        return "signup/signup";
    }
    
    /**
     * <p>入力エラーが無ければユーザー情報の仮登録を実施。</p>
     * <p>完了後、仮登録完了画面にリダイレクトする。</p>
     * @param signupForm フォームバインディング用
     * @param result バリデーション結果
     * @param redirectAttributes リダイレクト時のフラッシュ属性
     * @return 入力エラー：signup/signup.html　正常完了：redirect to /signup/tempuserregist
     */
    @PostMapping
    public String tempUserRegist(@Validated SignupForm signupForm, BindingResult result, RedirectAttributes redirectAttributes) {
    	
        if (result.hasErrors()) {
            return "signup/signup";
        }
        
        TempUser tempUser= new TempUser();
        tempUser.setName(signupForm.getName());
        tempUser.setEmail(signupForm.getEmail());
        tempUser.setPassword(signupForm.getPassword());
        tempUser.setCompanyName(signupForm.getCompanyName());
        tempUser.setRoleCode(Const.ROLE_ADMIN);
        
        ResultMessage resultMessage = tempUserService.register(tempUser);    
        
        redirectAttributes.addFlashAttribute("email", signupForm.getEmail());
        redirectAttributes.addFlashAttribute("resultMessage", resultMessage.getMessage());
        
        return "redirect:/signup/tempuserregist";
    }
    
    
    /**
     * <p>仮登録完了画面を表示する。</p>
     * @return signup/singup-tempuser-regist.html
     */
    @GetMapping("/tempuserregist")
    public String showSignupTempUserRegist() {
        return "signup/signup-tempuser-regist";
    }
    
    /**
     * <p>本登録完了画面を表示する。</p>
     * @param uuid 仮登録情報の一意識別子
     * @param model ビューへ受け渡す用のモデル
     * @return signup/signup-user-regist.html
     */
    @GetMapping("/userregist")
    public String showSignupUserRegist(@RequestParam("uuid")String uuid, Model model) {
        // ユーザ情報本登録
        ResultMessage resultMessage = userService.registUser(uuid);
        model.addAttribute("resultMessage", resultMessage.getMessage());
        
        return "signup/signup-user-regist";
    }
}
