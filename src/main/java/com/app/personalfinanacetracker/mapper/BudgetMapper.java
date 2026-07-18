package com.app.personalfinanacetracker.mapper;
import org.springframework.stereotype.Component;

import com.app.personalfinanacetracker.dto.BudgetDTO;
import com.app.personalfinanacetracker.entity.Budget;

@Component
public class BudgetMapper {
    public BudgetDTO toDTO(Budget b) {
        //Create DTO and map budget to DTO
        BudgetDTO dto = new BudgetDTO();
        dto.setId(b.getId());
        dto.setMonth(b.getMonth());
        dto.setIncome(b.getIncome());
        dto.setRetirement(b.getRetirement());
        dto.setInsurance(b.getInsurance());
        dto.setMobilePhone(b.getMobilePhone());
        dto.setInternet(b.getInternet());
        dto.setUtilities(b.getUtilities());
        dto.setTax(b.getTax());
        dto.setMortgage(b.getMortgage());
        dto.setDebt(b.getDebt());
        dto.setAllowancesForParents(b.getAllowancesForParents());
        dto.setTransport(b.getTransport());
        dto.setFood(b.getFood());
        dto.setGroceries(b.getGroceries());
        dto.setHaircut(b.getHaircut());
        dto.setMedical(b.getMedical());
        dto.setMisc(b.getMisc());
        dto.setSavings(b.getSavings());
        dto.setOverSpent(b.getOverSpent());
        
        //Return DTO
        return dto;
    }
}