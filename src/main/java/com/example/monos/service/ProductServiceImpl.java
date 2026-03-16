package com.example.monos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.monos.domain.Product;
import com.example.monos.dto.ProductSearchCondition;
import com.example.monos.exception.BusinessException;
import com.example.monos.mapper.ProductMapper;


/**
 * 商品関連のサービスを担当する実装クラス。
 * @author t.ueta
 */
@Service
public class ProductServiceImpl implements ProductService {
	private final ProductMapper productMapper;
	private final MessageSource messageSource;
	
	public ProductServiceImpl(ProductMapper productMapper, MessageSource messageSource) {
		this.productMapper = productMapper;
		this.messageSource = messageSource;
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

	@Override
	public void save(Product product) {
		// IDが未設定なら追加、それ以外は更新
		if (product.getProductId() == null) {
			
			// 商品コードの重複チェック
			if (productMapper.existsByProductCode(product.getProductCode(), product.getCompanyId())) {
				var errors = new HashMap<String, String>();
				errors.put("productCode", messageSource.getMessage("existsProductCode", new String[] {}, Locale.JAPAN));
				throw new BusinessException(errors);
			}
		}
	}
}
