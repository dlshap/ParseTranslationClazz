package properties

import i18n.LanguageLabels
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row

class ExcelPropertySheetProperties {

    ExcelPropertySheet excelPropertySheet

    def keyList
    def headerRowNum
    def language
    def isNewLanguage = false
    ArrayList<CellStyle> headerRowStyles
    ArrayList<CellStyle> dataRowStyles

    ExcelPropertySheetProperties(ExcelPropertySheet excelPropertySheet, int headerRowNum, ArrayList<CellStyle> dataRowStyles) {
        this.headerRowNum = headerRowNum
        this.dataRowStyles = dataRowStyles
        this.excelPropertySheet = excelPropertySheet
        this.buildHeaderRowPropertiesFromHeaderRow()
    }

    def buildHeaderRowPropertiesFromHeaderRow() {
        buildKeyListFromHeaderRow()
        buildLanguageFromHeaderRow()
        buildRowStylesFromHeaderRow()
    }

    private buildKeyListFromHeaderRow() {
        Row row = excelPropertySheet.sheet.getRow(headerRowNum)
        def cellList = row.cellIterator().collect { it.getStringCellValue().trim() }
        keyList = cellList.findResults { it != "" ? it : null }
    }

    private buildLanguageFromHeaderRow() {
        def languageList = LanguageLabels.getLanguageList()
        language = keyList.find { languageList.contains(it.toString()) && (it.toString() != "English") }
    }

    private buildRowStylesFromHeaderRow() {
        Row row = excelPropertySheet.sheet.getRow(headerRowNum)
        headerRowStyles = row.cellIterator().collect {it.getCellStyle()}
    }
}
