CREATE TABLE user(
  id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(75) NOT NULL,
  email VARCHAR(255),
  password VARCHAR(75)
);

CREATE TABLE bank_login(
  id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id INTEGER NOT NULL,
  bank_provider VARCHAR(12) NOT NULL,
  field1 BLOB,
  field2 BLOB,
  field3 BLOB,
  field4 BLOB,
  field5 BLOB
);

CREATE TABLE bank_account(
  id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  bank_login_id INTEGER NOT NULL,
  account_number VARCHAR(4),
  name VARCHAR(255) NOT NULL,
  alias VARCHAR(75),
  available_balance DECIMAL(10,2) ,
  current_balance DECIMAL(10,2) ,
  last_synced DATETIME
);

CREATE TABLE transaction(
  id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
  uid VARCHAR(255) NOT NULL,
  bank_account_id INTEGER NOT NULL,
  description_original VARCHAR(255) NOT NULL,
  description_processed VARCHAR(255),
  description_display VARCHAR(255),
  amount DECIMAL(10,2) NOT NULL,
  date DATETIME NOT NULL,
  flow VARCHAR(12),
  status VARCHAR(12)
);


