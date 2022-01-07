package com.experimentality.clothesstorelatamapi.product.controller;

import com.experimentality.clothesstorelatamapi.product.dto.ProductDto;
import com.experimentality.clothesstorelatamapi.product.service.ProductService;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/product")
public class ProductController {
    ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createProduct(@RequestPart ProductDto productDto,
                                           @RequestPart("frontImage") MultipartFile frontImage,
                                           @RequestPart("backImage") MultipartFile backImage) {
        try {
            Preconditions.checkArgument(frontImage.getBytes().length != 0, "No se encontró imagen para subir");
            Preconditions.checkArgument(backImage.getBytes().length != 0, "No se encontró imagen para subir");
            Preconditions.checkArgument(productDto != null, "Información de producto nula");

        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean response = productService.createProduct(productDto, frontImage, backImage);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/find")
    public ResponseEntity<?> getProduct(@RequestParam Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProduct(@RequestParam Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(productDto));
    }


}

