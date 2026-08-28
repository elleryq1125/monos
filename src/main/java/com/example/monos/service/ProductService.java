package com.example.monos.service;

import java.util.List;
import java.util.Optional;

import com.example.monos.domain.Product;
import com.example.monos.dto.ProductSearchCondition;
import com.example.monos.dto.ProductSuggestDto;

public interface ProductService {
	List<Product> search(ProductSearchCondition condition);
	Optional<Product> findById(int productId, int companyId);
	String save(Product product);
	List<ProductSuggestDto> suggest(String keyword, int companyId);
}
