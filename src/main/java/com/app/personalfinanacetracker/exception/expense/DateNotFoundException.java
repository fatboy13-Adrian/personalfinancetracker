package com.app.personalfinanacetracker.exception.expense;
import java.time.LocalDate;

public class DateNotFoundException extends RuntimeException {
    public DateNotFoundException(LocalDate date) {
        super("Expense not found for date: " + date);
    }
}