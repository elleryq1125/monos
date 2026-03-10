package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.monos.domain.Product;
import com.example.monos.dto.ProductSearchCondition;

@Mapper
public interface ProductMapper {
	List<Product> selectList(ProductSearchCondition condition);
}
