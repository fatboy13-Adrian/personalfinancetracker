package com.app.personalfinanacetracker.Budget;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.personalfinanacetracker.calculator.Calculator;
import com.app.personalfinanacetracker.dto.AverageDTO;
import com.app.personalfinanacetracker.dto.BudgetDTO;
import com.app.personalfinanacetracker.dto.SummaryDTO;
import com.app.personalfinanacetracker.entity.Budget;
import com.app.personalfinanacetracker.exception.budget.MonthAlreadyExistsException;
import com.app.personalfinanacetracker.exception.budget.MonthNotFoundException;
import com.app.personalfinanacetracker.mapper.BudgetMapper;
import com.app.personalfinanacetracker.repository.BudgetRepository;
import com.app.personalfinanacetracker.service.BudgetService;

@ExtendWith(MockitoExtension.class) 
public class BudgetServiceTest {
    //Service under test
    @InjectMocks    
    private BudgetService budgetSvc;

    //Mocked repository layers
    @Mock   
    private BudgetRepository repository;
    @Mock
    private Calculator calculator;
    @Mock
    private BudgetMapper mapper;

    private Budget b;
    private BudgetDTO dto;

    @BeforeEach
    public void setUp() {
        //Intialize DTO with sample data
        dto = new BudgetDTO();
        dto.setMonth(YearMonth.of(2026, 4));
        dto.setIncome(BigDecimal.valueOf(5000.0));
        dto.setRetirement(BigDecimal.valueOf(500.0));
        dto.setInsurance(BigDecimal.valueOf(155.0));
        dto.setTax(BigDecimal.valueOf(10.02));
        dto.setMortgage(BigDecimal.valueOf(1000.0));
        dto.setAllowancesForParents(BigDecimal.valueOf(800.0));
        dto.setDebt(BigDecimal.valueOf(650.0));
        dto.setTransport(BigDecimal.valueOf(122.0));
        dto.setUtilities(BigDecimal.valueOf(45.0));
        dto.setMobilePhone(BigDecimal.valueOf(15.0));
        dto.setInternet(BigDecimal.valueOf(90.46));
        dto.setFood(BigDecimal.valueOf(650.0));
        dto.setGroceries(BigDecimal.valueOf(100.0));
        dto.setHaircut(BigDecimal.valueOf(15.0));
        dto.setMedical(BigDecimal.valueOf(12.0));
        dto.setMisc(BigDecimal.valueOf(200.0));

        //Initialize budget matching DTO values
        b = new Budget();
        b.setId(1L);
        b.setMonth(dto.getMonth());
        b.setIncome(dto.getIncome());
        b.setRetirement(dto.getRetirement());
        b.setInsurance(dto.getInsurance());
        b.setTax(dto.getTax());
        b.setMortgage(dto.getMortgage());
        b.setAllowancesForParents(dto.getAllowancesForParents());
        b.setDebt(dto.getDebt());
        b.setTransport(dto.getTransport());
        b.setUtilities(dto.getUtilities());
        b.setMobilePhone(dto.getMobilePhone());
        b.setInternet(dto.getInternet());
        b.setFood(dto.getFood());
        b.setGroceries(dto.getGroceries());
        b.setHaircut(dto.getHaircut());
        b.setMedical(dto.getMedical());
        b.setMisc(dto.getMisc());
    }

    @Test
    public void testCreateBudget() {
        //Arrange: month does not exist yet
        when(repository.findByMonth(dto.getMonth()))
        .thenReturn(Optional.empty());
        when(repository.save(any(Budget.class)))
        .thenReturn(b);
        when(mapper.toDTO(any(Budget.class)))
        .thenReturn(dto);

        //Act: create budget
        BudgetDTO result = budgetSvc.createBudget(dto);

        //Assert: verify creation success
        assertNotNull(result);
        assertEquals(dto.getMonth(), result.getMonth());

        //Verify save operation occurred once
        verify(repository, times(1)).save(any(Budget.class));
        verify(calculator).calculateMonthlyExpenses(any(Budget.class));
        verify(calculator).calculateOverSpent(any(Budget.class));
    }

