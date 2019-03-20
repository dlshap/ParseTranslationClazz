package properties

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH
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
//        ArrayList<CellStyle> dataRowStyles = excelPropertySheet.getRowStyles(excelPropertySheet.getRow(headerRowNum+1))
        def dataRowStyles
        excelPropertySheet.setupSheet(headerRowNum, dataRowStyles)
        excelPropertySheet
    }

    static createExcelPropertySheetInWorkbookFromModelSheet(Workbook workbook, ExcelPropertySheet modelPropertySheet) {
        //copy from model spreadsheet
        ExcelPropertySheet newPropertySheet = new ExcelPropertySheet()
        def headerRowNum = modelPropertySheet.headerRowNum
        newPropertySheet.workbook = workbook
        newPropertySheet.sheet = workbook.createSheet(modelPropertySheet.sheetName)
        newPropertySheet.copyHeaderRowFromModel(modelPropertySheet)
//        ArrayList<CellStyle> dataRowStyles = newPropertySheet.getDataRowStylesFromModel(modelPropertySheet)
        def dataRowStyles
        newPropertySheet.setupSheet(headerRowNum, dataRowStyles)
        newPropertySheet
    }

    private getDataRowStylesFromModel(ExcelPropertySheet modelPropertySheet) {
        ArrayList<CellStyle> modelDataRowStyles = getRowStyles(modelPropertySheet.getRow(modelPropertySheet.headerRowNum + 1))
        this.cloneStylesFromModelStyles(modelDataRowStyles)
    }

    private copyHeaderRowFromModel(ExcelPropertySheet modelPropertySheet) {
        def headerRowNames = modelPropertySheet.headerRowNames
        def headerRowNum = modelPropertySheet.headerRowNum
        addHeaderRow(headerRowNum, headerRowNames)
        setColumnWidths(modelPropertySheet.getColumnWidths())
        ArrayList<CellStyle> modelHeaderRowStyles = getRowStyles(modelPropertySheet.getRow(headerRowNum))
        ArrayList<CellStyle> headerRowStyles = cloneStylesFromModelStyles(modelHeaderRowStyles)
        applyStylesToRow(headerRowStyles, headerRowNum)
    }

    private ArrayList<CellStyle> getRowStyles(Row row) {
        row.cellIterator().collect() { it.getCellStyle() }
    }

    private ArrayList<CellStyle> cloneStylesFromModelStyles(ArrayList<CellStyle> modelRowStyles) {
         ArrayList<CellStyle> cellStyles = []
         modelRowStyles.each { CellStyle cellStyle ->
             CellStyle newCellStyle = this.workbook.createCellStyle()
             newCellStyle.cloneStyleFrom(cellStyle)
             cellStyles << newCellStyle
         }
     }

    private applyStylesToRow(ArrayList<CellStyle> rowStyles, int rowNum) {
        Row row = sheet.getRow(rowNum)
        rowStyles.eachWithIndex { CellStyle cellStyle, int columnNum ->
            Cell cell = row.getCell(columnNum)
            cell.setCellStyle(cellStyle)
        }
    }

    private setupSheet(int headerRowNum, ArrayList<CellStyle> dataRowStyles) {
        sheetProperties = new ExcelPropertySheetProperties(this, headerRowNum, dataRowStyles)
        resetRows()
    }

    def resetRows() {
        rowIterator = sheet.rowIterator()
        // advance row iterator past header row
        (0..headerRowNum).each {
            rowIterator.next()
        }
    }

    Row getRow(int rowNum) {
        sheet.getRow(rowNum)
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

    def getHeaderRowNames() {
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
        Map<String, String> propertyMap = modelPropertyRow.getPropertyMap()
        if (sheetProperties.isNewLanguage) {
            propertyMap.put(getLanguage(), "")
            propertyMap.put("Date Changed", "")
        }
        newPropertyRow.putPropertyMap(propertyMap)
        newPropertyRow
    }

    def setLanguage(language) {
        if (sheetProperties.language != language) {
            Row row = sheet.getRow(headerRowNum)
            int languageColumn = getLanguageColumn()
            Cell newLanguageHeaderCell = row.getCell(languageColumn)
            newLanguageHeaderCell.setCellValue(language)
            sheetProperties.isNewLanguage = true
        }
    }

    int getLanguageColumn() {
        (getHeaderRowNames()).indexOf(sheetProperties.language)
    }

    String getLanguage() {
        sheetProperties.getLanguage()
    }

    private getColumnWidths() {
        Row row = sheet.getRow(headerRowNum)
        def columnWidths = row.cellIterator().collect() { Cell it ->
            sheet.getColumnWidth(it.getColumnIndex())
        }
        columnWidths
    }

    private setColumnWidths(columnWidths) {
        columnWidths.eachWithIndex { columnWidth, columnNumber ->
            sheet.setColumnWidth(columnNumber, columnWidth)
        }
    }
}


//    static createExcelPropertySheetFromExcelSheet(Workbook workbook, Sheet sheet) {
//        //from sheet in ExcelPropertyFile
//        ExcelPropertySheet excelPropertySheet = new ExcelPropertySheet()
//        excelPropertySheet.workbook = workbook
//        excelPropertySheet.sheet = sheet
//        ArrayList<CellStyle> dataRowStyles = excelPropertySheet.getRowStyles(excelPropertySheet.getRow(1))
//        excelPropertySheet.setupSheet(0, dataRowStyles)
//        excelPropertySheet
//    }

//    ArrayList<CellStyle> getRowStylesForRowNumber(int rowNum) {
//        Workbook workbook = excelPropertySheet.workbook
//        Row row = sheet.getRow(rowNum)
//        ArrayList<CellStyle> rowStyles = row.cellIterator().collect { Cell cell ->
//            CellStyle cellStyle = workbook.createCellStyle()
//            cellStyle.cloneStyleFrom(cell.getCellStyle())
//            cellStyle
//        }
//        rowStyles
//    }
//    private moveHeaderRowStylesFromModelToNewSheet(ExcelPropertySheet modelPropertySheet) {
//        def headerRowNum = modelPropertySheet.headerRowNum
//        Row modelHeaderRow = modelPropertySheet.sheet.getRow(headerRowNum)
//        def modelHeaderRowStyles = modelPropertySheet.getRowStyles(modelHeaderRow)
//        sheetProperties.headerRowStyles = cloneStylesFromModelStyles(modelHeaderRowStyles)
//    }


