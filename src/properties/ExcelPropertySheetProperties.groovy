package properties

import exceptions.NewLanguageNotInLanguageListException
import i18n.LanguageLabels
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

class ExcelPropertySheetProperties {

    ExcelPropertySheet excelPropertySheet

    def keyList
    def headerRowNum
    Sheet sheet
    def language
    def isNewLanguage = false
    ArrayList<CellStyle> headerRowStyles
    ArrayList<CellStyle> dataRowStyles

    ExcelPropertySheetProperties(ExcelPropertySheet excelPropertySheet, headerRowNum) {
        this.headerRowNum = headerRowNum
        this.excelPropertySheet = excelPropertySheet
        this.sheet = excelPropertySheet.sheet
        this.buildPropertySheetPropertiesFromHeaderRow()
    }

    def buildPropertySheetPropertiesFromHeaderRow() {
        buildKeyListFromHeaderRow()
        buildLanguageFromHeaderRow()
        buildRowStylesFromHeaderRow()
    }

    private buildKeyListFromHeaderRow() {
        Row row = sheet.getRow(headerRowNum)
        def cellList = row.cellIterator().collect { it.getStringCellValue().trim() }
        keyList = cellList.findResults { it != "" ? it : null }
    }

    private buildLanguageFromHeaderRow() {
        def languageList = LanguageLabels.getLanguageList()
        language = keyList.find { languageList.contains(it.toString()) && (it.toString() != "English") }
    }

    private buildRowStylesFromHeaderRow() {
        headerRowStyles = getRowStylesForRowNumber(headerRowNum)
        dataRowStyles = getRowStylesForRowNumber(headerRowNum+1)
    }


    private getRowStylesForRowNumber(int rowNum) {
        Workbook workbook = excelPropertySheet.workbook
        Row row = sheet.getRow(rowNum)
        ArrayList<CellStyle> rowStyles = row.cellIterator().collect { Cell cell ->
            CellStyle cellStyle = workbook.createCellStyle()
            cellStyle.cloneStyleFrom(cell.getCellStyle())
            cellStyle
        }
        rowStyles
    }

}
