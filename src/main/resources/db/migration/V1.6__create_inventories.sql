CREATE TABLE inventories (
	inventori_id SERIAL PRIMARY KEY,
	company_id INTEGER,
	product_id INTEGER,
	warehouse_id INTEGER,
	on_hand_qty INTEGER,
	reseved_qty INTEGER,
	version INTEGER,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	deleted_at TIMESTAMP WITH TIME ZONE,
	
	CONSTRAINT fk_inventories_company_id FOREIGN KEY (company_id) REFERENCES companies (company_id) ON DELETE RESTRICT,
	CONSTRAINT fk_inventories_product_id FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE RESTRICT,
	CONSTRAINT fk_inventories_warehouse_id FOREIGN KEY (warehouse_id) REFERENCES warehouses (warehouse_id) ON DELETE RESTRICT,
    UNIQUE (company_id, product_id, warehouse_id)
);