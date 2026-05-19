package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

public class ExcelUtil {

    private static Workbook wb;
    private static Sheet sheet;
    private static int rowNum = 0;

    static {
        try {
            // ✅ Initialize workbook once
            wb = new XSSFWorkbook();
            sheet = wb.createSheet("TestResults");

            // ✅ CREATE HEADER ROW (BEST PRACTICE)
            Row header = sheet.createRow(rowNum++);
            header.createCell(0).setCellValue("Test Case");
            header.createCell(1).setCellValue("Result");

        } catch (Exception e) {
            System.out.println("❌ Error initializing Excel: " + e.getMessage());
        }
    }

    // ✅ WRITE DATA (thread-safe simple use)
    public static synchronized void writeData(String testCase, String result) {

        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(testCase);
        row.createCell(1).setCellValue(result);
    }

    // ✅ SAVE FILE (SAFE + OVERWRITE)
    public static void saveExcel() {

        try {

            File file = new File("target/TestResults.xlsx");

            // ✅ Delete old file if exists
            if (file.exists()) {
                file.delete();
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
                wb.close();
            }

            System.out.println("✅ Excel file created successfully");

        } catch (Exception e) {
            System.out.println("❌ Error writing Excel: " + e.getMessage());
        }
    }
}
