ALTER TABLE inventories
    ADD COLUMN reorder_point INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN appropriate_stock_qty INTEGER NOT NULL DEFAULT 0;
