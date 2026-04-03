package com.dan.franchiseapi.report.controller;

import com.dan.franchiseapi.report.dto.TopStockProductResponse;
import com.dan.franchiseapi.report.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/api/franchises/{franchiseId}/top-stock-products")
    public Flux<TopStockProductResponse> getTopStockProductsByFranchiseId(@PathVariable Long franchiseId) {
        return reportService.getTopStockProductsByFranchiseId(franchiseId);
    }

}
