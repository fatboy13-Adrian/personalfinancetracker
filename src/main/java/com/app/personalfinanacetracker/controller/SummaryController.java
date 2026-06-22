package com.app.personalfinanacetracker.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.personalfinanacetracker.dto.SummaryDTO;
import com.app.personalfinanacetracker.service.SummaryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/budgets")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Summaries", description = "Personal Finance Tracker")
public class SummaryController {
    //Injects summary service dependency
    @Autowired
    private SummaryService svc;

    @GetMapping("/summaries")
	@Operation(summary = "Retrieve all yearly summary records")
	public ResponseEntity<List<SummaryDTO>> retrieveSummaries() {
		/**Calls the service layer to fetch all summary records 
        & returns HTTP 200 OK with the record info**/
		return ResponseEntity.ok(svc.retrieveSummaries());
	}
}