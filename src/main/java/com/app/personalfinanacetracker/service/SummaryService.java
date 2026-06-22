package com.app.personalfinanacetracker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.personalfinanacetracker.calculator.SummaryCalculator;
import com.app.personalfinanacetracker.dto.SummaryDTO;
import com.app.personalfinanacetracker.entity.Budget;
import com.app.personalfinanacetracker.repository.BudgetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SummaryService {
    private final BudgetRepository repository;
    private final SummaryCalculator calculator;

    public List <SummaryDTO> retrieveSummaries() {
        //Retrieve all budget records
        List<Budget> budgets = repository.findAll();

        //Store yearly summaries
        List<SummaryDTO> summaries = new ArrayList<>();

        //Iterate through all records
        for (Budget b : budgets) {
            int year = b.getMonth().getYear();

            //Find existing summary for year
            SummaryDTO existing = null;

            for (SummaryDTO dto : summaries) {
                if (dto.getYear() == year) {
                    existing = dto;
                    break;
                }
            }

            //If not found, create new DTO
            if (existing == null) {
                existing = new SummaryDTO();
                existing.setYear(year);
                calculator.initializeSummary(existing);
                summaries.add(existing);
            }

            //Accumulate values
            calculator.calculateYearlyBudget(existing, b);
        }

        //Return summaries
        return summaries;
    }
}