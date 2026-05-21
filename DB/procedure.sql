USE product_like_db;

DELIMITER //

CREATE PROCEDURE SP_GET_ALL_USERS()
BEGIN
    SELECT user_id, user_name, email, account, password, role
    FROM USERS
    ORDER BY user_id;
END //

CREATE PROCEDURE SP_GET_ALL_PRODUCTS()
BEGIN
    SELECT no, product_name, price, fee_rate
    FROM PRODUCT
    ORDER BY no;
END //

CREATE PROCEDURE SP_INSERT_LIKE(
    IN p_user_id          VARCHAR(20),
    IN p_product_no       BIGINT,
    IN p_purchase_quantity INT,
    IN p_account          VARCHAR(20)
)
BEGIN
    DECLARE v_price        DECIMAL(15,2);
    DECLARE v_fee_rate     DECIMAL(5,4);
    DECLARE v_total_fee    DECIMAL(15,2);
    DECLARE v_total_amount DECIMAL(15,2);
    DECLARE v_sn           BIGINT;

    SELECT price, fee_rate INTO v_price, v_fee_rate
    FROM PRODUCT WHERE no = p_product_no;

    IF v_price IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '產品不存在';
    END IF;

    SET v_total_fee    = ROUND(v_price * p_purchase_quantity * v_fee_rate, 2);
    SET v_total_amount = ROUND(v_price * p_purchase_quantity + v_total_fee, 2);

    INSERT INTO LIKE_LIST (user_id, product_no, purchase_quantity, account, total_fee, total_amount)
    VALUES (p_user_id, p_product_no, p_purchase_quantity, p_account, v_total_fee, v_total_amount);

    SET v_sn = LAST_INSERT_ID();

    SELECT sn, user_id, product_no, purchase_quantity, account, total_fee, total_amount
    FROM LIKE_LIST WHERE sn = v_sn;
END //

CREATE PROCEDURE SP_GET_LIKE_LIST(
    IN p_user_id VARCHAR(20)
)
BEGIN
    SELECT l.sn, l.user_id, u.user_name, p.product_name, p.price, p.fee_rate,
           l.purchase_quantity, l.account, l.total_fee, l.total_amount, u.email
    FROM LIKE_LIST l
    JOIN PRODUCT p ON l.product_no = p.no
    JOIN USERS u ON l.user_id = u.user_id
    WHERE l.user_id = p_user_id
    ORDER BY l.sn;
END //

CREATE PROCEDURE SP_DELETE_LIKE(
    IN p_sn BIGINT
)
BEGIN
    DECLARE v_count INT;

    SELECT COUNT(*) INTO v_count FROM LIKE_LIST WHERE sn = p_sn;

    IF v_count = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '喜好商品不存在';
    END IF;

    DELETE FROM LIKE_LIST WHERE sn = p_sn;
END //

CREATE PROCEDURE SP_UPDATE_LIKE(
    IN p_sn               BIGINT,
    IN p_product_no       BIGINT,
    IN p_product_name     VARCHAR(100),
    IN p_price            DECIMAL(15,2),
    IN p_fee_rate         DECIMAL(5,4),
    IN p_purchase_quantity INT,
    IN p_account          VARCHAR(20)
)
BEGIN
    DECLARE v_price        DECIMAL(15,2);
    DECLARE v_fee_rate     DECIMAL(5,4);
    DECLARE v_total_fee    DECIMAL(15,2);
    DECLARE v_total_amount DECIMAL(15,2);
    DECLARE v_count        INT;

    IF p_product_name IS NOT NULL AND p_price IS NOT NULL AND p_fee_rate IS NOT NULL THEN
        UPDATE PRODUCT
        SET product_name = p_product_name, price = p_price, fee_rate = p_fee_rate
        WHERE no = p_product_no;
    END IF;

    SELECT price, fee_rate INTO v_price, v_fee_rate
    FROM PRODUCT WHERE no = p_product_no;

    IF v_price IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '產品不存在';
    END IF;

    SET v_total_fee    = ROUND(v_price * p_purchase_quantity * v_fee_rate, 2);
    SET v_total_amount = ROUND(v_price * p_purchase_quantity + v_total_fee, 2);

    SELECT COUNT(*) INTO v_count FROM LIKE_LIST WHERE sn = p_sn;
    IF v_count = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '喜好商品不存在';
    END IF;

    UPDATE LIKE_LIST
    SET product_no = p_product_no,
        purchase_quantity = p_purchase_quantity,
        account = p_account,
        total_fee = v_total_fee,
        total_amount = v_total_amount
    WHERE sn = p_sn;

    SELECT sn, user_id, product_no, purchase_quantity, account, total_fee, total_amount
    FROM LIKE_LIST WHERE sn = p_sn;
END //

DELIMITER ;
