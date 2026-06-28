package com.app.personalfinanacetracker.controller;

import java.io.ByteArrayInputStream;
import java.time.Year;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.personalfinanacetracker.report.ExportReportService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/budgets")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Budgets", description = "Personal Finance Tracker")
public class ExportReportController {
    //Injects export budget report service dependency
    @Autowired
    private ExportReportService svc;

    @GetMapping("/export/{year}")
	public ResponseEntity<InputStreamResource> exportBudgetRecords
    (@PathVariable Year year) {
		/**Generate excel file from budget service 
        & wrap it as input stream resource**/
		InputStreamResource file = 
        new InputStreamResource(svc.exportBudgetRecords(year));

		/**Return excel file as 
        downloadable response**/
		return ResponseEntity.ok()
		.header(HttpHeaders.CONTENT_DISPOSITION, 
        "attachment; filename = Budget Records For Year " + year + ".xlsx")
		.contentType(MediaType.parseMediaType(
		"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
		.body(file);
	}

	@GetMapping("/summary/export")
	public ResponseEntity<InputStreamResource> exportYearlySummaries() {
        /**Generate excel file from export summary 
        report service & wrap it as input 
        stream resource**/
		ByteArrayInputStream file = svc.exportYearlyBudgets();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", 
        "attachment; filename= Yearly Summaries.xlsx");

        /**Return excel file as 
        downloadable response**/
		return ResponseEntity.ok()
		.headers(headers)
		.contentType(MediaType.parseMediaType(
		"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
		.body(new InputStreamResource(file));
	}
}