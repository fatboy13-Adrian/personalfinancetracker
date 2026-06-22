package com.app.personalfinanacetracker.exception.budget;
import java.time.YearMonth;

public class MonthAlreadyExistsException extends RuntimeException {
    public MonthAlreadyExistsException(YearMonth month) {
        super ("Budget already exists for " + month);
    }
}