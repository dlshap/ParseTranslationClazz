package properties

import exceptions.NoHeaderRowException
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

class ExcelPropertySheet {

    Sheet sheet
    Iterator rowIterator
    ExcelPropertySheetProperties sheetProperties

    ExcelPropertySheet() {
    }

    static createExcelPropertySheetFromSheetInExcelPropertyFile(ExcelPropertyFile excelPropertyFile, String sheetName, int headerRowNum) {
        ExcelPropertySheet excelPropertySheet = new ExcelPropertySheet()
        Workbook workbook = excelPropertyFile.workbook
        excelPropertySheet.sheet = workbook.getSheet(sheetName)
        excelPropertySheet.setupSheet(headerRowNum)
        excelPropertySheet
    }

    static createExcelPropertySheetFromExcelSheet(Sheet sheet) {
        ExcelPropertySheet excelPropertySheet = new ExcelPropertySheet()
        excelPropertySheet.sheet = sheet
        excelPropertySheet.setupSheet(0)
        excelPropertySheet
    }

    static createExcelPropertySheetInExcelPropertyFileWithHeaderRow(ExcelPropertyFile excelPropertyFile, String sheetName, int headerRowNum) {
        ExcelPropertySheet excelPropertySheet = new ExcelPropertySheet()
        Workbook workbook = excelPropertyFile.workbook
        excelPropertySheet.sheet = workbook.createSheet(sheetName)
        excelPropertySheet.setupSheet(headerRowNum)
        excelPropertySheet
    }

//    static createPropertySheetInExcelPropertyFile(ExcelPropertyFile excelPropertyFile, String sheetName) {
//        ExcelPropertySheet excelPropertySheet = new ExcelPropertySheet()
//        excelPropertySheet.initializeSheet(excelPropertyFile, sheetName)
//        excelPropertySheet
//    }
//

    private setupSheet(int headerRowNum) {
        sheetProperties = new ExcelPropertySheetProperties(this, headerRowNum)
        resetRowIterator()
    }

    private resetRowIterator() {
        rowIterator = sheet.rowIterator()
        // advance row iterator past header row
        (0..headerRowNum).each {
            rowIterator.next()
        }
    }

    def setHeaderRowNum(int headerRowNum) {
        sheetProperties.headerRowNum = headerRowNum
    }

    def getHeaderRowNum() {
        sheetProperties.headerRowNum
    }

    def hasNextExcelPropertyRow() {
        rowIterator.hasNext()
    }

    def nextExcelPropertyRow() {
        ExcelPropertyRow excelPropertyRow
        if (rowIterator.hasNext())
            excelPropertyRow = new ExcelPropertyRow(rowIterator.next(), sheetProperties.keyList)
        excelPropertyRow
    }

    def getKeyMaps() {
        def keyMaps = []
        while (this.hasNextExcelPropertyRow()) {
            ExcelPropertyRow row = this.nextExcelPropertyRow()
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

    def addRow(rowNum, valueMap) {
        if (keyList == null)
            throw new NoHeaderRowException()
        else {
            Row row = sheet.createRow(rowNum)
            ExcelPropertyRow excelPropertyRow = new ExcelPropertyRow(row, sheetProperties.keyList)
            excelPropertyRow.putPropertyMapIntoRow(valueMap)
            row
        }
    }

    def getLanguage() {
        sheetProperties.getLanguage()
    }
}