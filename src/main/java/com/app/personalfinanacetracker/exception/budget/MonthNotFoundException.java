package com.app.personalfinanacetracker.exception.budget;
import java.time.YearMonth;

public class MonthNotFoundException extends RuntimeException {
    public MonthNotFoundException(YearMonth month) {
        super("Budget not found: " + month);
    }
}