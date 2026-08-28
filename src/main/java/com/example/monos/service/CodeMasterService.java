package com.example.monos.service;

import java.util.List;

import com.example.monos.domain.CodeMaster;

public interface CodeMasterService {
	List<CodeMaster> findByCodeType(String codeType, boolean addEmpty);
}
