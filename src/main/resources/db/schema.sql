-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    salt VARCHAR(256),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(512) NOT NULL
);
-- 账单表
CREATE TABLE IF NOT EXISTS t_bill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    currency VARCHAR(10) DEFAULT 'CNY',
    account_type VARCHAR(50) COMMENT '现金, 银行卡, 信用卡, 支付宝, 微信等',
    type VARCHAR(8) NOT NULL COMMENT '收入, 支出',
    amount DECIMAL (10, 2) NOT NULL,
    description VARCHAR(512),
    bill_time TIMESTAMP NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES t_user (id)
);
