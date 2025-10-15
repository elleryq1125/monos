package com.example.monos.service;

import com.example.monos.domain.TempUser;
import com.example.monos.dto.ResultMessage;

/**
 * 仮ユーザー関連のサービスを担当する実装クラスのインターフェース。
 * @author t.ueta
 */
public interface TempUserService {
    ResultMessage register(TempUser tempUser);
}
