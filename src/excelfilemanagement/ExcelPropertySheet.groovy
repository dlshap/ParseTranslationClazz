package excelfilemanagement

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

class ExcelPropertySheet {
    Workbook workbook
    Sheet sheet
    private keyList
    private headerRowNum
    private Iterator<Row> rowIterator

    ExcelPropertySheet(ExcelPropertyFile excelFile, String sheetName) {
        new ExcelPropertySheet(excelFile, sheetName, 0)     // usually row 0
    }

    ExcelPropertySheet(ExcelPropertyFile excelFile, String sheetName, int headerRowNum) {
        this.workbook = excelFile.workbook
        this.sheet = this.workbook.getSheet(sheetName)
        this.headerRowNum = headerRowNum
        buildKeyListFromHeaderRow()
        resetRowIterator()
    }

    private buildKeyListFromHeaderRow() {
        Row headerRow = sheet.getRow(headerRowNum)
        keyList = headerRow.cellIterator().collect() { it.getStringCellValue() }
    }

    def resetRowIterator() {
        rowIterator = sheet.rowIterator()
//        // advance row iterator past header row
        (0..headerRowNum).each {
            def nextRow = rowIterator.next()
        }
    }

    def hasNextRow() {
        rowIterator.hasNext()
    }

    def nextRow() {
        Row nextRow
        if (rowIterator.hasNext()) {
            nextRow = rowIterator.next()
            if (nextRow.rowNum == headerRowNum)
                nextRow = rowIterator.next()
        }
        new ExcelPropertyRow(nextRow, keyList)
    }
}
