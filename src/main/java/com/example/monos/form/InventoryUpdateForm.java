package com.example.monos.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryUpdateForm {
    private Integer inventoryId;
    private Integer version;

    @NotNull(message = "{NotNull.inventoryUpdateForm.reorderPoint}")
    @Min(value = 0, message = "{Min.inventoryUpdateForm.reorderPoint}")
    private Integer reorderPoint;

    @NotNull(message = "{NotNull.inventoryUpdateForm.appropriateStockQty}")
    @Min(value = 0, message = "{Min.inventoryUpdateForm.appropriateStockQty}")
    private Integer appropriateStockQty;
}
