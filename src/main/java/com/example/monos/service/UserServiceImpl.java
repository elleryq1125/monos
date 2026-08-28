package com.example.monos.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.monos.common.Const;
import com.example.monos.domain.Company;
import com.example.monos.domain.TempUser;
import com.example.monos.domain.User;
import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.ResultMessage;
import com.example.monos.dto.UserInfo;
import com.example.monos.mapper.CompanyMapper;
import com.example.monos.mapper.TempUserMapper;
import com.example.monos.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {
    
    private final TempUserMapper tempUserMapper;
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;
    private final MessageSource messageSource;
    
    public UserServiceImpl(TempUserMapper tempUserMapper, UserMapper userMapper, CompanyMapper companyMapper, MessageSource messageSource) {
        this.tempUserMapper = tempUserMapper;
        this.userMapper = userMapper;
        this.companyMapper = companyMapper;
        this.messageSource = messageSource;
    }

    /**
     * getUserInfoInCompanyメソッド
     * 会社に属するユーザーを取得する
     * @param singinUserCompamnyId サインインユーザの会社ID
     * @return userList 会社に所属するユーザー情報
     */
    @Override
    public List<UserInfo> getUserInfoInCompany(int singinUserCompamnyId) {
        // TODO 自動生成されたメソッド・スタブ
        List<UserInfo> userList = userMapper.findByCompanyId(singinUserCompamnyId);  
        return userList;
    }

    /**
     * getUserInfoメソッド
     * サインインユーザと同じ会社IDを持つユーザ情報を取得する
     * @param userId 取得するユーザーID
     * @param signinUserCompanyId サインインユーザの会社ID
     * @return 有：user 無：null
     */
    @Override
    public Optional<UserInfo> getUserInfo(int userId, int signinUserCompanyId) {
        return userMapper.findByUserId(userId)
                .filter(user -> user.getCompanyId() == signinUserCompanyId);
    }
    
    /**
     * registerメソッド
     * UUIDからユーザ仮登録情報を取得し本登録する
     * 企業情報が未登録の場合は企業情報も登録する
     * @param uuid
     * @return resultMessage
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage registUser(String uuid) {
        // TODO 自動生成されたメソッド・スタブ  
        ResultMessage resultMessage = new ResultMessage();            
        
        // UUIDに紐づく仮登録情報が存在しない場合はエラー
        Optional<TempUser> tempUserOp = tempUserMapper.findByUuid(uuid);
        if (!tempUserOp.isPresent()) {
            resultMessage.setType(Const.MESSAGE_TYPE_ERROR);
            resultMessage.setMessage(messageSource.getMessage("urlExpired", new String[] {}, Locale.JAPAN));
            return resultMessage;
        }
        
        TempUser tempUser = tempUserOp.get(); 
        
        // 仮登録メールアドレスが本登録済の場合はエラー
        Optional<UserInfo> userOp = userMapper.findByEmail(tempUser.getEmail());
        if (userOp.isPresent()) {
            resultMessage.setType(Const.MESSAGE_TYPE_ERROR);
            resultMessage.setMessage(messageSource.getMessage("emailExists", new String[] {tempUser.getEmail()},Locale.JAPAN));
            return resultMessage;
        }
        
        // 仮登録情報の企業IDが0の場合は企業情報を新規登録
        if (tempUser.getCompanyId() == 0) {
            Company company = new Company();
            company.setName(tempUser.getCompanyName());
            companyMapper.insert(company);
            
            // 登録時に採番された企業IDを取得
            tempUser.setCompanyId(company.getCompanyId());
        }
        
        // 仮登録情報を本登録
        User user = new User();
        user.setEmail(tempUser.getEmail());
        user.setPassword(tempUser.getPassword());
        user.setName(tempUser.getName());
        user.setCompanyId(tempUser.getCompanyId());
        user.setRoleCode(tempUser.getRoleCode());
        userMapper.insert(user);
        
        resultMessage.setType(Const.MESSAGE_TYPE_SUCCESS);
        resultMessage.setMessage(messageSource.getMessage("userRegistComplete", new String[] {}, Locale.JAPAN));
        
        return resultMessage;
    }

    /**
     * updateUserメソッド
     * ユーザー情報を更新する
     * @param updateUser userのDomain
     * @param signinUserCompanyId サインインユーザが持つ会社ID
     * @return resultMessage
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMessage updateUser(User updateUser, int signinUserCompanyId) {
        // TODO 自動生成されたメソッド・スタブ
        var resultMessage = new ResultMessage();
        
        // 更新対象のユーザー情報を取得
        Optional<UserInfo> updUserInfoOp = userMapper.findByUserId(updateUser.getUserId());
        
        // 対象ユーザー情報が存在しない
        if (updUserInfoOp.isEmpty()) {
            resultMessage.setType(Const.MESSAGE_TYPE_ERROR);
            resultMessage.setMessage(messageSource.getMessage("userInfoNotExists", new String[] {}, Locale.JAPAN));
            return resultMessage;
        }
        
        // 対象ユーザ情報とサインインユーザーの会社IDが相違
        var updUserInfo = updUserInfoOp.get();
        if (updUserInfo.getCompanyId() != signinUserCompanyId) {
            resultMessage.setType(Const.MESSAGE_TYPE_ERROR);
            resultMessage.setMessage(messageSource.getMessage("updateFaild", new String[] {}, Locale.JAPAN));
            return resultMessage;
        }

        // 更新
        userMapper.update(updateUser);
        resultMessage.setType(Const.MESSAGE_TYPE_SUCCESS);
        resultMessage.setMessage(messageSource.getMessage("updateComplete", new String[] {}, Locale.JAPAN));    
        return resultMessage;
    }

    /**
     * deleteUserメソッド
     * ユーザー情報を削除する
     * @param deleteUserId 削除するユーザID
     * @param signinUserCompanyId サインインユーザが持つ会社ID
     * @return resultMessage
     */
    @Override
    public ResultMessage deleteUser(int deleteUserId, UserDetailsImpl signinUser) {
        // TODO 自動生成されたメソッド・スタブ
        var resultMessage = new ResultMessage();
        
        // 削除対象のユーザー情報を取得
        Optional<UserInfo> delUserInfoOp = userMapper.findByUserId(deleteUserId);
        
        // 対象ユーザー情報が存在しない
        if (delUserInfoOp.isEmpty()) {
            resultMessage.setType(Const.MESSAGE_TYPE_ERROR);
            resultMessage.setMessage(messageSource.getMessage("userInfoNotExists", new String[] {}, Locale.JAPAN));
            return resultMessage;
        }
        
        // 対象ユーザ情報とサインインユーザーの会社IDが相違
        var delUserInfo = delUserInfoOp.get();
        if (delUserInfo.getCompanyId() != signinUser.getCompanyId()) {
            resultMessage.setType(Const.MESSAGE_TYPE_ERROR);
            resultMessage.setMessage(messageSource.getMessage("deleteFaild", new String[] {}, Locale.JAPAN));
            return resultMessage;
        }
 
        // 対象ユーザーがサインインユーザー自身
        if (delUserInfo.getUserId() == signinUser.getUserId()) {
            resultMessage.setType(Const.MESSAGE_TYPE_ERROR);
            resultMessage.setMessage(messageSource.getMessage("isSigninUser", new String[] {}, Locale.JAPAN));
            return resultMessage;
        }
        
        // 削除
        userMapper.delete(deleteUserId);
        resultMessage.setType(Const.MESSAGE_TYPE_SUCCESS);
        resultMessage.setMessage(messageSource.getMessage("deleteComplete", new String[] {}, Locale.JAPAN));   
        
        return resultMessage;
    }
}
