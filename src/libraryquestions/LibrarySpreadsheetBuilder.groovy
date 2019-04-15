package libraryquestions

import filemanagement.BaseFile
import org.apache.poi.ss.usermodel.Workbook
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet

class LibrarySpreadsheetBuilder {

    ExcelPropertyFile newLibraryExcelFile

    LibrarySpreadsheetBuilder() {

    }

    LibrarySpreadsheetBuilder(String languageLibraryFileName) {
        createNewLibraryExcelFileFromFileName(languageLibraryFileName)
    }

    def createNewLibraryExcelFileFromFileName(String languageLibraryFileName) {
        newLibraryExcelFile = ExcelPropertyFile.createNewFileFromFileName(languageLibraryFileName, BaseFile.CreateFlag.CREATE)
    }

    def buildNewSpreadsheetFromModel(ExcelPropertyFile modelLibraryExcelFile) {
        while (modelLibraryExcelFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet modelPropertySheet = modelLibraryExcelFile.nextExcelPropertySheet()
            buildLanguageSheetFromModelSheet(modelPropertySheet)
            print "." // for impatient users
        }
        newLibraryExcelFile.writeAndClose()
    }

    def buildLanguageSheetFromModelSheet(ExcelPropertySheet modelPropertySheet) {
        Workbook languageWorkbook = newLibraryExcelFile.workbook
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
