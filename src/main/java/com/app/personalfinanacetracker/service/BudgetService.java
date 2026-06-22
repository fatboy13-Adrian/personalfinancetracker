package com.app.personalfinanacetracker.service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.personalfinanacetracker.calculator.BudgetCalculator;
import com.app.personalfinanacetracker.dto.BudgetDTO;
import com.app.personalfinanacetracker.entity.Budget;
import com.app.personalfinanacetracker.exception.budget.MonthAlreadyExistsException;
import com.app.personalfinanacetracker.exception.budget.MonthNotFoundException;
import com.app.personalfinanacetracker.mapper.BudgetMapper;
import com.app.personalfinanacetracker.repository.BudgetRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetMapper mapper;
    private final BudgetRepository repository;
    private final BudgetCalculator calculator;

    @Transactional
    public BudgetDTO addBudget(BudgetDTO dto) {
        //Checks if month already exists
        if (repository.findByMonth(dto.getMonth()).isPresent()) 
            throw new MonthAlreadyExistsException(dto.getMonth());

        //Create new budget
        Budget b = new Budget();

        //Copy user input fields
        addBudgetFields(b, dto);

        //Auto recalculate expense breakdown snapshot
        calculator.calculateMonthlyExpenses(b);

        //Persist to DB
        Budget saved = repository.save(b);

        //Convert budget to DTO for response
        return mapper.toDTO(saved);
    }

    public List <BudgetDTO> showAllBudgets() {
        return repository.findAll()
        .stream().map(mapper::toDTO)
        .toList();
    }

    public BudgetDTO findBudgetByMonth(YearMonth month) {
        //Fetch budget or throw exception
        Budget b = findBudget(month);

        //Convert & return DTO
        return mapper.toDTO(b);
    }

    @Transactional
    public BudgetDTO updateBudgetByMonth(YearMonth month, BudgetDTO dto) {
        Budget existing = findBudget(month);

        //Prevent duplicate month conflict when changing month
        if (!month.equals(dto.getMonth()) && repository.findByMonth(dto.getMonth()).isPresent()) 
            throw new MonthAlreadyExistsException(dto.getMonth());

        addBudgetFields(existing, dto);
        calculator.calculateMonthlyExpenses(existing);
        Budget updated = repository.save(existing);
        return mapper.toDTO(updated);
    }

    private void addBudgetFields(Budget b, BudgetDTO dto) {
        //Set month, income & retirement
        b.setMonth(dto.getMonth());
        b.setIncome(dto.getIncome());
        b.setRetirement(dto.getRetirement());
    }

    private Budget findBudget(YearMonth month) {
        //Fetch budget or throw exception if not found
        return repository.findByMonth(month)
        .orElseThrow(() -> new MonthNotFoundException(month));
    }

    @Transactional
    public void refreshBudget(LocalDate date) {
        //Do not allow update if date is future date
        if (date.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Future dates not allowed");

        //Get current date
        YearMonth month = YearMonth.from(date);

        //Find by month or return null if not found
        Budget b = repository.findByMonth(month).orElse(null);

        //Exit sliently if no records
        if (b == null) return;

        //Auto recalculate expense breakdown snapshot
        calculator.calculateMonthlyExpenses(b);

        //Save updated record
        repository.save(b);
    }

    public List <Budget> getYearlyBudgets(Year year) {
        //Find by year or throw exception if not found
		if (year == null) 
            throw new IllegalArgumentException("Year cannot be null");
        
        //Jan to Dec
		YearMonth start = YearMonth.of(year.getValue(), 1);
		YearMonth end = YearMonth.of(year.getValue(), 12);

        //Fetch year range
		return repository.findByMonthBetween(start, end);
	}
}