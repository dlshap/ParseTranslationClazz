package properties

import excelfilemanagement.ExcelWorkbook
import exceptions.NoHeaderRowException
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet

class ExcelPropertySheet {

    Sheet sheet
    Iterator rowIterator

    def keyList
    def headerRowNum

    ExcelPropertySheet() {
    }

    ExcelPropertySheet(Sheet sheet) {
        this.sheet = sheet
        this.headerRowNum = 0
        this.resetRowIterator()
        buildKeyListFromHeaderRow()
    }

    private resetRowIterator() {
        rowIterator = sheet.rowIterator()
        // advance row iterator past header row
        (0..headerRowNum).each {
            rowIterator.next()
        }
    }

    private buildKeyListFromHeaderRow() {
        Row headerRow = sheet.getRow(headerRowNum)
        keyList = headerRow.cellIterator().collect() { it.getStringCellValue() }
    }

    ExcelPropertySheet(ExcelPropertyFile excelPropertyFile, String sheetName, int headerRowNum) {
        def workbook = excelPropertyFile.workbook
        this.sheet = workbook.getSheet(sheetName)
        this.headerRowNum = headerRowNum
        this.resetRowIterator()
        buildKeyListFromHeaderRow()
    }

    static createPropertySheetInExcelPropertyFile(ExcelPropertyFile excelPropertyFile, String sheetName) {
        ExcelPropertySheet excelPropertySheet = new ExcelPropertySheet()
        excelPropertySheet.initializeSheet(excelPropertyFile, sheetName)
        excelPropertySheet
    }

    private initializeSheet(ExcelPropertyFile excelPropertyFile, String sheetName) {
        ExcelWorkbook excelWorkbook = excelPropertyFile.excelWorkbook
        sheet = excelWorkbook.create(sheetName)
    }

    def hasNextRow() {
        rowIterator.hasNext()
    }

    ExcelPropertyRow nextRow() {
        Row nextRow
        if (rowIterator.hasNext()) {
            nextRow = rowIterator.next()
            if (nextRow.rowNum == headerRowNum)
                nextRow = rowIterator.next()
        }
        new ExcelPropertyRow(nextRow, keyList)
    }

    def getKeyMaps() {
        def keyMaps = []
        while (this.hasNextRow()) {
            ExcelPropertyRow row = this.nextRow()
            def rowMap = row.getPropertyMap()
            keyMaps.add(rowMap)
        }
        keyMaps
    }

    def getSheetName() {
        sheet.sheetName
    }

    def addHeaderRow(int headerRowNum, keyList) {
        this.headerRowNum = headerRowNum
        this.keyList = keyList
        Row row = sheet.createRow(headerRowNum)
        keyList.eachWithIndex { keyName, columnNumber ->
            Cell cell = row.createCell(columnNumber)
            cell.setCellValue(keyList[columnNumber])
        }
    }

    def addRow(int rowNumber, valueMap) {
        if (keyList == null)
            throw new NoHeaderRowException()
        else {
            Row row = new Row(rowNumber)
            ExcelPropertyRow excelPropertyRow = new ExcelPropertyRow(row, keyList)
            excelPropertyRow.putPropertyMapIntoRow(valueMap)
        }
    }
}
