package cn.financial.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * Excel工具类
 */
public class ExcelUtil {

    
    public static List<String[]> read(InputStream is, int startRow) throws Exception {
        List<String[]> list = new ArrayList<>();
        try {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            int rowLength = sheet.getLastRowNum();
            list = new ArrayList<String[]>(rowLength);
            for (int i = startRow; i < rowLength; i++) {
                //读取行
                Row row = sheet.getRow(i);
                if (row != null) {
                    int cellLength = row.getLastCellNum();
                    String[] subList = new String[cellLength];
                    for (int j = 0; j < cellLength; j++) {
                        String cellValue = "";
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            cellValue = changeType(cell);
                        }
                        subList[j] = cellValue;
                    }
                    list.add(subList);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }


    private static String changeType(Cell cell) {
        String cellValue = null;
        try {
            if (cell != null) {
                //判断当前Cell的Type
                switch (cell.getCellType()) {
                    //如果当前Cell的Type为NUMERIC
                    case Cell.CELL_TYPE_NUMERIC:
                        double tnumeric = cell.getNumericCellValue();
                        if ((tnumeric - (long) tnumeric) > 0) {
                            cellValue = String.valueOf(cell.getNumericCellValue());
                        } else {
                            cellValue = String.valueOf((long) tnumeric);
                        }
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        Date date = cell.getDateCellValue();
                        cellValue=TimeUtils.format(date);
                        break;
                    //如果当前Cell的Type为STRING
                    case Cell.CELL_TYPE_STRING:
                        cellValue = cell.getRichStringCellValue().getString();
                        break;
                    default:
                        cellValue = "";
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cellValue;
    }

    public static void export(List<String[]> list, OutputStream os) throws Exception {
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("sheet");
            int i = 2;
            for (Object[] item : list) {
                Row row = sheet.createRow(i);
                int j = 0;
                for (Object item2 : item) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        cell = row.createCell(j);
                    }
                    setValue(cell, item2);
                    j++;
                }
                i++;
            }
            workbook.write(os);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void setValue(Cell cell, Object object) {
        if (object == null) {
            return;
        }
        if (object instanceof Boolean) {
            cell.setCellValue((Boolean) object);
        } else if (object instanceof Byte) {
            cell.setCellValue((Byte) object);
        } else if (object instanceof Integer) {
            cell.setCellValue((Integer) object);
        } else if (object instanceof Long) {
            cell.setCellValue((Long) object);
        } else if (object instanceof Float) {
            cell.setCellValue((Float) object);
        } else if (object instanceof Double) {
            cell.setCellValue((Double) object);
        } else if (object instanceof String) {
            cell.setCellValue((String) object);
        } else if (object instanceof Date) {
            cell.setCellValue((Date) object);
        } else {
            cell.setCellValue(object.toString());
        }
    }
	
}
