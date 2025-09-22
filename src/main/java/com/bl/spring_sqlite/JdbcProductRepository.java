package com.bl.spring_sqlite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository // ระบุว่าเป็น Bean สำหรับจัดการข้อมูล
public class JdbcProductRepository implements ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper ทำหน้าที่แปลงข้อมูลจาก ResultSet ของ SQL ไปเป็น Java Object
    private final RowMapper<Product> productRowMapper = new RowMapper<Product>() {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return product;
        }
    };

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    @Override
    public Optional<Product> findById(Integer id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try {
            Product product = jdbcTemplate.queryForObject(sql, new Object[]{id}, productRowMapper);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            return Optional.empty(); // คืนค่า Optional.empty() เมื่อไม่พบข้อมูล
        }
    }

    @Override
    public int save(Product product) {
        String sql = "INSERT INTO products (name, price) VALUES (?, ?)";
        return jdbcTemplate.update(sql, product.getName(), product.getPrice());
    }

    @Override
    public int update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?";
        return jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getId());
    }

    @Override
    public int deleteById(Integer id) {
        String sql = "DELETE FROM products WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
