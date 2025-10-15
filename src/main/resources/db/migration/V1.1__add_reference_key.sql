ALTER TABLE users ADD FOREIGN KEY (role_code) REFERENCES roles (role_code);
ALTER TABLE users ADD FOREIGN KEY (company_id) REFERENCES companies (company_id);
