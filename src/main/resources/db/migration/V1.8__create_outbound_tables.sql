CREATE TABLE outbound_schedules (
	outbound_schedule_id SERIAL PRIMARY KEY,
	company_id INTEGER NOT NULL,
	inventory_id INTEGER NOT NULL,
	schedule_qty INTEGER NOT NULL,
	schedule_date DATE NOT NULL,
	status INTEGER NOT NULL,
	version INTEGER NOT NULL DEFAULT 1,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	deleted_at TIMESTAMP WITH TIME ZONE,
	
	CONSTRAINT fk_outbound_schedules_company_id FOREIGN KEY (company_id) REFERENCES companies (company_id) ON DELETE RESTRICT,
	CONSTRAINT fk_outbound_schedules_inventory_id FOREIGN KEY (inventory_id) REFERENCES inventories (inventory_id) ON DELETE RESTRICT
);

CREATE TABLE outbound_results (
	outbound_result_id SERIAL PRIMARY KEY,
	company_id INTEGER  NOT NULL,
	outbound_schedule_id INTEGER  NOT NULL,
	result_qty INTEGER  NOT NULL,
	result_date DATE  NOT NULL,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	deleted_at TIMESTAMP WITH TIME ZONE,
	
	CONSTRAINT fk_outbound_results_company_id FOREIGN KEY (company_id) REFERENCES companies (company_id) ON DELETE RESTRICT,
	CONSTRAINT fk_outbound_results_outbound_schedule_id FOREIGN KEY (outbound_schedule_id) REFERENCES outbound_schedules (outbound_schedule_id) ON DELETE RESTRICT
);