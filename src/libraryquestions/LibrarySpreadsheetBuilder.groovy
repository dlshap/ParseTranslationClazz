package libraryquestions

import filemanagement.BaseFile
import org.apache.poi.ss.usermodel.Workbook
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet

class LibrarySpreadsheetBuilder {

    ExcelPropertyFile languageLibraryExcelFile

    LibrarySpreadsheetBuilder() {

    }

    LibrarySpreadsheetBuilder(String languageLibraryFileName) {
        createNewLibraryExcelFileFromFileName(languageLibraryFileName)
    }

    def createNewLibraryExcelFileFromFileName(String languageLibraryFileName) {
        languageLibraryExcelFile = ExcelPropertyFile.createNewFileFromFileName(languageLibraryFileName, BaseFile.CreateFlag.CREATE)
    }

    def buildNewSpreadsheetFromModel(ExcelPropertyFile modelLibraryExcelFile) {
        while (modelLibraryExcelFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet modelPropertySheet = modelLibraryExcelFile.nextExcelPropertySheet()
            buildLanguageSheetFromModelSheet(modelPropertySheet)
            print "." // for impatient users
        }
        languageLibraryExcelFile.writeAndClose()
    }

    def buildLanguageSheetFromModelSheet(ExcelPropertySheet modelPropertySheet) {
        Workbook languageWorkbook = languageLibraryExcelFile.workbook
        ExcelPropertySheet languagePropertySheet = ExcelPropertySheet.createExcelPropertySheetInWorkbookFromModelSheet(languageWorkbook, modelPropertySheet)
        buildDataRowsFromModel(languagePropertySheet, modelPropertySheet)
    }

     private buildDataRowsFromModel(ExcelPropertySheet languagePropertySheet, ExcelPropertySheet modelPropertySheet) {
        while (modelPropertySheet.hasNextExcelPropertyRow()) {
            ExcelPropertyRow modelPropertyRow = modelPropertySheet.nextExcelPropertyRow()
            languagePropertySheet.cloneExcelRow(modelPropertyRow.row.getRowNum(), modelPropertyRow)
        }
    }

}
