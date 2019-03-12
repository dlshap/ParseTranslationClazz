package excelfilemanagement

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class ExcelWorkbookForOutput extends ExcelWorkbook {

    ExcelWorkbookForOutput(file) {
        try {
            workbookStream = new FileOutputStream(file)
        } catch (FileNotFoundException) {}
        def fileExtension = file.name.substring(file.name.indexOf("."))
        if (fileExtension == ".xlsx")
            workbook = new XSSFWorkbook()
        else
            workbook = new HSSFWorkbook()
    }
}
