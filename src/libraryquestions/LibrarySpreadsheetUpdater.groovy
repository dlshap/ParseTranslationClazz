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
        while (modelLibraryExcelFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet modelSheet = modelLibraryExcelFile.nextExcelPropertySheet()
            ExcelPropertySheet origSheet = origLibraryExcelFile.getExcelPropertySheet(modelSheet.sheetName)
            if (origSheet == null) {
                buildNewSheetFromModel(modelSheet)
            } else {
                updateNewSheetFromModelAndOrig(modelSheet, origSheet)
            }
        }
    }

    def buildNewSheetFromModel(ExcelPropertySheet modelSheet) {
        println "New Sheet: ${modelSheet.sheetName}"
    }

    def updateNewSheetFromModelAndOrig(ExcelPropertySheet modelSheet, ExcelPropertySheet origSheet) {
        println "Old Sheet: ${origSheet.sheetName}"
    }

}
