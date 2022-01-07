package com.experimentality.clothesstorelatamapi.visit.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VisitDto {
    private Long id;
    private Long productId;
    private Long nroVisit;
    private Instant lastViewDate;
}
