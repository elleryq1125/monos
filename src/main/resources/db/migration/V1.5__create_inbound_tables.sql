CREATE TABLE inbound_schedules (
	inbound_schedule_id SERIAL PRIMARY KEY,
	company_id INTEGER,
	product_id INTEGER,
	warehouse_id INTEGER,
	schedule_qty INTEGER,
	schedule_date DATE,
	status INTEGER,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	deleted_at TIMESTAMP WITH TIME ZONE,
	
	CONSTRAINT fk_inbound_schedules_company_id FOREIGN KEY (company_id) REFERENCES companies (company_id) ON DELETE RESTRICT,
	CONSTRAINT fk_inbound_schedules_product_id FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE RESTRICT,
	CONSTRAINT fk_inbound_schedules_warehouse_id FOREIGN KEY (warehouse_id) REFERENCES warehouses (warehouse_id) ON DELETE RESTRICT
);

CREATE TABLE inbound_results (
	inbound_result_id SERIAL PRIMARY KEY,
	company_id INTEGER,
	product_id INTEGER,
	warehouse_id INTEGER,
	inbound_schedule_id INTEGER,
	result_qty INTEGER,
	result_date DATE,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	deleted_at TIMESTAMP WITH TIME ZONE,
	
	CONSTRAINT fk_inbound_results_company_id FOREIGN KEY (company_id) REFERENCES companies (company_id) ON DELETE RESTRICT,
	CONSTRAINT fk_inbound_results_product_id FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE RESTRICT,
	CONSTRAINT fk_inbound_results_warehouse_id FOREIGN KEY (warehouse_id) REFERENCES warehouses (warehouse_id) ON DELETE RESTRICT,
	CONSTRAINT fk_inbound_results_inbound_schedule_id FOREIGN KEY (inbound_schedule_id) REFERENCES inbound_schedules (inbound_schedule_id) ON DELETE RESTRICT
);