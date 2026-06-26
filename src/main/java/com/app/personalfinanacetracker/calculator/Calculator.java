package com.app.personalfinanacetracker.calculator;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Component;

import com.app.personalfinanacetracker.dto.SummaryDTO;
import com.app.personalfinanacetracker.entity.Budget;
import com.app.personalfinanacetracker.entity.Expense;
import com.app.personalfinanacetracker.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Calculator {
    //Data access layer for expense
    private final ExpenseRepository repository;

    public void calculateMonthlyExpenses(Budget b) {
        //Extract month for calculation
        YearMonth month = b.getMonth();

        //Find dates between 1st day to last day of the month
        List <Expense> expenseList = repository.
        findByDateBetween(month.atDay(1), month.atEndOfMonth());

        //Intialize all variables to 0.0
        BigDecimal insurance = BigDecimal.ZERO;
        BigDecimal mobile = BigDecimal.ZERO;
        BigDecimal internet = BigDecimal.ZERO;
        BigDecimal utilities = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal mortgage = BigDecimal.ZERO;
        BigDecimal debt = BigDecimal.ZERO;
        BigDecimal parents = BigDecimal.ZERO;
        BigDecimal transport = BigDecimal.ZERO;
        BigDecimal food = BigDecimal.ZERO;
        BigDecimal groceries = BigDecimal.ZERO;
        BigDecimal haircut = BigDecimal.ZERO;
        BigDecimal medical = BigDecimal.ZERO;
        BigDecimal misc = BigDecimal.ZERO;

        //Add up all expenses and update budget table
        for (Expense e : expenseList) {
            insurance = insurance.add(e.getAia())
            .add(e.getCriticare())
            .add(e.getTermProtector());

            mobile = mobile.add(e.getMobilePhone());
            internet = internet.add(e.getInternet());
            utilities = utilities.add(e.getUtilities());

            tax = tax.add(e.getIncomeTax())
            .add(e.getPropertyTax());

            mortgage = mortgage.add(e.getMortgage());
            debt = debt.add(e.getDebt());
            parents = parents.add(e.getAllowancesForParents());

            transport = transport.add(e.getPublicTransport())
            .add(e.getPrivateTransport());

            food = food.add(e.getBreakfast())
            .add(e.getLunch()).add(e.getDinner())
            .add(e.getEatingOut());

            groceries = groceries.add(e.getGroceries());
            haircut = haircut.add(e.getHaircut());
            medical = medical.add(e.getMedical());
            
            misc = misc.add(e.getEntertainment())
            .add(e.getHoliday())
            .add(e.getShopping())
            .add(e.getSports())
            .add(e.getTech())
            .add(e.getOthers());
        }

        //Store calculated values into budget
        b.setInsurance(insurance);
        b.setMobilePhone(mobile);
        b.setInternet(internet);
        b.setUtilities(utilities);
        b.setTax(tax);
        b.setMortgage(mortgage);
        b.setDebt(debt);
        b.setAllowancesForParents(parents);
        b.setTransport(transport);
        b.setFood(food);
        b.setGroceries(groceries);
        b.setHaircut(haircut);
        b.setMedical(medical);
        b.setMisc(misc);

        //Calculate savings after subtracting all expenses
        b.setSavings(calculateSavings(b));
    }

    public BigDecimal calculateSavings(Budget b) {
        //Income subtract expenses
        return b.getIncome()
                .subtract(b.getRetirement()
                        .add(b.getInsurance())
                        .add(b.getMobilePhone())
                        .add(b.getInternet())
                        .add(b.getUtilities())
                        .add(b.getTax())
                        .add(b.getMortgage())
                        .add(b.getDebt())
                        .add(b.getAllowancesForParents())
                        .add(b.getTransport())
                        .add(b.getFood())
                        .add(b.getGroceries())
                        .add(b.getHaircut())
                        .add(b.getMedical())
                        .add(b.getMisc())
                );
    }

    public void calculateYearlyBudget(SummaryDTO dto, Budget b) {
        //Calculate total budget for the current year
        dto.setIncome(dto.getIncome().add(b.getIncome()));
        dto.setRetirement(dto.getRetirement().add(b.getRetirement()));
        dto.setInsurance(dto.getInsurance().add(b.getInsurance()));
        dto.setMobilePhone(dto.getMobilePhone().add(b.getMobilePhone()));
        dto.setInternet(dto.getInternet().add(b.getInternet()));
        dto.setUtilities(dto.getUtilities().add(b.getUtilities()));
        dto.setTax(dto.getTax().add(b.getTax()));
        dto.setMortgage(dto.getMortgage().add(b.getMortgage()));
        dto.setDebt(dto.getDebt().add(b.getDebt()));
        dto.setAllowancesForParents(dto.getAllowancesForParents().add(b.getAllowancesForParents()));
        dto.setTransport(dto.getTransport().add(b.getTransport()));
        dto.setFood(dto.getFood().add(b.getFood()));
        dto.setGroceries(dto.getGroceries().add(b.getGroceries()));
        dto.setHaircut(dto.getHaircut().add(b.getHaircut()));
        dto.setMedical(dto.getMedical().add(b.getMedical()));
        dto.setMisc(dto.getMisc().add(b.getMisc()));
        dto.setSavings(dto.getSavings().add(b.getSavings()));        
    }
}