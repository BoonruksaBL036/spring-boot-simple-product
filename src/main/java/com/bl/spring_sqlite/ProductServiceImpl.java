package com.bl.spring_sqlite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // ระบุว่าเป็น Bean ของ Service Layer
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository; // ฉีด Interface เข้ามา

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        // สามารถใส่ Business Logic ตรงนี้ได้ เช่น ตรวจสอบข้อมูลก่อนบันทึก
        // if (product.getPrice() <= 0) {
        //     throw new IllegalArgumentException("Price must be positive.");
        // }
        productRepository.save(product);
        return product;
    }

    @Override
    public Optional<Product> updateProduct(Integer id, Product productDetails) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(productDetails.getName());
            existingProduct.setPrice(productDetails.getPrice());
            productRepository.update(existingProduct);
            return existingProduct;
        });
    }

    @Override
    public boolean deleteProduct(Integer id) {
        int result = productRepository.deleteById(id);
        return result > 0; // คืนค่า true ถ้ามีการลบเกิดขึ้น
    }
}
