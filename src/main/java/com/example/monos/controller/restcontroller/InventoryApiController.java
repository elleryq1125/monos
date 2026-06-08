package com.example.monos.controller.restcontroller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.AvaliableInventoryDto;
import com.example.monos.service.InventoryService;


@RestController
@RequestMapping("/api/inventories")
public class InventoryApiController {
    private final InventoryService inventoryService;

    public InventoryApiController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * 指定された商品IDの在庫状況を取得する。
     * @param signinUser サインインユーザー
     * @param productId  商品ID
     * @return <p>在庫状況を含むAPIレスポンス。</p>
     * <p>該当データが存在しない場合はエラーメッセージを返却する。</p> 
     */
    @GetMapping("/warehouse-availabilities")
    public List<AvaliableInventoryDto> getavailableInventories(@AuthenticationPrincipal UserDetailsImpl signinUser, 
                                                                     @RequestParam int productId) {

        List<AvaliableInventoryDto> availabilities = 
            inventoryService.getAvailableInventories(signinUser.getCompanyId(), productId);
        
        return availabilities;
    }
}
