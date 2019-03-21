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

    static createExcelPropertySheetFromAWorkbookSheet(Workbook workbook, Sheet sheet, int headerRowNum) {
        //existing ExcelPropertySheet
        ExcelPropertySheet excelPropertySheet = new ExcelPropertySheet()
        excelPropertySheet.workbook = workbook
        excelPropertySheet.sheet = sheet
        excelPropertySheet.setupSheetFromSheetWithStyles(excelPropertySheet, headerRowNum)
        excelPropertySheet
    }

    private setupSheetFromSheetWithStyles(ExcelPropertySheet stylesSourceSheet, int headerRowNum) {
        ArrayList<CellStyle> dataCellStyles = cloneStylesFromSheetWithStyles(stylesSourceSheet, headerRowNum + 1)
        sheetProperties = new ExcelPropertySheetProperties(this, headerRowNum, dataCellStyles)
        resetRows()
    }

    private ArrayList<CellStyle> cloneStylesFromSheetWithStyles(ExcelPropertySheet stylesSourceSheet, int rowNum) {
        ArrayList<CellStyle> cellStyles = []
        Row modelRow = stylesSourceSheet.getRow(rowNum)
        modelRow.cellIterator().eachWithIndex { Cell modelCell, int colNum ->
            CellStyle modelCellStyle = modelCell.getCellStyle()
            CellStyle cellStyle = workbook.createCellStyle()
            cellStyle.cloneStyleFrom(modelCellStyle)
            cellStyles << cellStyle
        }
        cellStyles
    }

    Row getRow(int rowNum) {
        sheet.getRow(rowNum)
    }

    static createExcelPropertySheetInWorkbookFromModelSheet(Workbook workbook, ExcelPropertySheet modelPropertySheet) {
        //copy from model spreadsheet
        ExcelPropertySheet newPropertySheet = new ExcelPropertySheet()
        newPropertySheet.workbook = workbook
        newPropertySheet.sheet = workbook.createSheet(modelPropertySheet.sheetName)
        newPropertySheet.copyHeaderRowFromModel(modelPropertySheet)
        newPropertySheet.setupSheetFromSheetWithStyles(modelPropertySheet, modelPropertySheet.headerRowNum)
        newPropertySheet
    }

    String getSheetName() {
        sheet.sheetName
    }

    private int getHeaderRowNum() {
        sheetProperties.headerRowNum
    }

    private copyHeaderRowFromModel(ExcelPropertySheet modelPropertySheet) {
        def headerRowNames = modelPropertySheet.sheetProperties.headerRowNames
        def headerRowNum = modelPropertySheet.headerRowNum
        addHeaderRow(headerRowNum, headerRowNames)
        setColumnWidths(modelPropertySheet.getColumnWidths())
        ArrayList<CellStyle> headerRowStyles = cloneStylesFromSheetWithStyles(modelPropertySheet, headerRowNum)
        applyStylesToRow(headerRowStyles, headerRowNum)
    }

    private getColumnWidths() {
        Row row = sheet.getRow(headerRowNum)
        def columnWidths = row.cellIterator().collect() { Cell it ->
            sheet.getColumnWidth(it.getColumnIndex())
        }
        columnWidths
    }

    private addHeaderRow(int headerRowNum, ArrayList keyList) {
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

    private applyStylesToRow(ArrayList<CellStyle> cellStyles, int rowNum) {
        Row row = sheet.getRow(rowNum)
        cellStyles.eachWithIndex { CellStyle cellStyle, int columnNum ->
            Cell cell = row.getCell(columnNum)
            cell.setCellStyle(cellStyle)
        }
    }

    boolean hasNextExcelPropertyRow() {
        rowIterator.hasNext()
    }

    ExcelPropertyRow nextExcelPropertyRow() {
        ExcelPropertyRow excelPropertyRow
        if (rowIterator.hasNext())
            excelPropertyRow = new ExcelPropertyRow(rowIterator.next(), sheetProperties.headerRowNames)
        excelPropertyRow
    }

    ArrayList<String> getKeyMaps() {
        def keyMaps = []
        Iterator localRowIterator = sheet.rowIterator()

        while (localRowIterator.hasNext()) {
            def nextRow = localRowIterator.next()
            if (nextRow.getRowNum() > headerRowNum) {
                ExcelPropertyRow row = new ExcelPropertyRow(nextRow, sheetProperties.headerRowNames)
                def rowMap = row.getPropertyMap()
                keyMaps.add(rowMap)
            }
        }
        keyMaps
    }

    private setColumnWidths(columnWidths) {
        columnWidths.eachWithIndex { columnWidth, columnNumber ->
            sheet.setColumnWidth(columnNumber, columnWidth)
        }
    }

    def resetRows() {
        rowIterator = sheet.rowIterator()
        // advance row iterator past header row
        (0..headerRowNum).each {
            rowIterator.next()
        }
    }

    String getLanguage() {
        sheetProperties.getLanguage()
    }

    def setLanguage(language) {
        if (sheetProperties.language != language) {
            Row row = sheet.getRow(headerRowNum)
            ArrayList<String> headerRowNames = sheetProperties.headerRowNames
            int languageColumn = headerRowNames.indexOf(sheetProperties.language)
            Cell newLanguageHeaderCell = row.getCell(languageColumn)
            newLanguageHeaderCell.setCellValue(language)
            sheetProperties.isNewLanguage = true
        }
    }

    ExcelPropertyRow getFirstExcelPropertyRowMatchingKeys(Map<String, String> matchKeyList) {
        // get FIRST property row with matching property keys
        ExcelPropertyRow excelPropertyRow
        Iterator localRowIterator = sheet.rowIterator()
        while (localRowIterator.hasNext()) {
            Row row = localRowIterator.next()
            ExcelPropertyRow matchPropertyRow = new ExcelPropertyRow(row, sheetProperties.headerRowNames)
            if (matchPropertyRow.keysMatch(matchKeyList)) {
                excelPropertyRow = matchPropertyRow         //first match
                break
            }
        }
        excelPropertyRow
    }

    def cloneExcelPropertyRow(int rowNum, ExcelPropertyRow modelPropertyRow) {
        Row row = sheet.createRow(rowNum)
        ExcelPropertyRow newPropertyRow = new ExcelPropertyRow(row, sheetProperties.headerRowNames)
        Map<String, String> propertyMap = modelPropertyRow.getPropertyMap()
        if (sheetProperties.isNewLanguage) {
            propertyMap.put(getLanguage(), "")
            propertyMap.put("Date Changed", "")
        }
        newPropertyRow.putPropertyMap(propertyMap)
        applyStylesToRow(sheetProperties.dataCellStyles, rowNum)
        newPropertyRow
    }

    def addRow(int rowNum, propertyMap) {
        Row row = sheet.getRow(rowNum)
        if (row != null)
            throw new RowAlreadyExistsException("$rowNum already exists in ${this.sheetName}")
        else {
            row = sheet.createRow(rowNum)
            ExcelPropertyRow excelPropertyRow = new ExcelPropertyRow(row, sheetProperties.headerRowNames)
            excelPropertyRow.putPropertyMap(propertyMap)
        }
    }

    ExcelPropertyRow getExcelPropertyRow(int rowNum) {
        new ExcelPropertyRow(getRow(rowNum), sheetProperties.headerRowNames)
    }
}
