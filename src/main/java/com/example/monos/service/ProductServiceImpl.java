package com.example.monos.service;

import java.util.List;
import java.util.Optional;

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
	 * @return 商品情報のリスト
	 */
	@Override
	public List<Product> search(ProductSearchCondition condition) {
		return productMapper.selectList(condition);
	}

	/**
	 * 商品を1件取得する。
	 * @param productId 商品ID
	 * @param companyId 企業ID
	 * @return 商品情報
	 */
	@Override
	public Optional<Product> findById(int productId, int companyId) {
		return Optional.ofNullable(productMapper.selectById(productId, companyId));
	}
}
