package com.example.ssccwebbe.domain.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ssccwebbe.domain.admin.dto.CodingExpDistributionResponse;
import com.example.ssccwebbe.domain.admin.dto.GenderDistributionResponse;
import com.example.ssccwebbe.domain.applyform.entity.ApplyFormStatus;
import com.example.ssccwebbe.domain.applyform.entity.CodingExp;
import com.example.ssccwebbe.domain.applyform.repository.ApplyFormRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplyFormAdminService {

    private final ApplyFormRepository applyFormRepository;

    public GenderDistributionResponse getGenderDistribution() {
        long maleCount = applyFormRepository.countByGenderAndStatusNot("남", ApplyFormStatus.DELETED);
        long femaleCount = applyFormRepository.countByGenderAndStatusNot("여", ApplyFormStatus.DELETED);
        long totalCount = applyFormRepository.countByStatusNot(ApplyFormStatus.DELETED);

        double malePercentage = totalCount == 0 ? 0 : (double) maleCount / totalCount * 100;
        double femalePercentage = totalCount == 0 ? 0 : (double) femaleCount / totalCount * 100;

        return GenderDistributionResponse.builder()
                .maleCount(maleCount)
                .femaleCount(femaleCount)
                .malePercentage(Math.round(malePercentage * 10) / 10.0)
                .femalePercentage(Math.round(femalePercentage * 10) / 10.0)
                .build();
    }

    public CodingExpDistributionResponse getCodingExpDistribution() {
        long totalCount = applyFormRepository.countByStatusNot(ApplyFormStatus.DELETED);

        List<CodingExpDistributionResponse.ExpLevelDistribution> distributions = java.util.Arrays
                .stream(CodingExp.values())
                .map(exp -> {
                    long count = applyFormRepository.countByCodingExpAndStatusNot(exp, ApplyFormStatus.DELETED);
                    double percentage = totalCount == 0 ? 0 : (double) count / totalCount * 100;
                    return CodingExpDistributionResponse.ExpLevelDistribution.builder()
                            .level(exp.name())
                            .description(exp.getDescription())
                            .count(count)
                            .percentage(Math.round(percentage * 10) / 10.0)
                            .build();
                })
                .toList();

        return CodingExpDistributionResponse.builder()
                .totalCount(totalCount)
                .distributions(distributions)
                .build();
    }
}
