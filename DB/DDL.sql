CREATE DATABASE IF NOT EXISTS product_like_db;

USE product_like_db;

CREATE TABLE IF NOT EXISTS USERS (
    user_id     VARCHAR(20)   PRIMARY KEY COMMENT '使用者 ID',
    user_name   VARCHAR(50)   NOT NULL    COMMENT '使用者名稱',
    email       VARCHAR(100)  NOT NULL    COMMENT '電子郵件',
    account     VARCHAR(20)   NOT NULL    COMMENT '扣款帳號',
    password    VARCHAR(255)  NOT NULL    COMMENT '登入密碼',
    role        VARCHAR(20)   NOT NULL DEFAULT 'USER' COMMENT '角色 (ADMIN / USER)'
);

CREATE TABLE IF NOT EXISTS PRODUCT (
    no           BIGINT        AUTO_INCREMENT PRIMARY KEY COMMENT '產品流水號',
    product_name VARCHAR(100)  NOT NULL COMMENT '產品名稱',
    price        DECIMAL(15,2) NOT NULL COMMENT '產品價格',
    fee_rate     DECIMAL(5,4)  NOT NULL COMMENT '手續費率'
);

CREATE TABLE IF NOT EXISTS LIKE_LIST (
    sn                BIGINT        AUTO_INCREMENT PRIMARY KEY COMMENT '流水序號',
    user_id           VARCHAR(20)   NOT NULL COMMENT '使用者 ID',
    product_no        BIGINT        NOT NULL COMMENT '產品流水號',
    purchase_quantity INT           NOT NULL COMMENT '購買數量',
    account           VARCHAR(20)   NOT NULL COMMENT '扣款帳號',
    total_fee         DECIMAL(15,2) NOT NULL COMMENT '總手續費用',
    total_amount      DECIMAL(15,2) NOT NULL COMMENT '預計扣款總金額',
    CONSTRAINT fk_like_user    FOREIGN KEY (user_id)    REFERENCES USERS(user_id),
    CONSTRAINT fk_like_product FOREIGN KEY (product_no) REFERENCES PRODUCT(no)
);

