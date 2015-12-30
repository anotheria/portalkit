ALTER TABLE account add column tenant VARCHAR(10) ;
CREATE INDEX account_tenant_idx ON account (tenant);