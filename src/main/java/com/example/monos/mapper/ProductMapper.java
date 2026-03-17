package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.monos.domain.Product;
import com.example.monos.dto.ProductSearchCondition;

@Mapper
public interface ProductMapper {
	List<Product> selectList(ProductSearchCondition condition);
	Product selectById(@Param("productId") int productId, @Param("companyId") int companyId);
	boolean existsByProductCode(@Param("productCode") String productCode, @Param("companyId") int companyId);
	void insert(Product product);
}
