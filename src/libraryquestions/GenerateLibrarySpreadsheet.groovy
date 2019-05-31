package libraryquestions

import i18n.LanguageLabels
import logging.Log
import logging.LogUtils
import properties.ExcelPropertyFile
import properties.ExcelPropertyRow
import properties.ExcelPropertySheet

class GenerateLibrarySpreadsheet {

    LibraryArgs libraryArgs
    String foreignLangFileName, masterLangFileName

    GenerateLibrarySpreadsheet(LibraryArgs libraryArgs) {
        this.libraryArgs = libraryArgs
        setupPathsAndNames()
        if (LanguageLabels.isLanguageInList(this.libraryArgs.language)) {
            LogUtils.openLogs(libraryArgs.librarySpreadsheetLogPath, "${libraryArgs.language}-library-spreadsheet")
            Log.writeLine "Generating ${libraryArgs.language} library question spreadsheet..."
            generateSpreadsheet()
        }
    }

    def setupPathsAndNames() {
        foreignLangFileName = "QuestionAnswerLibrary (${libraryArgs.language})"
        masterLangFileName = "QuestionAnswerLibrary (English)"
    }

    def generateSpreadsheet() {
        ExcelPropertyFile modelLibraryExcelFile = getModelFile()
        if (modelLibraryExcelFile != null) {
            ExcelPropertyFile newLibraryExcelFile = createNewLanguageLibraryExcelFileUsingModel(modelLibraryExcelFile)
            ExcelPropertyFile oldLibraryExcelFile = getLibraryFile()
            if (oldLibraryExcelFile != null)
                updateNewLanguageLibraryExcelFileFromOldFile(newLibraryExcelFile, oldLibraryExcelFile)
            newLibraryExcelFile.writeAndClose()
        }
    }

    ExcelPropertyFile getModelFile() {
        ExcelPropertyFile.openFileUsingFileName(libraryArgs.spreadsheetPath + masterLangFileName + ".xlsx")
    }

    ExcelPropertyFile getLibraryFile() {
        ExcelPropertyFile.openFileUsingFileName(libraryArgs.spreadsheetPath + foreignLangFileName + ".xlsx")
    }

    def createNewLanguageLibraryExcelFileUsingModel(ExcelPropertyFile modelLibraryExcelFile) {
        String newLibraryFileName = libraryArgs.spreadsheetPath + "\\new\\" + foreignLangFileName + "_new.xlsx"
        LibrarySpreadsheetUpdater.buildNewSpreadsheetFromModel(newLibraryFileName, modelLibraryExcelFile)
    }

    static def updateNewLanguageLibraryExcelFileFromOldFile(ExcelPropertyFile newLibraryExcelFile, ExcelPropertyFile oldLibraryExcelFile) {
        newLibraryExcelFile.resetSheetIterator()
        while (newLibraryExcelFile.hasNextExcelPropertySheet()) {
            ExcelPropertySheet newSheet = newLibraryExcelFile.nextExcelPropertySheet()
            ExcelPropertySheet oldSheet = oldLibraryExcelFile.getExcelPropertySheet(newSheet.sheetName)
            updateNewSheetFromOldSheet(newSheet, oldSheet)
            logAddedRowsInNewSheetNotInOldSheet(newSheet, oldSheet)
        }
    }

    static def updateNewSheetFromOldSheet(ExcelPropertySheet newSheet, ExcelPropertySheet oldSheet) {
        while (oldSheet.hasNextExcelPropertyRow()) {
            ExcelPropertyRow oldRow = oldSheet.nextExcelPropertyRow()
            ExcelPropertyRow newRow = LibrarySpreadsheetUpdater.getRowMatchingKeysFromModelRow(newSheet, oldRow)
            if (newRow != null) {
                LibrarySpreadsheetUpdater.updateNewRowTranslationsFromOldRowForSheet(newRow, oldRow, newSheet.sheetName)
            } else {
                Log.writeLine("deletes", "Deleted old row for translation in ${newSheet.sheetName}: ${LibrarySpreadsheetUpdater.getRowKeys(oldRow)}")
            }
        }
    }

    static def logAddedRowsInNewSheetNotInOldSheet(ExcelPropertySheet newSheet, ExcelPropertySheet oldSheet) {
        while (newSheet.hasNextExcelPropertyRow()) {
            ExcelPropertyRow newRow = newSheet.nextExcelPropertyRow()
            if (LibrarySpreadsheetUpdater.getRowMatchingKeysFromModelRow(oldSheet, newRow) == null) {
                Log.writeLine("adds", "Added new row for translation in ${newSheet.sheetName}: ${LibrarySpreadsheetUpdater.getRowKeys(newRow)}")
            }
        }
    }
}
