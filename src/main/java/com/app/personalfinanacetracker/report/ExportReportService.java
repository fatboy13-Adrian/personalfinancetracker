package com.app.personalfinanacetracker.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.app.personalfinanacetracker.dto.SummaryDTO;
import com.app.personalfinanacetracker.entity.Budget;
import com.app.personalfinanacetracker.exception.ExportExcelFailedException;
import com.app.personalfinanacetracker.repository.BudgetRepository;
import com.app.personalfinanacetracker.service.BudgetService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExportReportService {
    private final BudgetService budgetSvc;
    private final BudgetRepository repository;

    public List <Budget> getYearlyBudgets(Year year) {
        //Find by year or throw exception if not found
		if (year == null) 
            throw new IllegalArgumentException("Year cannot be null");
        
        //Jan to Dec
		YearMonth start = YearMonth.of(year.getValue(), 1);
		YearMonth end = YearMonth.of(year.getValue(), 12);

        //Fetch year range
		return repository.findByMonthBetween(start, end);
	}
    
    //Helper method to export budget records
	public ByteArrayInputStream exportBudgetRecords(Year year) {
		//Retrieve all budget records
	    List<Budget> budgets = getYearlyBudgets(year);

		//Build & return excel file as byte array input stream
		return buildExcel(budgets, "Budget Records For Year " + year);
	}

	//Helper method to build excel file
	private ByteArrayInputStream buildExcel(List <Budget> budgets, String sheetName) {
		//Create workbook & output stream using try catch block to auto close
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			//Create new worksheet in workbook with given file name
			Sheet sheet = workbook.createSheet(sheetName);

			//Create style & font for header cells
			CellStyle headerStyle = workbook.createCellStyle();
			Font font = workbook.createFont();

			//Create, set font & alignment as center for header to bold
			font.setBold(true);
        	headerStyle.setFont(font);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);

			//Create currency style & set alignment as center
			CellStyle currencyStyle = workbook.createCellStyle();
			currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("[$$-en-SG]#,##0.00"));
			currencyStyle.setAlignment(HorizontalAlignment.CENTER);

			//Define column headers for excel sheet
			String[] columns = {
				"Month",
				"Income",
				"Retirement", 
				"Insurance",
                "Mobile Phone",
                "Internet",
                "Utilities",
                "Tax",
                "Mortgage",
                "Debt",
                "Allowances For Parents",
                "Transport",
                "Food",
                "Groceries",
                "Haircut",
                "Medical",
                "Misc",
                "Savings"
			};

			//Create header row at index 0
			Row header = sheet.createRow(0);
			for (int i = 0; i < columns.length; i++) {
				//Create cell, set column title, apply bold header style
				Cell cell = header.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerStyle);
			}

			//Loop through budget list using index
			for (int i = 0; i < budgets.size(); i++) {
				//Retrieve budget object at current index
				Budget b = budgets.get(i);

				//Create new row starting from row 1 (row 0 = header row)
				Row row = sheet.createRow(i + 1);

				//Fill each column with budget data
				row.createCell(0).setCellValue(b.getMonth().toString());
                
				BigDecimal[] values = {
					b.getIncome(),
                    b.getRetirement(),
                    b.getInsurance(),
                    b.getMobilePhone(),
					b.getInternet(),
                    b.getUtilities(),
                    b.getTax(),
                    b.getMortgage(),
                    b.getDebt(),
                    b.getAllowancesForParents(),
                    b.getTransport(),
                    b.getFood(),
					b.getGroceries(),
					b.getHaircut(),
					b.getMedical(),
					b.getMisc(),
                    b.getSavings()
				};

				//Loop to create cells & apply $ format
				for (int j = 0; j < values.length; j++) {
					Cell cell = row.createCell(j + 1);
					cell.setCellValue(values[j].doubleValue());
					cell.setCellStyle(currencyStyle);
				}
			}

			//Auto size all columns for better readability
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			//Write workbook data into output stream
			workbook.write(output);

			//Convert output to input stream for download
			return new ByteArrayInputStream(output.toByteArray());
		} catch (Exception e) {
			//Handle error
			throw new ExportExcelFailedException("Excel export failed", e);
		}
	}

	 public ByteArrayInputStream exportYearlyBudgets() {
        List <SummaryDTO> summaries = budgetSvc.retrieveBudgetsByYear();
        return buildYearlySummaryExcel(summaries);
    }

    private ByteArrayInputStream buildYearlySummaryExcel(List <SummaryDTO> summaries) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Yearly Summaries");
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(
                    workbook.createDataFormat().getFormat("[$$-en-SG]#,##0.00")
            );
            currencyStyle.setAlignment(HorizontalAlignment.CENTER);

            String[] columns = {
                    "Year",
                    "Income",
                    "Retirement",
                    "Insurance",
                    "Mobile Phone",
                    "Internet",
                    "Utilities",
                    "Tax",
                    "Mortgage",
                    "Debt",
                    "Allowances For Parents",
                    "Transport",
                    "Food",
                    "Groceries",
                    "Haircut",
                    "Medical",
                    "Misc",
                    "Savings"
            };

            //Header row
            Row header = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            //Data rows
            for (int i = 0; i < summaries.size(); i++) {
                SummaryDTO dto = summaries.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(dto.getYear());
                BigDecimal[] values = {
                                        dto.getIncome(),
                                        dto.getRetirement(),
                                        dto.getInsurance(),
                                        dto.getMobilePhone(),
                                        dto.getInternet(),
                                        dto.getUtilities(),
                                        dto.getTax(),
                                        dto.getMortgage(),
                                        dto.getDebt(),
                                        dto.getAllowancesForParents(),
                                        dto.getTransport(),
                                        dto.getFood(),
                                        dto.getGroceries(),
                                        dto.getHaircut(),
                                        dto.getMedical(),
                                        dto.getMisc(),
                                        dto.getSavings()
                };

                for (int j = 0; j < values.length; j++) {
                    Cell cell = row.createCell(j + 1);
                    cell.setCellValue(values[j].doubleValue());
                    cell.setCellStyle(currencyStyle);
                }
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(output);
            return new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            throw new ExportExcelFailedException("Yearly summary export failed", e);
        }
    }
}