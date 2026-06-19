package com.app.personalfinanacetracker.repository;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.personalfinanacetracker.entity.Budget;

@Repository
public interface BudgetRepository extends JpaRepository <Budget, Long> {
	//Find by month or months in between
	Optional <Budget> findByMonth(YearMonth month);
	List <Budget> findByMonthBetween(YearMonth start, YearMonth end);
}