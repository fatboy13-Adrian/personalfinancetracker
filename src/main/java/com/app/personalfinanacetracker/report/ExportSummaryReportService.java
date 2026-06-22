package com.app.personalfinanacetracker.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
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
import com.app.personalfinanacetracker.exception.ExportExcelFailedException;
import com.app.personalfinanacetracker.service.SummaryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExportSummaryReportService {
    private final SummaryService summarySvc;

    public ByteArrayInputStream exportYearlySummaries() {
        List <SummaryDTO> summaries = summarySvc.retrieveSummaries();
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