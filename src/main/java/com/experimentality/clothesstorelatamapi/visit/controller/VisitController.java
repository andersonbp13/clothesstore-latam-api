package com.experimentality.clothesstorelatamapi.visit.controller;

import com.experimentality.clothesstorelatamapi.visit.dto.VisitDto;
import com.experimentality.clothesstorelatamapi.visit.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/visit")
public class VisitController {
    VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createVisit(@RequestBody VisitDto visitDto) {
        boolean response = visitService.createVisitWithDto(visitDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getVisits() {
        return ResponseEntity.ok(visitService.getVisits());
    }

    @GetMapping("/find")
    public ResponseEntity<?> getVisits(@RequestParam Long id) {
        return ResponseEntity.ok(visitService.getVisit(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteVisit(@RequestParam Long id) {
        return ResponseEntity.ok(visitService.deleteVisit(id));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateVisit(@RequestBody VisitDto visitDto) {
        return ResponseEntity.ok(visitService.updateVisit(visitDto));
    }

    @GetMapping("/most-wanted")
    public ResponseEntity<?> lookForMostWanted(@RequestParam(value = "offset", required = false) Long offSet,
                                               @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(visitService.lookForMostWanted(offSet, limit));
    }

    @GetMapping("/add-visit")
    public ResponseEntity<?> addVisit(@RequestParam(value = "productId") Long productId) {
        visitService.addVisit(productId);
        return ResponseEntity.ok().build();
    }

}
