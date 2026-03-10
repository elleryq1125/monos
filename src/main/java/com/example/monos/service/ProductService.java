package com.example.monos.service;

import java.util.List;

import com.example.monos.domain.Product;
import com.example.monos.dto.ProductSearchCondition;
import com.example.monos.form.ProductSearchForm;

public interface ProductService {
	List<Product> search(ProductSearchCondition condition);
}
