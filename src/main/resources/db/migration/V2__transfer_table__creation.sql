CREATE TABLE transfers (
  id BIGINT AUTO_INCREMENT,
  debit_account_no VARCHAR(60) NOT NULL,
  credit_account_no VARCHAR(60) NOT NULL,
  amount DECIMAL NOT NULL,
  currency VARCHAR(3) NOT NULL DEFAULT 'GBP',
  created_at TIMESTAMP DEFAULT NOW()
);