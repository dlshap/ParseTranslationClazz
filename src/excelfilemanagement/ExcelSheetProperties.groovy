package excelfilemanagement

import excelfilemanagement.ExcelSheet
import i18n.LanguageLabels
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import properties.ExcelPropertySheet

class ExcelSheetProperties {

    ExcelSheet excelSheet

    ArrayList<String> headerRowNames
    int headerRowNum
    String language
    boolean isNewLanguage = false
    ArrayList<CellStyle> dataCellStyles

    ExcelSheetProperties(ExcelPropertySheet excelPropertySheet, int headerRowNum, ArrayList<CellStyle> dataCellStyles) {
        this.headerRowNum = headerRowNum
        this.dataCellStyles = dataCellStyles
        this.excelSheet = excelPropertySheet
        this.buildHeaderRowPropertiesFromHeaderRow()
    }

    def buildHeaderRowPropertiesFromHeaderRow() {
        buildKeyListFromHeaderRow()
        buildLanguageFromHeaderRow()
    }

    private buildKeyListFromHeaderRow() {
        Row row = excelSheet.sheet.getRow(headerRowNum)
        def cellList = row.cellIterator().collect { it.getStringCellValue().trim() }
        headerRowNames = cellList.findResults { it != "" ? it : null }
    }

    private buildLanguageFromHeaderRow() {
        def languageList = LanguageLabels.getLanguageList()
        language = headerRowNames.find { languageList.contains(it.toString()) && (it.toString() != "English") }
    }
}
