package properties

import excelfilemanagement.ExcelSheet
import excelfilemanagement.ExcelSheetProperties
import exceptions.RowAlreadyExistsException
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

class ExcelPropertySheet extends ExcelSheet {

    private static enum OnBlankCell {
        STOP,
        NOSTOP
    }

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
        ArrayList<CellStyle> dataCellStyles = cloneStylesFromSheetWithStyles(stylesSourceSheet, headerRowNum + 1, OnBlankCell.NOSTOP)
        sheetProperties = new ExcelSheetProperties(this, headerRowNum, dataCellStyles)
        resetRows()
    }

    private ArrayList<CellStyle> cloneStylesFromSheetWithStyles(ExcelPropertySheet stylesSourceSheet, int rowNum, OnBlankCell onBlankCell) {
        ArrayList<CellStyle> cellStyles = []
        Row modelRow = stylesSourceSheet.getRow(rowNum)
        for (int colNum = 0; colNum < modelRow.getLastCellNum(); colNum++) {
            Cell modelCell = modelRow.getCell(colNum)
            String cellValue = modelCell.toString().trim()
            if ((onBlankCell == OnBlankCell.STOP) && ((cellValue == null) || (cellValue == "")))
                break
            else {
                CellStyle modelCellStyle = modelCell.getCellStyle()
                CellStyle cellStyle = workbook.createCellStyle()
                cellStyle.cloneStyleFrom(modelCellStyle)
                cellStyles << cellStyle
            }
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

    private copyHeaderRowFromModel(ExcelPropertySheet modelPropertySheet) {
        addHeaderRowFromPropertySheet(modelPropertySheet)
        setColumnWidths(modelPropertySheet.getColumnWidths())
        cloneStylesToHeaderRowFromPropertySheet(modelPropertySheet)
    }

    private cloneStylesToHeaderRowFromPropertySheet(ExcelPropertySheet modelPropertySheet) {
        int modelHeaderRowNum = modelPropertySheet.headerRowNum
//        ArrayList<String> modelHeaderRowNames = modelPropertySheet.headerRowNames
        ArrayList<CellStyle> headerRowStyles = cloneStylesFromSheetWithStyles(modelPropertySheet, modelHeaderRowNum, OnBlankCell.STOP)
        applyStylesToRow(headerRowStyles, modelHeaderRowNum)
    }

    private addHeaderRowFromPropertySheet(ExcelPropertySheet modelPropertySheet) {
        Row row = sheet.createRow(modelPropertySheet.headerRowNum)
        ArrayList<String> headerRowNames = modelPropertySheet.headerRowNames
        modelPropertySheet.headerRowNames.eachWithIndex { String keyName, int columnNumber ->
            Cell cell = row.createCell(columnNumber)
            cell.setCellValue(headerRowNames[columnNumber])
        }
    }

    private applyStylesToRow(ArrayList<CellStyle> cellStyles, int rowNum) {
        Row row = sheet.getRow(rowNum)
        cellStyles.eachWithIndex { CellStyle cellStyle, int columnNum ->
            Cell cell = row.getCell(columnNum)
            if (cell != null)
                cell.setCellStyle(cellStyle)
        }
    }

    boolean hasNextExcelPropertyRow() {
        rowIterator.hasNext()
    }

    ExcelPropertyRow nextExcelPropertyRow() {
        ExcelPropertyRow excelPropertyRow
        if (rowIterator.hasNext())
            excelPropertyRow = new ExcelPropertyRow(rowIterator.next(), headerRowNames)
        excelPropertyRow
    }

    ExcelPropertyRow getExcelPropertyRow(Integer rowNum) {
        ExcelPropertyRow excelPropertyRow
        Row row = getRow(rowNum)
        excelPropertyRow = new ExcelPropertyRow(row, headerRowNames)
        excelPropertyRow
    }

    ArrayList<String> getAllRowsInSheet() {
        def allRows = []
        Iterator localRowIterator = sheet.rowIterator()

        while (localRowIterator.hasNext()) {
            def nextRow = localRowIterator.next()
            if (nextRow.getRowNum() > headerRowNum) {
                ExcelPropertyRow row = new ExcelPropertyRow(nextRow, headerRowNames)
                def rowMap = row.getPropertyMap()
                allRows.add(rowMap)
            }
        }
        allRows
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
            ExcelPropertyRow matchPropertyRow = new ExcelPropertyRow(row, headerRowNames)
            if (matchPropertyRow.keysMatch(matchKeyList)) {
                excelPropertyRow = matchPropertyRow         //first match
                break
            }
        }
        excelPropertyRow
    }

    def cloneExcelRow(int rowNum, ExcelPropertyRow modelPropertyRow) {
        Row row = sheet.createRow(rowNum)
        ExcelPropertyRow newPropertyRow = new ExcelPropertyRow(row, headerRowNames)
        Map<String, String> propertyMap = modelPropertyRow.getPropertyMap()
        newPropertyRow.putPropertyMap(propertyMap)
        applyStylesToRow(dataCellStyles, rowNum)
        newPropertyRow
    }

    def cloneExcelPropertyRow(int rowNum, ExcelPropertyRow modelPropertyRow) {
        Row row = sheet.createRow(rowNum)
        ExcelPropertyRow newPropertyRow = new ExcelPropertyRow(row, headerRowNames)
        Map<String, String> propertyMap = modelPropertyRow.getPropertyMap()
        if (sheetProperties.isNewLanguage) {
            propertyMap.put(getLanguage(), "")
            propertyMap.put("Date Changed", "")
        }
        newPropertyRow.putPropertyMap(propertyMap)
        applyStylesToRow(dataCellStyles, rowNum)
        newPropertyRow
    }

    private ArrayList<CellStyle> getDataCellStyles() {
        sheetProperties.dataCellStyles
    }

    def addRow(int rowNum, propertyMap) {
        Row row = sheet.getRow(rowNum)
        if (row != null)
            throw new RowAlreadyExistsException("$rowNum already exists in ${this.sheetName}")
        else {
            row = sheet.createRow(rowNum)
            ExcelPropertyRow excelPropertyRow = new ExcelPropertyRow(row, headerRowNames)
            excelPropertyRow.putPropertyMap(propertyMap)
            applyStylesToRow(dataCellStyles, rowNum)
        }
    }

    ArrayList<String> getHeaderRowNames() {
        sheetProperties.headerRowNames
    }

    CellStyle getDateStyle() {
        Workbook workbook = this.workbook
        CellStyle dateCellStyle = workbook.createCellStyle()
        short dateFormat = workbook.createDataFormat().getFormat("mm/dd/yyyy")
        dateCellStyle.setDataFormat(dateFormat)
        dateCellStyle
    }
}
