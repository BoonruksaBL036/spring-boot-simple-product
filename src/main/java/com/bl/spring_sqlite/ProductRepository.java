package com.bl.spring_sqlite;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(Integer id);
    int save(Product product);
    int update(Product product);
    int deleteById(Integer id);
}
