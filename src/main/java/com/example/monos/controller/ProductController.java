package com.example.monos.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.monos.domain.Product;
import com.example.monos.domain.Role;
import com.example.monos.domain.TempUser;
import com.example.monos.domain.User;
import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.ProductSearchCondition;
import com.example.monos.dto.ResultMessage;
import com.example.monos.dto.UserInfo;
import com.example.monos.form.ProductSearchForm;
import com.example.monos.form.UserAddForm;
import com.example.monos.form.UserUpdateForm;
import com.example.monos.service.MasterService;
import com.example.monos.service.ProductService;
import com.example.monos.service.TempUserService;
import com.example.monos.service.UserService;


/**
 * <p>商品関連画面への遷移を担当する。</p>
 * @author t.ueta
 */
@Controller
@RequestMapping("/products")
public class ProductController {
    private final static Logger log = LoggerFactory.getLogger(ProductController.class);
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
    	log.info("商品一覧画面：表示");
    	
    	// 入力条件を検索用DTOに設定
    	var condition = new ProductSearchCondition();
    	condition.setCompanyId(signinUser.getCompanyId());
    	condition.setProductCode(form.getProductCode());
    	condition.setName(form.getName());
    	
        List<Product> products = productService.search(condition);
        model.addAttribute("products", products);
        
        return "/products/products";
    }
}
