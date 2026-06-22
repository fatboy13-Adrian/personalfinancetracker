package com.app.personalfinanacetracker.controller;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.personalfinanacetracker.dto.BudgetDTO;
import com.app.personalfinanacetracker.service.BudgetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/budgets")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Budgets", description = "Personal Finance Tracker")
public class BudgetController {
    //Injects budget service dependency
    @Autowired	
	private BudgetService svc;

    @PostMapping("/addBudget")
	@Operation(summary = "Create a new budget record")
	public ResponseEntity<BudgetDTO> addBudget(@RequestBody BudgetDTO dto) {
		/**Calls the service layer to create a 
        new budget record & returns HTTP 200 OK 
        with the created budget record in the 
        response body**/
		return ResponseEntity.ok(svc.addBudget(dto));
	}

    @GetMapping
	@Operation(summary = "Show all budget records")
	public ResponseEntity<List <BudgetDTO>> showAllBudgets() {
		/**Calls the service layer to fetch all 
        budget records &  returns HTTP 200 OK 
        with the list of records**/
		return ResponseEntity.ok(svc.showAllBudgets());
	}

    @GetMapping("month/{month}")
	@Operation(summary = "Retrieve budget record by month")
	public ResponseEntity<BudgetDTO> findBudgetByMonth(@PathVariable YearMonth month) {
		/**Calls the service layer to fetch the budget 
        record by month & returns HTTP 200 OK with 
        the record info**/
		return ResponseEntity.ok(svc.findBudgetByMonth(month));
	}

    @PutMapping("month/{month}")
	@Operation(summary = "Update an existing budget record")
	public ResponseEntity<BudgetDTO> updateBudgetByMonth
    (@PathVariable YearMonth month, @RequestBody BudgetDTO dto) {
		/**Calls the service layer to fetch the budget record by month
        & returns HTTP 200 OK with the record info**/
		return ResponseEntity.ok(svc.updateBudgetByMonth(month, dto));
	}
}