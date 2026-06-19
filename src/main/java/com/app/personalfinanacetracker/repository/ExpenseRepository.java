package com.app.personalfinanacetracker.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.personalfinanacetracker.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository <Expense, Long> {
	//Find by date or dates between
	Optional <Expense> findByDate(LocalDate date);
	List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);
}