package properties

import i18n.LanguageLabels
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row

class ExcelPropertySheetProperties {

    ExcelPropertySheet excelPropertySheet

    ArrayList<String> headerRowNames
    int headerRowNum
    String language
    boolean isNewLanguage = false
    ArrayList<CellStyle> dataCellStyles

    ExcelPropertySheetProperties(ExcelPropertySheet excelPropertySheet, int headerRowNum, ArrayList<CellStyle> dataCellStyles) {
        this.headerRowNum = headerRowNum
        this.dataCellStyles = dataCellStyles
        this.excelPropertySheet = excelPropertySheet
        this.buildHeaderRowPropertiesFromHeaderRow()
    }

    def buildHeaderRowPropertiesFromHeaderRow() {
        buildKeyListFromHeaderRow()
        buildLanguageFromHeaderRow()
    }

    private buildKeyListFromHeaderRow() {
        Row row = excelPropertySheet.sheet.getRow(headerRowNum)
        def cellList = row.cellIterator().collect { it.getStringCellValue().trim() }
        headerRowNames = cellList.findResults { it != "" ? it : null }
    }

    private buildLanguageFromHeaderRow() {
        def languageList = LanguageLabels.getLanguageList()
        language = headerRowNames.find { languageList.contains(it.toString()) && (it.toString() != "English") }
    }
}
