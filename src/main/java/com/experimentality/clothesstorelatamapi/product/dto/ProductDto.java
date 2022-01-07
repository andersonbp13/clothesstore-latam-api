package com.experimentality.clothesstorelatamapi.product.dto;

import com.experimentality.clothesstorelatamapi.Util.Enum.Country;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDto {
    private Long id;
    private String name;
    private Double price;
    private Double discount;
    private String section;
    private String description;
    private Long stock;
    private Country country;
    private String[] images;

}
