CREATE TABLE USERS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    exclusive_plan BOOLEAN,
    balance DECIMAL(19, 2),
    account_number VARCHAR(255),
    birth_date DATE,
    CONSTRAINT UK_accountNumber UNIQUE (account_number)
);