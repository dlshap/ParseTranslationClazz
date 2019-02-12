package excelfilemanagement

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
//import org.apache.poi.ss.usermodel.Workbook

class ExcelSheet {

//    Workbook workbook
    Sheet sheet
    Iterator<Row> rowIterator

    ExcelSheet(Sheet sheet) {
        this.sheet = sheet
        rowIterator = sheet.rowIterator()
    }

    def resetRows() {
        rowIterator = sheet.rowIterator()
    }

    def hasNextRow() {
        rowIterator.hasNext()
    }

    def nextExcelRow() {
        new ExcelRow(rowIterator.next())
    }

    def getFirstRowNum() {
        sheet.getFirstRowNum()
    }

    def getLastRowNum() {
        sheet.getLastRowNum()
    }

}
