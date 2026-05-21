package com.productlike.system.repository;

import com.productlike.system.model.dto.LikeDetailDto;
import com.productlike.system.model.entity.LikeItem;
import com.productlike.system.model.entity.Product;
import com.productlike.system.model.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
public class LikeRepository {

    private final JdbcTemplate jdbcTemplate;

    public LikeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAllUsers() {
        return jdbcTemplate.query("CALL SP_GET_ALL_USERS()", (rs, rowNum) -> mapUser(rs));
    }

    public Optional<User> findUserById(String userId) {
        List<User> users = jdbcTemplate.query(
                "SELECT user_id, user_name, email, account, password, role FROM USERS WHERE user_id = ?",
                (rs, rowNum) -> mapUser(rs),
                userId
        );
        return users.stream().findFirst();
    }

    public Optional<User> findUserByCredentials(String userId, String password) {
        List<User> users = jdbcTemplate.query(
                "SELECT user_id, user_name, email, account, password, role FROM USERS WHERE user_id = ? AND password = ?",
                (rs, rowNum) -> mapUser(rs),
                userId, password
        );
        return users.stream().findFirst();
    }

    public User createUser(String userId, String userName, String email, String account, String password) {
        jdbcTemplate.update(
                "INSERT INTO USERS (user_id, user_name, email, account, password, role) VALUES (?, ?, ?, ?, ?, 'USER')",
                userId, userName, email, account, password
        );
        return findUserById(userId).orElseThrow();
    }

    public List<Product> findAllProducts() {
        return jdbcTemplate.query("CALL SP_GET_ALL_PRODUCTS()", (rs, rowNum) -> {
            Product p = new Product();
            p.setNo(rs.getLong("no"));
            p.setProductName(rs.getString("product_name"));
            p.setPrice(rs.getBigDecimal("price"));
            p.setFeeRate(rs.getBigDecimal("fee_rate"));
            return p;
        });
    }

    public LikeItem insertLike(String userId, Long productNo, int purchaseQuantity, String account) {
        return jdbcTemplate.query(
                "CALL SP_INSERT_LIKE(?, ?, ?, ?)",
                rs -> {
                    if (rs.next()) return mapLikeItem(rs);
                    return null;
                },
                userId, productNo, purchaseQuantity, account
        );
    }

    public List<LikeDetailDto> getLikeList(String userId) {
        return jdbcTemplate.query(
                "CALL SP_GET_LIKE_LIST(?)",
                (rs, rowNum) -> mapLikeDetail(rs),
                userId
        );
    }

    public List<LikeDetailDto> getAllLikeList() {
        return jdbcTemplate.query(
                """
                SELECT l.sn, l.user_id, u.user_name, p.product_name, p.price, p.fee_rate,
                       l.purchase_quantity, l.account, l.total_fee, l.total_amount, u.email
                FROM LIKE_LIST l
                JOIN PRODUCT p ON l.product_no = p.no
                JOIN USERS u ON l.user_id = u.user_id
                ORDER BY l.user_id, l.sn
                """,
                (rs, rowNum) -> mapLikeDetail(rs)
        );
    }

    public Optional<String> findLikeOwner(Long sn) {
        List<String> owners = jdbcTemplate.query(
                "SELECT user_id FROM LIKE_LIST WHERE sn = ?",
                (rs, rowNum) -> rs.getString("user_id"),
                sn
        );
        return owners.stream().findFirst();
    }

    public void deleteLike(Long sn) {
        jdbcTemplate.update("CALL SP_DELETE_LIKE(?)", sn);
    }

    public LikeItem updateLike(Long sn, Long productNo, String productName,
                               BigDecimal price, BigDecimal feeRate,
                               int purchaseQuantity, String account) {
        return jdbcTemplate.query(
                "CALL SP_UPDATE_LIKE(?, ?, ?, ?, ?, ?, ?)",
                rs -> {
                    if (rs.next()) return mapLikeItem(rs);
                    return null;
                },
                sn, productNo, productName, price, feeRate, purchaseQuantity, account
        );
    }

    private LikeItem mapLikeItem(ResultSet rs) throws java.sql.SQLException {
        LikeItem item = new LikeItem();
        item.setSn(rs.getLong("sn"));
        item.setUserId(rs.getString("user_id"));
        item.setProductNo(rs.getLong("product_no"));
        item.setPurchaseQuantity(rs.getInt("purchase_quantity"));
        item.setAccount(rs.getString("account"));
        item.setTotalFee(rs.getBigDecimal("total_fee"));
        item.setTotalAmount(rs.getBigDecimal("total_amount"));
        return item;
    }

    private LikeDetailDto mapLikeDetail(ResultSet rs) throws java.sql.SQLException {
        LikeDetailDto dto = new LikeDetailDto();
        dto.setSn(rs.getLong("sn"));
        dto.setUserId(getStringOrDefault(rs, "user_id", ""));
        dto.setUserName(getStringOrDefault(rs, "user_name", ""));
        dto.setProductName(rs.getString("product_name"));
        dto.setPrice(rs.getBigDecimal("price"));
        dto.setFeeRate(rs.getBigDecimal("fee_rate"));
        dto.setPurchaseQuantity(rs.getInt("purchase_quantity"));
        dto.setAccount(rs.getString("account"));
        dto.setTotalFee(rs.getBigDecimal("total_fee"));
        dto.setTotalAmount(rs.getBigDecimal("total_amount"));
        dto.setEmail(rs.getString("email"));
        return dto;
    }

    private User mapUser(ResultSet rs) throws java.sql.SQLException {
        User user = new User();
        user.setUserId(rs.getString("user_id"));
        user.setUserName(rs.getString("user_name"));
        user.setEmail(rs.getString("email"));
        user.setAccount(rs.getString("account"));
        user.setPassword(getStringOrDefault(rs, "password", ""));
        user.setRole(getStringOrDefault(rs, "role", "USER"));
        return user;
    }

    private String getStringOrDefault(ResultSet rs, String column, String fallback) {
        try {
            String value = rs.getString(column);
            return value == null || value.isBlank() ? fallback : value;
        } catch (Exception ex) {
            return fallback;
        }
    }
}
