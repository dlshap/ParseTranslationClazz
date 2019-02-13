package properties

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook

class ExcelPropertySheet {

    Workbook workbook
    private keyList
    private headerRowNum

    ExcelPropertySheet() {
    }

    ExcelPropertySheet(ExcelPropertyFile excelFile, String sheetName) {
        new ExcelPropertySheet(excelFile, sheetName, 0)     // usually row 0
    }

    ExcelPropertySheet(ExcelPropertyFile excelFile, String sheetName, int headerRowNum) {
        this.workbook = excelFile.excelWorkbook.workbook
        this.sheet = this.workbook.getSheet(sheetName)
        this.headerRowNum = headerRowNum
        buildKeyListFromHeaderRow()
        resetRowIterator()
    }

    static def createNewPropertySheet(ExcelPropertyFile excelPropertyFile, String sheetName) {
        // TODO: Add row, headerRow, etc. (might be done later...not sure yet)
        def newExcelPropertySheet = new ExcelPropertySheet()
        newExcelPropertySheet.sheet = excelPropertyFile.excelWorkbook.createSheet()
        newExcelPropertySheet
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
