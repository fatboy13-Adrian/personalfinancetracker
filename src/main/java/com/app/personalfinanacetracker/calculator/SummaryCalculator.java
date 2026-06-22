package com.app.personalfinanacetracker.calculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import com.app.personalfinanacetracker.dto.SummaryDTO;
import com.app.personalfinanacetracker.entity.Budget;

@Component
public class SummaryCalculator {
    public void initializeSummary(SummaryDTO dto) {
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
