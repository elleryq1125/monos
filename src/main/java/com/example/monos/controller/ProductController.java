package com.example.monos.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.monos.domain.Product;
import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.ProductSearchCondition;
import com.example.monos.form.ProductSearchForm;
import com.example.monos.service.ProductService;


/**
 * <p>商品関連画面への遷移を担当する。</p>
 * @author t.ueta
 */
@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * <p>商品一覧画面を表示する。</p>
     * @return /products/products.html
     */
    @GetMapping
    public String showProducts(@AuthenticationPrincipal UserDetailsImpl signinUser,
    											@ModelAttribute ProductSearchForm form,
												Model model) {
    	
    	// 入力条件を検索用DTOに設定
    	var condition = new ProductSearchCondition();
    	condition.setCompanyId(signinUser.getCompanyId());
    	condition.setProductCode(form.getProductCode());
    	condition.setName(form.getName());
    	condition.setActive(form.isActive());
    	
        List<Product> products = productService.search(condition);
        model.addAttribute("products", products);
        
        return "/products/products";
    }
}
