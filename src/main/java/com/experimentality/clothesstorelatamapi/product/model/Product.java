package com.experimentality.clothesstorelatamapi.product.model;

import com.experimentality.clothesstorelatamapi.Util.Enum.Country;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Double discount;
    private String section;
    private String description;
    private Long stock;
    @Enumerated(EnumType.STRING)
    private Country country;
    private String frontImage;
    private String backImage;
}
