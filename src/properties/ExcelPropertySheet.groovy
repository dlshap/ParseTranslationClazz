package properties

import exceptions.RowAlreadyExistsException
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

class ExcelPropertySheet {

    Sheet sheet
    Iterator rowIterator            // sheet rowIterator
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

    static createExcelPropertySheetInWorkbookFromModelSheet(Workbook workbook, ExcelPropertySheet modelPropertySheet) {
        ExcelPropertySheet newPropertySheet = new ExcelPropertySheet()
        newPropertySheet.sheet = workbook.createSheet(modelPropertySheet.sheetName)
        def headerRow = modelPropertySheet.headerRow
        def headerRowNum = modelPropertySheet.headerRowNum
        newPropertySheet.addHeaderRow(headerRowNum, headerRow)
        newPropertySheet.setupSheet(headerRowNum)
        newPropertySheet.setColumnWidths(modelPropertySheet.getColumnWidths())
        newPropertySheet
    }

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

    def getFirstExcelPropertyRowMatchingKeys(Map<String, String> matchKeyList) {
        // get FIRST property row with matching property keys
        ExcelPropertyRow excelPropertyRow
        Iterator localRowIterator = sheet.rowIterator()
        while (localRowIterator.hasNext()) {
            Row row = localRowIterator.next()
            ExcelPropertyRow matchPropertyRow = new ExcelPropertyRow(row, sheetProperties.keyList)
            if (matchPropertyRow.keysMatch(matchKeyList)) {
                excelPropertyRow = matchPropertyRow         //first match
                break
            }
        }
        excelPropertyRow
    }

    def getHeaderRowNum() {
        sheetProperties.headerRowNum
    }

    def getHeaderRow() {
        sheetProperties.keyList
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
        Iterator localRowIterator = sheet.rowIterator()

        while (localRowIterator.hasNext()) {
            def nextRow = localRowIterator.next()
            if (nextRow.getRowNum() > headerRowNum) {
                ExcelPropertyRow row = new ExcelPropertyRow(nextRow, sheetProperties.keyList)
                def rowMap = row.getPropertyMap()
                keyMaps.add(rowMap)
            }
        }
        keyMaps
    }

    def getSheetName() {
        sheet.sheetName
    }

    def addHeaderRow(int headerRowNum, ArrayList keyList) {
        Row row = sheet.createRow(headerRowNum)
        keyList.eachWithIndex { keyName, columnNumber ->
            Cell cell = row.createCell(columnNumber)
            cell.setCellValue(keyList[columnNumber])
        }
        /* add Date Changed Column */
        if (!(keyList.contains("Date Changed"))) {
            def columnNumber = keyList.size()
            Cell cell = row.createCell(columnNumber)
            cell.setCellValue("Date Changed")
            sheet.setColumnWidth(columnNumber, 3500)
        }
    }

    def addRow(int rowNum, propertyMap) {
        Row row = sheet.getRow(rowNum)
        if (row != null)
            throw new RowAlreadyExistsException("$rowNum already exists in ${this.sheetName}")
        else {
            row = sheet.createRow(rowNum)
            ExcelPropertyRow excelPropertyRow = new ExcelPropertyRow(row, sheetProperties.keyList)
            excelPropertyRow.putPropertyMap(propertyMap)
        }
    }


    def getLanguage() {
        sheetProperties.getLanguage()
    }

    def getColumnWidths() {
        Row row = sheet.getRow(headerRowNum)
        def columnWidths = row.cellIterator().collect() { Cell it ->
            sheet.getColumnWidth(it.getColumnIndex())
        }
        columnWidths
    }

    def setColumnWidths(columnWidths) {
        columnWidths.eachWithIndex { columnWidth, columnNumber ->
            sheet.setColumnWidth(columnNumber, columnWidth)
        }
    }

    private getRowStyles(int rowNum) {
        Row row = sheet.getRow(rowNum)
        row.cellIterator().collect() { it.getCellStyle() }
    }
}
