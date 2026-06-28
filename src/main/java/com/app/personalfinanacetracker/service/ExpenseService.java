package com.app.personalfinanacetracker.service;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import com.app.personalfinanacetracker.dto.ExpenseDTO;
import com.app.personalfinanacetracker.entity.Expense;
import com.app.personalfinanacetracker.exception.expense.DateAlreadyExistsException;
import com.app.personalfinanacetracker.exception.expense.DateNotFoundException;
import com.app.personalfinanacetracker.mapper.ExpenseMapper;
import com.app.personalfinanacetracker.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    //Data access layer for expense
    private final ExpenseRepository repository;

    //Service used to recalculate & refresh budget data
    private final BudgetService budgetSvc;

    //Mapper class for conversion between entity to dto
    private final ExpenseMapper mapper;

    @Transactional
    public ExpenseDTO createExpense(ExpenseDTO dto) {
        //Checks if date already exists
        repository.findByDate(dto.getDate()).ifPresent(existing -> {
            throw new DateAlreadyExistsException(dto.getDate());
        });

        //Convert DTO to expense
        Expense e = mapper.toEntity(dto);

        //Save new record to DB
        Expense saved = repository.save(e);

        //Refresh budget after new record is created
        budgetSvc.refreshBudget(dto.getDate());

        //Convert expense to DTO
        return mapper.toDTO(saved);
    }

    public List <ExpenseDTO> retrieveExpenses() {
        return repository.findAll()
        .stream().map(mapper::toDTO)
        .toList();
    }

    public ExpenseDTO retrieveExpenseByDate(LocalDate date) {
        //Fetch expense by date or throw exception
        Expense e = repository.findByDate(date)
        .orElseThrow(() -> new DateNotFoundException(date));

        //Convert expense to DTO & return
        return mapper.toDTO(e);
    }

    @Transactional
    public ExpenseDTO updateExpenseByDate(LocalDate date, ExpenseDTO dto) {
        Expense existing = repository.findByDate(date)
        .orElseThrow(() -> new DateNotFoundException(date));
        updateExpenseRecord(existing, dto);
        Expense updated = repository.save(existing);
        budgetSvc.refreshBudget(dto.getDate());
        return mapper.toDTO(updated);
    }

    private void updateExpenseRecord(Expense e, ExpenseDTO dto) {
        //Update all fields from DTO into expense
        e.setDate(dto.getDate());
        e.setAia(dto.getAia());
        e.setCriticare(dto.getCriticare());
        e.setTermProtector(dto.getTermProtector());
        e.setMobilePhone(dto.getMobilePhone());
        e.setInternet(dto.getInternet());
        e.setUtilities(dto.getUtilities());
        e.setIncomeTax(dto.getIncomeTax());
        e.setPropertyTax(dto.getPropertyTax());
        e.setMortgage(dto.getMortgage());
        e.setDebt(dto.getDebt());
        e.setAllowancesForParents(dto.getAllowancesForParents());
        e.setPublicTransport(dto.getPublicTransport());
        e.setPrivateTransport(dto.getPrivateTransport());
        e.setBreakfast(dto.getBreakfast());
        e.setLunch(dto.getLunch());
        e.setDinner(dto.getDinner());
        e.setEatingOut(dto.getEatingOut());
        e.setGroceries(dto.getGroceries());
        e.setHaircut(dto.getHaircut());
        e.setMedical(dto.getMedical());
        e.setEntertainment(dto.getEntertainment());
        e.setHoliday(dto.getHoliday());
        e.setShopping(dto.getShopping());
        e.setSports(dto.getSports());
        e.setTech(dto.getTech());
        e.setOthers(dto.getOthers());
    }
}