package properties

import exceptions.NewLanguageNotInLanguageListException
import i18n.LanguageLabels
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet

class ExcelPropertySheetProperties {

    ExcelPropertySheet excelPropertySheet

    def keyList
    def headerRowNum
    Sheet sheet
    def language
    def isNewLanguage = false

    ExcelPropertySheetProperties(ExcelPropertySheet excelPropertySheet, headerRowNum) {
        this.headerRowNum = headerRowNum
        this.excelPropertySheet = excelPropertySheet
        this.sheet = excelPropertySheet.sheet
        this.buildPropertySheetPropertiesFromHeaderRow()
    }

    def buildPropertySheetPropertiesFromHeaderRow() {
        buildKeyListFromHeaderRow()
        buildLanguageFromHeaderRow()
    }

    private buildLanguageFromHeaderRow() {
        def languageList = LanguageLabels.getLanguageList()
        language = keyList.find { languageList.contains(it.toString()) && (it.toString() != "English") }
    }

    private buildKeyListFromHeaderRow() {
        Row row = sheet.getRow(headerRowNum)
        def cellList = row.cellIterator().collect { it.getStringCellValue().trim() }
        keyList = cellList.findResults { it != "" ? it : null }
    }

}
