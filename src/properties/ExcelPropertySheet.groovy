package properties

import exceptions.RowAlreadyExistsException
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

class ExcelPropertySheet {

    Sheet sheet
    Workbook workbook
    Iterator rowIterator            // sheet rowIterator
    ExcelPropertySheetProperties sheetProperties

    ExcelPropertySheet() {
    }

    static createExcelPropertySheetFromSheetInExcelPropertyFile(Workbook workbook, String sheetName, int headerRowNum) {
        ExcelPropertySheet excelPropertySheet = new ExcelPropertySheet()
        excelPropertySheet.workbook = workbook
        excelPropertySheet.sheet = workbook.getSheet(sheetName)
        excelPropertySheet.setupSheet(headerRowNum)
        excelPropertySheet
    }

    static createExcelPropertySheetFromExcelSheet(Workbook workbook, Sheet sheet) {
        ExcelPropertySheet excelPropertySheet = new ExcelPropertySheet()
        excelPropertySheet.sheet = sheet
        excelPropertySheet.workbook = workbook
        excelPropertySheet.setupSheet(0)
        excelPropertySheet
    }

    static createExcelPropertySheetInWorkbookFromModelSheet(Workbook workbook, ExcelPropertySheet modelPropertySheet) {
        def headerRowNum = modelPropertySheet.headerRowNum
        ExcelPropertySheet newPropertySheet = new ExcelPropertySheet()
        newPropertySheet.sheet = workbook.createSheet(modelPropertySheet.sheetName)
        newPropertySheet.workbook = workbook
        newPropertySheet.moveHeaderRowDataFromModelToNewSheet(modelPropertySheet)
        newPropertySheet.setupSheet(headerRowNum)
        newPropertySheet.moveHeaderRowStylesFromModelToNewSheet(modelPropertySheet)
        newPropertySheet
    }

    private moveHeaderRowDataFromModelToNewSheet(ExcelPropertySheet modelPropertySheet) {
        def headerRow = modelPropertySheet.headerRow
        def headerRowNum = modelPropertySheet.headerRowNum
        addHeaderRow(headerRowNum, headerRow)
    }

    private moveHeaderRowStylesFromModelToNewSheet(ExcelPropertySheet modelPropertySheet) {
        def headerRowNum = modelPropertySheet.headerRowNum
        Row modelHeaderRow = modelPropertySheet.sheet.getRow(headerRowNum)
        setColumnWidths(modelPropertySheet.getColumnWidths())
        def headerStyles = modelPropertySheet.getRowStyles(modelHeaderRow)
        cloneStylesFromModelStyles(headerStyles)
    }

    private cloneStylesFromModelStyles(headerStyles) {
        Row headerRow = sheet.getRow(headerRowNum)
        headerStyles.eachWithIndex { CellStyle headerStyle, int columnNum ->
            CellStyle newCellStyle = workbook.createCellStyle()
            newCellStyle.cloneStyleFrom(headerStyle)
            Cell cell = headerRow.getCell(columnNum)
            cell.setCellStyle(newCellStyle)
        }
    }

    private setupSheet(int headerRowNum) {
        sheetProperties = new ExcelPropertySheetProperties(this, headerRowNum)
        resetRows()
    }

    def resetRows() {
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

    def cloneExcelPropertyRow(int rowNum, ExcelPropertyRow modelPropertyRow) {
        Row row = sheet.createRow(rowNum)
        ExcelPropertyRow newPropertyRow = new ExcelPropertyRow(row, sheetProperties.keyList)
        def propertyMap = modelPropertyRow.getPropertyMap()
        newPropertyRow.putPropertyMap(propertyMap)
        newPropertyRow
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

    private getRowStyles(Row row) {
        row.cellIterator().collect() { it.getCellStyle() }
    }
}
