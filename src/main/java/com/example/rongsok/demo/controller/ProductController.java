package com.example.rongsok.demo.controller;


import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rongsok.demo.model.Product;
import com.example.rongsok.demo.model.ProductResponse;
import com.example.rongsok.demo.repository.ProductRepository;
import com.example.rongsok.demo.security.JwtTokenProvider;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    JwtTokenProvider tokenProvider;

    @GetMapping("/all")
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

     
     @SecurityRequirement(name = "Bearer Authentication")
     @PostMapping("/add")
         
    public ResponseEntity<?> addProduct(@RequestBody @NonNull Product product, HttpServletRequest request) {
        String token = resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        productRepository.save(product);
        
        ProductResponse response = new ProductResponse();
        response.setMessage("Product added successfully");
        response.setProduct(product);
        return ResponseEntity.ok(response);
    
        
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct, HttpServletRequest request) {
        String token = resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    
        // Cari produk dengan ID tertentu
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            // Update atribut-atribut produk dengan atribut-atribut yang diberikan pada updatedProduct
            product.setId(updatedProduct.getId());
            product.setnama(updatedProduct.getnama());
            product.setnpm(updatedProduct.getnpm());
            product.setkelas(updatedProduct.getkelas());
            // Simpan perubahan ke dalam database
            productRepository.save(product);
            // Kembalikan respons berhasil
            return ResponseEntity.ok("user updated successfully");
        } else {
            // Jika produk dengan ID tertentu tidak ditemukan, kembalikan respons not found
            return ResponseEntity.notFound().build();
        }
    }    
    @SecurityRequirement(name = "Bearer Authentication")
@DeleteMapping("/delete/{id}")
public ResponseEntity<?> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
    String token = resolveToken(request);
    if (token == null || !tokenProvider.validateToken(token)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }

    // Cari produk dengan ID tertentu
    Product product = productRepository.findById(id).orElse(null);
    if (product != null) {
        // Hapus produk dari database
        productRepository.delete(product);
        // Kembalikan respons berhasil
        return ResponseEntity.ok("user deleted successfully");
    } else {
        // Jika produk dengan ID tertentu tidak ditemukan, kembalikan respons not found
        return ResponseEntity.notFound().build();
    }
}

    private String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}