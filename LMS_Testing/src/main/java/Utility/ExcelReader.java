package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;

public class ExcelReader {

	public static List<Map<String, String>> getPCRPlateData(String excelPath, String sheetName) {
	    List<Map<String, String>> dataList = new ArrayList<>();
	    try (FileInputStream fis = new FileInputStream(excelPath);
	         Workbook workbook = WorkbookFactory.create(fis)) {

	        Sheet sheet = workbook.getSheet(sheetName);
	        Row headerRow = sheet.getRow(0);
	        int rowCount = sheet.getPhysicalNumberOfRows();

	        for (int i = 1; i < rowCount; i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            Map<String, String> dataMap = new LinkedHashMap<>();
	            for (int j = 0; j < row.getLastCellNum(); j++) {
	                Cell headerCell = headerRow.getCell(j);
	                Cell cell = row.getCell(j);
	                if (headerCell != null) {
	                    String key = headerCell.getStringCellValue();
	                    String value = cell != null ? cell.toString().trim() : "";
	                    dataMap.put(key, value);
	                }
	            }
	            dataList.add(dataMap);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return dataList;
	}
}
