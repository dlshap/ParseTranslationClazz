package properties

import i18n.LanguageLabels
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet

class ExcelPropertySheetProperties {

    ExcelPropertySheet excelPropertySheet

    def keyList
    def headerRowNum
    Sheet sheet
    def headerRowStyles
    def dataRowStyles
    def columnWidths
    def language

    ExcelPropertySheetProperties(ExcelPropertySheet excelPropertySheet) {
        this.excelPropertySheet = excelPropertySheet
        this.headerRowNum = excelPropertySheet.headerRowNum
        this.sheet = excelPropertySheet.sheet
        this.buildKeyList()
        this.buildLanguage()
    }

    private buildKeyList() {
        this.headerRowNum = headerRowNum
        buildKeyListFromHeaderRow()
    }

    private buildLanguage() {
        def languageList = LanguageLabels.getLanguageList()
        language = keyList.find {languageList.contains(it.toString()) && (it.toString() != "English")}
    }

    private buildKeyListFromHeaderRow() {
        Row row = sheet.getRow(headerRowNum)
        keyList = row.cellIterator().collect() { it.getStringCellValue() }
    }

    def buildColumnWidths() {
        Row row = sheet.getRow(headerRowNum)
        columnWidths = row.cellIterator().collect() { Cell it ->
            sheet.getColumnWidth(it.getColumnIndex())
        }
    }

    private buildRowStyles(int rowNum) {
        Row row = sheet.getRow(rowNum)
        row.cellIterator().collect() {it.getCellStyle()}
    }
}
