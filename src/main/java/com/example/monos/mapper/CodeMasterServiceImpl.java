package com.example.monos.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.monos.domain.CodeMaster;
import com.example.monos.service.CodeMasterService;

@Service
public class CodeMasterServiceImpl implements CodeMasterService {
	public final CodeMasterMapper codeMasterMapper;
	
	public CodeMasterServiceImpl(CodeMasterMapper codeMasterMapper) {
		this.codeMasterMapper = codeMasterMapper;
	}
	
	/**
	 * コードタイプを条件にコードマスタを取得する。
	 * @param codeType コードタイプ
	 * @param addEmpty 空白追加フラグ
	 */
	@Override
	public List<CodeMaster> findByCodeType(String codeType, boolean addEmpty) {
		List<CodeMaster> codeMasters = codeMasterMapper.selectListByCodeType(codeType);
		
		if (addEmpty) {
			codeMasters.add(0, new CodeMaster());
		}
		
		return codeMasters;
	}

}
