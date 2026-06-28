package com.app.personalfinanacetracker.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.personalfinanacetracker.calculator.Calculator;
import com.app.personalfinanacetracker.dto.AverageDTO;
import com.app.personalfinanacetracker.dto.BudgetDTO;
import com.app.personalfinanacetracker.dto.SummaryDTO;
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
    private final Calculator calculator;

    @Transactional
    public BudgetDTO createBudget(BudgetDTO dto) {
        //Checks if month already exists
        if (repository.findByMonth(dto.getMonth()).isPresent()) 
            throw new MonthAlreadyExistsException(dto.getMonth());

        //Create new budget
        Budget b = new Budget();

        //Copy user input fields
        createOrUpdateBudgetFields(b, dto);

        //Auto recalculate expense breakdown snapshot
        calculator.calculateMonthlyExpenses(b);

        //Persist to DB
        Budget saved = repository.save(b);

        //Convert budget to DTO for response
        return mapper.toDTO(saved);
    }

    public List <BudgetDTO> retrieveBudgets() {
        return repository.findAll()
        .stream().map(mapper::toDTO)
        .toList();
    }

    public BudgetDTO retrieveBudgetByMonth(YearMonth month) {
        //Fetch budget or throw exception
        Budget b = findMonth(month);

        //Convert & return DTO
        return mapper.toDTO(b);
    }

    public List <SummaryDTO> retrieveBudgetsByYear() {
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
                summaries.add(existing);
            }

            //Accumulate values
            calculator.calculateYearlyBudget(existing, b);
        }

        //Return summaries
        return summaries;
    }

    public List <AverageDTO> retrieveAverageBudget() {
        List<Budget> budgets = repository.findAll();
        List <AverageDTO> average = new ArrayList<>();
        if (budgets == null || budgets.isEmpty()) return average;
        
        AverageDTO totals = new AverageDTO();
        initializeAverageFieldsToZero(totals);

        for (Budget b : budgets) {
            totals.setIncome(totals.getIncome().add(b.getIncome()));
            totals.setRetirement(totals.getRetirement().add(b.getRetirement()));
            totals.setInsurance(totals.getInsurance().add(b.getInsurance()));
            totals.setMobilePhone(totals.getMobilePhone().add(b.getMobilePhone()));
            totals.setInternet(totals.getInternet().add(b.getInternet()));
            totals.setUtilities(totals.getUtilities().add(b.getUtilities()));
            totals.setTax(totals.getTax().add(b.getTax()));
            totals.setMortgage(totals.getMortgage().add(b.getMortgage()));
            totals.setDebt(totals.getDebt().add(b.getDebt()));
            totals.setAllowancesForParents(totals.getAllowancesForParents().add(b.getAllowancesForParents()));
            totals.setTransport(totals.getTransport().add(b.getTransport()));
            totals.setFood(totals.getFood().add(b.getFood()));
            totals.setGroceries(totals.getGroceries().add(b.getGroceries()));
            totals.setHaircut(totals.getHaircut().add(b.getHaircut()));
            totals.setMedical(totals.getMedical().add(b.getMedical()));
            totals.setMisc(totals.getMisc().add(b.getMisc()));
            totals.setSavings(totals.getSavings().add(b.getSavings()));
        }

        calculator.calculateAverageBudget(totals);
        average.add(totals);
        return average;
    }

    @Transactional
    public BudgetDTO updateBudgetByMonth(YearMonth month, BudgetDTO dto) {
        Budget existing = findMonth(month);

        //Prevent duplicate month conflict when changing month
        if (!month.equals(dto.getMonth()) && repository.findByMonth(dto.getMonth()).isPresent()) 
            throw new MonthAlreadyExistsException(dto.getMonth());

        createOrUpdateBudgetFields(existing, dto);
        calculator.calculateMonthlyExpenses(existing);
        Budget updated = repository.save(existing);
        return mapper.toDTO(updated);
    }

    private void createOrUpdateBudgetFields(Budget b, BudgetDTO dto) {
        //Set month, income & retirement
        b.setMonth(dto.getMonth());
        b.setIncome(dto.getIncome());
        b.setRetirement(dto.getRetirement());
    }

    private Budget findMonth(YearMonth month) {
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

    private void initializeAverageFieldsToZero(AverageDTO dto) {
        dto.setIncome(BigDecimal.ZERO);
        dto.setRetirement(BigDecimal.ZERO);
        dto.setInsurance(BigDecimal.ZERO);
        dto.setMobilePhone(BigDecimal.ZERO);
        dto.setInternet(BigDecimal.ZERO);
        dto.setUtilities(BigDecimal.ZERO);
        dto.setTax(BigDecimal.ZERO);
        dto.setMortgage(BigDecimal.ZERO);
        dto.setDebt(BigDecimal.ZERO);
        dto.setAllowancesForParents(BigDecimal.ZERO);
        dto.setTransport(BigDecimal.ZERO);
        dto.setFood(BigDecimal.ZERO);
        dto.setGroceries(BigDecimal.ZERO);
        dto.setHaircut(BigDecimal.ZERO);
        dto.setMedical(BigDecimal.ZERO);
        dto.setMisc(BigDecimal.ZERO);
        dto.setSavings(BigDecimal.ZERO);
    }
}