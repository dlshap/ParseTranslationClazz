package excelfilemanagement

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class ExcelWorkbookForInput extends ExcelWorkbook {

    ExcelWorkbookForInput(file) {
        workbookStream = new FileInputStream(file)
        def fileExtension = file.name.substring(file.name.indexOf("."))
        if (fileExtension == ".xlsx")
            workbook = new XSSFWorkbook(workbookStream)
        else
            workbook = new HSSFWorkbook(workbookStream)
    }
}
