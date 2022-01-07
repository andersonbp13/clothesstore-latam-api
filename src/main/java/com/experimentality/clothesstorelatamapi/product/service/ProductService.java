package com.experimentality.clothesstorelatamapi.product.service;

import com.experimentality.clothesstorelatamapi.Util.Enum.Country;
import com.experimentality.clothesstorelatamapi.external.firebase.service.FirebaseService;
import com.experimentality.clothesstorelatamapi.product.dto.ProductDto;
import com.experimentality.clothesstorelatamapi.product.model.Product;
import com.experimentality.clothesstorelatamapi.product.repository.ProductRepository;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    ProductRepository productRepository;
    FirebaseService firebaseService;

    @Autowired
    public ProductService(ProductRepository productRepository, FirebaseService firebaseService) {
        this.productRepository = productRepository;
        this.firebaseService = firebaseService;
    }

    public boolean createProduct(ProductDto productDto, MultipartFile frontImage, MultipartFile backImage) {
        Preconditions.checkArgument(StringUtils.isNotBlank(productDto.getName()), "Nombre de producto inválido");
        Preconditions.checkArgument(productDto.getPrice() > 0, "Precio de producto inválido");
        Preconditions.checkArgument(productDto.getDiscount() != null, "Descuento de producto nulo");
        Preconditions.checkArgument(StringUtils.isNotBlank(productDto.getSection()), "Sección de producto inválido");
        Preconditions.checkArgument(StringUtils.isNotBlank(productDto.getDescription()), "Descripción de producto inválido");
        Preconditions.checkArgument(productDto.getStock() != null, "Stock de producto inválido");
        Preconditions.checkArgument(productDto.getCountry() != null, "País de venta de producto inválido");

        boolean response = true;
        try {
            if (productDto.getCountry() == Country.COLOMBIA || productDto.getCountry() == Country.MEXICO) {
                Preconditions.checkArgument(productDto.getDiscount() <= 50, "El descuento es muy alto para este país");
            } else {
                Preconditions.checkArgument(productDto.getDiscount() <= 30, "El descuento es muy alto para este país");
            }

            String[] images = new String[2];

            images[0] = firebaseService.uploadFile(frontImage);
            images[1] = firebaseService.uploadFile(backImage);

            productDto.setImages(images);
            Product product = convertDtoToDao(productDto);
            productRepository.save(product);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response = false;
        }
        return response;
    }

    public List<ProductDto> getProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<ProductDto>();
        products.forEach((product) -> {
            productDtos.add(convertDaoToDto(product));
        });
        return productDtos;
    }

    public List<ProductDto> getProductsWithLimit(Long offSet, Integer limit) {
        List<Product> products = productRepository.findAll().stream()
                .skip(offSet != null ? offSet : 0)
                .limit(limit)
                .collect(Collectors.toList());
        List<ProductDto> productDtos = new ArrayList<ProductDto>();
        products.forEach((product) -> {
            productDtos.add(convertDaoToDto(product));
        });
        return productDtos;
    }

    public ProductDto getProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        Preconditions.checkArgument(productOptional.isPresent(), "El producto no se encontró");
        Product product = productOptional.get();
        return convertDaoToDto(product);
    }

    public boolean deleteProduct(Long id) {
        boolean response = true;
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            response = false;
        }
        return response;
    }

    public boolean updateProduct(ProductDto productDto) {
        boolean response = true;
        try {
            Product product = convertDtoToDao(productDto);
            productRepository.save(product);

        } catch (Exception e) {
            response = false;
        }
        return response;
    }

    private Product convertDtoToDao(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .discount(productDto.getDiscount())
                .section(productDto.getSection())
                .description(productDto.getDescription())
                .stock(productDto.getStock())
                .country(productDto.getCountry())
                .frontImage(productDto.getImages()[0])
                .backImage(productDto.getImages()[1])
                .build();

        if (productDto.getId() != null) {
            product.setId(productDto.getId());
        }
        return product;
    }

    private ProductDto convertDaoToDto(Product product) {
        String[] images = {product.getFrontImage(), product.getBackImage()};

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .section(product.getSection())
                .description(product.getDescription())
                .stock(product.getStock())
                .country(product.getCountry())
                .images(images)
                .build();
    }

}
