package com.example.monos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.monos.domain.Role;
import com.example.monos.mapper.RoleMapper;


/**
 * マスタ関連のサービスを担当する実装クラス。
 * @author t.ueta
 */
@Service
public class MasterServiceImpl implements MasterService {
    
    private final RoleMapper roleMapper;

    public MasterServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }
    
    /**
     * ロールを全件取得する。
     */
    @Override
    public List<Role> getAllRoles() {
        // TODO 自動生成されたメソッド・スタブ
        List<Role> roles = roleMapper.findAll();
        return roles;
    }

}
