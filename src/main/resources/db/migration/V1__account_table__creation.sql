CREATE TABLE accounts (
  id BIGINT AUTO_INCREMENT,
  account_number VARCHAR(36) DEFAULT RANDOM_UUID(),
  account_holder VARCHAR(255) NOT NULL,
  account_balance DECIMAL NOT NULL default 0.00,
  created_at TIMESTAMP DEFAULT NOW()
);