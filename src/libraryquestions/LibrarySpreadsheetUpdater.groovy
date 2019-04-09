package libraryquestions

import filemanagement.BaseFile
import properties.ExcelPropertyFile
import properties.ExcelPropertySheet

class LibrarySpreadsheetUpdater extends LibrarySpreadsheetBuilder {

    ExcelPropertyFile origLibraryExcelFile

    LibrarySpreadsheetUpdater(ExcelPropertyFile origLibraryExcelFile) {
        this.origLibraryExcelFile = origLibraryExcelFile
        String pathName = origLibraryExcelFile.file.getParent() + "\\"
        String newLibraryFileName = pathName + (origLibraryExcelFile.fileName).replace(")", ")_new")
        this.createNewLibraryExcelFileFromFileName(newLibraryFileName)
    }

    def updateSpreadsheetFromModel(ExcelPropertyFile modelLibraryExcelFile) {
        while (modelLibraryExcelFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet modelSheet = modelLibraryExcelFile.nextExcelPropertySheet()
            String sheetName = modelSheet.sheetName
            ExcelPropertySheet origSheet = origLibraryExcelFile.getExcelPropertySheet(sheetName)
            if (origSheet == null) {
                buildLanguageSheetFromModelSheet(modelSheet)
            } else {
                updateNewSheetFromModelAndOrig(modelSheet, origSheet)
            }
            print "." // for impatient users
        }
        languageLibraryExcelFile.writeAndClose()
    }

    def updateNewSheetFromModelAndOrig(ExcelPropertySheet modelSheet, ExcelPropertySheet origSheet) {
        buildLanguageSheetFromModelSheet(modelSheet)
    }

}
