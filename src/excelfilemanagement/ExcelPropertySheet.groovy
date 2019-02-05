package excelfilemanagement

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook


class ExcelPropertySheet {
    Workbook workbook
    Sheet sheet
    private keyList
    private headerRowNum
    private Iterator<Row> rowIterator

    ExcelPropertySheet(ExcelFile excelFile, String sheetName) {
        workbook = excelFile.workbook
        buildSheet(sheetName)
    }

    def buildSheet(String sheetName) {
        sheet = workbook.getSheet(sheetName)
        rowIterator = sheet.rowIterator()
        populateKeyListUsingHeaderRow(0)
    }

    def populateKeyListUsingHeaderRow(int headerRowNum) {
        this.headerRowNum = headerRowNum
        Row headerRow = sheet.getRow(headerRowNum)
        keyList = headerRow.cellIterator().collect() { it.getStringCellValue() }
    }

    def getPropertyMapFromRow(Row row) {
        def keyMap = ["row":row.rowNum]
        row.cellIterator().eachWithIndex { Cell cell, int colNum ->
            if (colNum < keyList.size()) {
                switch (cell.getCellType()) {
                    case "NUMERIC":
                        keyMap.put(keyList[colNum], cell.getNumericCellValue().toInteger())
                        break;
                    case "STRING":
                        keyMap.put(keyList[colNum], cell.getStringCellValue())
                        break;
                    default:
                        keyMap.put(keyList[colNum], "")
                }
            }
        }
        keyMap
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
        nextRow
    }
}
