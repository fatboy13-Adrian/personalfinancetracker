package com.app.personalfinanacetracker.exception;

public class ExportExcelFailedException extends RuntimeException {
	public ExportExcelFailedException(String message, Throwable cause) {
        super(message);
    }
}