    @Test
    void testCreateBudgetMonthAlreadyExists() {
        //Arrange: month already exists in DB
        YearMonth month = dto.getMonth();

        when(repository.findByMonth(month))
        .thenReturn(Optional.of(b));

        //Act & Assert: exception expected
        MonthAlreadyExistsException ex =
        assertThrows(MonthAlreadyExistsException.class,
        () -> budgetSvc.createBudget(dto));
        assertNotNull(ex);

        //Ensure no save happens
        verify(repository, never()).save(any(Budget.class));
    }

    @Test
    void testRetreiveBudgets() {
        //Arrange: budget exists
        when(repository.findAll()).thenReturn(List.of(b));
        when(mapper.toDTO(any(Budget.class)))
        .thenReturn(dto);

        //Act
        List<BudgetDTO> result = budgetSvc.retrieveBudgets();

        //Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(b.getMonth(), dto.getMonth());
        assertEquals(b.getIncome(), dto.getIncome());

        //Verify
        verify(repository).findAll();
        verify(mapper).toDTO(b);
    }

    @Test
    void testRetrieveBudgetsEmpty() {
        //Arrange: no budgets exist
        when(repository.findAll()).thenReturn(List.of());

        //Act
        List<BudgetDTO> result = budgetSvc.retrieveBudgets();

        //Assert empty list
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(repository, times(1)).findAll();
    }

    @Test
    void testRetrieveBudgetByMonth() {
        //Arrange
        YearMonth month = YearMonth.of(2026, 4);
        when(repository.findByMonth(month))
        .thenReturn(Optional.of(b));
        when(mapper.toDTO(any(Budget.class)))
        .thenReturn(dto);

        //Act
        BudgetDTO result = budgetSvc.retrieveBudgetByMonth(month);

        //Assert
        assertNotNull(result);
        assertEquals(dto.getMonth(), result.getMonth());
        verify(repository, times(1)).findByMonth(month);
    }

    @Test
    void testRetrieveBudgetByMonthNotFound() {
        //Arrange: no record found
        YearMonth month = YearMonth.of(2026, 4);
        when(repository.findByMonth(month)).thenReturn(Optional.empty());

        //Act & Assert
        MonthNotFoundException exception =
        assertThrows(MonthNotFoundException.class,
        () -> budgetSvc.retrieveBudgetByMonth(month));
        assertNotNull(exception);
        verify(repository, times(1)).findByMonth(month);
    }

    @Test
    void testRetrieveBudgetsByYear() {
        when(repository.findAll())
        .thenReturn(List.of(b));
        List<SummaryDTO> result = budgetSvc.retrieveBudgetsByYear();
        verify(repository, times(1)).findAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testRetrieveBudgetsByYearEmpty() {
        when(repository.findAll())
        .thenReturn(List.of());
        List<SummaryDTO> result = budgetSvc.retrieveBudgetsByYear();
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testRetrieveAverageBudget() {
        when(repository.findAll())
        .thenReturn(List.of(b));
        List<AverageDTO> result = budgetSvc.retrieveAverageBudget();
        verify(repository, times(1)).findAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testRetrieveBudgetByYearEmpty() {
        when(repository.findAll())
        .thenReturn(List.of());
        List<AverageDTO> result = budgetSvc.retrieveAverageBudget();
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testUpdateBudgetByMonth() {
        YearMonth month = YearMonth.of(2026, 4);

        //Arrange: existing budget found
        when(repository.findByMonth(month))
        .thenReturn(Optional.of(b));
        when(repository.save(any(Budget.class)))
        .thenReturn(b);
        when(mapper.toDTO(any(Budget.class)))
        .thenReturn(dto);

        //Act
        BudgetDTO result = budgetSvc.updateBudgetByMonth(month, dto);

        //Assert
        assertNotNull(result);
        assertEquals(dto.getMonth(), result.getMonth());
        verify(repository, times(1)).findByMonth(month);
        verify(repository, times(1)).save(any(Budget.class));
    }

    @Test
    void testUpdateBudgetByMonthNotFound() {
        //Arrange: no budget found
        YearMonth month = YearMonth.of(2026, 4);
        when(repository.findByMonth(month))
        .thenReturn(Optional.empty());

        //Act & Assert
        MonthNotFoundException exception = 
        assertThrows(MonthNotFoundException.class,
        () -> budgetSvc.updateBudgetByMonth(month, dto));

        assertNotNull(exception);
        verify(repository, times(1)).findByMonth(month);
        verify(repository, never()).save(any(Budget.class));
    }
}