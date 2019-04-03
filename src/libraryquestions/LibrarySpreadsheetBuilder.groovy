package libraryquestions

import filemanagement.BaseFile
import org.apache.poi.ss.usermodel.Sheet
import properties.ExcelPropertyFile
import properties.ExcelPropertySheet

class LibrarySpreadsheetBuilder {

    ExcelPropertyFile languageLibraryExcelFile

    LibrarySpreadsheetBuilder(String languageLibraryFileName) {
        languageLibraryExcelFile = ExcelPropertyFile.createNewFileFromFileName(languageLibraryFileName, BaseFile.CreateFlag.CREATE)
    }

    def buildNewSpreadsheetFromModel(ExcelPropertyFile modelLibraryExcelFile) {
        while (modelLibraryExcelFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet excelPropertySheet = modelLibraryExcelFile.nextExcelPropertySheet()
            buildLanguageSheetFromModelSheet(excelPropertySheet)
        }
        languageLibraryExcelFile.writeAndClose()
    }

    private buildLanguageSheetFromModelSheet(ExcelPropertySheet modelPropertySheet) {
        String sheetName = modelPropertySheet.getSheetName()
        languageLibraryExcelFile.createSheet(sheetName)
    }


}
