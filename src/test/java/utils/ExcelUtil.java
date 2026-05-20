
package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public class ExcelUtil {

    // ✅ WRITE PURPOSE VARIABLES
    private static Workbook wb;
    private static Sheet sheet;
    private static int rowNum = 0;

    // ✅ INPUT FILE PATH (CHANGE IF NEEDED)
    private static String inputFilePath = System.getProperty("user.dir")+"/src/test/resources/testdata.xlsx";

    // ✅ INITIALIZE OUTPUT EXCEL (for results)
    static {
        try {
            wb = new XSSFWorkbook();
            sheet = wb.createSheet("TestResults");

            // Header row
            Row header = sheet.createRow(rowNum++);
            header.createCell(0).setCellValue("Test Case");
            header.createCell(1).setCellValue("Result");

        } catch (Exception e) {
            System.out.println("❌ Error initializing Excel: " + e.getMessage());
        }
    }

    // ✅ WRITE DATA (RESULTS)
    public static synchronized void writeData(String testCase, String result) {

        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(testCase);
        row.createCell(1).setCellValue(result);
    }

    // ✅ SAVE OUTPUT FILE
    public static void saveExcel() {

        try {

            File file = new File("target/TestResults.xlsx");

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

    // ✅ ✅ READ DATA FROM INPUT EXCEL
    public static String getData(String sheetName, int rowNumber, int colNumber) {

        try (FileInputStream fis = new FileInputStream(inputFilePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rowNumber);
            Cell cell = row.getCell(colNumber);

            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(cell);

        } catch (Exception e) {
            System.out.println("❌ Error reading Excel: " + e.getMessage());
            return "";
        }
    }
}
 