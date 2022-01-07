package com.experimentality.clothesstorelatamapi.visit.repository;

import com.experimentality.clothesstorelatamapi.visit.model.Visit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends CrudRepository<Visit, Long> {
    List<Visit> findAll();

    Optional<Visit> findByProductId(Long productId);
}
