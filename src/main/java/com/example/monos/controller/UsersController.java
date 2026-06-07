package com.example.monos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.monos.domain.Role;
import com.example.monos.domain.TempUser;
import com.example.monos.domain.User;
import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.ResultMessage;
import com.example.monos.dto.UserInfo;
import com.example.monos.form.UserAddForm;
import com.example.monos.form.UserUpdateForm;
import com.example.monos.service.MasterService;
import com.example.monos.service.TempUserService;
import com.example.monos.service.UserService;


/**
 * <p>ユーザー関連画面への遷移を担当する。</p>
 * @author t.ueta
 */
@Controller
@RequestMapping("/users")
public class UsersController {
    private final TempUserService tempUserService;
    private final UserService userService;
    private final MasterService masterService;
    
    public UsersController(TempUserService tempUserService, UserService userService, MasterService masterService) {
        this.tempUserService = tempUserService;
        this.userService = userService;
        this.masterService = masterService;
    }

    /**
     * <p>ユーザー一覧画面を表示する。</p>
     * @return /user/users.html
     */
    @GetMapping
    public String showUsers(@AuthenticationPrincipal UserDetailsImpl signinUser, Model model) {
        List<UserInfo> companyUsers = userService.getUserInfoInCompany(signinUser.getCompanyId());
        model.addAttribute("companyUsers", companyUsers);
        
        return "/users/users";
    }
    
    
    /**
     * <p>ユーザー追加画面を表示する。</p>
     * @return /users/user-add.html
     */
    @GetMapping("/add")
    public String showUserAddForm(UserAddForm userAddForm, Model model) {
        setUserAddFormSelectionValues(model);
        return "/users/user-add";
    }
    
    /**
     * <p>入力エラーが無ければユーザー情報の仮登録を実施。</p>
     * <p>完了後、仮登録完了画面にリダイレクトする。</p>
     * @param signinUser サインインユーザーの情報
     * @param userAddForm フォームバインディング
     * @param result バリデーション結果                            
     * @param model ビューへ受け渡す用のモデル（入力値）
     * @param redirectAttributes リダイレクト時のフラッシュ属性
     * @return 正常完了：redirect to /users 入力エラー：/users/user-add.html
     */
    @PostMapping("/add")
    public String userAdd(@AuthenticationPrincipal UserDetailsImpl signinUser, @Validated UserAddForm userAddForm, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
    	
        if (result.hasErrors()) {
            setUserAddFormSelectionValues(model);
            return "/users/user-add";
        }
        
        TempUser tempUser = new TempUser();
        tempUser.setName(userAddForm.getName());
        tempUser.setEmail(userAddForm.getEmail());
        tempUser.setPassword(userAddForm.getPassword());
        tempUser.setRoleCode(userAddForm.getRoleCode());
        tempUser.setCompanyId(signinUser.getCompanyId());
        
        ResultMessage resultMessage = tempUserService.register(tempUser);
        
        redirectAttributes.addFlashAttribute("resultMessageType", resultMessage.getType());
        redirectAttributes.addFlashAttribute("resultMessage", resultMessage.getMessage());
        
        return "redirect:/users";
    }
    
    /**
     * <p>ユーザー更新画面を表示する。
     * @param signinUser サインインユーザーの情報
     * @param userId 更新対象のユーザーID（パスから取得）
     * @param userUpdateForm フォームバインディング
     * @param model ビューに渡すモデル
     * @return 正常終了：/users/user-update.html 不正なユーザーID：redirect to /users
     */
    @GetMapping("/update/{userId}")
    public String showUserUpdateForm(@AuthenticationPrincipal UserDetailsImpl signinUser, @PathVariable int userId, UserUpdateForm userUpdateForm, Model model) {
    	
        // 会社に存在しないユーザーID
        Optional<UserInfo> updateUserOp = userService.getUserInfo(userId, signinUser.getCompanyId());
        if (updateUserOp.isEmpty()) {
            return "redirect:/users";
        }

        var updateUser = updateUserOp.get();
        
        setUserUpdateFormSelectionValues(model);
        model.addAttribute("userId", updateUser.getUserId());
        userUpdateForm.setName(updateUser.getName());
        userUpdateForm.setRoleCode(updateUser.getRoleCode());

        return "/users/user-update";
    }
    
    
    /**
     * <p>入力エラーが無ければユーザー情報の更新を実施する。</p>
     * <p>更新後、ユーザー一覧画面にリダイレクトする。</pp>
     * @param signinUser サインインユーザーの情報
     * @param userId 更新対象のユーザーID（パスから取得）
     * @param userUpdateForm フォームバインディング
     * @param result バリデーション結果
     * @param model ビューに渡すモデル
     * @param redirectAttributes リダイレクト時のフラッシュ属性
     * @return ユーザー一覧画面
     */
    @PostMapping("/update/{userId}")
    public String userUpdate(@AuthenticationPrincipal UserDetailsImpl signinUser,@PathVariable int userId, @Validated UserUpdateForm userUpdateForm, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        
    	if (result.hasErrors()) {
    		setUserUpdateFormSelectionValues(model);
            return "users/user-update";
        }
        
        User updateUser = new User();
        updateUser.setUserId(userId);
        updateUser.setName(userUpdateForm.getName());
        updateUser.setRoleCode(userUpdateForm.getRoleCode());
        ResultMessage resultMessage = userService.updateUser(updateUser, signinUser.getCompanyId());
        
        redirectAttributes.addFlashAttribute("resultMessageType", resultMessage.getType());
        redirectAttributes.addFlashAttribute("resultMessage", resultMessage.getMessage());
        
        return "redirect:/users";
    }
    
    /**
     * <p>ユーザー情報を削除する。</p>
     * <p>削除後、ユーザー一覧画面にリダイレクトする。</p>
     * @param signinUser サインインユーザーの情報
     * @param userId 削除対象のユーザーID（パスから取得）
     * @param redirectAttributes リダイレクト時のフラッシュ属性
     * @return redirect to /users
     */
    @PostMapping("/delete/{userId}")
    public String userDelete(@AuthenticationPrincipal UserDetailsImpl signinUser,@PathVariable int userId, RedirectAttributes redirectAttributes) {
        ResultMessage resultMessage = userService.deleteUser(userId, signinUser);
        
        redirectAttributes.addFlashAttribute("resultMessageType", resultMessage.getType());
        redirectAttributes.addFlashAttribute("resultMessage", resultMessage.getMessage());
        
        return "redirect:/users";
    }
    
    /**
     * <p>ユーザー追加画面の入力項目を作成する。</p>
     * @param model ユーザー追加画面のモデル
     */
    private void setUserAddFormSelectionValues(Model model) {
        List<Role> roles = masterService.getAllRoles();
        model.addAttribute("roles", roles);
    }
    
    /**
     * <p>ユーザー更新画面の入力項目を作成する。</p>
     * @param model ユーザー更新画面のモデル
     */
    private void setUserUpdateFormSelectionValues(Model model) {
        List<Role> roles = masterService.getAllRoles();
        model.addAttribute("roles", roles);
    }
}
