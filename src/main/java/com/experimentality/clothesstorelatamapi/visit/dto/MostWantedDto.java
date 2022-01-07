package com.experimentality.clothesstorelatamapi.visit.dto;

import com.experimentality.clothesstorelatamapi.product.dto.ProductDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MostWantedDto {
    private Long nextOffset;
    private List<ProductDto> productDtos;
}
