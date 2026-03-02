CREATE TABLE products(
	product_id SERIAL PRIMARY KEY,
	company_id INTEGER,
	product_code CHAR(10),
	name VARCHAR(40),
	unit VARCHAR(10),
	active BOOLEAN DEFAULT true,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	deleted_at TIMESTAMP WITH TIME ZONE,
	CONSTRAINT fk_products_company_id FOREIGN KEY (company_id) REFERENCES companies (company_id) ON DELETE CASCADE,
	UNIQUE(company_id, product_code)
);

CREATE TABLE warehouses(
	warehouse_id SERIAL PRIMARY KEY,
	company_id INTEGER,
	warehouse_code CHAR(10),
	name VARCHAR(40),
	active BOOLEAN DEFAULT true,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	deleted_at TIMESTAMP WITH TIME ZONE,
	CONSTRAINT fk_warehouses_company_id FOREIGN KEY (company_id) REFERENCES companies (company_id) ON DELETE CASCADE,
	UNIQUE(company_id, warehouse_code)
);