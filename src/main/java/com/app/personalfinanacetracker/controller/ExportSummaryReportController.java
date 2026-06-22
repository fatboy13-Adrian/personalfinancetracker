package com.app.personalfinanacetracker.controller;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.personalfinanacetracker.report.ExportSummaryReportService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/budgets")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Summaries", description = "Personal Finance Tracker")
public class ExportSummaryReportController {
    //Injects export summary report service dependency
    @Autowired
    private ExportSummaryReportService svc;

    @GetMapping("/summary/export")
	public ResponseEntity<InputStreamResource> exportYearlySummaries() {
        /**Generate excel file from export summary 
        report service & wrap it as input 
        stream resource**/
		ByteArrayInputStream file = svc.exportYearlySummaries();
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