package com.example.monos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.monos.domain.Product;
import com.example.monos.dto.ProductSearchCondition;
import com.example.monos.mapper.ProductMapper;


/**
 * 商品関連のサービスを担当する実装クラス。
 * @author t.ueta
 */
@Service
public class ProductServiceImpl implements ProductService {
	private final ProductMapper productMapper;
	
	public ProductServiceImpl(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}

	/**
	 * 商品を検索する。
	 * @param condition 商品一覧画面の検索条件DTO
	 */
	@Override
	public List<Product> search(ProductSearchCondition condition) {
		return productMapper.selectList(condition);
	}

}
