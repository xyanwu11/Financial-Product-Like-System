USE product_like_db;

INSERT INTO USERS (user_id, user_name, email, account, password, role) VALUES
('admin',       '管理員', 'admin@system.com', '0000000000', 'admin', 'ADMIN'),
('A1236456789', '王曉明',     'wang@email.com',   '1111999666', '1234', 'USER'),
('B9876543210', '李大華',     'lee@email.com',    '2222888555', '1234', 'USER');

INSERT INTO PRODUCT (product_name, price, fee_rate) VALUES
('高成長基金',       584.07, 0.015),
('中小型股基金',     313.50, 0.025),
('高科技基金',       190.45,  0.02),
('金滿意基金',       240.22,  0.01),
('店頭市場基金',     181.16,  0.03);

INSERT INTO LIKE_LIST (user_id, product_no, purchase_quantity, account, total_fee, total_amount) VALUES
('A1236456789', 1,  50, '1111999666',  438.05, 29641.55),
('A1236456789', 3, 200, '1111999666',  761.80, 38851.80),
('A1236456789', 5, 150, '1111999666',  815.22, 27989.22),
('B9876543210', 2, 100, '2222888555',  783.75, 32133.75),
('B9876543210', 4, 300, '2222888555',  720.66, 72786.66),
('B9876543210', 5,  80, '2222888555',  434.78, 14927.58);