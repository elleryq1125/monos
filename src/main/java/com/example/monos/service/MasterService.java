package com.example.monos.service;

import java.util.List;

import com.example.monos.domain.Role;

/**
 * マスタ関連のサービスを担当する実装クラスのインターフェース。
 * @author t.ueta
 */
public interface MasterService {
    List<Role> getAllRoles();
}
