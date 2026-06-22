package com.app.personalfinanacetracker.controller;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.personalfinanacetracker.dto.ExpenseDTO;
import com.app.personalfinanacetracker.service.ExpenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/expenses")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Expenses", description = "Personal Finance Tracker")
public class ExpenseController {
	//Injects the service dependency automatically
	@Autowired	
	private ExpenseService svc;

    @PostMapping("/addExpense")
	@Operation(summary = "Add a new expense record")
	public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO dto) {
		/**Calls the service layer to add a new record & 
        returns HTTP 200 OK with the added record in the
        response body**/
		return ResponseEntity.ok(svc.addExpense(dto));
	}

    @GetMapping
	@Operation(summary = "Show all expense records")
	public ResponseEntity<List<ExpenseDTO>> showAllExpenses() {
		/**Calls the service layer to fetch all 
        records & returns HTTP 200 OK with the 
        list of records**/
		return ResponseEntity.ok(svc.showAllExpenses());
	}

    @GetMapping("/date/{date}")
	@Operation(summary = "Show expense record by date")
	public ResponseEntity<ExpenseDTO> findExpenseByDate(@PathVariable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		/**Calls the service layer to fetch the record 
        by date & returns HTTP 200 OK with the 
        record info**/
		ExpenseDTO dto = svc.findExpenseByDate(date);
		return ResponseEntity.ok(dto);
	}

    @PutMapping("/date/{date}")
	@Operation(summary = "Update an existing expense record by date")
	public ResponseEntity<ExpenseDTO> updateExpenseByDate(@PathVariable 
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, 
    @RequestBody ExpenseDTO dto) {
		return ResponseEntity.ok(svc.updateExpenseByDate(date, dto));
	}
}