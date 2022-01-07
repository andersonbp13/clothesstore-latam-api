package com.experimentality.clothesstorelatamapi.visit.service;

import com.experimentality.clothesstorelatamapi.product.dto.ProductDto;
import com.experimentality.clothesstorelatamapi.product.service.ProductService;
import com.experimentality.clothesstorelatamapi.visit.dto.MostWantedDto;
import com.experimentality.clothesstorelatamapi.visit.dto.VisitDto;
import com.experimentality.clothesstorelatamapi.visit.model.Visit;
import com.experimentality.clothesstorelatamapi.visit.repository.VisitRepository;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitService {
    VisitRepository visitRepository;
    ProductService productService;

    @Autowired
    public VisitService(VisitRepository visitRepository, ProductService productService) {
        this.visitRepository = visitRepository;
        this.productService = productService;
    }

    public boolean createVisitWithDto(VisitDto visitDto) {
        boolean response = true;
        try {
            Visit visit = convertDtoToDao(visitDto);
            visitRepository.save(visit);
        } catch (Exception e) {
            response = false;
        }
        return response;
    }

    public boolean createVisit(Long productId) {
        boolean response = true;
        try {
            Visit visit = Visit.builder()
                    .productId(productId)
                    .nroVisit(1L)
                    .lastViewDate(Instant.now())
                    .build();
            visitRepository.save(visit);
        } catch (Exception e) {
            response = false;
        }
        return response;
    }

    public List<VisitDto> getVisits() {
        List<Visit> visits = visitRepository.findAll();
        List<VisitDto> visitDtos = new ArrayList<VisitDto>();
        visits.forEach((visit) -> {
            visitDtos.add(convertDaoToDto(visit));
        });
        return visitDtos;
    }

    public VisitDto getVisit(Long id) {
        Optional<Visit> visitOptional = visitRepository.findById(id);
        Preconditions.checkArgument(visitOptional.isPresent(), "La visita no se encontró");
        Visit visit = visitOptional.get();
        return convertDaoToDto(visit);
    }

    public boolean deleteVisit(Long id) {
        boolean response = true;
        try {
            visitRepository.deleteById(id);
        } catch (Exception e) {
            response = false;
        }
        return response;
    }

    public boolean updateVisit(VisitDto visitDto) {
        boolean response = true;
        try {
            Visit visit = convertDtoToDao(visitDto);
            visitRepository.save(visit);

        } catch (Exception e) {
            response = false;
        }
        return response;
    }

    public MostWantedDto lookForMostWanted(Long offSet, Integer limit) {
        List<Visit> visits = visitRepository.findAll().stream()
                .filter(visit -> visit.getNroVisit() > 0)
                .sorted(Comparator.comparing(Visit::getNroVisit).reversed())
                .skip(offSet != null ? offSet : 0)
                .limit(limit)
                .collect(Collectors.toList());

        List<ProductDto> productDtos = new ArrayList<ProductDto>();

        if (CollectionUtils.isEmpty(visits) && offSet != null && offSet == 0) {
            productDtos = productService.getProductsWithLimit(offSet, limit);
        } else {
            List<ProductDto> finalProductDtos = productDtos;
            visits.forEach(visit -> finalProductDtos.add(productService.getProduct(visit.getProductId())));
            productDtos = finalProductDtos;
        }

        return MostWantedDto.builder()
                .nextOffset(CollectionUtils.isEmpty(productDtos) ? 0 : (offSet != null ? offSet : 0) + limit)
                .productDtos(productDtos)
                .build();
    }

    public void addVisit(Long id) {
        Optional<Visit> visitOptional = visitRepository.findByProductId(id);
        if (visitOptional.isPresent()) {
            Visit visit = visitOptional.get();
            visit.setNroVisit(visit.getNroVisit() + 1);
            visitRepository.save(visit);
        } else {
            createVisit(id);
        }
    }

    private Visit convertDtoToDao(VisitDto visitDto) {
        Visit visit = Visit.builder()
                .productId(visitDto.getProductId())
                .nroVisit(visitDto.getNroVisit())
                .lastViewDate(Instant.now())
                .build();

        if (visitDto.getId() != null) {
            visit.setId(visitDto.getId());
        }
        return visit;
    }

    private VisitDto convertDaoToDto(Visit visit) {
        return VisitDto.builder()
                .id(visit.getId())
                .productId(visit.getProductId())
                .nroVisit(visit.getNroVisit())
                .lastViewDate(visit.getLastViewDate())
                .build();
    }

    public void resetVisits() {
        visitRepository.deleteAll();
    }
}
