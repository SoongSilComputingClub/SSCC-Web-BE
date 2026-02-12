package com.example.ssccwebbe.domain.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ssccwebbe.domain.admin.dto.CodingExpDistributionResponse;
import com.example.ssccwebbe.domain.admin.dto.GenderDistributionResponse;
import com.example.ssccwebbe.domain.admin.service.ApplyFormAdminService;
import com.example.ssccwebbe.global.apipayload.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/apply-forms")
public class ApplyFormAdminController {

    private final ApplyFormAdminService applyFormAdminService;

    @GetMapping("/gender-distribution")
    public ApiResponse<GenderDistributionResponse> getGenderDistribution() {
        return ApiResponse.success(applyFormAdminService.getGenderDistribution());
    }

    @GetMapping("/coding-exp-distribution")
    public ApiResponse<CodingExpDistributionResponse> getCodingExpDistribution() {
        return ApiResponse.success(applyFormAdminService.getCodingExpDistribution());
    }
}
