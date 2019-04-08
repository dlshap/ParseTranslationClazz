package libraryquestions

import properties.ExcelPropertyFile
import properties.ExcelPropertySheet

class LibrarySpreadsheetUpdater {

    ExcelPropertyFile origLibraryExcelFile
    ExcelPropertyFile newLibraryExcelFile

    LibrarySpreadsheetUpdater(ExcelPropertyFile languageLibraryExcelFile) {
        origLibraryExcelFile = languageLibraryExcelFile
    }

    def updateSpreadsheetFromModel(ExcelPropertyFile modelLibraryExcelFile) {
        while (origLibraryExcelFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet nextSheet = origLibraryExcelFile.nextExcelPropertySheet()
            print nextSheet.sheetName + ","
        }
    }
}
