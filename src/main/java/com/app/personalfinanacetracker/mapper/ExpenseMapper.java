package com.app.personalfinanacetracker.mapper;
import org.springframework.stereotype.Component;

import com.app.personalfinanacetracker.dto.ExpenseDTO;
import com.app.personalfinanacetracker.entity.Expense;

@Component
public class ExpenseMapper {
    public ExpenseDTO toDTO(Expense e) {
        //Create empty DTO and map expense to DTO fields
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(e.getId());
        dto.setDate(e.getDate());
        dto.setAia(e.getAia());
        dto.setCriticare(e.getCriticare());
        dto.setTermProtector(e.getTermProtector());
        dto.setMobilePhone(e.getMobilePhone());
        dto.setInternet(e.getInternet());
        dto.setUtilities(e.getUtilities());
        dto.setIncomeTax(e.getIncomeTax());
        dto.setPropertyTax(e.getPropertyTax());
        dto.setMortgage(e.getMortgage());
        dto.setDebt(e.getDebt());
        dto.setAllowancesForParents(e.getAllowancesForParents());
        dto.setPublicTransport(e.getPublicTransport());
        dto.setPrivateTransport(e.getPrivateTransport());
        dto.setBreakfast(e.getBreakfast());
        dto.setLunch(e.getLunch());
        dto.setDinner(e.getDinner());
        dto.setEatingOut(e.getEatingOut());
        dto.setGroceries(e.getGroceries());
        dto.setHaircut(e.getHaircut());
        dto.setMedical(e.getMedical());
        dto.setEntertainment(e.getEntertainment());
        dto.setHoliday(e.getHoliday());
        dto.setShopping(e.getShopping());
        dto.setSports(e.getSports());
        dto.setTech(e.getTech());
        dto.setOthers(e.getOthers());

        //Return mapped DTO
        return dto;
    }

    public Expense toEntity(ExpenseDTO dto) {
        //Create new expense and map DTO to expense fields
        Expense e = new Expense();
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

        //Return expense ready for persistance
        return e;
    }
}