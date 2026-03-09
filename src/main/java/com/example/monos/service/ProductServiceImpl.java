package com.example.monos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.monos.domain.Product;
import com.example.monos.form.ProductSearchForm;

@Service
public class ProductServiceImpl implements ProductService {

	@Override
	public List<Product> search(ProductSearchForm form, int companyId) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
