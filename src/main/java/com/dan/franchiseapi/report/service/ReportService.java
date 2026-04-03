package com.dan.franchiseapi.report.service;

import com.dan.franchiseapi.franchise.service.FranchiseService;
import com.dan.franchiseapi.report.dto.TopStockProductResponse;
import com.dan.franchiseapi.report.repository.ReportRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final FranchiseService franchiseService;

    public ReportService(ReportRepository reportRepository, FranchiseService franchiseService) {
        this.reportRepository = reportRepository;
        this.franchiseService = franchiseService;
    }

    public Flux<TopStockProductResponse> getTopStockProductsByFranchiseId(Long franchiseId) {
        return franchiseService.getFranchiseById(franchiseId)
                .thenMany(reportRepository.findTopStockProductsByFranchiseId(franchiseId));
    }

}
