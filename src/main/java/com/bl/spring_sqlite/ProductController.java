package com.bl.spring_sqlite;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// เพิ่ม @Tag เพื่อกำหนดชื่อและคำอธิบายสำหรับกลุ่ม API นี้ใน Swagger UI
@Tag(name = "Product Management", description = "CRUD operations for managing products in the system.")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService; // ฉีด Service เข้ามาแทน

    @Operation(
            summary = "Retrieve a list of all products",
            description = "Fetches all products stored in the database.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved list",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class)) // ระบุ Schema ที่เป็น List ของ Product
                    )
            }
    )
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @Operation(
            summary = "Get product by ID",
            description = "Retrieve product details by its unique identifier.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved product",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "ID of the product to be retrieved", required = true, example = "1")
            @PathVariable Integer id
    ) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok) // ถ้าเจอ ส่ง 200 OK
                .orElse(ResponseEntity.notFound().build()); // ถ้าไม่เจอ ส่ง 404 Not Found
    }

    @Operation(
            summary = "Create a new product",
            description = "Adds a new product to the database and returns the created product.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product object to be created",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Product.class))
            )
            @RequestBody Product product
    ) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @Operation(
            summary = "Update an existing product",
            description = "Updates the details of an existing product identified by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product successfully updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Product.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "ID of the product to be updated", required = true, example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product details to update",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Product.class))
            )
            @RequestBody Product productDetails
    ) {
        return productService.updateProduct(id, productDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete a product by ID",
            description = "Deletes a product from the database by its unique identifier.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Product successfully deleted (No Content)"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to be deleted", required = true, example = "1")
            @PathVariable Integer id
    ) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build(); // ถ้าลบสำเร็จ ส่ง 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // ถ้าไม่เจอ ส่ง 404 Not Found
        }
    }
}
