package com.app.personalfinanacetracker.Expense;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.app.personalfinanacetracker.dto.ExpenseDTO;
import com.app.personalfinanacetracker.entity.Expense;
import com.app.personalfinanacetracker.exception.expense.DateAlreadyExistsException;
import com.app.personalfinanacetracker.exception.expense.DateNotFoundException;
import com.app.personalfinanacetracker.mapper.ExpenseMapper;
import com.app.personalfinanacetracker.repository.ExpenseRepository;
import com.app.personalfinanacetracker.service.BudgetService;
import com.app.personalfinanacetracker.service.ExpenseService;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {
    //Service under test
    @InjectMocks    
    private ExpenseService expSvc;

    //Mock mapper, service & repository class to simulate DB operations
    @Mock   
    private ExpenseMapper mapper;
    @Mock
    private BudgetService budgetSvc;
    @Mock
    private ExpenseRepository repository;

    private LocalDate date;
    private Expense e;
    private ExpenseDTO dto;

    @BeforeEach
    public void setUp() {
        date = LocalDate.of(2026, 1, 1);

        //DTO used as input for service methods
        dto = new ExpenseDTO();
        dto.setDate(date);
        dto.setBreakfast(BigDecimal.valueOf(5.1));
        dto.setLunch(BigDecimal.valueOf(6.4));
        dto.setDinner(BigDecimal.valueOf(7.1));
        dto.setGroceries(BigDecimal.valueOf(9.99));
        dto.setMedical(BigDecimal.valueOf(22.34));

        //Expense representing DB model
        e = new Expense();
        e.setDate(date);
        e.setBreakfast(BigDecimal.valueOf(5.1));
        e.setLunch(BigDecimal.valueOf(6.4));
        e.setDinner(BigDecimal.valueOf(7.1));
        e.setGroceries(BigDecimal.valueOf(9.99));
        e.setMedical(BigDecimal.valueOf(22.34));
    }

    @Test
    void testCreateExpense() {
        //Mock: no existing expense for this date
        when(repository.findByDate(date)).thenReturn(Optional.empty());

        //Mock the mapper conversions so they don't return null
        when(mapper.toEntity(any(ExpenseDTO.class))).thenReturn(e);
        when(mapper.toDTO(any(Expense.class))).thenReturn(dto);

        //Mock: save returns the entity
        when(repository.save(any(Expense.class))).thenReturn(e);

        //Call service method
        ExpenseDTO result = expSvc.createExpense(dto);

        //Verify result mapping
        assertNotNull(result);
        assertEquals(dto.getDate(), result.getDate());
        assertEquals(dto.getBreakfast(), result.getBreakfast());

        //Verify save was called once
        verify(repository, times(1)).save(any(Expense.class));
        verify(budgetSvc).refreshBudget(dto.getDate());
    }

    @Test
    void testCreateExpenseDateAlreadyExists() {
        //Mock: expense already exists for this date
        when(repository.findByDate(date)).thenReturn(Optional.of(e));

        //Expect exception when creating duplicate expense
        DateAlreadyExistsException exception =
        assertThrows(DateAlreadyExistsException.class,
        () -> expSvc.createExpense(dto));
        assertNotNull(exception);

        //Ensure save is never called
        verify(repository, never()).save(any(Expense.class));
    }

    @Test
    void testRetrieveExpenses() {
        //Mock: return list with one expense
        when(repository.findAll()).thenReturn(List.of(e));
        when(mapper.toDTO(any(Expense.class))).thenReturn(dto);
        List<ExpenseDTO> result = expSvc.retrieveExpenses();
        assertNotNull(result);
        assertEquals(1, result.size());

        //Validate mapping of values
        assertEquals(dto.getBreakfast(), result.get(0).getBreakfast());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testRetrieveExpensesEmpty() {
        //Mock: empty database result
        when(repository.findAll()).thenReturn(List.of());
        List<ExpenseDTO> result = expSvc.retrieveExpenses();
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testRetrieveExpenseByDate() {
        //Mock: expense exists for given date
        when(repository.findByDate(date)).thenReturn(Optional.of(e));
        when(mapper.toDTO(e)).thenReturn(dto);
        ExpenseDTO result = expSvc.retrieveExpenseByDate(date);
        assertNotNull(result);
        assertEquals(dto.getDate(), result.getDate());
        verify(repository, times(1)).findByDate(date);
    }

    @Test
    void testRetrieveExpenseByDateNotFound() {
        //Mock: no expense found
        when(repository.findByDate(date)).thenReturn(Optional.empty());

        //Expect exception when not found
        DateNotFoundException exception =
        assertThrows(DateNotFoundException.class,
        () -> expSvc.retrieveExpenseByDate(date));
        assertNotNull(exception);
        verify(repository, times(1)).findByDate(date);
    }

    @Test
    void testUpdateExpenseByDate() {
        //Mock: existing record found
        when(repository.findByDate(date)).thenReturn(Optional.of(e));

        //Mock: save operation
        when(repository.save(any(Expense.class))).thenReturn(e);

        //Modify input DTO values
        dto.setBreakfast(BigDecimal.valueOf(10.0));
        dto.setMedical(BigDecimal.valueOf(5.0));
        when(mapper.toDTO(any(Expense.class))).thenReturn(dto);
        ExpenseDTO result = expSvc.updateExpenseByDate(date, dto);
        assertNotNull(result);
        assertEquals(dto.getBreakfast(), result.getBreakfast());
        assertEquals(dto.getMedical(), result.getMedical());
        verify(repository, times(1)).findByDate(date);
        verify(repository, times(1)).save(any(Expense.class));
    }

    @Test
    void testUpdateExpenseByDateNotFound() {
        //Mock: no record exists
        when(repository.findByDate(date)).thenReturn(Optional.empty());

        //Expect exception
        DateNotFoundException exception =
        assertThrows(DateNotFoundException.class,
        () -> expSvc.updateExpenseByDate(date, dto));
        assertNotNull(exception);
        verify(repository, times(1)).findByDate(date);
        verify(repository, never()).save(any(Expense.class));
    }
}