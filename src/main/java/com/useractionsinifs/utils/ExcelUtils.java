package com.useractionsinifs.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {
    private static final Logger logger = LogManager.getLogger(ExcelUtils.class);
    private static final String USERS_EXCEL_FILE = "test-output/CreatedUsers.xlsx";
    private static final String SHEET_NAME = "UserData";
    
    /**
     * Save user data to Excel file
     * @param firstName User's first name
     * @param lastName User's last name
     * @param email User's email
     * @return true if successfully saved, false otherwise
     */
    public static boolean saveUserToExcel(String firstName, String lastName, String email) {
        try {
            // Create directory if it doesn't exist
            File dir = new File("test-output");
            if (!dir.exists()) {
                dir.mkdir();
                logger.info("Created test-output directory");
            }
            
            File file = new File(USERS_EXCEL_FILE);
            Workbook workbook;
            Sheet sheet;
            
            // Check if file exists and load it, or create a new workbook
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheet(SHEET_NAME);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet(SHEET_NAME);
                
                // Create header row if new file
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("First Name");
                headerRow.createCell(1).setCellValue("Last Name");
                headerRow.createCell(2).setCellValue("Email");
                
                // Apply some formatting to header
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                
                for (int i = 0; i < 3; i++) {
                    headerRow.getCell(i).setCellStyle(headerStyle);
                }
            }
            
            // Find the next available row
            int lastRowNum = sheet.getLastRowNum();
            int newRowIndex = lastRowNum + 1;
            
            // Create a new row and add user data
            Row newRow = sheet.createRow(newRowIndex);
            newRow.createCell(0).setCellValue(firstName);
            newRow.createCell(1).setCellValue(lastName);
            newRow.createCell(2).setCellValue(email);
            
            // Auto size columns for better readability
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write the workbook to file
            try (FileOutputStream fileOut = new FileOutputStream(USERS_EXCEL_FILE)) {
                workbook.write(fileOut);
            }
            
            // Close workbook to prevent memory leaks
            workbook.close();
            
            logger.info("Successfully saved user data to Excel: " + firstName + " " + lastName);
            return true;
            
        } catch (IOException e) {
            logger.error("Error saving user data to Excel", e);
            return false;
        }
    }
}
